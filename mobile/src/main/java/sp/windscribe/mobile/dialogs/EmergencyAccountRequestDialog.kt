package sp.windscribe.mobile.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import sp.windscribe.mobile.R
import sp.windscribe.mobile.databinding.EmailRequestTestBinding
import sp.windscribe.vpn.serverlist.entity.ConfigFile
import sp.windscribe.vpn.sp.Data

interface EmergencyDialogCallback {
    fun onSubmitEmail(email: String?)
}

class EmergencyAccountRequestDialog : FullScreenDialog() {
    private var requestDialogCallback: EmergencyDialogCallback? = null
    private var binding: EmailRequestTestBinding? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        requestDialogCallback = context as? EmergencyDialogCallback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = EmailRequestTestBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            binding?.requestAlertOk?.setOnClickListener {
                dismiss()
                requestDialogCallback?.onSubmitEmail(binding?.email?.text.toString())
            }
            binding?.requestAlertCancel?.setOnClickListener {
                dismiss()
                requestDialogCallback?.onSubmitEmail("")
            }
    }

    companion object {
        const val tag = "EmergencyAccountRequestDialog"
        private const val configFileKey = "configFile"

        fun show(fr: FragmentManager) {
            Data.static.MainApplicationExecuter({
                kotlin.runCatching {
                    EmergencyAccountRequestDialog().apply {
                        arguments = Bundle().apply {
                            putSerializable(configFileKey, tag) // no effect
                        }
                    }.showNow(fr, tag)
                }
            }, Data.static.mainApplication)
        }
    }

}