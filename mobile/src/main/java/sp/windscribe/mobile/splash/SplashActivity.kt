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
import sp.windscribe.mobile.mrb.util.getAllServers
import sp.windscribe.mobile.mrb.util.saveDataAndFinish
import sp.windscribe.mobile.welcome.WelcomeActivity
import sp.windscribe.mobile.windscribe.WindscribeActivity
import sp.windscribe.vpn.qq.MmkvManager

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

        if (MmkvManager.getLoginStorage().decodeBool("is_login", false)) {
            setup()
        } else {
            this.navigateToLogin()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun setup() {
        val keyStr = MmkvManager.getLoginStorage().decodeString("key_login", null)

        GlobalScope.launch {
            getAllServers(
                    keyStr!!,
                    {
                        launch {
                            setDataAndLoad(it)
                        }
                    },
                    {
                        failGetServers()
                    }
            )
        }
    }

    private suspend fun setDataAndLoad(data: GetServersQuery.Data?) = coroutineScope {
        saveDataAndFinish(data,
                {
                    navigateToHome()
                },
                {
                    failGetServers()
                }
        )
    }

    private fun failGetServers() {
        runOnUiThread {
            Toast.makeText(
                    this@SplashActivity,
                    "دریافت سرور ها موفقیت امیز نبود! لطفا دوباره وارد شوید",
                    Toast.LENGTH_LONG
            ).show()
            navigateToLogin()
        }
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

    private fun navigateToLogin() {
        logger.info("Navigating to login activity...")
        val loginIntent = Intent(this, WelcomeActivity::class.java)
        startActivity(loginIntent)
        finish()
    }
}