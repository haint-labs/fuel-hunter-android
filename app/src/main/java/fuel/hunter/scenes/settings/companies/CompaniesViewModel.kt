package fuel.hunter.scenes.settings.companies

import android.util.Log
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
    private val update = Channel<Fuel>()

    private val companies = flow {
        val query = Company.Query.getDefaultInstance()
        val result = client.getCompanies(query).companiesList

        emit(result)
    }.shareIn(viewModelScope, SharingStarted.Lazily)

    private val companiesPrefs = preferences.data
        .combine(companies) { preferences, companies ->
            preferences.companiesMap.takeIf { it.isNotEmpty() }
                ?: companies
                    .map { it.name to false }
                    .toMap()
        }
        .map { it.toMutableMap() }
        .shareIn(viewModelScope, SharingStarted.Lazily)

    val companiesUiMap = companiesPrefs
        .combine(companies) { prefs, companies ->
            listOf(Fuel.Header, Fuel.Cheapest(false)) + companies.map {
                Fuel.Company(
                    logo = Fuel.Logo.Url(it.logo.x2),
                    name = it.name,
                    isChecked = prefs[it.name] ?: true
                )
            }
        }

    init {
        update
            .consumeAsFlow()
            .mapNotNull { it as? Fuel.Company }
            .combine(companiesPrefs) { item, companies ->
                companies.also { it[item.name] = item.isChecked }
            }
            .onEach {
                preferences.updateData { p ->
                    p.toBuilder()
                        .putAllCompanies(it)
                        .build()
                }
            }
            .launchIn(viewModelScope)
    }

    fun updateCompaniesPreference(item: Fuel) = update.offer(item)
}
