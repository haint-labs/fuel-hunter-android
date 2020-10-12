package fuel.hunter.scenes.settings.types

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fuel.hunter.models.Price
import fuel.hunter.scenes.settings.Fuel
import fuel.hunter.tools.dataStore
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class FuelTypeViewModel(app: Application) : AndroidViewModel(app) {
    private val preferences by dataStore()

    private val update = Channel<Fuel>()

    private val fuelTypeMap = preferences.data
        .map { preferences ->
            preferences.fuelTypesMap.takeIf { it.isNotEmpty() }
                ?: Price.FuelType.values()
                    .filterNot { it == Price.FuelType.UNRECOGNIZED }
                    .map { it.toString() to false }
                    .toMap()
        }
        .map { it.toMutableMap() }
        .buffer(Channel.CONFLATED)
        .broadcastIn(viewModelScope)
        .asFlow()

    val fuelTypeUiMap = fuelTypeMap
        .map {
            listOf<Fuel>(Fuel.Header) + it.map { (name, isChecked) ->
                Fuel.Type(name, isChecked)
            }
        }

    init {
        update
            .consumeAsFlow()
            .mapNotNull { it as? Fuel.Type }
            .combine(fuelTypeMap) { item, typeMap ->
                typeMap.also { it[item.name] = item.isChecked }
            }
            .onEach {
                preferences.updateData { p ->
                    p.toBuilder()
                        .putAllFuelTypes(it)
                        .build()
                }
            }
            .launchIn(viewModelScope)
    }

    fun updateFuelTypePreference(item: Fuel.Type) = update.offer(item)
}