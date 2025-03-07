package sp.windscribe.mobile.welcome.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sp.windscribe.mobile.R
import sp.windscribe.mobile.databinding.FragmentEmergencyConnectBinding
import sp.windscribe.mobile.dialogs.EditConfigFileDialogCallback
import sp.windscribe.mobile.dialogs.EmergencyDialogCallback
import sp.windscribe.mobile.sp.OperationManager
import sp.windscribe.mobile.sp.util.StaticData
import sp.windscribe.mobile.welcome.WelcomeActivity
import sp.windscribe.mobile.welcome.state.EmergencyConnectUIState
import sp.windscribe.mobile.welcome.viewmodal.EmergencyConnectViewModal
import sp.windscribe.mobile.windscribe.WindscribeActivity
import sp.windscribe.vpn.serverlist.entity.ConfigFile
import sp.windscribe.vpn.sp.Data
import sp.xray.lite.util.Utils

class EmergencyConnectFragment : Fragment(), EmergencyDialogCallback {
    private var _binding: FragmentEmergencyConnectBinding? = null
    private val viewModal: EmergencyConnectViewModal? by lazy {
        return@lazy (activity as? WelcomeActivity)?.emergencyConnectViewModal?.value
    }
    private val ac: WelcomeActivity? by lazy {
        return@lazy (activity as? WelcomeActivity)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmergencyConnectBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        bindState()
        StaticData.fragmentManager = activity?.supportFragmentManager
        StaticData.requestDialogCallback = this //as? EmergencyDialogCallback
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun bindState() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
//                viewModal?.uiState?.collectLatest {
//                    when (it) {
//                        EmergencyConnectUIState.Disconnected -> {
//                            _binding?.tvDescription?.visibility = View.VISIBLE
//                            _binding?.tvStatus?.visibility = View.INVISIBLE
//                            _binding?.progressBar?.visibility = View.INVISIBLE
//                            _binding?.tvDescription?.text =
//                                    getString(R.string.emergency_connect_description)
//                            _binding?.ok?.text = getString(R.string.connect)
//
//                        }
//
//                        EmergencyConnectUIState.Connecting -> {
//                            _binding?.tvDescription?.visibility = View.INVISIBLE
//                            _binding?.tvStatus?.visibility = View.VISIBLE
//                            _binding?.progressBar?.visibility = View.VISIBLE
//                            _binding?.ok?.text = getString(R.string.disconnect)
//                        }
//
//                        EmergencyConnectUIState.Connected -> {
//                            _binding?.tvDescription?.visibility = View.VISIBLE
//                            _binding?.tvStatus?.visibility = View.INVISIBLE
//                            _binding?.progressBar?.visibility = View.INVISIBLE
//                            _binding?.tvDescription?.text =
//                                    getString(R.string.emergency_connected_description)
//                            _binding?.ok?.text = getString(R.string.disconnect)
//                        }
//                    }
//                }
//            }
//        }
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModal?.connectionProgressText?.collect {
//                    _binding?.tvStatus?.text = it //
//                }
//            }
//        }
    }

    private fun getAndroidId(): String {
        // Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        return Utils.getDeviceIdForXUDPBaseKey()
    }

    private fun bindViews() {
        _binding?.ok?.setOnClickListener {
            viewModal?.connectButtonClick()
        }
        _binding?.cancel?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        _binding?.closeIcon?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    override fun onSubmitEmail(email: String?) {
        _binding?.tvDescription?.visibility = View.VISIBLE
        _binding?.progressBar?.visibility = View.VISIBLE
        _binding?.tvStatus?.visibility = View.VISIBLE
        if(email == ""){
            _binding?.tvDescription?.visibility = View.VISIBLE
            _binding?.tvStatus?.visibility = View.INVISIBLE
            _binding?.progressBar?.visibility = View.INVISIBLE
            return
        }
        OperationManager.startServiceOperation(
            getAndroidId(),
            {
                _binding?.tvStatus?.text = ";)"
                Data.serviceStorage.encode("is_login", true)
                ac?.gotoHomeActivity(true)
                _binding?.progressBar?.visibility = View.INVISIBLE
            },
            {
                if (it) {
                    _binding?.tvStatus?.text = "Failer. (key)"
                } else {
                    _binding?.tvStatus?.text = "Failer."
                }

                _binding?.progressBar?.visibility = View.INVISIBLE
            }, justUpdateService = false, test = true, email = email.toString()
        )
    }

    companion object {
        private const val backStackKey = "EmergencyConnectFragment"
        fun show(manager: FragmentManager, container: Int) {
            val fragment = EmergencyConnectFragment()
            manager.beginTransaction().addToBackStack(backStackKey).add(container, fragment)
                    .commit()
        }
    }
}