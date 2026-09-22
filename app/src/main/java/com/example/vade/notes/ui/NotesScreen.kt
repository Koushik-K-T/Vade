package com.example.vade.notes.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vade.notes.data.Note
import com.example.vade.notes.data.Deck

@Composable
fun NotesScreen(
    viewModel: NotesViewModel = hiltViewModel(),
    onStartReview: () -> Unit = {}
) {
    val notes by viewModel.notes.collectAsState()
    val decks by viewModel.decks.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Text("Decks", style = MaterialTheme.typography.titleLarge)
                Button(onClick = onStartReview) { Text("Review Due") }
            }
        }
        
        if (decks.isEmpty()) {
            item { Text("No decks yet.", style = MaterialTheme.typography.bodyMedium) }
        } else {
            items(decks) { deck ->
                DeckCard(deck)
            }
        }
        
        item {
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text("Notes", style = MaterialTheme.typography.titleLarge)
        }
        
        if (notes.isEmpty()) {
            item { Text("No notes yet.", style = MaterialTheme.typography.bodyMedium) }
        } else {
            items(notes) { note ->
                NoteCard(note)
            }
        }
    }
}

@Composable
fun DeckCard(deck: Deck) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(deck.name, style = MaterialTheme.typography.titleMedium)
            if (deck.description.isNotBlank()) {
                Text(deck.description, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun NoteCard(note: Note) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(note.title, style = MaterialTheme.typography.titleMedium)
            Text(
                note.body,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            if (note.tags.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(note.tags, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
