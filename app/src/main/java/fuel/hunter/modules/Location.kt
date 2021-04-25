package fuel.hunter.modules

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.os.Bundle
import androidx.core.content.ContextCompat
import fuel.hunter.tools.di.Container
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun Container.location(
    context: Context,
    key: String = "location",
) {
    val manager = requireNotNull(
        ContextCompat.getSystemService(context, LocationManager::class.java)
    )

    single(key = key, value = manager)
}

@ExperimentalCoroutinesApi
val LocationManager.location: Flow<Location>
    @SuppressLint("MissingPermission")
    get() = callbackFlow {
        val listener = object : LocationListener {
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}

            override fun onLocationChanged(location: Location) {
                offer(location)
            }
        }

        requestLocationUpdates(
            GPS_PROVIDER,
            1 * 60 * 1000,
            10f,
            listener
        )

        getLastKnownLocation(GPS_PROVIDER)?.let(::offer)

        awaitClose { removeUpdates(listener) }
    }