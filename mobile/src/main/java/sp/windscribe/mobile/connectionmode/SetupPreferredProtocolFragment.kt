package sp.windscribe.mobile.connectionmode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import sp.windscribe.mobile.R
import sp.windscribe.vpn.autoconnection.AutoConnectionModeCallback
import sp.windscribe.vpn.autoconnection.ProtocolInformation
import sp.windscribe.vpn.backend.Util

class SetupPreferredProtocolFragment : DialogFragment() {

    @BindView(R.id.title)
    lateinit var titleView: TextView

    private var protocolInformation: ProtocolInformation? = null
    private var autoConnectionModeCallback: AutoConnectionModeCallback? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.setup_preferred_protocol, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        protocolInformation?.let {
            titleView.text = getString(
                    R.string.set_this_protocol_as_preferred, Util.getProtocolLabel(it.protocol)
            )
        } ?: kotlin.run {
            dismiss()
        }
    }

    @OnClick(R.id.cancel, R.id.img_close_btn)
    fun onCancelClick() {
        dismiss()
        autoConnectionModeCallback?.onCancel()
    }

    @OnClick(R.id.set_as_preferred)
    fun onSetAsPreferredProtocolClick() {
        dismiss()
        autoConnectionModeCallback?.onSetAsPreferredClicked()
    }

    companion object {
        fun newInstance(
                protocolInformation: ProtocolInformation?,
                autoConnectionModeCallback: AutoConnectionModeCallback
        ): SetupPreferredProtocolFragment {
            val fragment = SetupPreferredProtocolFragment()
            fragment.protocolInformation = protocolInformation
            fragment.autoConnectionModeCallback = autoConnectionModeCallback
            return fragment
        }
    }
}