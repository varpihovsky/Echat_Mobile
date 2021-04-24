package com.example.echatmobile.system

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavController
import com.example.echatmobile.MainActivity
import com.example.echatmobile.di.modules.BaseFragmentModule
import com.example.echatmobile.di.modules.MainActivityModule
import javax.inject.Inject

abstract class BaseFragment<T : BaseViewModel, B : ViewDataBinding> : Fragment() {
    protected val navigationController: NavController
        protected get() {
            if (_nav == null) {
                inject()
            }
            return _nav ?: throw UninitializedPropertyAccessException()
        }

    protected lateinit var viewModel: T
    protected lateinit var binding: B

    private var _nav: NavController? = null
    private var isNavControllerInjected = false

    protected abstract fun viewModel(): Class<T>
    protected abstract fun viewModelFactory(): ViewModelProvider.AndroidViewModelFactory?
    protected abstract fun layoutId(): Int
    protected abstract fun handleExtendedObservers(baseEvent: BaseEventTypeInterface)

    private fun inject() {
        EchatApplication.instance
            .daggerApplicationComponent
            .plus(MainActivityModule(activity as MainActivity))
            .plus(BaseFragmentModule())
            .inject(this as BaseFragment<BaseViewModel, ViewDataBinding>)
    }

    @Inject
    fun injectNavController(navController: NavController) {
        if (!isNavControllerInjected) {
            isNavControllerInjected = true
        } else {
            throw RuntimeException("This is internal method, you cant invoke it by your own")
        }
        _nav = navController
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        DataBindingUtil.inflate<B>(
            layoutInflater,
            layoutId(),
            container,
            false
        ).let {
            binding = it
            binding.root
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initBaseObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        isNavControllerInjected = false
    }

    private fun initViewModel() {
        viewModel = if (viewModelFactory() == null) {
            ViewModelProvider(this).get(viewModel())
        } else {
            viewModelFactory()?.let { ViewModelProvider(this, it).get(viewModel()) }!!
        }
    }

    private fun initBaseObservers() {
        viewModel.baseData.baseEventLiveData.observe(viewLifecycleOwner) { handleBaseEvent(it) }
    }

    private fun handleBaseEvent(baseEvent: BaseEvent<BaseEventTypeInterface>) {
        baseEvent.get().let {
            when (it) {
                BaseEventType.HIDE_KEYBOARD -> view?.let { view -> activity?.hideKeyboard(view) }
                BaseEventType.TOAST_RESOURCE -> showToastResource(it.data())
                BaseEventType.TOAST_STRING -> showToastString(it.data())
                BaseEventType.NAVIGATE -> navigate(it.data())
                BaseEventType.EMPTY -> {
                }
                else -> handleExtendedObservers(it)
            }
        }
    }

    @SuppressLint("ShowToast")
    private fun showToastResource(data: ToastResourceData) {
        Toast.makeText(context, resources.getString(data.resource), data.length).show()
    }

    @SuppressLint("ShowToast")
    private fun showToastString(data: ToastStringData) {
        Toast.makeText(context, data.text, data.length).show()
    }

    private fun navigate(data: NavigateEventData) {
        navigationController.navigate(data.action, data.data)
    }

    companion object {
        const val TOAST_SHORT = Toast.LENGTH_SHORT
        const val TOAST_LONG = Toast.LENGTH_LONG
    }
}