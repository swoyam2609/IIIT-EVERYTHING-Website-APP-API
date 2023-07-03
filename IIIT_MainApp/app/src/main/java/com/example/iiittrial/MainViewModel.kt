package com.example.iiittrial

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iiittrial.models.FileItem
import com.example.iiittrial.models.FileList
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

class MainViewModel(private val repository: FileRepo): ViewModel(){


    //Values for the UI
    private val _files = mutableStateListOf<FileItem>()
    var files: List<FileItem>  = _files

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading


    //Values for the API
    var downloadfileValue : MutableLiveData<Response<ResponseBody>> = MutableLiveData()
    var findFilesValue : MutableLiveData<Response<List<FileItem>>> = MutableLiveData()

    //Functions for the UI
    fun findFiles(sub: String, docType: String){
        viewModelScope.launch {
            val findFiles = repository.findFiles(sub, docType)
            findFilesValue.value = findFiles
        }
    }

    fun downloadFile(fileId: String){
        viewModelScope.launch {
            val downloadFile = repository.downloadFile(fileId)
            downloadfileValue.value = downloadFile
        }
    }

}