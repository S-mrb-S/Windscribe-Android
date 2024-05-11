/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.vpn.localdatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import javax.inject.Singleton;

import sp.windscribe.vpn.localdatabase.tables.NetworkInfo;
import sp.windscribe.vpn.localdatabase.tables.PingTestResults;
import sp.windscribe.vpn.localdatabase.tables.PopupNotificationTable;
import sp.windscribe.vpn.localdatabase.tables.ServerStatusUpdateTable;
import sp.windscribe.vpn.localdatabase.tables.UserStatusTable;
import sp.windscribe.vpn.localdatabase.tables.WindNotification;
import sp.windscribe.vpn.serverlist.dao.CityAndRegionDao;
import sp.windscribe.vpn.serverlist.dao.CityDao;
import sp.windscribe.vpn.serverlist.dao.CityDetailDao;
import sp.windscribe.vpn.serverlist.dao.ConfigFileDao;
import sp.windscribe.vpn.serverlist.dao.FavouriteDao;
import sp.windscribe.vpn.serverlist.dao.PingTimeDao;
import sp.windscribe.vpn.serverlist.dao.RegionAndCitiesDao;
import sp.windscribe.vpn.serverlist.dao.RegionDao;
import sp.windscribe.vpn.serverlist.dao.StaticRegionDao;
import sp.windscribe.vpn.serverlist.entity.City;
import sp.windscribe.vpn.serverlist.entity.ConfigFile;
import sp.windscribe.vpn.serverlist.entity.Favourite;
import sp.windscribe.vpn.serverlist.entity.PingTime;
import sp.windscribe.vpn.serverlist.entity.Region;
import sp.windscribe.vpn.serverlist.entity.StaticRegion;

@Database(entities = {PingTestResults.class, UserStatusTable.class, ServerStatusUpdateTable.class,
        PopupNotificationTable.class, Region.class,
        City.class, Favourite.class, PingTime.class, StaticRegion.class, NetworkInfo.class, ConfigFile.class,
        WindNotification.class}, version = 34)
@Singleton
public abstract class WindscribeDatabase extends RoomDatabase {


    public abstract CityAndRegionDao cityAndRegionDao();

    public abstract CityDao cityDao();

    public abstract CityDetailDao cityDetailDao();

    public abstract ConfigFileDao configFileDao();

    public abstract FavouriteDao favouriteDao();

    public abstract NetworkInfoDao networkInfoDao();

    public abstract PingTestDao pingTestDao();

    public abstract PingTimeDao pingTimeDao();

    public abstract PopupNotificationDao popupNotificationDao();

    public abstract RegionAndCitiesDao regionAndCitiesDao();

    public abstract RegionDao regionDao();

    public abstract ServerStatusDao serverStatusDao();

    public abstract StaticRegionDao staticRegionDao();

    public abstract UserStatusDao userStatusDao();

    public abstract WindNotificationDao windNotificationDao();
}
