package com.example.echatmobile.system.components

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.example.echatmobile.system.BaseEventTypeInterface
import com.example.echatmobile.system.BaseFragment
import com.example.echatmobile.system.components.events.UpdateListEvent
import com.example.echatmobile.system.components.events.UpdateListEvent.Companion.ADD
import com.example.echatmobile.system.components.events.UpdateListEvent.Companion.REMOVE

abstract class ListableFragment<T : ListableViewModel<D>, B : ViewDataBinding, D> :
    BaseFragment<T, B>() {

    protected val dataList: MutableList<D> = mutableListOf()
    private var listenToListUpdates = true

    abstract fun onListReplacedCallback()
    abstract fun onListUpdatedCallback(updateType: String, index: Int)

    override fun handleExtendedObservers(baseEvent: BaseEventTypeInterface) {
        when (baseEvent) {
            is UpdateListEvent<*> -> processListUpdate(baseEvent as UpdateListEvent<D>)
        }
    }

    private fun processListUpdate(updateEvent: UpdateListEvent<D>) {
        var index: Int = -1
        if (updateEvent.updateType == ADD) {
            dataList.add(updateEvent.data)
            index = dataList.indexOf(updateEvent.data)
        }
        if (updateEvent.updateType == REMOVE) {
            index = dataList.indexOf(updateEvent.data)
            dataList.remove(updateEvent.data)
        }
        listenToListUpdates = false
        onListUpdatedCallback(updateEvent.updateType, index)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
    }

    private fun initObservers() {
        viewModel.listableData.dataList.observe(viewLifecycleOwner) { processListReplacing(it) }
    }

    private fun processListReplacing(list: List<D>) {
        if (listenToListUpdates) {
            dataList.clear()
            dataList.addAll(list)
            onListReplacedCallback()
        }
        listenToListUpdates = true
    }
}