package fuel.hunter.tools

import android.util.Log

fun Any.debug(message: String): Unit {
    Log.d(this::class.java.simpleName, message)
}