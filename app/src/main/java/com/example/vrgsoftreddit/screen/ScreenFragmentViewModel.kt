package com.example.vrgsoftreddit.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vrgsoftreddit.data.repository.Repository
import com.example.vrgsoftreddit.model.JsonReddit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ScreenFragmentViewModel : ViewModel() {

    private var repo = Repository()
    private val _response = MutableLiveData<JsonReddit>()
    val response: LiveData<JsonReddit> = _response

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getDataInfo(after: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.postValue(true)

            val response = repo.getD(after)
            when {
                response.isSuccess -> {
                    _response.postValue(response.getOrNull())
                    _loading.postValue(false)
                }
                response.isFailure -> {
                    _error.postValue(response.exceptionOrNull()?.localizedMessage ?: "Unknown error")
                    _loading.postValue(false)
                }
            }

        }
    }

}