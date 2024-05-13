/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.vpn.localdatabase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import io.reactivex.Completable;
import io.reactivex.Single;
import sp.windscribe.vpn.localdatabase.tables.ServerStatusUpdateTable;

@Dao
public interface ServerStatusDao {

    @Query("SELECT * FROM server_status_update WHERE user_name =:username")
    Single<ServerStatusUpdateTable> getServerStatus(String username);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertOrUpdateStatus(ServerStatusUpdateTable serverStatusUpdateTable);


}
