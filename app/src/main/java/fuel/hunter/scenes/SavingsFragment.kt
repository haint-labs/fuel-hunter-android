package fuel.hunter.scenes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import fuel.hunter.databinding.FragmentSavingsBinding
import kotlinx.android.synthetic.main.layout_toolbar.*

class SavingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentSavingsBinding.inflate(inflater, container, false).root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }
}
