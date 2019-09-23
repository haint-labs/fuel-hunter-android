package fuel.hunter.scenes.settings

import android.view.View
import androidx.core.view.isGone
import fuel.hunter.R
import fuel.hunter.scenes.base.BaseFragment
import fuel.hunter.tools.debug
import kotlinx.android.synthetic.main.layout_setting_item.view.*

typealias LanguageItem = Pair<String, String>

internal val languageItems = listOf(
    "Latviski" to "Latviski",
    "English" to "Angliski",
    "Русский" to "Krieviski"
)

class LanguageFragment : BaseFragment<LanguageItem>() {
    override val title = R.string.title_language
    override val items = languageItems

    override val layoutProvider = { _: Int ->
        R.layout.layout_setting_item
    }

    override val binder = { view: View, item: LanguageItem ->
        with(view) {
            settingTitle.text = item.first
            settingsDescription.text = item.second
            settingToggle.isGone = true
        }
    }

    override var onClick = { (title, _): LanguageItem ->
        debug("clicked")
    }
}