/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.vpn.localdatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sp.windscribe.vpn.localdatabase.tables.WindNotification
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface WindNotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(newsFeedNotification: List<WindNotification>): Completable

    @Query("Select * from WindNotification order by date DESC")
    fun getWindNotifications(): Single<List<WindNotification>>
}
