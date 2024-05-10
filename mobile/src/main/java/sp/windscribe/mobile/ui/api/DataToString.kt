package sp.windscribe.mobile.ui.api

import sp.windscribe.mobile.GetServersQuery

class DataToString {
    data class Node(
        val ip: String,
        val ip2: String,
        val ip3: String,
        val hostname: String,
        val weight: Int,
        val health: Int
    )

    data class Group(
        val id: Int,
        val city: String,
        val nick: String,
        val pro: Int,
        val gps: String,
        val tz: String,
        val wg_pubkey: String,
        val wg_endpoint: String,
        val ovpn_x509: String,
        val ping_ip: String,
        val ping_host: String,
        val link_speed: String,
        val nodes: List<Node>?,
        val health: Int
    )

    data class Location(
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

    fun create(data: GetServersQuery.Data?): String {
        // ساخت یک نمونه از دیتای ارائه شده
        val location = Location(
            id = 7,
            name = "Canada East",
            country_code = "CA",
            status = 1,
            premium_only = 0,
            short_name = "CA",
            p2p = 1,
            tz = "America/Toronto",
            tz_offset = "-5,EST",
            loc_type = "normal",
            dns_hostname = "ca.windscribe.com",
            groups = listOf(
                Group(
                    id = 72,
                    city = "Toronto",
                    nick = "The 6",
                    pro = 0,
                    gps = "43.59,-79.76",
                    tz = "America/Toronto",
                    wg_pubkey = "pKXBuReWe+HfrILovyFzIybA8AVAsFgfDUyo42tLT1g=",
                    wg_endpoint = "yyz-72-wg.whiskergalaxy.com",
                    ovpn_x509 = "yyz-72.windscribe.com",
                    ping_ip = "104.254.92.90",
                    ping_host = "https://ca-009.whiskergalaxy.com:6363/latency",
                    link_speed = "1000",
                    nodes = listOf(
                        Node(
                            ip = "104.254.92.74",
                            ip2 = "104.254.92.75",
                            ip3 = "104.254.92.76",
                            hostname = "ca-037.whiskergalaxy.com",
                            weight = 1,
                            health = 33
                        )
                    ),
                    health = 33
                ),
                // اضافه کردن گروه‌های دیگر به همین صورت
            )
        ).toString()

        return location
    }

}