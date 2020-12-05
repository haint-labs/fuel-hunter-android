package fuel.hunter.scenes.settings.types

import androidx.datastore.DataStore
import fuel.hunter.data.preferences.Preferences
import fuel.hunter.models.Price
import fuel.hunter.scenes.settings.Fuel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class FuelTypeViewModel(
    private val scope: CoroutineScope,
    private val preferences: DataStore<Preferences>
) {
    private val fuelTypesMap = MutableStateFlow<Map<String, Boolean>>(mapOf())
    private val update = Channel<Fuel.Type>()

    val fuelTypes = fuelTypesMap
        .map {
            it.map { (name, isChecked) -> Fuel.Type(name, isChecked) }
        }
        .stateIn(scope, SharingStarted.Lazily, emptyList())

    init {
        wireFromStore()
        handleUpdates()
    }

    fun updatePreference(item: Fuel.Type) = update.offer(item)

    private fun wireFromStore() {
        preferences.data
            .map { preferences ->
                preferences.fuelTypesMap.takeIf { it.isNotEmpty() }
                    ?: Price.FuelType.values()
                        .filterNot { it == Price.FuelType.UNRECOGNIZED }
                        .map { it.toString() to false }
                        .toMap()
            }
            .onEach { fuelTypesMap.emit(it) }
            .launchIn(scope)
    }

    private fun handleUpdates() {
        update
            .consumeAsFlow()
            .map { item ->
                fuelTypesMap.value
                    .toMutableMap()
                    .also { it[item.name] = item.isChecked }
            }
            .onEach { fuelTypesMap.emit(it) }
            .onCompletion {
                preferences.updateData { p ->
                    p.toBuilder()
                        .putAllFuelTypes(fuelTypesMap.value)
                        .build()
                }
            }
            .launchIn(scope)
    }
}