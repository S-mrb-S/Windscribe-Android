/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.vpn.localdatabase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import sp.windscribe.vpn.localdatabase.tables.PopupNotificationTable;

@Dao
public interface PopupNotificationDao {

    @Query("SELECT * FROM notification_table WHERE user_name =:userName")
    Flowable<List<PopupNotificationTable>> getPopupNotification(String userName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPopupNotification(PopupNotificationTable popupNotificationTable);


}
