package com.example.vade.today.ui

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
import com.example.vade.tasks.data.Task
import com.example.vade.today.TodayViewModel

@Composable
fun TodayScreen(
    viewModel: TodayViewModel = hiltViewModel(),
    onStartReview: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Good Morning!", style = MaterialTheme.typography.headlineMedium)
            Text("Here is your focus for today.", style = MaterialTheme.typography.bodyLarge)
        }

        if (uiState.dueCardsCount > 0) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Text("${uiState.dueCardsCount} flashcards due for review", style = MaterialTheme.typography.bodyLarge)
                        Button(onClick = onStartReview) {
                            Text("Start")
                        }
                    }
                }
            }
        }

        if (uiState.top3Tasks.isNotEmpty()) {
            item {
                Text("Top 3", style = MaterialTheme.typography.titleLarge)
            }
            items(uiState.top3Tasks) { task ->
                TaskCard(task = task, onCheckedChange = { viewModel.toggleTaskCompletion(task) })
            }
        }

        if (uiState.scheduleTasks.isNotEmpty()) {
            item {
                Text("Schedule", style = MaterialTheme.typography.titleLarge)
            }
            items(uiState.scheduleTasks) { task ->
                TaskCard(task = task, onCheckedChange = { viewModel.toggleTaskCompletion(task) })
            }
        }
        
        if (uiState.habits.isNotEmpty()) {
            item {
                Text("Habits", style = MaterialTheme.typography.titleLarge)
            }
            items(uiState.habits) { habit ->
                val checkIn = uiState.checkInsToday[habit.id]
                val isDone = checkIn?.isCompleted == true
                
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Text(habit.name, style = MaterialTheme.typography.bodyLarge)
                        Checkbox(
                            checked = isDone,
                            onCheckedChange = { viewModel.toggleHabitCheckIn(habit.id) }
                        )
                    }
                }
            }
        }
        
        if (uiState.expensesThisMonth.isNotEmpty() || uiState.expensesToday.isNotEmpty()) {
            item {
                Text("Money", style = MaterialTheme.typography.titleLarge)
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Spent Today: $${"%.2f".format(uiState.totalSpentToday)}", style = MaterialTheme.typography.bodyLarge)
                        Text("This Month: $${"%.2f".format(uiState.totalSpentThisMonth)}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { uiState.budgetProgress },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
        
        if (uiState.top3Tasks.isEmpty() && uiState.scheduleTasks.isEmpty() && uiState.habits.isEmpty() && uiState.expensesToday.isEmpty() && !uiState.isLoading) {
            item {
                Text("No tasks for today. Add one?", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun TaskCard(task: Task, onCheckedChange: (Boolean) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Checkbox(checked = task.isCompleted, onCheckedChange = onCheckedChange)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = task.title, style = MaterialTheme.typography.bodyLarge)
                if (task.notes.isNotEmpty()) {
                    Text(text = task.notes, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
