package sp.windscribe.mobile.mrb.util

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.coroutineScope
import okhttp3.internal.toImmutableList
import sp.windscribe.mobile.GetServersQuery
import sp.windscribe.vpn.qq.Data


data class Server(
        val id: Int,
        val name: String,
        val country_code: String,
        val status: Int,
        val premium_only: Int,
        val short_name: String,
        val p2p: Int,
        val tz: String,
        val tz_offset: String,
        val loc_type: String,
        val dns_hostname: String,
        val groups: List<Group>
)

data class Group(
        val id: Int,
        val city: String,
        val nick: String,
        val pro: Int,
        val gps: String?,
        val tz: String?,
        val wg_pubkey: String?,
        val wg_endpoint: String?,
        val ovpn_x509: String?,
        val ping_ip: String?,
        val ping_host: String?,
        val link_speed: String?,
        val nodes: List<Node>?,
        val health: Int
)

data class Node(
        val ip: String,
        val ip2: String,
        val ip3: String,
        val hostname: String,
        val weight: Int,
        val health: Int
)

class ListCreator(var data: GetServersQuery.Data) {
    private var num = 0
    // child
    private var v2rayChildrens: List<Group> = ArrayList()
    private var openVpnChildrens: List<Group> = ArrayList()
    private var ciscoChildrens: List<Group> = ArrayList()
    // flag (example: CA, US,..)
    private var v2rayFlag: String? = null
    private var openVpnFlag: String? = null
    private var ciscoFlag: String? = null

    suspend fun createAndGet(): String = coroutineScope {
        try {
            initAllChildrens()
            val res: List<Server> = when (Data.defaultItemDialog) {
                0 -> { // v2ray
                    listOf(createV2ray())
                }

                1 -> { // openvpn
                    listOf(createOpenVpn())
                }

                2 -> { // cisco
                    listOf(createCisco())
                }

                else -> {
                    listOf()
                }
            }

            val gson = Gson()
            return@coroutineScope gson.toJson(res)
        } catch (ignore: Exception) {}
        return@coroutineScope ""
    }

    private fun initAllChildrens() {
        val chilV2ray = v2rayChildrens.toMutableList()
        val chilOpenVPN = openVpnChildrens.toMutableList()
        val chilCisco = ciscoChildrens.toMutableList()

        if (data.servers != null) {
            try {
                for (data in data.servers!!) {
                    try {
                        when (data?.serverType) {
                            "V2Ray" -> {
                                if(v2rayFlag.isNullOrEmpty()){
                                    v2rayFlag = data.flag
                                }

                                chilV2ray.add(
                                        Group(
                                                num,
                                                data.name.toString(),
                                                "v2ray",
                                                0,
                                                "44.46,-63.61",
                                                "America/Halifax",
                                                "w262TI0UyIg9pFunMiekVURYUuT/z4qXRor2Z7VcOn4=",
                                                "yhz-386-wg.whiskergalaxy.com",
                                                data.url,
                                                "23.191.80.2",
                                                "https://ca-021.whiskergalaxy.com:6363/latency",
                                                "1000",
                                                listOf(
                                                        Node(
                                                                "172.98.68.238",
                                                                "172.98.68.239",
                                                                "172.98.68.240",
                                                                "ca-055.whiskergalaxy.com",
                                                                1,
                                                                2
                                                        ),
                                                        Node(
                                                                "172.98.68.227",
                                                                "172.98.68.228",
                                                                "172.98.68.229",
                                                                "ca-054.whiskergalaxy.com",
                                                                1,
                                                                2
                                                        )
                                                ),
                                                0
                                        )
                                )
                            }

                            "OpenVPN" -> {
                                if(openVpnFlag.isNullOrEmpty()){
                                    openVpnFlag = data.flag
                                }

                                val configContent = fetchOvpnConfig(data.url.toString())

                                configContent?.let {
                                    chilOpenVPN.add(
                                            Group(
                                                    num,
                                                    data.name.toString(),
                                                    "openvpn",
                                                    0,
                                                    "44.46,-63.61",
                                                    "America/Halifax",
                                                    "w262TI0UyIg9pFunMiekVURYUuT/z4qXRor2Z7VcOn4=",
                                                    "yhz-386-wg.whiskergalaxy.com",
                                                    it, // content file
                                                    "23.191.80.2",
                                                    "https://ca-021.whiskergalaxy.com:6363/latency",
                                                    "1000",
                                                    listOf(
                                                            Node(
                                                                    "172.98.68.238",
                                                                    "172.98.68.239",
                                                                    "172.98.68.240",
                                                                    "ca-055.whiskergalaxy.com",
                                                                    1,
                                                                    2
                                                            ),
                                                            Node(
                                                                    "172.98.68.227",
                                                                    "172.98.68.228",
                                                                    "172.98.68.229",
                                                                    "ca-054.whiskergalaxy.com",
                                                                    1,
                                                                    2
                                                            )
                                                    ),
                                                    0
                                            )
                                    )
                                } ?: run {
                                    // no content file
                                }
                            }

                            "Cisco" -> {
                                if(ciscoFlag.isNullOrEmpty()){
                                    ciscoFlag = data.flag
                                }

                                chilCisco.add(
                                        Group(
                                                num,
                                                data.name.toString(),
                                                "cisco",
                                                0,
                                                "44.46,-63.61",
                                                "America/Halifax",
                                                "w262TI0UyIg9pFunMiekVURYUuT/z4qXRor2Z7VcOn4=",
                                                "yhz-386-wg.whiskergalaxy.com",
                                                data.url.toString(), // cisco server
                                                "23.191.80.2",
                                                "https://ca-021.whiskergalaxy.com:6363/latency",
                                                "1000",
                                                listOf(
                                                        Node(
                                                                "172.98.68.238",
                                                                "172.98.68.239",
                                                                "172.98.68.240",
                                                                "ca-055.whiskergalaxy.com",
                                                                1,
                                                                2
                                                        ),
                                                        Node(
                                                                "172.98.68.227",
                                                                "172.98.68.228",
                                                                "172.98.68.229",
                                                                "ca-054.whiskergalaxy.com",
                                                                1,
                                                                2
                                                        )
                                                ),
                                                0
                                        )
                                )

                                ++num
                            }
                        }
                    } finally {
                        ++num
                    }
                }
            } finally {
                v2rayChildrens = chilV2ray.toImmutableList()
                openVpnChildrens = chilOpenVPN.toImmutableList()
                ciscoChildrens = chilCisco.toImmutableList()
            }
        }
    }

    // v2ray
    private fun createV2ray(): Server {
        return Server(
                1,
                "V2ray",
                v2rayFlag.toString(),
                1,
                0,
                v2rayFlag.toString(),
                1,
                "America/Toronto",
                "-5,EST",
                "normal",
                "ca.windscribe.com",
                v2rayChildrens
        )
    }

    // openvpn
    private fun createOpenVpn(): Server {
        return Server(
                2,
                "OpenVpn",
                openVpnFlag.toString(),
                1,
                0,
                openVpnFlag.toString(),
                1,
                "America/Toronto",
                "-5,EST",
                "normal",
                "ca.windscribe.com",
                openVpnChildrens
        )
    }

    // cisco
    private fun createCisco(): Server {
        return Server(
                3,
                "Cisco",
                ciscoFlag.toString(),
                1,
                0,
                ciscoFlag.toString(),
                1,
                "America/Toronto",
                "-5,EST",
                "normal",
                "ca.windscribe.com",
                ciscoChildrens
        )
    }
}
