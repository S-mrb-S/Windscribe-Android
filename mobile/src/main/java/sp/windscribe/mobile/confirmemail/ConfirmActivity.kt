package sp.windscribe.mobile.confirmemail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.work.Data
import butterknife.BindView
import butterknife.OnClick
import sp.windscribe.mobile.R
import sp.windscribe.mobile.base.BaseActivity
import sp.windscribe.mobile.di.ActivityModule
import sp.windscribe.mobile.email.AddEmailActivity
import sp.windscribe.vpn.Windscribe
import javax.inject.Inject

class ConfirmActivity : BaseActivity(), ConfirmEmailView {

    @Inject
    lateinit var presenter: ConfirmEmailPresenter

    @BindView(R.id.description)
    lateinit var descriptionView: TextView

    @BindView(R.id.progress_view)
    lateinit var progressView: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActivityModule(ActivityModule(this, this)).inject(this)
        setContentLayout(R.layout.activity_confirm, true)
        presenter.init(intent.getStringExtra(ReasonToConfirmEmail))
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun finishActivity() {
        Windscribe.appContext.workManager.updateSession(Data.EMPTY)
        finish()
    }

    @OnClick(R.id.change_email)
    fun onChangeEmailClicked() {
        startActivity(Intent(this, AddEmailActivity::class.java))
        finish()
    }

    @OnClick(R.id.close)
    fun onCloseClicked() {
        finishActivity()
    }

    @OnClick(R.id.resend_email)
    fun onResendEmailClicked() {
        presenter.resendVerificationEmail()
    }

    override fun setReasonToConfirmEmail(reasonForConfirmEmail: String) {
        descriptionView.text = reasonForConfirmEmail
    }

    override fun showEmailConfirmProgress(show: Boolean) {
        runOnUiThread { progressView.visibility = if (show) View.VISIBLE else View.GONE }
    }

    override fun showToast(toast: String) {
        runOnUiThread { Toast.makeText(this@ConfirmActivity, toast, Toast.LENGTH_SHORT).show() }
    }

    companion object {
        const val ReasonToConfirmEmail = "reasonToConfirmEmail"
        @JvmStatic
        fun getStartIntent(context: Context?): Intent {
            return Intent(context, ConfirmActivity::class.java)
        }
    }
}