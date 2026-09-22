package com.example.vade.tasks.ui

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
import com.example.vade.tasks.TasksViewModel
import com.example.vade.today.ui.TaskCard

@Composable
fun TasksScreen(viewModel: TasksViewModel = hiltViewModel()) {
    val tasks by viewModel.tasks.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("All Tasks", style = MaterialTheme.typography.headlineMedium)
        }
        items(tasks) { task ->
            TaskCard(task = task, onCheckedChange = { viewModel.toggleTaskCompletion(task) })
        }
    }
}
