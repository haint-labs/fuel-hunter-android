package fuel.hunter.router

sealed class Screen {
    object Main : Screen()
    object Savings : Screen()
}

interface Router {
    fun goTo(screen: Screen)
}