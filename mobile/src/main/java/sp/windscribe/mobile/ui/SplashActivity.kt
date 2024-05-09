package sp.windscribe.mobile.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.apollographql.apollo3.api.Error
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sp.windscribe.mobile.R
import sp.windscribe.mobile.welcome.WelcomeActivity
import sp.windscribe.mobile.windscribe.WindscribeActivity
import org.slf4j.LoggerFactory
import sp.windscribe.mobile.GetLoginQuery
import sp.windscribe.mobile.GetServersQuery
import sp.windscribe.mobile.ui.api.GetLoginWithKeyQuery
import sp.windscribe.mobile.ui.api.GetServersWithKeyQuery

import sp.windscribe.vpn.qq.MmkvManager

@SuppressLint("CustomSplashScreen")
class SplashActivity: AppCompatActivity() {
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

        if(MmkvManager.getLoginStorage().decodeBool("is_login", false)){
            setup()
        }else{
            navigateToLogin()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun setup(){
        GlobalScope.launch {
                GetServersWithKeyQuery().performWork("username",
                    object : GetServersWithKeyQuery.GetServersCallback {
                        override fun onSuccess(data: GetServersQuery.Data?) {
                            navigateToHome()
                        }

                        override fun onFailure(errors: List<Error>?) {
                            Toast.makeText(this@SplashActivity, "دریافت سرور ها موفقیت امیز نبود! لطفا دوباره وارد شوید", Toast.LENGTH_LONG).show()
                            navigateToLogin()
                        }

                    })
        }
    }

    fun navigateToHome() {
        logger.info("Navigating to home activity...")
        val homeIntent = Intent(this, WindscribeActivity::class.java)
        if (intent.extras != null) {
            logger.debug("Forwarding intent extras home activity.")
            homeIntent.putExtras(intent.extras!!)
        }
        startActivity(homeIntent)
        finish()
    }

    fun navigateToLogin() {
        logger.info("Navigating to login activity...")
        val loginIntent = Intent(this, WelcomeActivity::class.java)
        startActivity(loginIntent)
        finish()
    }
}