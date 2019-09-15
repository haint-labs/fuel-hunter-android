package fuel.hunter.scenes.savings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fuel.hunter.databinding.FragmentSavingsBinding
import fuel.hunter.extensions.init
import kotlinx.android.synthetic.main.fragment_savings.view.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*

class SavingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentSavingsBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        init(toolbar, toolbarShadow, scrollView)
    }
}
