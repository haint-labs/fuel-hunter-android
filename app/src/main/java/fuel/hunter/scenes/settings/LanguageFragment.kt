package fuel.hunter.scenes.settings

import android.view.View
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import fuel.hunter.R
import fuel.hunter.scenes.base.BaseFragment
import kotlinx.android.synthetic.main.layout_setting_item.view.*

typealias LanguageItem = Pair<String, String>

class LanguageFragment : BaseFragment<LanguageItem>() {
    override val title = R.string.title_language

    override val itemDiff = object: DiffUtil.ItemCallback<LanguageItem>() {
        override fun areItemsTheSame(oldItem: LanguageItem, newItem: LanguageItem) = oldItem == newItem
        override fun areContentsTheSame(oldItem: LanguageItem, newItem: LanguageItem) = oldItem == newItem
    }

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
}

private val languageItems = listOf(
    "Latviski" to "Latviski",
    "English" to "Angliski",
    "Русский" to "Krieviski"
)