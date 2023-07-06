package com.example.iiittrial.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iiittrial.data.models.DownloadedFile
import com.example.iiittrial.data.models.FileItem
import com.example.iiittrial.domain.repo.FileRepo
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

    private val _downloadedFile = mutableStateOf<DownloadedFile?>(null)
    val downloadedFile: DownloadedFile? get() = _downloadedFile.value

    fun setDownloadedFile(responseBody: ResponseBody, fileName: String) {
        _downloadedFile.value = DownloadedFile(responseBody, fileName)
    }

    fun clearDownloadedFile() {
        _downloadedFile.value = null
    }

    //Functions for the UI
    fun findFiles(sub: String, docType: String){
        viewModelScope.launch {
            //Clear the list before adding new items
            _files.clear()


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