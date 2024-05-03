package sp.windscribe.vpn.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import sp.windscribe.vpn.Windscribe.Companion.appContext
import sp.windscribe.vpn.api.IApiCallManager
import sp.windscribe.vpn.commonutils.WindUtilities
import sp.windscribe.vpn.constants.AdvanceParamKeys
import sp.windscribe.vpn.localdatabase.LocalDbInterface
import sp.windscribe.vpn.model.User
import sp.windscribe.vpn.serverlist.entity.City
import sp.windscribe.vpn.serverlist.entity.Region
import sp.windscribe.vpn.serverlist.entity.RegionAndCities
import sp.windscribe.vpn.state.AppLifeCycleObserver
import sp.windscribe.vpn.state.PreferenceChangeObserver
import sp.windscribe.vpn.workers.WindScribeWorkManager
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.await
import org.json.JSONObject
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServerListRepository @Inject constructor(
    private val scope: CoroutineScope,
    private val apiCallManager: IApiCallManager,
    private val localDbInterface: LocalDbInterface,
    private val preferenceChangeObserver: PreferenceChangeObserver,
    private val userRepository: UserRepository,
    private val appLifeCycleObserver: AppLifeCycleObserver,
    private val workManager: WindScribeWorkManager
) {
    private val logger = LoggerFactory.getLogger("server_list_repository")
    private var _events = MutableSharedFlow<List<RegionAndCities>>(replay = 1)
    val regions: SharedFlow<List<RegionAndCities>> = _events
    var globalServerList = true

    init {
        load()
    }

    fun load() {
        scope.launch {
            _events.emit(localDbInterface.allRegion.await())
        }
    }

    private fun getCountryOverride(): String? {
        val extraKeys = WindUtilities.toKeyValuePairs(appContext.preference.advanceParamText)
        return if (extraKeys.containsKey(AdvanceParamKeys.WS_SERVER_LIST_COUNTRY_OVERRIDE)) {
            val countryCode = extraKeys[AdvanceParamKeys.WS_SERVER_LIST_COUNTRY_OVERRIDE]
            if (countryCode == "ignore") {
                "ZZ"
            } else {
                countryCode
            }
        } else {
            appLifeCycleObserver.overriddenCountryCode
        }
    }

    fun update(): Completable {
        logger.debug("Starting server list update")
        return apiCallManager.getSessionGeneric(null).flatMap {
                it.dataClass?.let { userSession ->
                    userRepository.reload(userSession)
                    val user = User(userSession)
                    val countryOverride = getCountryOverride()
                    if (countryOverride != null){
                        globalServerList = false
                    }
                    logger.debug("Country override: $countryOverride")
                    apiCallManager.getServerList(
                        null,
                        user.userStatusInt.toString(),
                        user.locationHash,
                        user.alcList, countryOverride
                    )
                } ?: it.errorClass?.let { error ->
                    logger.debug("Error updating session $error")
                    throw Exception()
                } ?: kotlin.run {
                    logger.debug("Unknown error updating session")
                    throw Exception()
                }
            }.flatMap { response ->
                Single.fromCallable {
                    logger.debug("Parsing server list JSON")
                    response.dataClass?.let {
                        val jsonObject = JSONObject(it)
                        val infoObject = jsonObject.getJSONObject("info")
                        logger.debug(infoObject.toString())
                        appLifeCycleObserver.overriddenCountryCode = if(infoObject.has("country_override")){
                            infoObject.getString("country_override")
                        }else{
                            null
                        }
                        val dataArray = jsonObject.getJSONArray("data")
                        Gson().fromJson<List<Region>>(
                            dataArray.toString(),
                            object : TypeToken<ArrayList<Region?>?>() {}.type
                        )
                    } ?: response.errorClass?.let {
                        throw Exception(it.errorMessage)
                    }
                }
            }.flatMapCompletable { regions: List<Region> -> addToDatabase(regions) }
    }

    private fun addToDatabase(regions: List<Region>): Completable {
        logger.debug("Saving server list to database")
        val cities: MutableList<City> = ArrayList()
        for (region in regions) {
            if (region.getCities() != null) {
                for (city in region.getCities()) {
                    city.regionID = region.id
                    cities.add(city)
                }
            }
        }
        return localDbInterface.addToRegions(regions)
            .andThen(localDbInterface.addToCities(cities))
            .andThen(Completable.fromAction { preferenceChangeObserver.postCityServerChange() })
            .doOnError { logger.debug("Error saving server list to database") }
    }


}
