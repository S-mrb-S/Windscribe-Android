package sp.windscribe.mobile.ticket

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import butterknife.BindView
import butterknife.OnClick
import butterknife.OnItemSelected
import sp.windscribe.mobile.R
import sp.windscribe.mobile.base.BaseActivity
import sp.windscribe.mobile.di.ActivityModule
import sp.windscribe.mobile.dialogs.ErrorDialog
import sp.windscribe.mobile.dialogs.ProgressDialog
import sp.windscribe.mobile.dialogs.SuccessDialog
import sp.windscribe.mobile.welcome.SoftInputAssist
import sp.windscribe.vpn.api.response.QueryType
import sp.windscribe.vpn.commonutils.ThemeUtils
import java.util.Objects
import javax.inject.Inject

class SendTicketActivity : BaseActivity(), SendTicketView, TextWatcher {
    @Inject
    lateinit var presenter: SendTicketPresenter

    @BindView(R.id.btn_send_ticket)
    lateinit var btnSendButton: Button

    @BindView(R.id.tv_current_category)
    lateinit var currentQueryType: TextView

    @BindView(R.id.email)
    lateinit var emailView: AppCompatEditText

    @BindView(R.id.scroll_view)
    lateinit var scrollView: ScrollView

    @BindView(R.id.message)
    lateinit var messageView: AppCompatEditText

    @BindView(R.id.spinner_query)
    lateinit var queryTypeSpinner: Spinner

    @BindView(R.id.subject)
    lateinit var subjectView: AppCompatEditText

    @BindView(R.id.nav_title)
    lateinit var tvActivityTitle: TextView

    private var softInputAssist: SoftInputAssist? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActivityModule(ActivityModule(this, this)).inject(this)
        setContentLayout(R.layout.activity_send_ticket, true)
        softInputAssist = SoftInputAssist(this, intArrayOf())
        presenter.init()
    }

    override fun onResume() {
        super.onResume()
        softInputAssist?.onResume()
    }

    override fun onPause() {
        super.onPause()
        softInputAssist?.onPause()
    }

    override fun onDestroy() {
        softInputAssist?.onDestroy()
        super.onDestroy()
    }

    override fun addTextChangeListener() {
        messageView.addTextChangedListener(this)
        emailView.addTextChangedListener(this)
        subjectView.addTextChangedListener(this)
    }

    override fun afterTextChanged(s: Editable) {
        val message = Objects.requireNonNull(
                messageView.text
        ).toString()
        val email = Objects.requireNonNull(
                emailView.text
        ).toString()
        val subject = Objects.requireNonNull(
                subjectView.text
        ).toString()
        presenter.onInputChanged(email, subject, message)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.nav_button)
    fun onBackButtonPressed() {
        super.onBackPressed()
    }

    override fun onBackPressed() {
        setInputState(true)
        super.onBackPressed()
    }

    @OnClick(R.id.tv_current_category, R.id.img_category_drop_down_btn)
    fun onCurrentQueryTypeClick() {
        queryTypeSpinner.performClick()
    }

    @OnItemSelected(R.id.spinner_query)
    fun onQueryTypeSelected() {
        val queryType = queryTypeSpinner.selectedItem.toString()
        currentQueryType.text = queryType
        QueryType.values()[queryTypeSpinner.selectedItemPosition].let {
            presenter.onQueryTypeSelected(it)
        }
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_send_ticket)
    fun sendTicketClicked() {
        hideKeyBoard()
        messageView.clearFocus()
        presenter.onSendTicketClicked(
                Objects.requireNonNull(
                        emailView.text
                ).toString(), Objects
                .requireNonNull(subjectView.text).toString(),
                Objects.requireNonNull(messageView.text).toString()
        )
    }

    override fun setActivityTitle(title: String) {
        tvActivityTitle.text = title
    }

    override fun setEmail(email: String) {
        emailView.setText(email)
    }

    private fun setInputState(isEnabled: Boolean) {
        queryTypeSpinner.isEnabled = isEnabled
        emailView.isEnabled = isEnabled
        subjectView.isEnabled = isEnabled
        btnSendButton.isEnabled = isEnabled
        messageView.isEnabled = isEnabled
    }

    override fun setErrorLayout(message: String) {
        setInputState(false)
        ErrorDialog.show(this, message)
    }

    override fun setProgressView(show: Boolean) {
        if (show) {
            ProgressDialog.show(this)
        } else {
            ProgressDialog.hide(this)
        }
    }

    override fun setQueryTypeSpinner() {
        val queryAdapter = ArrayAdapter(
                this,
                R.layout.drop_down_layout,
                R.id.tv_drop_down,
                resources.getStringArray(R.array.query_types)
        )
        queryTypeSpinner.adapter = queryAdapter
    }

    override fun setSendButtonState(enabled: Boolean) {
        btnSendButton.isEnabled = enabled
    }

    override fun setSuccessLayout(message: String) {
        emailView.setText("")
        subjectView.setText("")
        messageView.setText("")
        queryTypeSpinner.setSelection(0)
        SuccessDialog.show(
                this,
                message,
                ThemeUtils.getColor(this, R.attr.wdPrimaryInvertedColor, R.color.colorBackgroundDark),
                true
        )
    }

    private fun hideKeyBoard() {
        try {
            currentFocus?.windowToken?.let {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it, 0)
            }
        } catch (ignored: Exception) {
        }
    }

    companion object {
        fun getStartIntent(context: Context?): Intent {
            return Intent(context, SendTicketActivity::class.java)
        }
    }
}