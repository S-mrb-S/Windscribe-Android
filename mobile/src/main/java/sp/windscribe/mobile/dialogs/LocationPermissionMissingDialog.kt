package sp.windscribe.mobile.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import sp.windscribe.mobile.databinding.LocationPermissionMissingDialogBinding
import sp.windscribe.mobile.utils.PermissionManagerImpl.Companion.okButtonKey
import sp.windscribe.mobile.utils.PermissionManagerImpl.Companion.resultKey

class LocationPermissionMissingDialog : FullScreenDialog() {
    private var binding: LocationPermissionMissingDialogBinding? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = LocationPermissionMissingDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.tvOk?.setOnClickListener {
            setFragmentResult(resultKey, Bundle().apply { putBoolean(okButtonKey, true) })
            dismiss()
        }
        binding?.tvCancel?.setOnClickListener {
            setFragmentResult(resultKey, Bundle())
            dismissAllowingStateLoss()
        }
        dialog?.setOnCancelListener {
            setFragmentResult(resultKey, Bundle())
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}