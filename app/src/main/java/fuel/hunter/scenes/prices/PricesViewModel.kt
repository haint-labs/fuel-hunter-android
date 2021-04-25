package fuel.hunter.scenes.prices

import android.location.Location
import android.location.LocationManager
import android.util.Log
import fuel.hunter.FuelHunterServiceGrpcKt.FuelHunterServiceCoroutineStub
import fuel.hunter.data.Fuel
import fuel.hunter.models.Company
import fuel.hunter.models.Price
import fuel.hunter.models.Station
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

interface PricesSceneVM {
    val locations: Flow<List<Location>>
    fun getPrices(location: Location): Flow<List<FuelTypedItem>>
}

class OnlinePricesSceneVM(
    private val scope: CoroutineScope,
    private val client: FuelHunterServiceCoroutineStub,
) : PricesSceneVM {
    private val companies = flow {
        val items = client
            .getCompanies(Company.Query.getDefaultInstance())
            .companiesList

        emit(items)
    }
        .shareIn(scope, SharingStarted.WhileSubscribed())

    private val stations = flow {
        val items = client
            .getStations(Station.Query.getDefaultInstance())
            .stationsList

        emit(items)
    }
        .shareIn(scope, SharingStarted.WhileSubscribed())

    override val locations: Flow<List<Location>> = flow {
        val riga = Location(LocationManager.GPS_PROVIDER).apply {
            latitude = 56.9713962
            longitude = 23.9890821
        }

        val ogre = Location(LocationManager.GPS_PROVIDER).apply {
            latitude = 56.8104753
            longitude = 24.5707587
        }

        val jurmala = Location(LocationManager.GPS_PROVIDER).apply {
            latitude = 56.9653867
            longitude = 23.5810785
        }

        emit(listOf(riga, ogre, jurmala))
    }

    override fun getPrices(location: Location): Flow<List<FuelTypedItem>> {
        val request = Price.Query.newBuilder()
            .setLocation(
                Price.Location.newBuilder()
                    .setLongitude(location.latitude.toFloat())
                    .setLatitude(location.longitude.toFloat())
                    .build()
            )
            .setDistance(2000f)
            .build()

        Log.d("MOX", request.toString())

        val prices = flow {
            val items = client
                .getPrices(request)
                .itemsList

            emit(items)
        }

        return combine(prices, companies, stations, ::transformToFuelPrices)
            .map(::flattenFuelTypes)
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
