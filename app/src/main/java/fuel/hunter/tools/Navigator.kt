package fuel.hunter.tools

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import fuel.hunter.R

fun Fragment.navigateTo(@IdRes destination: Int) {
    val options = navOptions {
        anim {
            enter = R.anim.nav_enter
            exit = R.anim.nav_exit
            popEnter = R.anim.nav_pop_enter
            popExit = R.anim.nav_pop_exit
        }
    }

    with(findNavController()) {
        // NOTE: https://stackoverflow.com/a/56410391
        currentDestination?.getAction(destination) ?: return
        navigate(destination, null, options)
    }
}

fun Fragment.navigateUp() {
    findNavController().navigateUp()
}