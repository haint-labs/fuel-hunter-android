package fuel.hunter

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import fuel.hunter.models.Company
import fuel.hunter.models.Price
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = resources.getColor(android.R.color.white, null)
        }

        val channel = ManagedChannelBuilder
            .forAddress("162.243.16.251", 50051)
            .usePlaintext()
            .executor(Dispatchers.Default.asExecutor())
            .build()

        val client = FuelHunterServiceGrpcKt.FuelHunterServiceCoroutineStub(channel)

        lifecycleScope.launchWhenStarted {
            val location = Price.Location
                .newBuilder()
                .setLongitude(56.9464303f)
                .setLatitude(24.2472912f)
                .build()

            val query = Price.Query.newBuilder()
                .setLocation(location)
                .setDistance(1000f)
                .addType(Price.FuelType.E95.toString())
                .build()

            val logger: (String) -> Unit = { it -> Log.d("COMPANIES", it) }

            client
                .getPrices(query)
                .itemsList
                .map { "${it.stationId} - ${it.pricesList.joinToString { "${it.type} - ${it.price}" }}" }
                .forEach(logger)
        }
    }
}

