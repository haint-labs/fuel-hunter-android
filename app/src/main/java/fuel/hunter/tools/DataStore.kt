package fuel.hunter.tools

import android.app.Application
import androidx.datastore.DataStore
import androidx.datastore.createDataStore
import androidx.lifecycle.AndroidViewModel
import fuel.hunter.data.preferences.Preferences
import fuel.hunter.data.preferences.PreferencesSerializer
import kotlin.properties.ReadOnlyProperty

fun AndroidViewModel.dataStore() =
    ReadOnlyProperty<Any?, DataStore<Preferences>> { _, _ ->
        getApplication<Application>()
            .createDataStore(
                fileName = "preferences.pb",
                serializer = PreferencesSerializer,
            )
    }