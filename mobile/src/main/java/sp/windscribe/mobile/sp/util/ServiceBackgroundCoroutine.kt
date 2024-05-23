//package sp.windscribe.mobile.sp.util
//
//import de.blinkt.openvpn.core.OpenVPNThread
//import dev.dev7.lib.v2ray.V2rayController
//import kotlinx.coroutines.*
//import sp.openconnect.core.OpenVpnService
//import sp.openconnect.core.VPNConnector
//import sp.windscribe.mobile.sp.util.api.updateService
//import sp.windscribe.vpn.sp.Data
//
// runtime update
//class ServiceBackgroundCoroutine {
//
//    private var job: Job? = null
//    private var stopService: Boolean = false
//
//    fun start() {
//        job = CoroutineScope(Dispatchers.Default).launch {
//            val licence = Data.serviceStorage.decodeString("key_login", "")
//            if(licence.isNullOrEmpty()) {
//                stop()
//                return@launch
//            }
//
//                Data.static.showToast("سرویس شروع شد")
//
//
//            while (isActive) {
//                delay(5000)
//
//                updateService(licence.toString(), {},
//                        {
//
//                                Data.static.showToast("اجرای سرویس موفقیت امیز نبود!")
//                            Data.static.showToast("سرویس شما تا 15 ثانیه دیگر قطع میشود!")
//
//                                stopService = true
//
//                        })
//
//                if(stopService){
//                    // ۱۵ ثانیه تاخیر
//                    delay(15000)
//                    try {
//                        V2rayController.stopV2ray(Data.static.mainApplication) // v2ray
//                        OpenVPNThread.stop() // openvpn
//
//                        // cisco
//                        val cc = object : VPNConnector(Data.static.mainApplication, false) {
//                            override fun onUpdate(service: OpenVpnService) {}
//                        }
//                        // IDN
//                        // Hard stop
//                        try{
//                            cc.stop()
//                        }catch (ignore: Exception){}
//                        try{
//                            cc.unbind()
//                        }catch (ignore: Exception){}
//                        try{
//                            cc.stopActiveDialog()
//                        }catch (ignore: Exception){}
//                        try{
//                            cc.service.stopVPN()
//                        }catch (ignore: Exception){}
//                    }catch (ignore: Exception){}
//
//                    stop()
//                    return@launch
//                }
//                // ۱۵ ثانیه تاخیر
//                delay(10000)
//            }
//        }
//    }
//
//    fun stop() {
//
//            Data.static.showToast("سرویس متوقف شد!")
//
//
//        job?.cancel()
//    }
//}
