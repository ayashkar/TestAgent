package com.example.photodiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.photodiary.ui.theme.PhotoDiaryTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DiaryEntry(
    val id: Long,
    val title: String,
    val note: String,
    val photoLabel: String,
    val createdAt: String
)

class PhotoDiaryViewModel : ViewModel() {
    private val dateFormatter = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    private var nextId = 1L

    var entries by mutableStateOf(listOf<DiaryEntry>())
        private set

    fun addEntry(title: String, note: String, photoLabel: String) {
        val trimmedTitle = title.trim()
        val trimmedNote = note.trim()
        val trimmedPhotoLabel = photoLabel.trim()
        if (trimmedTitle.isEmpty() || trimmedNote.isEmpty()) return

        val newEntry = DiaryEntry(
            id = nextId++,
            title = trimmedTitle,
            note = trimmedNote,
            photoLabel = if (trimmedPhotoLabel.isBlank()) "No photo selected" else trimmedPhotoLabel,
            createdAt = dateFormatter.format(Date())
        )
        entries = listOf(newEntry) + entries
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhotoDiaryTheme {
                PhotoDiaryApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDiaryApp(viewModel: PhotoDiaryViewModel = viewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("PhotoDiary") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            AddEntryForm(
                onAddEntry = viewModel::addEntry,
                modifier = Modifier.fillMaxWidth()
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(viewModel.entries, key = { it.id }) { entry ->
                    DiaryEntryCard(entry)
                }
            }
        }
    }
}

@Composable
private fun AddEntryForm(
    onAddEntry: (String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var photoLabel by remember { mutableStateOf("") }

    Column(
        modifier = modifier.padding(top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("What happened?") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = photoLabel,
            onValueChange = { photoLabel = it },
            label = { Text("Photo label or URI") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Button(
            onClick = {
                onAddEntry(title, note, photoLabel)
                title = ""
                note = ""
                photoLabel = ""
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Save entry")
        }
    }
}

@Composable
private fun DiaryEntryCard(entry: DiaryEntry) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = entry.title, style = MaterialTheme.typography.titleMedium)
            Text(text = entry.note, style = MaterialTheme.typography.bodyMedium)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Image(
                    imageVector = Icons.Default.Photo,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
                Text(text = entry.photoLabel, style = MaterialTheme.typography.bodySmall)
            }
            Text(text = entry.createdAt, style = MaterialTheme.typography.labelSmall)
        }
    }
}
