package com.example.echatmobile.system

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavController
import com.example.echatmobile.MainActivity
import com.example.echatmobile.di.modules.BaseFragmentModule
import com.example.echatmobile.di.modules.MainActivityModule
import com.example.echatmobile.system.EchatApplication.Companion.LOG_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import javax.inject.Inject
import kotlin.properties.Delegates

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

    protected abstract fun viewModel(): Class<T>
    protected abstract fun viewModelFactory(): ViewModelProvider.AndroidViewModelFactory?
    protected abstract fun layoutId(): Int

    private var _nav: NavController? = null
    private var isNavControllerInjected = false

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

        GlobalScope.launch {
            log()

            initViewModel()
            GlobalScope.launch(Dispatchers.Main) { initBaseObservers() }
        }
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
        viewModel.baseData.baseEventLiveData.observe(viewLifecycleOwner) {
            when (it.eventType) {
                BaseEventType.HIDE_KEYBOARD -> view?.let { view -> activity?.hideKeyboard(view) }
                BaseEventType.TOAST_RESOURCE -> showToastResource(it.eventType.data())
                BaseEventType.TOAST_STRING -> showToastString(it.eventType.data())
                BaseEventType.NAVIGATE -> navigate(it.eventType.data())
                else -> handleExtendedObservers(it)
            }
        }
    }

    abstract fun handleExtendedObservers(baseEvent: BaseEvent<BaseEventTypeInterface>)

    private fun inject() {
        Log.d(LOG_TAG, "activity state: ${activity?.lifecycle?.currentState}")
        EchatApplication.instance
            .daggerApplicationComponent
            .plus(MainActivityModule(activity as MainActivity))
            .plus(BaseFragmentModule())
            .inject(this as BaseFragment<BaseViewModel, ViewDataBinding>)
    }

    private fun log() {
        Log.d(LOG_TAG, "Activity created successfully")
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
        navigationController.navigate(data.action)
    }

    companion object{
        const val TOAST_SHORT = Toast.LENGTH_SHORT
        const val TOAST_LONG = Toast.LENGTH_LONG
    }
}