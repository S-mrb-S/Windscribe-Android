package sp.windscribe.mobile.robert

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.OnClick
import sp.windscribe.mobile.R
import sp.windscribe.mobile.adapter.RobertSettingsAdapter
import sp.windscribe.mobile.base.BaseActivity
import sp.windscribe.mobile.di.ActivityModule
import sp.windscribe.mobile.dialogs.ErrorDialog
import sp.windscribe.mobile.dialogs.ProgressDialog
import sp.windscribe.mobile.utils.UiUtil
import javax.inject.Inject

class RobertSettingsActivity : BaseActivity(), RobertSettingsView {
    @BindView(R.id.cl_custom_rules)
    lateinit var clCustomRules: ConstraintLayout

    @BindView(R.id.tv_custom_rules)
    lateinit var customRulesLabel: TextView

    @BindView(R.id.nav_title)
    lateinit var activityTitleView: TextView

    @BindView(R.id.custom_rules_arrow)
    lateinit var customRulesArrow: ImageView

    @BindView(R.id.custom_rules_progress)
    lateinit var customRulesProgressView: ProgressBar

    @Inject
    lateinit var presenter: RobertSettingsPresenter

    @BindView(R.id.recycle_settings_view)
    lateinit var recyclerSettingsView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActivityModule(ActivityModule(this, this)).inject(this)
        setContentLayout(R.layout.activity_robert_settings, true)
        presenter.init()
        customRulesArrow.tag = R.drawable.link_arrow_icon
        UiUtil.setupOnTouchListener(
                container = clCustomRules,
                iconView = customRulesArrow,
                textView = customRulesLabel
        )
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun hideProgress() {
        ProgressDialog.hide(this)
    }

    override fun openUrl(url: String) {
        openURLInBrowser(url)
    }

    override fun setAdapter(robertSettingsAdapter: RobertSettingsAdapter) {
        recyclerSettingsView.layoutManager = LinearLayoutManager(this)
        recyclerSettingsView.adapter = robertSettingsAdapter
    }

    override fun setTitle(title: String) {
        activityTitleView.text = title
    }

    override fun setWebSessionLoading(loading: Boolean) {
        customRulesArrow.visibility = if (loading) View.GONE else View.VISIBLE
        customRulesProgressView.visibility =
                if (loading) View.VISIBLE else View.GONE
        clCustomRules.isEnabled = !loading
    }

    override fun showError(error: String) {
        ErrorDialog.show(this, error)
    }

    override fun showErrorDialog(error: String) {
        ErrorDialog.show(this, error)
    }

    override fun showProgress() {
        ProgressDialog.show(this)
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @OnClick(R.id.nav_button)
    fun onBackButtonClick() {
        onBackPressed()
    }

    @OnClick(R.id.cl_custom_rules)
    fun onCustomRulesClick() {
        presenter.onCustomRulesClick()
    }

    @OnClick(R.id.learn_more)
    fun onLearnMoreClick() {
        presenter.onLearnMoreClick()
    }

    companion object {
        fun getStartIntent(context: Context?): Intent {
            return Intent(context, RobertSettingsActivity::class.java)
        }
    }
}