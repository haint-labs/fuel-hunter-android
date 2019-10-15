package fuel.hunter.scenes.settings.notification

internal fun Int.toSymbol(): Char {
    val code = when (this) {
        0 -> 9471
        in 1..10 -> 10101 + this
        else -> this
    }

    return code.toChar()
}