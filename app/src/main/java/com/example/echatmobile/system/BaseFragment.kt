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
import androidx.navigation.NavController
import com.example.echatmobile.MainActivity
import com.example.echatmobile.di.modules.BaseFragmentModule
import com.example.echatmobile.di.modules.MainActivityModule
import javax.inject.Inject

abstract class BaseFragment<T : BaseViewModel, B : ViewDataBinding> : Fragment() {
    protected val navigationController: NavController
        get() {
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
        navigationController.navigate(action, data)
    }

    companion object {
        const val TOAST_SHORT = Toast.LENGTH_SHORT
        const val TOAST_LONG = Toast.LENGTH_LONG
    }
}