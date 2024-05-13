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
                createOpenVpn()
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

}


fun createExample(): String {

    val groupsChild = listOf(
        Group(
            72,
            "Toronto",
            "The 6",
            0,
            "43.59,-79.76",
            "America/Toronto",
            "pKXBuReWe+HfrILovyFzIybA8AVAsFgfDUyo42tLT1g=",
            "yyz-72-wg.whiskergalaxy.com",
            "yyz-72.windscribe.com",
            "104.254.92.90",
            "https://ca-009.whiskergalaxy.com:6363/latency",
            "1000",
            listOf(
                Node(
                    "104.254.92.74",
                    "104.254.92.75",
                    "104.254.92.76",
                    "ca-037.whiskergalaxy.com",
                    1,
                    33
                )
            ),
            33
        ),
        Group(
            197,
            "Toronto",
            "Comfort Zone",
            0,
            "43.59,-79.76",
            "America/Toronto",
            "U5s7Yy/2fCqlaFcI96dFKupqEVCn+BYF04LRLD1zOhg=",
            "yyz-197-wg.whiskergalaxy.com",
            "yyz-197.windscribe.com",
            "181.215.52.132",
            "https://ca-038.whiskergalaxy.com:6363/latency",
            "10000",
            listOf(
                Node(
                    "149.88.98.161",
                    "149.88.98.162",
                    "149.88.98.163",
                    "ca-058.whiskergalaxy.com",
                    1,
                    3
                ),
                Node(
                    "149.88.98.177",
                    "149.88.98.178",
                    "149.88.98.179",
                    "ca-059.whiskergalaxy.com",
                    1,
                    2
                )
            ),
            3
        ),
        Group(
            316,
            "Montreal",
            "Bagel Poutine",
            1,
            "45.51,-73.56",
            "America/Montreal",
            "DxBtB3enAlS3OtJ9+jFtrTmuiGs36aV6HyyjKcit71o=",
            "yul-316-wg.whiskergalaxy.com",
            "yul-316.windscribe.com",
            "38.170.155.242",
            "https://ca-027.whiskergalaxy.com:6363/latency",
            "1000",
            null,
            0
        ),
        Group(
            359,
            "Montreal",
            "Expo 67",
            0,
            "45.51,-73.56",
            "America/Montreal",
            "nfFRpFZ0ZXWVoz8C4gP5ti7V1snFT1gV8EcIxTWJtB4=",
            "yul-359-wg.whiskergalaxy.com",
            "yul-359.windscribe.com",
            "172.98.82.2",
            "https://ca-050.whiskergalaxy.com:6363/latency",
            "10000",
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
            2
        ),
        Group(
            386,
            "Halifax",
            "Crosby",
            1,
            "44.46,-63.61",
            "America/Halifax",
            "w262TI0UyIg9pFunMiekVURYUuT/z4qXRor2Z7VcOn4=",
            "yhz-386-wg.whiskergalaxy.com",
            "yhz-386.windscribe.com",
            "23.191.80.2",
            "https://ca-021.whiskergalaxy.com:6363/latency",
            "1000",
            null,
            0
        )
    )

    val groupsHead = Server(
        7,
        "Canada Bitch",
        "CA",
        1,
        0,
        "CA",
        1,
        "America/Toronto",
        "-5,EST",
        "normal",
        "ca.windscribe.com",
        groupsChild
    )

    val groupsHeadV2ray = Server(
        10,
        "V2ray",
        "US",
        1,
        0,
        "US",
        1,
        "America/Toronto",
        "-5,EST",
        "normal",
        "ca.windscribe.com",
        groupsChild
    )

    val groupsHeadArray = listOf(
        groupsHead,
        groupsHeadV2ray
    )

    val gson = Gson()
    val json = gson.toJson(groupsHeadArray)

    longLog(json)

    return json
}