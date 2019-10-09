package fuel.hunter.scenes.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.appcompat.app.AppCompatDialogFragment
import fuel.hunter.R
import kotlinx.android.synthetic.main.fragment_notifications.*


class NotificationFragment : AppCompatDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text = "Saņemt paziņojumus kad samazinās degvielas cena par ➊ centiem."

        notificationDescription.text = text
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
    }
}