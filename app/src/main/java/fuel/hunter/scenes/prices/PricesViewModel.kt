package fuel.hunter.scenes.prices

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager.GPS_PROVIDER
import android.util.Log
import androidx.datastore.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import fuel.hunter.FuelHunterServiceGrpcKt.FuelHunterServiceCoroutineStub
import fuel.hunter.data.Fuel
import fuel.hunter.data.preferences.Preferences
import fuel.hunter.models.Company
import fuel.hunter.models.Price
import fuel.hunter.models.Station
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PricesViewModel(
    private val client: FuelHunterServiceCoroutineStub,
    private val preferences: DataStore<Preferences>
) : ViewModel() {
    @SuppressLint("MissingPermission")
    private val location: Flow<Location> = callbackFlow {
//        val manager = getSystemService(app, LocationManager::class.java)
//            ?: return@callbackFlow
//
//        val listener = object : LocationListener {
//            override fun onStatusChanged(
//                provider: String?,
//                status: Int,
//                extras: Bundle?
//            ) {}
//
//            override fun onProviderEnabled(provider: String?) {}
//            override fun onProviderDisabled(provider: String?) {}
//
//            override fun onLocationChanged(location: Location?) {
//                location?.let { offer(it) }
//            }
//        }
//
//        manager.requestLocationUpdates(
//            GPS_PROVIDER,
//            1 * 60 * 1000,
//            0f,
//            listener
//        )
//
//        offer(manager.getLastKnownLocation(GPS_PROVIDER))
//
//        awaitClose { manager.removeUpdates(listener) }
        offer(Location(GPS_PROVIDER))
    }

    private val _companies = MutableStateFlow(emptyList<Company>())
    private val _stations = MutableStateFlow(emptyList<Station>())
    private val _prices = MutableStateFlow(emptyList<Price.Response.Item>())

    val prices = combine(_prices, _companies, _stations, ::transformToFuelPrices)
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
        val priceQuery = combine(preferences.data) { (preferences) ->
            val builder = Price.Query.newBuilder()
                .addCity("Jelgava")
//                .setLocation(
//                    Price.Location.newBuilder()
//                        .setLongitude(location.latitude.toFloat())
//                        .setLatitude(location.longitude.toFloat())
//                        .build()
//                )
//                .setDistance(2000f)

            preferences.fuelTypesMap
                .mapNotNull { (key, value) -> if (value) key else null }
                .takeIf { it.isNotEmpty() }
                ?.let { builder.addAllType(it) }

            builder.build()
        }

        priceQuery
            .onEach { Log.d("MOX", it.toString()) }
            .map { client.getPrices(it).itemsList }
            .onEach { _prices.value = it }
            .launchIn(viewModelScope)
    }

    private fun transformToFuelPrices(
        items: List<Price.Response.Item>,
        companies: List<Company>,
        stations: List<Station>
    ): Map<Fuel.Category, List<Fuel.Price>> {
        return items
            .map { item ->
                Fuel.Category(item.type.toString()) to item.pricesList.mapNotNull {
                    val logoUrl = companies
                        .find { company -> company.name == it.company }
                        ?.logo?.x2
                        ?: return@mapNotNull null

                    val mergedAddress = stations
                        .filter { station -> it.stationsList.contains(station.id) }
                        .fold("") { acc, station ->
                            "${if (acc.isEmpty()) "" else "${acc}\n"}${station.address}"
                        }

                    Fuel.Price(
                        title = it.company,
                        address = mergedAddress,
                        type = item.type.toString(),
                        price = it.price,
                        logo = Fuel.Logo.Url(logoUrl),
                    )
                }
            }
            .toMap()
    }

}