package sp.windscribe.vpn.serverlist.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.Single
import sp.windscribe.vpn.serverlist.entity.RegionAndCities

@Dao
interface RegionAndCitiesDao {
    @Transaction
    @Query("select * from Region")
    fun getAllRegion(): Single<List<RegionAndCities>>

    @Transaction
    @Query("select * from Region where region_id = :id")
    fun getRegion(id: Int): Single<RegionAndCities>
}