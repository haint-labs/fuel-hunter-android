package fuel.hunter.scenes.settings.companies

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fuel.hunter.FuelHunterServiceGrpcKt
import fuel.hunter.models.Company
import fuel.hunter.scenes.settings.Fuel
import fuel.hunter.tools.dataStore
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

@Suppress("EXPERIMENTAL_API_USAGE")
class CompaniesViewModel(app: Application) : AndroidViewModel(app) {
    private val client by lazy {
        val executor = Dispatchers.IO.asExecutor()

        val channel = ManagedChannelBuilder.forAddress("162.243.16.251", 50051)
            .usePlaintext()
            .executor(executor)
            .build()

        FuelHunterServiceGrpcKt.FuelHunterServiceCoroutineStub(channel)
    }
    private val preferences by dataStore()

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
