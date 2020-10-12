package fuel.hunter

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import fuel.hunter.FuelHunterServiceGrpcKt.FuelHunterServiceCoroutineStub
import fuel.hunter.data.Fuel
import fuel.hunter.models.Company
import fuel.hunter.models.Price
import fuel.hunter.models.Station
import fuel.hunter.scenes.prices.flattenFuelTypes
import fuel.hunter.tools.dataStore
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val client by lazy {
        val executor = Dispatchers.IO.asExecutor()

        val channel = ManagedChannelBuilder.forAddress("162.243.16.251", 50051)
            .usePlaintext()
            .executor(executor)
            .build()

        FuelHunterServiceCoroutineStub(channel)
    }

    private val preferences by dataStore()

    private val _companies = MutableStateFlow(emptyList<Company>())
    private val _stations = MutableStateFlow(emptyList<Station>())
    private val _prices = MutableStateFlow(emptyList<Price.Response.Item>())

    val prices = combine(_prices, _companies, _stations) { prices, companies, stations ->
        transformToFuelPrices(
            prices,
            companies,
            stations
        )
    }
        .map { flattenFuelTypes(it) }
        .asLiveData()

    init {
        updateCompanies()
        updateStations()
        handlePricesUpdates()
    }

    private fun updateCompanies() {
        viewModelScope.launch {
            _companies.value = client
                .getCompanies(Company.Query.getDefaultInstance())
                .companiesList
        }
    }

    private fun updateStations() {
        viewModelScope.launch {
            _stations.value = client
                .getStations(Station.Query.getDefaultInstance())
                .stationsList
        }
    }

    private fun handlePricesUpdates() {
        preferences.data
            .map { preferences ->
                val builder = Price.Query.newBuilder()
                    .addCity("Jelgava")

                preferences.fuelTypesMap
                    .mapNotNull { (key, value) -> if (value) key else null }
                    .takeIf { it.isNotEmpty() }
                    ?.let { builder.addAllType(it) }

                builder.build()
            }
            .map { client.getPrices(it).itemsList }
            .onEach { _prices.value = it }
            .launchIn(viewModelScope)
    }

    private fun transformToFuelPrices(
        prices: List<Price.Response.Item>,
        companies: List<Company>,
        stations: List<Station>
    ): Map<Fuel.Category, List<Fuel.Price>> =
        prices
            .flatMap { responseItem ->
                responseItem
                    .pricesList
                    .mapNotNull {
                        val station = stations
                            .find { station -> station.id == responseItem.stationId }
                            ?: return@mapNotNull null

                        val company = companies
                            .find { company -> company.name == station.company }
                            ?: return@mapNotNull null

                        Fuel.Price(
                            title = station.name,
                            address = station.address,
                            type = it.type.toString(),
                            price = it.price,
                            logo = Fuel.Logo.Url(company.logo.x2)
                        )
                    }
            }
            .groupBy { it.type }
            .mapKeys { Fuel.Category(it.key) }
}