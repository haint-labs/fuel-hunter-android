package fuel.hunter.scenes.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fuel.hunter.R

class SavingsFragment(private val switcher: ScreenSwitcher) : Fragment() {

    companion object {
        fun create(switcher: ScreenSwitcher) = SavingsFragment(switcher)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_main_savings, container, false)

}
