package fuel.hunter.scenes.settings.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import fuel.hunter.R
import fuel.hunter.extensions.onClick
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach

private const val MIN_VALUE = 0
private const val MAX_VALUE = 10

class NotificationFragment : AppCompatDialogFragment() {
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

        setupCentsControls()
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
    }

    private fun setupCentsControls() {

        viewModel.cents
            .observe(this) {
                notificationDescription.text = getString(R.string.notification_text, it.toSymbol())
            }

        viewModel.cents
            .observe(this) {
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
