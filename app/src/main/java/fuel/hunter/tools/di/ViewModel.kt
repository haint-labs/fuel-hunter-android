package fuel.hunter.tools.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T> Fragment.viewModel(key: String = T::class.java.simpleName): Lazy<T> {
    return viewModels(
        ownerProducer = { this },
        factoryProducer = {
            val container = (requireActivity() as? ContainerHolder)?.container
                ?: throw NullPointerException("No container provided")


            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return container.provide(key, modelClass)
                }
            }
        }
    )
}