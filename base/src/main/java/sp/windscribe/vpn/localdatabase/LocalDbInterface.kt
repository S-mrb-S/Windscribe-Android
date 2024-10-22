package sp.windscribe.vpn.localdatabase

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import sp.windscribe.vpn.localdatabase.tables.NetworkInfo
import sp.windscribe.vpn.localdatabase.tables.PingTestResults
import sp.windscribe.vpn.localdatabase.tables.PopupNotificationTable
import sp.windscribe.vpn.localdatabase.tables.ServerStatusUpdateTable
import sp.windscribe.vpn.localdatabase.tables.UserStatusTable
import sp.windscribe.vpn.localdatabase.tables.WindNotification
import sp.windscribe.vpn.serverlist.entity.City
import sp.windscribe.vpn.serverlist.entity.CityAndRegion
import sp.windscribe.vpn.serverlist.entity.ConfigFile
import sp.windscribe.vpn.serverlist.entity.Favourite
import sp.windscribe.vpn.serverlist.entity.PingTime
import sp.windscribe.vpn.serverlist.entity.Region
import sp.windscribe.vpn.serverlist.entity.RegionAndCities
import sp.windscribe.vpn.serverlist.entity.StaticRegion

interface LocalDbInterface {
    fun addConfig(configFile: ConfigFile): Completable
    fun addNetwork(networkInfo: NetworkInfo): Single<Long>
    fun addPing(pingTime: PingTime): Completable
    fun addStaticRegions(staticRegions: List<StaticRegion>): Completable
    fun addToCities(cities: List<City>): Completable
    fun rmCities(): Completable
    fun rmRegions(): Completable
    fun addToFavourites(favourite: Favourite): Single<Long>
    fun addToPopupNotification(popupNotificationTable: PopupNotificationTable)
    fun addToRegions(regions: List<Region>): Completable
    fun clearAllTables()
    fun delete(favourite: Favourite)
    fun delete(id: Int): Completable
    fun deleteAllCities(): Completable
    fun deleteNetwork(networkName: String): Single<Int>
    fun getAllCities(id: Int): Single<List<City>>
    val allConfigs: Single<List<ConfigFile>>
    val allNetworksWithUpdate: Flowable<List<NetworkInfo>>
    val allPingTimes: Single<List<PingTime>>
    val allPings: Single<List<PingTestResults>>
    val allRegion: Single<List<RegionAndCities>>
    val allStaticRegions: Single<List<StaticRegion>>
    val allStaticRegionsFlowAble: Flowable<List<StaticRegion>>
    val cities: Single<List<City>>
    val pingableCities: Single<List<City>>
    fun getCitiesByRegion(regionID: Int, pro: Int): Single<Int>
    val city: Single<CityAndRegion>
    fun getCityAndRegionByID(cityAndRegionID: Int): Single<CityAndRegion>
    fun getCityByID(cityID: Int): Single<City>
    fun getCityByID(ids: IntArray?): Single<List<City>>
    fun getConfigFile(configFileID: Int): Single<ConfigFile>
    fun getCordsByRegionId(regionId: Int): Single<String>
    val favourites: Single<List<Favourite>>
    fun getFreePingIdFromTime(pro: Boolean, pingTime: Int): Single<Int>
    val lowestPing: Single<Int>
    fun getLowestPingForFreeUser(pro: Boolean): Single<Int>
    val lowestPingId: Single<Int>
    val maxPrimaryKey: Single<Int>
    fun getNetwork(networkName: String): Single<NetworkInfo>
    fun getPingIdFromTime(pingTime: Int): Single<Int>
    fun getPopupNotifications(userName: String): Flowable<List<PopupNotificationTable>>
    fun getRegion(id: Int): Single<RegionAndCities>
    fun getRegionByCountryCode(countryCode: String): Single<Region>
    fun getRegionIdFromCity(cityID: Int): Single<Int>
    fun getServerStatus(username: String): Single<ServerStatusUpdateTable>
    fun getStaticRegionByID(id: Int): Single<StaticRegion>
    val staticRegionCount: Single<Int>
    fun getUserStatus(username: String): Flowable<UserStatusTable>
    val windNotifications: Single<List<WindNotification>>
    fun insertOrUpdateServerUpdateStatusTable(serverStatusUpdateTable: ServerStatusUpdateTable)
    fun insertOrUpdateStatus(serverStatusUpdateTable: ServerStatusUpdateTable): Completable
    fun insertWindNotifications(windNotifications: List<WindNotification>): Completable
    fun updateNetwork(networkInfo: NetworkInfo): Single<Int>
    fun updateUserStatus(userStatusTable: UserStatusTable?): Completable
    fun getCityAndRegion(cityId: Int): CityAndRegion
}