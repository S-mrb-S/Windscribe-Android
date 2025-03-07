/*
 * Copyright (c) 2021 Windscribe Limited.
 */

package sp.windscribe.vpn.serverlist.dao;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Single;
import sp.windscribe.vpn.serverlist.entity.Favourite;

@Dao
public interface FavouriteDao {

    @Insert(onConflict = REPLACE)
    Single<Long> addToFavourites(Favourite favourite);

    @Delete
    void delete(Favourite favourite);

    @Query("Select * from Favourite")
    Single<List<Favourite>> getFavourites();
}
