package sp.windscribe.vpn.api

import io.reactivex.Single
import sp.windscribe.vpn.api.response.AddEmailResponse
import sp.windscribe.vpn.api.response.ApiErrorResponse
import sp.windscribe.vpn.api.response.BestLocationResponse
import sp.windscribe.vpn.api.response.BillingPlanResponse
import sp.windscribe.vpn.api.response.ClaimAccountResponse
import sp.windscribe.vpn.api.response.GenericResponseClass
import sp.windscribe.vpn.api.response.GenericSuccess
import sp.windscribe.vpn.api.response.GetMyIpResponse
import sp.windscribe.vpn.api.response.Latency
import sp.windscribe.vpn.api.response.NewsFeedNotification
import sp.windscribe.vpn.api.response.PortMapResponse
import sp.windscribe.vpn.api.response.RegToken
import sp.windscribe.vpn.api.response.RobertFilterResponse
import sp.windscribe.vpn.api.response.RobertSettingsResponse
import sp.windscribe.vpn.api.response.ServerCredentialsResponse
import sp.windscribe.vpn.api.response.StaticIPResponse
import sp.windscribe.vpn.api.response.TicketResponse
import sp.windscribe.vpn.api.response.UserLoginResponse
import sp.windscribe.vpn.api.response.UserRegistrationResponse
import sp.windscribe.vpn.api.response.UserSessionResponse
import sp.windscribe.vpn.api.response.VerifyExpressLoginResponse
import sp.windscribe.vpn.api.response.WebSession
import sp.windscribe.vpn.api.response.WgConnectResponse
import sp.windscribe.vpn.api.response.WgInitResponse
import sp.windscribe.vpn.api.response.XPressLoginCodeResponse
import sp.windscribe.vpn.api.response.XPressLoginVerifyResponse

interface IApiCallManager {

    fun addUserEmailAddress(extraParams: Map<String, String>? = null): Single<GenericResponseClass<AddEmailResponse?, ApiErrorResponse?>>
    fun checkConnectivityAndIpAddress(extraParams: Map<String, String>? = null): Single<GenericResponseClass<String?, ApiErrorResponse?>>
    fun getConnectedIp(): Single<GenericResponseClass<String?, ApiErrorResponse?>>
    fun claimAccount(extraParams: Map<String, String>? = null): Single<GenericResponseClass<ClaimAccountResponse?, ApiErrorResponse?>>

    //    fun getAccessIp(accessIpMap: Map<String, String>? = null): Single<GenericResponseClass<AccessIpResponse?, ApiErrorResponse?>>
    fun getBestLocation(extraParams: Map<String, String>? = null): Single<GenericResponseClass<BestLocationResponse?, ApiErrorResponse?>>
    fun getBillingPlans(extraParams: Map<String, String>? = null): Single<GenericResponseClass<BillingPlanResponse?, ApiErrorResponse?>>
    fun getMyIp(extraParams: Map<String, String>? = null): Single<GenericResponseClass<GetMyIpResponse?, ApiErrorResponse?>>
    fun getNotifications(extraParams: Map<String, String>? = null): Single<GenericResponseClass<NewsFeedNotification?, ApiErrorResponse?>>
    fun getPortMap(extraParams: Map<String, String>? = null): Single<GenericResponseClass<PortMapResponse?, ApiErrorResponse?>>
    fun getReg(extraParams: Map<String, String>? = null): Single<GenericResponseClass<RegToken?, ApiErrorResponse?>>
    fun getServerConfig(extraParams: Map<String, String>? = null): Single<GenericResponseClass<String?, ApiErrorResponse?>>
    fun getServerCredentials(extraParams: Map<String, String>? = null): Single<GenericResponseClass<ServerCredentialsResponse?, ApiErrorResponse?>>
    fun getServerCredentialsForIKev2(extraParams: Map<String, String>? = null): Single<GenericResponseClass<ServerCredentialsResponse?, ApiErrorResponse?>>
    fun getServerList(
        extraParams: Map<String, String>? = null,
        billingPlan: String?,
        locHash: String?,
        alcList: String?,
        overriddenCountryCode: String?
    ): Single<GenericResponseClass<String?, ApiErrorResponse?>>

