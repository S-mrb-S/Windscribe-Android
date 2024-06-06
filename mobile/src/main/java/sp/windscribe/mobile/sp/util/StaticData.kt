package sp.windscribe.mobile.sp.util

import androidx.fragment.app.FragmentManager
import sp.windscribe.mobile.GetServersQuery
import sp.windscribe.mobile.dialogs.EmergencyDialogCallback
import sp.windscribe.vpn.sp.Ads

object StaticData {
    var data: GetServersQuery.Data? = null // servers cache for reload protocol
    var fragmentManager: FragmentManager? = null // emergency dialog
    var requestDialogCallback: EmergencyDialogCallback? = null
    var noServer: Boolean = false

    var adsmanager: Ads? = null
}