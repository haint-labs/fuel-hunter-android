package fuel.hunter.extensions

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

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

fun <T : ViewBinding> Fragment.viewBinding(
    bind: (View) -> T
): ReadOnlyProperty<Fragment, T> =
    object : ReadOnlyProperty<Fragment, T> {
        private var binding: T? = null

        private val lifecycleObserver = object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                owner.lifecycle.removeObserver(this)
                binding = null
            }
        }

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
            binding?.let { return it }

            thisRef.viewLifecycleOwner.lifecycle.addObserver(lifecycleObserver)
            binding = bind(requireView())

            return binding!!
        }
    }