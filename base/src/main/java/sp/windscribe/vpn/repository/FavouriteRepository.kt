package sp.windscribe.vpn.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import sp.windscribe.vpn.commonutils.Ext.toResult
import sp.windscribe.vpn.localdatabase.LocalDbInterface
import sp.windscribe.vpn.serverlist.entity.City
import sp.windscribe.vpn.serverlist.entity.Favourite
import javax.inject.Inject

class FavouriteRepository @Inject constructor(
        private val scope: CoroutineScope, private val localDbInterface: LocalDbInterface
) {
    private val _favourites = MutableSharedFlow<List<City>>(replay = 1)
    val favourites: SharedFlow<List<City>> = _favourites

    init {
        load()
    }

    fun load() {
        scope.launch {
            localDbInterface.favourites.toResult().onSuccess { favourites ->
                val favouriteCityList = favourites.map {
                    return@map localDbInterface.getCityByID(it.id).toResult().getOrNull()
                }.filterNotNull().toList()
                _favourites.emit(favouriteCityList)
            }.onFailure {}
        }
    }

    suspend fun add(city: City): Result<Long> {
        return localDbInterface.addToFavourites(Favourite(city.id)).toResult()
    }

    fun remove(city: City): Result<Unit> {
        return kotlin.runCatching { localDbInterface.delete(Favourite(city.id)) }
    }
}