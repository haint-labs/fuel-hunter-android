package fuel.hunter

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ColorPropKey
import androidx.compose.animation.DpPropKey
import androidx.compose.animation.core.TransitionState
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.core.tween
import androidx.compose.animation.transition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.*
import fuel.hunter.modules.client
import fuel.hunter.modules.preferences
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

                        val x = transition(
                            definition = definition,
                            initState = "start",
                            toState = "end",
                            onStateChangeFinished = { Log.d("MOX", "$it done") },
                        )

                        WithTransition(state = x) {
                            FuelTypeSettingScene(
                                viewModel = viewModel,
                                onNavigationClick = {
                                    scope.cancel()
                                    navController.popBackStack()
                                },
                            )
                        }


                    }
                }

                composable("prices") {
                    PricesScene(
                        viewModel = container.get(),
                        goToSettings = { navController.navigate("summary", navOptions) }
                    )
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = resources.getColor(android.R.color.white, null)
        }
    }
}

val offset = DpPropKey()
val colorKey = ColorPropKey()

val definition = transitionDefinition<String> {
    state("start") {
        this[offset] = (500).dp
        this[colorKey] = Color.Red
    }

    state("end") {
        this[offset] = 0.dp
        this[colorKey] = Color.Green
    }

    transition(fromState = "start", toState = "end") {
        offset using tween(durationMillis = 400)
        colorKey using tween(durationMillis = 400)
    }
}

@Composable
fun WithTransition(
    state: TransitionState,
    children: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
//            .background(state[colorKey])
            .offset(x = state[offset])
            .fillMaxSize()
    ) {
        children()
    }
}