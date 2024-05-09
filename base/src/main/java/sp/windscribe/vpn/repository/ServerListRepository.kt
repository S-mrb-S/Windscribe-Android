package sp.windscribe.vpn.repository

import android.util.Log
import android.widget.Toast
import androidx.work.Data
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
import okhttp3.internal.toImmutableList
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
                    logger.debug("Parsing server list JSON")
                        val dataArray = """
                            [
                              {
                                "id": 7,
                                "name": "Canada East",
                                "country_code": "CA",
                                "status": 1,
                                "premium_only": 0,
                                "short_name": "CA",
                                "p2p": 1,
                                "tz": "America/Toronto",
                                "tz_offset": "-5,EST",
                                "loc_type": "normal",
                                "dns_hostname": "ca.windscribe.com",
                                "groups": [
                                  {
                                    "id": 72,
                                    "city": "Toronto",
                                    "nick": "The 6",
                                    "pro": 0,
                                    "gps": "43.59,-79.76",
                                    "tz": "America/Toronto",
                                    "wg_pubkey": "pKXBuReWe+HfrILovyFzIybA8AVAsFgfDUyo42tLT1g=",
                                    "wg_endpoint": "yyz-72-wg.whiskergalaxy.com",
                                    "ovpn_x509": "yyz-72.windscribe.com",
                                    "ping_ip": "104.254.92.90",
                                    "ping_host": "https://ca-009.whiskergalaxy.com:6363/latency",
                                    "link_speed": "1000",
                                    "nodes": [
                                      {
                                        "ip": "104.254.92.74",
                                        "ip2": "104.254.92.75",
                                        "ip3": "104.254.92.76",
                                        "hostname": "ca-037.whiskergalaxy.com",
                                        "weight": 1,
                                        "health": 33
                                      }
                                    ],
                                    "health": 33
                                  },
                                  {
                                    "id": 197,
                                    "city": "Toronto",
                                    "nick": "Comfort Zone",
                                    "pro": 0,
                                    "gps": "43.59,-79.76",
                                    "tz": "America/Toronto",
                                    "wg_pubkey": "U5s7Yy/2fCqlaFcI96dFKupqEVCn+BYF04LRLD1zOhg=",
                                    "wg_endpoint": "yyz-197-wg.whiskergalaxy.com",
                                    "ovpn_x509": "yyz-197.windscribe.com",
                                    "ping_ip": "181.215.52.132",
                                    "ping_host": "https://ca-038.whiskergalaxy.com:6363/latency",
                                    "link_speed": "10000",
                                    "nodes": [
                                      {
                                        "ip": "149.88.98.161",
                                        "ip2": "149.88.98.162",
                                        "ip3": "149.88.98.163",
                                        "hostname": "ca-058.whiskergalaxy.com",
                                        "weight": 1,
                                        "health": 3
                                      },
                                      {
                                        "ip": "149.88.98.177",
                                        "ip2": "149.88.98.178",
                                        "ip3": "149.88.98.179",
                                        "hostname": "ca-059.whiskergalaxy.com",
                                        "weight": 1,
                                        "health": 2
                                      }
                                    ],
                                    "health": 3
                                  },
                                  {
                                    "id": 316,
                                    "city": "Montreal",
                                    "nick": "Bagel Poutine",
                                    "pro": 1,
                                    "gps": "45.51,-73.56",
                                    "tz": "America/Montreal",
                                    "wg_pubkey": "DxBtB3enAlS3OtJ9+jFtrTmuiGs36aV6HyyjKcit71o=",
                                    "wg_endpoint": "yul-316-wg.whiskergalaxy.com",
                                    "ovpn_x509": "yul-316.windscribe.com",
                                    "ping_ip": "38.170.155.242",
                                    "ping_host": "https://ca-027.whiskergalaxy.com:6363/latency",
                                    "link_speed": "1000"
                                  },
                                  {
                                    "id": 359,
                                    "city": "Montreal",
                                    "nick": "Expo 67",
                                    "pro": 0,
                                    "gps": "45.51,-73.56",
                                    "tz": "America/Montreal",
                                    "wg_pubkey": "nfFRpFZ0ZXWVoz8C4gP5ti7V1snFT1gV8EcIxTWJtB4=",
                                    "wg_endpoint": "yul-359-wg.whiskergalaxy.com",
                                    "ovpn_x509": "yul-359.windscribe.com",
                                    "ping_ip": "172.98.82.2",
                                    "ping_host": "https://ca-050.whiskergalaxy.com:6363/latency",
                                    "link_speed": "10000",
                                    "nodes": [
                                      {
                                        "ip": "172.98.68.238",
                                        "ip2": "172.98.68.239",
                                        "ip3": "172.98.68.240",
                                        "hostname": "ca-055.whiskergalaxy.com",
                                        "weight": 1,
                                        "health": 2
                                      },
                                      {
                                        "ip": "172.98.68.227",
                                        "ip2": "172.98.68.228",
                                        "ip3": "172.98.68.229",
                                        "hostname": "ca-054.whiskergalaxy.com",
                                        "weight": 1,
                                        "health": 2
                                      }
                                    ],
                                    "health": 2
                                  },
                                  {
                                    "id": 386,
                                    "city": "Halifax",
                                    "nick": "Crosby",
                                    "pro": 1,
                                    "gps": "44.46,-63.61",
                                    "tz": "America/Halifax",
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
                        val list: List<Region> = Gson().fromJson<List<Region>>(
                            dataArray,
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
