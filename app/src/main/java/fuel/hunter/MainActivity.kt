package fuel.hunter

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import fuel.hunter.modules.client
import fuel.hunter.modules.preferences
import fuel.hunter.scenes.prices.PricesScene
import fuel.hunter.scenes.prices.PricesViewModel
import fuel.hunter.scenes.settings.SettingsScene
import fuel.hunter.scenes.settings.companies.CompaniesViewModel
import fuel.hunter.scenes.settings.types.FuelTypeViewModel
import fuel.hunter.tools.di.Container
import fuel.hunter.tools.di.ContainerHolder
import fuel.hunter.tools.di.container

class MainActivity : AppCompatActivity(), ContainerHolder {
    override val container: Container by container {
        single<Context>(this@MainActivity.applicationContext)

        client()
        preferences(
            key = "preferences",
            context = get()
        )

        // Prices
        factory { PricesViewModel(get(), get(), get(key = "preferences")) }

        // Settings
        factory { FuelTypeViewModel(get(key = "preferences")) }
        factory { CompaniesViewModel(get(), get(key = "preferences")) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val navOptions: NavOptionsBuilder.() -> Unit = {
                anim {
                    enter = R.anim.nav_enter
                    exit = R.anim.nav_exit
                    popEnter = R.anim.nav_pop_enter
                    popExit = R.anim.nav_pop_exit
                }
            }

            NavHost(navController = navController, startDestination = "prices") {
                composable("prices") {
                    val viewModel = PricesViewModel(
                        container.get(),
                        container.get(),
                        container.get(key = "preferences"),
                    )

                    PricesScene(
                        viewModel = viewModel,
                        goToSettings = { navController.navigate("settings", navOptions) }
                    )
                }

                composable("settings") {
                    SettingsScene()
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = resources.getColor(android.R.color.white, null)
        }
    }
}

