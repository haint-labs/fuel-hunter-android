package fuel.hunter.scenes.settings.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationViewModel : ViewModel() {
    val cents = MutableLiveData(0)
}