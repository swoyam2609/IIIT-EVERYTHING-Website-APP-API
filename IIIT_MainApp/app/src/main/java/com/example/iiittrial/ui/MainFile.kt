package com.example.iiittrial.ui

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.iiittrial.domain.repo.FileRepo
import com.example.iiittrial.MainActivity
import com.example.iiittrial.presentation.MainViewModel
import com.example.iiittrial.R
import com.example.iiittrial.data.models.DownloadedFile
import com.example.iiittrial.data.models.FileItem
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import okio.IOException
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainFile(application: MainActivity){
    val lifecycleOwner = LocalLifecycleOwner.current

    val viewModel = viewModel<MainViewModel>(factory = object : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(FileRepo()) as T
        }
    })

    val appContext: Context = application.applicationContext

    val scope = rememberCoroutineScope()

    val searching = remember { mutableStateOf(false) }// to check if searching is going on or not

    var expanded_sub by remember { mutableStateOf(false) }
    var expanded_doc by remember { mutableStateOf(false) }

    var selectedSubject by remember { mutableStateOf("") }
    val subjectList = listOf("BET","CD","DSA Lab","Data Structures","Engineering Chemistry","GRAPH THEORY","Maths 1","Maths-2","OS","PHYSICS","ROBOTICS","WSM","Workshop")

    var selectedDocType by remember { mutableStateOf("") }
    val docTypeList = listOf("NOTES", "PAPER", "BOOK")

    val downloadedFile = viewModel.downloadedFile // Store the downloaded file in the ViewModel


    // Register observer for downloadedFile in a LaunchedEffect block
    LaunchedEffect(downloadedFile) {
        downloadedFile?.let { file ->
            saveResponseBodyAsPdf(
                responseBody = file.responseBody,
                context = appContext,
                fileName = file.fileName, viewModel = viewModel)
            viewModel.clearDownloadedFile() // Clear the downloaded file in ViewModel
        }
    }


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp)) {
        Column(modifier = Modifier
            .fillMaxHeight()
            .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Row(horizontalArrangement = Arrangement.SpaceEvenly) {

                var textfieldSize by remember {
                    mutableStateOf(Size.Zero)
                }

                val icon_sub = if (expanded_sub)
                    Icons.Filled.KeyboardArrowUp
                else
                    Icons.Filled.KeyboardArrowDown

                Column(
                    Modifier
                        .padding(5.dp)
                        .weight(1f)) {
                    OutlinedTextField(
                        value = selectedSubject,
                        onValueChange = { selectedSubject = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                //This value is used to assign to the DropDown the same width
                                textfieldSize = coordinates.size.toSize()
                            },
                        label = {Text("Subjects")},
                        trailingIcon = {
                            Icon(icon_sub,"contentDescription",
                                Modifier.clickable { expanded_sub = !expanded_sub })
                        }
                    )

                    DropdownMenu(
                        expanded = expanded_sub,
                        onDismissRequest = { expanded_sub = false },
                        modifier = Modifier.width(with(LocalDensity.current){textfieldSize.width.toDp()})
                    ) {
                        subjectList.forEach { label ->
                            DropdownMenuItem(onClick =
                            { selectedSubject = label
                                expanded_sub = false
                            }, text = { Text(text = label) })
                        }
                    }
                }

                Log.d("MainFile",selectedSubject)

                val icon_doc = if (expanded_doc)
                    Icons.Filled.KeyboardArrowUp
                else
                    Icons.Filled.KeyboardArrowDown


                Column(
                    Modifier
                        .padding(5.dp)
                        .weight(1f)) {
                    OutlinedTextField(
                        value = selectedDocType,
                        onValueChange = { selectedDocType = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                //This value is used to assign to the DropDown the same width
                                textfieldSize = coordinates.size.toSize()
                            },
                        label = {Text("Document")},
                        trailingIcon = {
                            Icon(icon_doc,"contentDescription",
                                Modifier.clickable { expanded_doc = !expanded_doc })
                        }
                    )
                    DropdownMenu(
                        expanded = expanded_doc,
                        onDismissRequest = { expanded_doc = false },
                        modifier = Modifier.width(with(LocalDensity.current){textfieldSize.width.toDp()})
                    ) {
                        docTypeList.forEach { label ->
                            DropdownMenuItem(onClick =
                            { selectedDocType = label
                                expanded_doc = false
                            }, text = { Text(text = label) })
                        }
                    }
                }
                Log.d("MainFile",selectedDocType)

            }

            Spacer(modifier =Modifier.height(10.dp))

            // Search button
            Button(
                onClick = {
                    Log.e("MainFile", "MainFile: $selectedSubject $selectedDocType")
                    val subject = if (selectedSubject.isEmpty()) {
                        // Handle empty subject case here, e.g., set a default value or show an error message
                        // For now, let's assume an empty string as the default value
                        ""
                    } else {
                        selectedSubject
                    }
                    val docType = if (selectedDocType.isEmpty()) {
                        // Handle empty docType case here, e.g., set a default value or show an error message
                        // For now, let's assume an empty string as the default value
                        ""
                    } else {
                        selectedDocType
                    }
                    scope.launch{
                        viewModel.findFiles(sub = subject, docType = docType)
                    }
                }
            ) {
                Text("Search")
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Observe findFilesValue in LaunchedEffect
            LaunchedEffect(key1 = viewModel.findFilesValue) {
                viewModel.findFilesValue.observe(lifecycleOwner, Observer { response ->
                    if (response.isSuccessful) {
                        searching.value = true
                        response.body()?.let {
                            for (file in it) {
                                Log.d("MainFile", "File: ${file.id} ${file.filename} ${file.sub} ${file.documentType}")
                            }
                            viewModel.files = it
                        }
                        Log.d("MainFile", "MainFile: ${response.body()}")
                    } else {
                        searching.value = false
                        Log.d("MainFile", "MainFile: ${response.errorBody()}")
                    }
                })
            }

            // Show the List of files
            if (searching.value) {
                FileListView(files = viewModel.files, appContext = appContext, viewModel = viewModel, lifecycleOwner = lifecycleOwner)
            }

        }
    }
}

