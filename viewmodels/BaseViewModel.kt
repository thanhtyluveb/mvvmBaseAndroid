package com.example.thefirstprojecttdtdemo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BaseViewModel : ViewModel() {
    val liveData : MutableLiveData<String> = MutableLiveData()
    init {

    }
}