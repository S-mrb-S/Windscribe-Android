package sp.windscribe.mobile.mrb.util

import android.util.Log
import com.google.gson.Gson
import okhttp3.internal.toImmutableList
import sp.windscribe.mobile.GetServersQuery


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

    fun createAndGet(): String {
        try{
            val res = listOf(
                createV2ray(),
                createOpenVpn(),
                createCisco()
            )

            val gson = Gson()
            return gson.toJson(res)
        }catch (e: Exception){
            Log.d("MRB S", "EX")
        }
        return ""
    }

    private fun getChildV2ray(): List<Group> {

        val childrens: List<Group> = ArrayList()
        val chil = childrens.toMutableList()

        if(data.servers != null) {
            for (data in data.servers!!) {
                if (
                    data?.serverType == "V2Ray"
                ) {
                    chil.add(
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
                    ++num
                }
            }
        }

        return chil.toImmutableList()
    }

    private fun createV2ray(): Server {
        return Server(
            1,
            "V2ray",
            "V2",
            1,
            0,
            "CA",
            1,
            "America/Toronto",
            "-5,EST",
            "normal",
            "ca.windscribe.com",
            getChildV2ray()
        )
    }

    // openvpn

    private fun getChildOpenVpn(): List<Group> {

        val childrens: List<Group> = ArrayList()
        val chil = childrens.toMutableList()

        if(data.servers != null) {
            for (data in data.servers!!) {
                if (
                    data?.serverType == "OpenVPN"
                ) {
                        val configContent = fetchOvpnConfig(data.url.toString())

                        configContent?.let {
                            chil.add(
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

                    ++num
                }
            }
        }

        return chil.toImmutableList()
    }

    private fun createOpenVpn(): Server {
        return Server(
            2,
            "OpenVpn",
            "V2",
            1,
            0,
            "CA",
            1,
            "America/Toronto",
            "-5,EST",
            "normal",
            "ca.windscribe.com",
            getChildOpenVpn()
        )
    }

    // cisco

    private fun getChildCisco(): List<Group> {

        val childrens: List<Group> = ArrayList()
        val chil = childrens.toMutableList()

        if(data.servers != null) {
            for (data in data.servers!!) {
                if (
                    data?.serverType == "Cisco"
                ) {
                        chil.add(
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
        }

        return chil.toImmutableList()
    }

    private fun createCisco(): Server {
        return Server(
            3,
            "Cisco",
            "V2",
            1,
            0,
            "CA",
            1,
            "America/Toronto",
            "-5,EST",
            "normal",
            "ca.windscribe.com",
            getChildCisco()
        )
    }
}
