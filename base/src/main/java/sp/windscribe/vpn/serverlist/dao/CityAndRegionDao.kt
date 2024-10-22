package sp.windscribe.vpn.serverlist.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.Single
import sp.windscribe.vpn.serverlist.entity.CityAndRegion

@Dao
interface CityAndRegionDao {
    @Query("Select city_id from City where region_id=:regionId and pro=:isPro and nodes>0 limit 1 ")
    fun getCitiesByRegion(regionId: Int, isPro: Int): Single<Int>

    @Transaction
    @Query("Select * from City limit 1")
    fun getCity(): Single<CityAndRegion>

    @Transaction
    @Query("Select * from City where city_id=:cityId limit 1")
    fun getCityAndRegionByID(cityId: Int): Single<CityAndRegion>

    @Transaction
    @Query("Select * from City where city_id=:cityId limit 1")
    fun getCityAndRegion(cityId: Int): CityAndRegion

    @Query("Select region_id From city where city_id=:cityId limit 1")
    fun getRegionIdFromCity(cityId: Int): Single<Int>
}