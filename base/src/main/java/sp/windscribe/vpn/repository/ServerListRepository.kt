package sp.windscribe.vpn.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.await
import org.slf4j.LoggerFactory
import sp.windscribe.vpn.Windscribe.Companion.appContext
import sp.windscribe.vpn.api.IApiCallManager
import sp.windscribe.vpn.commonutils.WindUtilities
import sp.windscribe.vpn.constants.AdvanceParamKeys
import sp.windscribe.vpn.localdatabase.LocalDbInterface
import sp.windscribe.vpn.serverlist.entity.City
import sp.windscribe.vpn.serverlist.entity.Region
import sp.windscribe.vpn.serverlist.entity.RegionAndCities
import sp.windscribe.vpn.state.AppLifeCycleObserver
import sp.windscribe.vpn.state.PreferenceChangeObserver
import sp.windscribe.vpn.workers.WindScribeWorkManager
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
        var str = sp.windscribe.vpn.qq.Data.dataString
        if (str.isNullOrEmpty()) {
            str = "{}"
        }
        val list: List<Region> = Gson().fromJson<List<Region>>(
                str.trimIndent(),
                object : TypeToken<ArrayList<Region?>?>() {}.type
        )

        return addToDatabase(list)
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
