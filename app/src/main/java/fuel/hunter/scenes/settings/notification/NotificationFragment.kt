package fuel.hunter.scenes.settings.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import fuel.hunter.R
import fuel.hunter.databinding.FragmentNotificationsBinding
import fuel.hunter.extensions.onClick
import fuel.hunter.extensions.viewBinding
import fuel.hunter.tools.navigateUp
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach

private const val MIN_VALUE = 0
private const val MAX_VALUE = 10

class NotificationFragment : AppCompatDialogFragment() {
    private val binding by viewBinding(FragmentNotificationsBinding::bind)
    private val viewModel by viewModels<NotificationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.notificationBackground.onClick
            .onEach { navigateUp() }
            .launchIn(lifecycleScope)

        setupCentsControls()
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
    }

    private fun setupCentsControls() = with(binding) {
        viewModel.cents
            .observe(viewLifecycleOwner) {
                notificationDescription.text = getString(R.string.notification_text, it.toSymbol())
            }

        viewModel.cents
            .observe(viewLifecycleOwner) {
                plusBtn.isEnabled = it < MAX_VALUE
                minusBtn.isEnabled = it > MIN_VALUE
            }

        plusBtn.onClick
            .mapNotNull { viewModel.cents.value }
            .onEach { viewModel.cents.postValue(it + 1) }
            .launchIn(lifecycleScope)

        minusBtn.onClick
            .mapNotNull { viewModel.cents.value }
            .onEach { viewModel.cents.postValue(it - 1) }
            .launchIn(lifecycleScope)
    }
}