@Composable
fun FileListView(
    files: List<FileItem>,
    appContext: Context,
    viewModel: MainViewModel,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner
){

    LazyColumn(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(files) { file ->

            // Card for each file
            Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(8.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){

                    // Column for file details
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)) {
                        EllipsisText(text = "Name:${file.filename}", maxLength = 35)

                        EllipsisText(text = "Subject:${file.sub}" , maxLength = 25)

                        EllipsisText(text = "Type:${file.documentType}", maxLength = 25)
                    }

                    // Download Icon
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_download_24),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .clickable {
                                viewModel.downloadFile(file.id)
                                viewModel.downloadfileValue.observe(
                                    lifecycleOwner,
                                    Observer { response ->
                                        if (response.isSuccessful) {
                                            response
                                                .body()
                                                ?.let {
                                                    saveResponseBodyAsPdf(
                                                        responseBody = it,
                                                        context = appContext,
                                                        fileName = file.filename,
                                                        viewModel = viewModel
                                                    )

                                                }
                                        }

                                    })
                            }
                    )
                }

            }
        }
    }
}



@Composable
fun EllipsisText(text: String, maxLength: Int) {
    val trimmedText = remember {
        if (text.length > maxLength) {
            text.substring(0, maxLength - 3) + "..."
        } else {
            text
        }
    }

    Text(
        text = trimmedText,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

//Download file and save it to Downloads folder
fun saveResponseBodyAsPdf(responseBody: ResponseBody, context: Context, fileName: String,viewModel: MainViewModel) {
    try {
        viewModel.clearDownloadedFile() // Clear the previous downloaded file, if any

        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val outputFile = File(storageDir, fileName)

        val inputStream = responseBody.byteStream()
        val outputStream = FileOutputStream(outputFile)

        val buffer = ByteArray(4096)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }

        outputStream.flush()
        outputStream.close()
        inputStream.close()

        viewModel.setDownloadedFile(responseBody, fileName)

        showToast(context, "File downloaded successfully")
    } catch (e: IOException) {
        e.printStackTrace()
        showToast(context, "Error downloading file")
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}



