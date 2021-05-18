package com.example.echatmobile.system.components

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.echatmobile.system.BaseEvent
import com.example.echatmobile.system.BaseViewModel
import com.example.echatmobile.system.components.events.UpdateListEvent
import com.example.echatmobile.system.components.events.UpdateListEvent.Companion.ADD
import com.example.echatmobile.system.components.events.UpdateListEvent.Companion.REMOVE
import kotlinx.coroutines.launch

abstract class ListableViewModel<T>(application: Application) : BaseViewModel(application) {
    val listableData by lazy { ListableData() }

    private val dataList = MutableLiveData<List<T>>()

    private val updateListEvent = MutableLiveData<BaseEvent<UpdateListEvent<T>>>()

    inner class ListableData {
        val dataList: LiveData<List<T>> = this@ListableViewModel.dataList
    }

    protected fun addAllToList(list: List<T>) {
        viewModelScope.launch { dataList.value = list }
    }

    protected fun addToList(element: T) {
        viewModelScope.launch {
            if (dataList.value == null) {
                dataList.value = listOf(element)
                return@launch
            }

            updateListEvent.value = BaseEvent(UpdateListEvent(element, ADD))
            dataList.value = dataList.value?.toMutableList()?.apply { add(element) }
        }
    }

    protected fun removeFromList(element: T) {
        if (dataList.value == null) {
            return
        }

        viewModelScope.launch {
            updateListEvent.value = BaseEvent(UpdateListEvent(element, REMOVE))
            dataList.value = dataList.value?.toMutableList()?.apply { remove(element) }
        }
    }

    fun isListEmpty() = dataList.value == null
}