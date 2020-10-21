package fuel.hunter.scenes.settings.types

import androidx.datastore.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fuel.hunter.data.preferences.Preferences
import fuel.hunter.models.Price
import fuel.hunter.scenes.settings.Fuel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class FuelTypeViewModel(
    private val preferences: DataStore<Preferences>
) : ViewModel() {

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