    fun getSessionGeneric(
        extraParams: Map<String, String>? = null,
        protect: Boolean = false
    ): Single<GenericResponseClass<UserSessionResponse?, ApiErrorResponse?>>

    fun getSessionGeneric(extraParams: Map<String, String>? = null): Single<GenericResponseClass<UserSessionResponse?, ApiErrorResponse?>>
    fun getSessionGenericInConnectedState(extraParams: Map<String, String>? = null): Single<GenericResponseClass<UserSessionResponse?, ApiErrorResponse?>>
    fun getStaticIpList(extraParams: Map<String, String>? = null): Single<GenericResponseClass<StaticIPResponse?, ApiErrorResponse?>>
    fun logUserIn(extraParams: Map<String, String>? = null): Single<GenericResponseClass<UserLoginResponse?, ApiErrorResponse?>>
    fun getWebSession(extraParams: Map<String, String>? = null): Single<GenericResponseClass<WebSession?, ApiErrorResponse?>>
    fun recordAppInstall(extraParams: Map<String, String>? = null): Single<GenericResponseClass<String?, ApiErrorResponse?>>
    fun resendUserEmailAddress(extraParams: Map<String, String>? = null): Single<GenericResponseClass<AddEmailResponse?, ApiErrorResponse?>>
    fun sendTicket(map: Map<String, String>? = null): Single<GenericResponseClass<TicketResponse?, ApiErrorResponse?>>
    fun signUserIn(extraParams: Map<String, String>? = null): Single<GenericResponseClass<UserRegistrationResponse?, ApiErrorResponse?>>
    fun verifyPurchaseReceipt(extraParams: Map<String, String>? = null): Single<GenericResponseClass<GenericSuccess?, ApiErrorResponse?>>
    fun verifyExpressLoginCode(extraParams: Map<String, String>? = null): Single<GenericResponseClass<VerifyExpressLoginResponse?, ApiErrorResponse?>>
    fun generateXPressLoginCode(extraParams: Map<String, String>? = null): Single<GenericResponseClass<XPressLoginCodeResponse?, ApiErrorResponse?>>
    fun verifyXPressLoginCode(extraParams: Map<String, String>? = null): Single<GenericResponseClass<XPressLoginVerifyResponse?, ApiErrorResponse?>>
    fun postDebugLog(extraParams: Map<String, String>? = null): Single<GenericResponseClass<GenericSuccess?, ApiErrorResponse?>>
    fun postPromoPaymentConfirmation(extraParams: Map<String, String>? = null): Single<GenericResponseClass<GenericSuccess?, ApiErrorResponse?>>
    fun getRobertSettings(extraParams: Map<String, String>? = null): Single<GenericResponseClass<RobertSettingsResponse?, ApiErrorResponse?>>
    fun getRobertFilters(extraParams: Map<String, String>? = null): Single<GenericResponseClass<RobertFilterResponse?, ApiErrorResponse?>>
    fun updateRobertSettings(extraParams: Map<String, String>? = null): Single<GenericResponseClass<GenericSuccess?, ApiErrorResponse?>>
    fun syncRobert(extraParams: Map<String, String>? = null): Single<GenericResponseClass<GenericSuccess?, ApiErrorResponse?>>
    fun deleteSession(extraParams: Map<String, String>? = null): Single<GenericResponseClass<GenericSuccess?, ApiErrorResponse?>>
    fun wgInit(
        extraParams: Map<String, String>? = null,
        protect: Boolean
    ): Single<GenericResponseClass<WgInitResponse?, ApiErrorResponse?>>

    fun wgConnect(
        extraParams: Map<String, String>? = null,
        protect: Boolean
    ): Single<GenericResponseClass<WgConnectResponse?, ApiErrorResponse?>>

    fun sendDecoyTraffic(
        url: String,
        data: String,
        sizeToReceive: String?
    ): Single<GenericResponseClass<String?, ApiErrorResponse?>>

    suspend fun getLatency(
        url: String,
        ip: String
    ): Result<GenericResponseClass<Latency?, ApiErrorResponse?>>
}
