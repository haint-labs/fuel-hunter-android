package fuel.hunter.scenes.savings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fuel.hunter.R
import fuel.hunter.databinding.FragmentSavingsBinding
import fuel.hunter.databinding.LayoutToolbarBinding
import fuel.hunter.extensions.init
import fuel.hunter.extensions.viewBinding

class SavingsFragment : Fragment() {
    private val binding by viewBinding(FragmentSavingsBinding::bind)
    private val toolbarBinding by viewBinding(LayoutToolbarBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSavingsBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(toolbarBinding) {
            toolbarTitle.setText(R.string.title_savings)
            toolbar.setNavigationIcon(R.drawable.ic_back_arrow)

            init(toolbar, toolbarShadow, binding.scrollView)
        }
    }
}
