/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.vpn.serverlist.dao;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import sp.windscribe.vpn.serverlist.entity.Region;

@Dao
abstract public class RegionDao {


    @Insert(onConflict = REPLACE)
    public abstract Completable addAll(List<Region> regions);

    public Completable addRegions(List<Region> regions) {
        return deleteAll().andThen(addAll(regions));
    }

    @Query("Delete from Region")
    abstract public Completable deleteAll();

    @Query("Select * from Region where country_code = :countryCode limit 1")
    public abstract Single<Region> getRegionByCountryCode(String countryCode);
}