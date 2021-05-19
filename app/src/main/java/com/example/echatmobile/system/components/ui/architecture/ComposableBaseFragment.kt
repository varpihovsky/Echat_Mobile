package com.example.echatmobile.system.components.ui.architecture

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.echatmobile.MainActivity
import com.example.echatmobile.di.EchatViewModelFactoryComponent
import com.example.echatmobile.di.modules.BaseFragmentModule
import com.example.echatmobile.di.modules.EchatViewModelFactoryModule
import com.example.echatmobile.di.modules.MainActivityModule
import com.example.echatmobile.system.*
import javax.inject.Inject

abstract class ComposableBaseFragment<T : BaseViewModel> : Fragment() {
    private val navController: NavController
        get() {
            if (mNavController == null) {
                inject()
            }
            return mNavController ?: throw UninitializedPropertyAccessException()
        }

    protected lateinit var viewModel: T

    private var mNavController: NavController? = null
    private var isNavControllerInjected = false
    private val onCreatedCallbacks = mutableListOf<() -> Unit>()

    protected abstract fun viewModel(): Class<T>

    protected abstract fun handleExtendedObservers(baseEvent: BaseEventTypeInterface)

    /**
     * This method used for initialization of ViewModelFactory which is used for initialization of ViewModel.
     * To get return value use provideViewModelFactorySelector.
     * @param viewModelFactorySelector
     * @return Unit
     */
    protected abstract fun viewModelFactorySelector(): (EchatViewModelFactoryComponent.() -> ViewModelProvider.AndroidViewModelFactory)?


    private fun inject() {
        EchatApplication.instance
            .daggerApplicationComponent
            .plus(MainActivityModule(activity as MainActivity))
            .plus(BaseFragmentModule())
            .inject(this as ComposableBaseFragment<BaseViewModel>)
    }

    @Inject
    fun injectNavController(navController: NavController) {
        if (!isNavControllerInjected) {
            isNavControllerInjected = true
        } else {
            throw RuntimeException("This is internal method, you cant invoke it by your own")
        }
        mNavController = navController
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initBaseObservers()
    }

    private fun initViewModel() {
        viewModel = if (viewModelFactorySelector() == null) {
            ViewModelProvider(this).get(viewModel())
        } else {
            getViewModelFactoryFromSelector()
        }
    }

    private fun getViewModelFactoryFromSelector() =
        viewModelFactorySelector()!!
            // Not safe call is not dangerous here because this method is surrounded by null check
            .invoke(
                EchatApplication.instance
                    .daggerApplicationComponent
                    .plus(EchatViewModelFactoryModule())
            )
            .let { ViewModelProvider(this, it).get(viewModel()) }

    private fun initBaseObservers() {
        viewModel.baseData.baseEventLiveData.observe(viewLifecycleOwner) { handleBaseEvent(it) }
    }

    override fun onStart() {
        super.onStart()
        runTasks()
    }

    private fun runTasks() {
        onCreatedCallbacks.forEach { it() }
        onCreatedCallbacks.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        isNavControllerInjected = false
    }

    private fun handleBaseEvent(baseEvent: BaseEvent<BaseEventTypeInterface>) {
        baseEvent.get()?.let {
            when (it) {
                is HideKeyboardEvent -> view?.let { view -> activity?.hideKeyboard(view) }
                is ToastResourceEvent -> showToastResource(it.resource, it.length)
                is ToastStringEvent -> showToastString(it.text, it.length)
                is NavigateEvent -> navigate(it.action, it.navigationData)

                else -> handleExtendedObservers(it)
            }
        }
    }

    @SuppressLint("ShowToast")
    private fun showToastResource(resource: Int, length: Int) {
        Toast.makeText(context, resources.getString(resource), length).show()
    }

    @SuppressLint("ShowToast")
    private fun showToastString(text: String, length: Int) {
        Toast.makeText(context, text, length).show()
    }

    private fun navigate(action: Int, data: Bundle?) {
        navController.navigate(action, data)
    }

    fun addOnCreatedTask(task: () -> Unit) {
        if (isTaskCanRun()) {
            task()
            return
        }
        onCreatedCallbacks.add(task)
    }

    private fun isTaskCanRun() =
        listOf(Lifecycle.State.RESUMED, Lifecycle.State.STARTED).contains(
            lifecycle.currentState
        )


    companion object {
        const val TOAST_SHORT = Toast.LENGTH_SHORT
        const val TOAST_LONG = Toast.LENGTH_LONG

        fun provideViewModelSelector(selector: EchatViewModelFactoryComponent.() -> ViewModelProvider.AndroidViewModelFactory):
                (EchatViewModelFactoryComponent.() -> ViewModelProvider.AndroidViewModelFactory) =
            selector

    }
}