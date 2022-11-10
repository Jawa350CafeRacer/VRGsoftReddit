package com.example.vrgsoftreddit.screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vrgsoftreddit.data.repository.Repository
import com.example.vrgsoftreddit.model.JsonReddit
import kotlinx.coroutines.launch


class ScreenFragmentViewModel : ViewModel() {
    private var repo = Repository()
    val allApi: MutableLiveData<JsonReddit> = MutableLiveData()


    fun getDataInfo(after: String?) {
        viewModelScope.launch {
            val response = repo.getD(after)
            allApi.postValue(response)
        }
    }

}