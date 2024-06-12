package sp.windscribe.vpn.repository

import android.util.Log
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
import sp.windscribe.vpn.sp.Data
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
        var str = Data.dataString
        if (str.isNullOrBlank() || str.toString() == "null" || str.isEmpty()) {
            Data.dataString = """
[
  {
    "id": 1,
    "name": "None",
    "country_code": "ZZ",
    "status": 1,
    "premium_only": 0,
    "short_name": "None",
    "p2p": 1,
    "tz": "",
    "tz_offset": "-5,EST",
    "loc_type": "normal",
    "dns_hostname": "ca.windscribe.com",
    "groups": [
      {
        "id": 386,
        "city": "",
        "nick": "",
        "pro": 1,
        "gps": "44.46,-63.61",
        "tz": "",
        "wg_pubkey": "w262TI0UyIg9pFunMiekVURYUuT/z4qXRor2Z7VcOn4=",
        "wg_endpoint": "yhz-386-wg.whiskergalaxy.com",
        "ovpn_x509": "yhz-386.windscribe.com",
        "ping_ip": "23.191.80.2",
        "ping_host": "https://ca-021.whiskergalaxy.com:6363/latency",
        "link_speed": "1000"
      }
    ]
  }
]
            """.trimIndent()

            Data.static.MainApplicationExecuter({
                // run ViewModel on application thread
                Data.static.getmViewModel().saveIsChanged(2)
            }, Data.static.mainApplication)
        }else{
            Data.settingsStorage.putString("server_cache", Data.dataString!!.trimIndent())
        }
        val list: List<Region> = Gson().fromJson<List<Region>>(
                Data.dataString!!.trimIndent(),
                object : TypeToken<ArrayList<Region?>?>() {}.type
        )

        return addToDatabase(list)
    }

    fun cleanDatabase(): Completable {
        logger.debug("Cleaning server list to database")
        Data.settingsStorage.putString("server_cache", null)
        return localDbInterface.rmRegions()
            .andThen(localDbInterface.rmCities())
//            .andThen(Completable.fromAction { preferenceChangeObserver.postCityServerChange() })
            .doOnError { logger.debug("Error cleaning server list to database") }
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
