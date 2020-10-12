package fuel.hunter.data.preferences

import androidx.datastore.Serializer
import java.io.InputStream
import java.io.OutputStream

object PreferencesSerializer : Serializer<Preferences> {
    override fun readFrom(input: InputStream): Preferences = Preferences.parseFrom(input)
    override fun writeTo(t: Preferences, output: OutputStream) = t.writeTo(output)
}


