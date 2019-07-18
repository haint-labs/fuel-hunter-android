package fuel.hunter.extensions

import android.os.Build
import android.view.View
import androidx.annotation.ColorRes

fun <T> T.dp(value: Int) where T : View = value * context.resources.displayMetrics.density

fun View.color(@ColorRes value: Int) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(value, null)
    } else {
        resources.getColor(value)
    }