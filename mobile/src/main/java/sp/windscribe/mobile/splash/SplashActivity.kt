package sp.windscribe.mobile.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.apollographql.apollo3.api.Error
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sp.windscribe.mobile.R
import sp.windscribe.mobile.welcome.WelcomeActivity
import sp.windscribe.mobile.windscribe.WindscribeActivity
import org.slf4j.LoggerFactory
import sp.windscribe.mobile.GetServersQuery
import sp.windscribe.mobile.base.BaseActivity
import sp.windscribe.mobile.di.ActivityModule
import sp.windscribe.mobile.di.DaggerActivityComponent
import sp.windscribe.mobile.ui.api.GetServersWithKeyQuery
import sp.windscribe.vpn.Windscribe.Companion.appContext

import sp.windscribe.vpn.qq.MmkvManager
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity(), SplashView {
    private val logger = LoggerFactory.getLogger("splash_a")

    @Inject
    lateinit var presenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        DaggerActivityComponent.builder().activityModule(ActivityModule(this, this))
            .applicationComponent(
                appContext
                    .applicationComponent
            ).build().inject(this)
        if (Build.VERSION.SDK_INT >= 23) {
            splashScreen.setKeepOnScreenCondition { true }
        } else {
            setContentView(R.layout.activity_splash)
        }

        logger.info("OnCreate: Splash Activity")

        if(MmkvManager.getLoginStorage().decodeBool("is_login", false)){
            this.setup()
        }else{
            this.navigateToLogin()
        }

//        presenter.checkNewMigration()
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun setup(){
        GlobalScope.launch {
            try{
                val key = MmkvManager.getLoginStorage().decodeString("key_login", null)

                GetServersWithKeyQuery().performWork(
                    key!!,
                    object : GetServersWithKeyQuery.GetServersCallback {
                        override fun onSuccess(data: GetServersQuery.Data?) {
                            try{
                                presenter.setDataAndLoad(data)
                            }catch (e:Exception){
                                Log.d("MRT", "ERR catch2: " + e.toString())
                                failGetServers()
                            }
                        }

                        override fun onFailure(errors: List<Error>?) {
                            Log.d("MRT", "ERR failer" + errors.toString())
                            failGetServers()
                        }

                    })
            }catch (e: Exception){
                Log.d("MRT", "ERR catch: " + e.toString())
                failGetServers()
            }
        }

    }

    fun failGetServers(){
        runOnUiThread {
            Toast.makeText(this@SplashActivity, "دریافت سرور ها موفقیت امیز نبود! لطفا دوباره وارد شوید", Toast.LENGTH_LONG).show()
            navigateToLogin()
        }
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun navigateToAccountSetUp() {
        TODO("Not yet implemented")
    }

    override fun navigateToHome() {
        logger.info("Navigating to home activity...")
        val homeIntent = Intent(this, WindscribeActivity::class.java)
        if (intent.extras != null) {
            logger.debug("Forwarding intent extras home activity.")
            homeIntent.putExtras(intent.extras!!)
        }
        startActivity(homeIntent)
        finish()
    }

    override fun navigateToLogin() {
        logger.info("Navigating to login activity...")
        val loginIntent = Intent(this, WelcomeActivity::class.java)
        startActivity(loginIntent)
        finish()
    }
}