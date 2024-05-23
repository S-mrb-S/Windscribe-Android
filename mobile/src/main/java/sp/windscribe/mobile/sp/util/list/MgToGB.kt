package sp.windscribe.mobile.sp.util.list

fun mgToGb(num: Int): String {
    return if (num > 0) {
        val gigabytes = num / 1024.0

        // فرمت کردن عدد به دو رقم اعشاری
        val formattedGigabytes = String.format("%.2f", gigabytes)
        "$formattedGigabytes GB"
    } else {
        "00.00 GB"
    }
}