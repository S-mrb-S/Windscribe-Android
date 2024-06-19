package sp.windscribe.mobile.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import sp.windscribe.mobile.GetServersQuery
import sp.windscribe.mobile.R
import sp.windscribe.mobile.sp.OperationManager
import sp.windscribe.mobile.welcome.WelcomeActivity
import sp.windscribe.mobile.windscribe.WindscribeActivity
import sp.windscribe.vpn.sp.CheckInternetConnection
import sp.windscribe.vpn.sp.Data
import sp.windscribe.vpn.sp.MmkvManager

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val logger = LoggerFactory.getLogger("splash_a")

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 23) {
            splashScreen.setKeepOnScreenCondition { true }
        } else {
            setContentView(R.layout.activity_splash)
        }

        logger.info("OnCreate: Splash Activity")

        if (Data.serviceStorage.decodeBool("is_login", false)) {
            checkInternetLayer()
        } else {
            this.navigateToLogin()
        }
    }

    private fun checkInternetLayer() {
        if (CheckInternetConnection.netCheck(this)) {
            OperationManager.startServiceOperation(Data.serviceStorage.decodeString("key_login", null).toString(),
                {
                    navigateToHome()
                },
                {
                    if(it){
                        failGetServers(true) } else {
                        failGetServers(false) }
                }, false)
        } else {
            threadCheckInternet()
        }
    }

    private fun threadCheckInternet() {
        object : Thread() {
            var isShowText = true
            var isThread = false
            override fun run() {
                try {
                    while (!this.isInterrupted) {
                        sleep(2000) // ui refresh
                        if (isThread) {
                            break
                        }
                        runOnUiThread {
                            if (isShowText) {
                                isShowText = false
                                Toast.makeText(this@SplashActivity, "No internet connection", Toast.LENGTH_SHORT).show()
                            }
                            if (CheckInternetConnection.netCheck(this@SplashActivity)) {
                                checkInternetLayer()
                                isThread = true
                            }
                        }
                    }
                } catch (e: InterruptedException) {
                    isThread = true
                    checkInternetLayer()
                }
            }
        }.start()
    }

    private fun navigateToHome() {
        logger.info("Navigating to home activity...")
        val homeIntent = Intent(this, WindscribeActivity::class.java)
        if (intent.extras != null) {
            logger.debug("Forwarding intent extras home activity.")
            homeIntent.putExtras(intent.extras!!)
        }
        startActivity(homeIntent)
        finish()
    }

    private fun failGetServers(w: Boolean) {
        runOnUiThread {
            if(w){
                Toast.makeText(
                    this@SplashActivity,
                    "ورود موفق امیز نبود! لطفا دوباره وارد شوید",
                    Toast.LENGTH_LONG
                ).show()
                Data.serviceStorage.encode("is_login", false)
                navigateToLogin()
            }else{
                Toast.makeText(
                    this@SplashActivity,
                    "دریافت اطلاعات موفق امیز نبود! اینترنت خود را بررسی کنید",
                    Toast.LENGTH_LONG
                ).show()
                navigateToHome()
            }
        }
    }

    private fun navigateToLogin() {
        logger.info("Navigating to login activity...")
        val loginIntent = Intent(this, WelcomeActivity::class.java)
        startActivity(loginIntent)
        finish()
    }
}