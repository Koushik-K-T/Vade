package com.example.vade.habits.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vade.habits.data.Habit

@Composable
fun HabitsScreen(viewModel: HabitsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Text("+")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Your Habits", style = MaterialTheme.typography.headlineMedium)
                Text("Consistency is key. Don't worry if you miss a day.", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (uiState.activeHabits.isEmpty() && !uiState.isLoading) {
                item {
                    Text("No habits active. Start small!", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                items(uiState.activeHabits) { habit ->
                    val checkIn = uiState.checkInsToday[habit.id]
                    val isDone = checkIn?.isCompleted == true
                    
                    HabitCard(
                        habit = habit,
                        isDone = isDone,
                        onToggle = { viewModel.toggleCheckIn(habit.id) }
                    )
                }
            }
        }
        
        if (showAddDialog) {
            AddHabitDialog(
                onDismiss = { showAddDialog = false },
                onSave = { name ->
                    viewModel.addHabit(Habit(name = name))
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun HabitCard(habit: Habit, isDone: Boolean, onToggle: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(habit.name, style = MaterialTheme.typography.titleMedium)
                // Linear progress indicator acts as a visual for the Consistency Score
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = habit.consistencyScore,
                    modifier = Modifier.width(100.dp)
                )
            }
            
            Checkbox(checked = isDone, onCheckedChange = { onToggle() })
        }
    }
}

@Composable
fun AddHabitDialog(onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var name by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Habit") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Habit Name") },
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = { if (name.isNotBlank()) onSave(name) }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
