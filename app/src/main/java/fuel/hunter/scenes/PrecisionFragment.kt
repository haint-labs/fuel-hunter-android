package fuel.hunter.scenes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fuel.hunter.databinding.FragmentPrecisionBinding
import fuel.hunter.extensions.init
import kotlinx.android.synthetic.main.fragment_savings.view.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*

class PrecisionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentPrecisionBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        init(toolbar, toolbarShadow, scrollView)
    }
}
