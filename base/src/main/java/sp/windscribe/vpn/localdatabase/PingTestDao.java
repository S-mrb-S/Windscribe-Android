/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.vpn.localdatabase;

import androidx.room.Dao;
import androidx.room.Query;

import sp.windscribe.vpn.localdatabase.tables.PingTestResults;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface PingTestDao {

    @Query("SELECT * FROM ping_results")
    Single<List<PingTestResults>> getPingList();

}
