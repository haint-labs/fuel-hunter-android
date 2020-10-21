package fuel.hunter.modules

import android.content.Context
import androidx.datastore.Serializer
import androidx.datastore.createDataStore
import fuel.hunter.data.preferences.Preferences
import fuel.hunter.data.preferences.PreferencesSerializer
import fuel.hunter.tools.di.Container

fun Container.preferences(
    context: Context,
    filename: String = "preferences.pb",
    serializer: Serializer<Preferences> = PreferencesSerializer,
    key: String = "preferences"
) {
    val dataStore = context.createDataStore(filename, serializer)

    single(key = key, value = dataStore)
}