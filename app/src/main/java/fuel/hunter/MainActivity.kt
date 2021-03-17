package fuel.hunter

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.*
import fuel.hunter.modules.client
import fuel.hunter.modules.preferences
import fuel.hunter.scenes.details.DetailsScene
import fuel.hunter.scenes.prices.PricesScene
import fuel.hunter.scenes.prices.PricesViewModel
import fuel.hunter.scenes.settings.NavActions
import fuel.hunter.scenes.settings.SettingsScene
import fuel.hunter.scenes.settings.companies.CompaniesViewModel
import fuel.hunter.scenes.settings.types.FuelTypeSettingScene
import fuel.hunter.scenes.settings.types.FuelTypeViewModel
import fuel.hunter.tools.di.Container
import fuel.hunter.tools.di.ContainerHolder
import fuel.hunter.tools.di.container
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

class MainActivity : AppCompatActivity(), ContainerHolder {
    override val container: Container by container {
        single<Context>(this@MainActivity.applicationContext)

        client()
        preferences(
            key = "preferences",
            context = get()
        )

        factory { CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate) }

        // Prices
        factory { PricesViewModel(get(), get(), get(key = "preferences")) }

        // Settings
        factory { FuelTypeViewModel(get(), get(key = "preferences")) }
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
                navigation(route = "settings", startDestination = "summary") {
                    composable("summary") {
                        SettingsScene(
                            navActions = object : NavActions {
                                override fun back() {
                                    navController.popBackStack()
                                }

                                override fun toFuelTypes() = navController.navigate("types")
                                override fun toNotifications() {}
                                override fun toLanguage() {}
                            },
                            onNavigationClick = { navController.popBackStack() }
                        )
                    }

                    composable("types") {
                        val scope = container.get<CoroutineScope>()
                        val viewModel = FuelTypeViewModel(
                            scope = scope,
                            preferences = container.get(key = "preferences")
                        )

                        FuelTypeSettingScene(
                            viewModel = viewModel,
                            onNavigationClick = {
                                scope.cancel()
                                navController.popBackStack()
                            },
                        )
                    }
                }

                composable("prices") {
                    PricesScene(
                        viewModel = container.get(),
                        goToSettings = { navController.navigate("summary", navOptions) },
                        goToDetails = { navController.navigate("details", navOptions) }
                    )
                }

                composable("details") {
                    DetailsScene()
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = resources.getColor(android.R.color.white, null)
        }
    }
}