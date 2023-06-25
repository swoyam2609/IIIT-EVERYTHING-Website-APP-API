package com.example.iiiteverything.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.iiiteverything.data.model.FileItem
import com.example.iiiteverything.domain.viewmodel.FileViewModel
import com.example.iiiteverything.util.DownloadResult
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val viewModel: FileViewModel = viewModel()

    val query = remember { mutableStateOf("") }
    val docType = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val downloadResult = remember { mutableStateOf<DownloadResult?>(null) }


    Column {
        TextField(
            value = query.value,
            onValueChange = { query.value = it },
            label = { Text("Search query") }
        )

        TextField(
            value = docType.value,
            onValueChange = { docType.value = it },
            label = { Text("Document type") }
        )

        Button(
            onClick = { viewModel.searchFiles(query.value, docType.value) },
            enabled = !viewModel.isLoading.value
        ) {
            Text("Search")
        }

        if (viewModel.isLoading.value) {
            CircularProgressIndicator()
        } else {
            FileList(files = viewModel.files) { fileId ->
                // Handle file selection here
                coroutineScope.launch {
                    val success = viewModel.downloadFile(fileId)
                    if (success) {
                        downloadResult.value = DownloadResult.Success
                    } else {
                        downloadResult.value = DownloadResult.Failure
                    }
                }
            }
        }
        when (val result = downloadResult.value) {
            DownloadResult.Success -> {
                Text("File downloaded successfully")
            }
            DownloadResult.Failure -> {
                Text("File download failed")
            }
            else -> {
                // Show initial state or loading state
                Text("Press the button to download the file")
            }
        }
    }
}

@Composable
fun FileList(files: List<FileItem>, onFileSelected: (String) -> Unit) {
    LazyColumn {
        items(files) { file ->
            Row(
                modifier = Modifier.fillMaxWidth().clickable { onFileSelected(file.id) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = file.name)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = file.type)
            }
        }
    }
}
