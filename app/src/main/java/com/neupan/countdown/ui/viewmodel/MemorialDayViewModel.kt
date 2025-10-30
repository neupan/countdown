package com.neupan.countdown.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.neupan.countdown.data.AppDatabase
import com.neupan.countdown.data.MemorialDay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MemorialDayViewModel(private val database: AppDatabase) : ViewModel() {
    
    val memorialDays = database.memorialDayDao().getAllMemorialDays()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    fun insertMemorialDay(memorialDay: MemorialDay) {
        viewModelScope.launch {
            database.memorialDayDao().insertMemorialDay(memorialDay)
        }
    }
    
    fun updateMemorialDay(memorialDay: MemorialDay) {
        viewModelScope.launch {
            database.memorialDayDao().updateMemorialDay(memorialDay)
        }
    }
    
    fun deleteMemorialDay(memorialDay: MemorialDay) {
        viewModelScope.launch {
            database.memorialDayDao().deleteMemorialDay(memorialDay)
        }
    }
}

class MemorialDayViewModelFactory(
    private val database: AppDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MemorialDayViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MemorialDayViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

