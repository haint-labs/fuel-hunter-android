package fuel.hunter.scenes.settings.companies

import androidx.datastore.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fuel.hunter.FuelHunterServiceGrpcKt.FuelHunterServiceCoroutineStub
import fuel.hunter.data.preferences.Preferences
import fuel.hunter.models.Company
import fuel.hunter.scenes.settings.Fuel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class CompaniesViewModel(
    private val client: FuelHunterServiceCoroutineStub,
    private val preferences: DataStore<Preferences>
) : ViewModel() {
    private val companiesMap = MutableStateFlow(mapOf<String, Boolean>())
    private val update = Channel<Fuel>()

    private val snapshot = flow {
        val query = Company.Query.getDefaultInstance()
        val result = client.getCompanies(query).companiesList

        emit(result)
    }
        .shareIn(viewModelScope, SharingStarted.Lazily)

    val companies = companiesMap
        .combine(snapshot) { raw, companies ->
            raw.map { (name, isChecked) ->
                when (name) {
                    Cheapest -> Fuel.Company(
                        name = name,
                        isChecked = isChecked,
                        description = "Ieslēdzot šo - vienmēr tiks rādīta arī tā kompānija, kurai Latvijā ir lētākā degviela attiecīgajā brīdī"
                    )
                    else -> Fuel.Company(
                        name = name,
                        isChecked = isChecked,
                        logo = Fuel.Logo.Url(companies.first { it.name === name }.logo.x2),
                    )
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        wireFromStore()
        handleUpdates()
    }

    fun updateCompaniesPreference(item: Fuel) = update.offer(item)

    private fun wireFromStore() {
        preferences.data
            .combine(snapshot) { preferences, companies ->
                val choices = companies
                    .associateBy(
                        keySelector = { it.name },
                        valueTransform = { preferences.companiesMap[it.name] ?: true }
                    )

                val cheapest = Cheapest to (preferences.companiesMap[Cheapest] ?: true)

                mapOf(cheapest) + choices
            }
            .onEach { companiesMap.emit(it) }
            .launchIn(viewModelScope)
    }

    private fun handleUpdates() {
        update
            .consumeAsFlow()
            .mapNotNull { it as? Fuel.Company }
            .map { item ->
                companiesMap.value
                    .toMutableMap()
                    .also { it[item.name] = item.isChecked }
            }
            .onEach { companiesMap.emit(it) }
            .onCompletion {
                preferences.updateData { p ->
                    p.toBuilder()
                        .putAllCompanies(companiesMap.value)
                        .build()
                }
            }
            .launchIn(viewModelScope)
    }
}
