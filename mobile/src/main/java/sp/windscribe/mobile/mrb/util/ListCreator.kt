package sp.windscribe.mobile.mrb.util

import com.google.gson.Gson
import kotlinx.coroutines.coroutineScope
import okhttp3.internal.toImmutableList
import sp.windscribe.mobile.GetServersQuery
import sp.windscribe.vpn.qq.Data

// fake data
// by MRB
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

    suspend fun createAndGet(): String = coroutineScope {
        try {
            val res: List<Server> = crGCList()

            val gson = Gson()
            return@coroutineScope gson.toJson(res)
        } catch (ignore: Exception) {
        }
        return@coroutineScope ""
    }

//    private fun initAllChildrens() {
//        val chilOpenVPN = openVpnChildrens.toMutableList()
//        val chilCisco = ciscoChildrens.toMutableList()
//
//        if (data.servers != null) {
//            try {
//                for (data in data.servers!!) {
//                    try {
//                        when (data?.serverType) {
//                            "V2Ray" -> {
//                                if (v2rayFlag.isNullOrEmpty()) {
//                                    v2rayFlag = data.flag
//                                }
//
//
//                            }
//
//                            "OpenVPN" -> {
//                                if (openVpnFlag.isNullOrEmpty()) {
//                                    openVpnFlag = data.flag
//                                }
//
//                                val configContent = fetchOvpnConfig(data.url.toString())
//
//                                configContent?.let {
//                                    chilOpenVPN.add(
//                                            Group(
//                                                    numChild,
//                                                    data.name.toString(),
//                                                    "openvpn",
//                                                    0,
//                                                    "44.46,-63.61",
//                                                    "America/Halifax",
//                                                    "w262TI0UyIg9pFunMiekVURYUuT/z4qXRor2Z7VcOn4=",
//                                                    "yhz-386-wg.whiskergalaxy.com",
//                                                    it, // content file
//                                                    "23.191.80.2",
//                                                    "https://ca-021.whiskergalaxy.com:6363/latency",
//                                                    "1000",
//                                                    listOf(
//                                                            Node(
//                                                                    "172.98.68.238",
//                                                                    "172.98.68.239",
//                                                                    "172.98.68.240",
//                                                                    "ca-055.whiskergalaxy.com",
//                                                                    1,
//                                                                    2
//                                                            ),
//                                                            Node(
//                                                                    "172.98.68.227",
//                                                                    "172.98.68.228",
//                                                                    "172.98.68.229",
//                                                                    "ca-054.whiskergalaxy.com",
//                                                                    1,
//                                                                    2
//                                                            )
//                                                    ),
//                                                    0
//                                            )
//                                    )
//                                } ?: run {
//                                    // no content file
//                                }
//                            }
//
//                            "Cisco" -> {
//                                if (ciscoFlag.isNullOrEmpty()) {
//                                    ciscoFlag = data.flag
//                                }
//
//                                chilCisco.add(
//                                        Group(
//                                                numChild,
//                                                data.name.toString(),
//                                                "cisco",
//                                                0,
//                                                "44.46,-63.61",
//                                                "America/Halifax",
//                                                "w262TI0UyIg9pFunMiekVURYUuT/z4qXRor2Z7VcOn4=",
//                                                "yhz-386-wg.whiskergalaxy.com",
//                                                data.url.toString(), // cisco server
//                                                "23.191.80.2",
//                                                "https://ca-021.whiskergalaxy.com:6363/latency",
//                                                "1000",
//                                                listOf(
//                                                        Node(
//                                                                "172.98.68.238",
//                                                                "172.98.68.239",
//                                                                "172.98.68.240",
//                                                                "ca-055.whiskergalaxy.com",
//                                                                1,
//                                                                2
//                                                        ),
//                                                        Node(
//                                                                "172.98.68.227",
//                                                                "172.98.68.228",
//                                                                "172.98.68.229",
//                                                                "ca-054.whiskergalaxy.com",
//                                                                1,
//                                                                2
//                                                        )
//                                                ),
//                                                0
//                                        )
//                                )
//                            }
//                        }
//                    } finally {
//                        ++numChild
//                    }
//                }
//            } finally {
//                openVpnChildrens = chilOpenVPN.toImmutableList()
//                ciscoChildrens = chilCisco.toImmutableList()
//            }
//        }
//    }

    // better
    private fun crGCList(): List<Server> {
        return when (Data.defaultItemDialog) {
            0 -> { // v2ray
                var numChild = 1
                var numGroup = 1

                // Group servers by flag
                val groupedServers = data.servers
                        ?.filter { it?.serverType == "V2Ray" } // Filter servers with serverType V2
                        ?.groupBy { it?.flag }

                val gV2ray = mutableListOf<Server>()

                // Iterate over grouped servers
                groupedServers?.forEach { (_, servers) -> // Group servers by flag
                    try {
                        // Create a list to hold grouped servers
                        val chilV2ray = mutableListOf<Group>()
                        val currentData = servers.filterNotNull()
                        val currentFlag = servers.firstOrNull()?.flag.toString()

                        for (server in currentData) {
                            chilV2ray.add(
                                    Group(
                                            numChild,
                                            server.name.toString(),
                                            "vpn",
                                            0,
                                            "44.46,-63.61",
                                            "America/Halifax",
                                            "w262TI0UyIg9pFunMiekVURYUuT/z4qXRor2Z7VcOn4=",
                                            "yhz-386-wg.whiskergalaxy.com",
                                            server.url.toString(),
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
                            ++numChild
                        }

                        gV2ray.add(
                                Server(
                                        numGroup,
                                        currentFlag,
                                        currentFlag,
                                        1,
                                        0,
                                        currentFlag,
                                        1,
                                        "America/Toronto",
                                        "-5,EST",
                                        "normal",
                                        "ca.windscribe.com",
                                        chilV2ray.toImmutableList()
                                )
                        )
                    } finally {
                        ++numGroup
                    }
                }

                // return
                gV2ray.toImmutableList()
            }

            else -> {
                listOf()
            }
        }
    }
}
