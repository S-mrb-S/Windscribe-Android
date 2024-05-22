package sp.windscribe.vpn.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.await
import kotlinx.coroutines.rx2.rxCompletable
import org.json.JSONObject
import sp.windscribe.vpn.api.IApiCallManager
import sp.windscribe.vpn.apppreference.PreferencesHelper
import sp.windscribe.vpn.constants.NetworkKeyConstants
import sp.windscribe.vpn.localdatabase.LocalDbInterface
import sp.windscribe.vpn.serverlist.entity.StaticRegion
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StaticIpRepository @Inject constructor(
        val scope: CoroutineScope,
        private val preferencesHelper: PreferencesHelper,
        private val apiCallManager: IApiCallManager,
        private val localDbInterface: LocalDbInterface,
) {
    private var _events = MutableStateFlow(emptyList<StaticRegion>())
    val regions: StateFlow<List<StaticRegion>> = _events

    init {
        load()
    }

    fun load() {
        scope.launch {
            try {
                val regions = localDbInterface.allStaticRegions.await()
                _events.emit(regions)
            } catch (e: Exception) {
            }
        }
    }

    private suspend fun updateFromApi() {
        val sessionMap: MutableMap<String, String> = HashMap()
        preferencesHelper.getDeviceUUID(preferencesHelper.userName)?.let {
            sessionMap[NetworkKeyConstants.UUID_KEY] = it
        }
        val response = apiCallManager.getStaticIpList(sessionMap).await()
        val regions = response.dataClass?.let {
            val jsonObject = JSONObject(Gson().toJson(it))
            Gson().fromJson<List<StaticRegion>>(
                    jsonObject.getJSONArray("static_ips").toString(),
                    object : TypeToken<List<StaticRegion>?>() {}.type
            )
        } ?: emptyList()
        if (regions.isNotEmpty()) {
            regions[0].deviceName
        }
        localDbInterface.addStaticRegions(regions).await()
    }

    fun update(): Completable {
        return rxCompletable {
            updateFromApi()
        }
    }
}
