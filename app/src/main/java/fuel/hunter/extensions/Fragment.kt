package fuel.hunter.extensions

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.init(
    toolbar: Toolbar,
    toolbarShadow: View,
    scrollableContainer: View
) {
    toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

    val alphaMax = toolbarShadow.dp(50)

    scrollableContainer.viewTreeObserver
        .addOnScrollChangedListener {
            val dy = scrollableContainer.scrollY.coerceIn(0, alphaMax.toInt())
            val alpha = dy / alphaMax

            toolbarShadow.alpha = alpha
        }

    toolbarShadow.alpha = 0f
}