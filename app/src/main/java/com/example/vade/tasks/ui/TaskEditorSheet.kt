package com.example.vade.tasks.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vade.tasks.TasksViewModel
import com.example.vade.tasks.data.Priority
import com.example.vade.tasks.data.Task
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditorSheet(
    onDismiss: () -> Unit,
    viewModel: TasksViewModel = hiltViewModel()
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    
    var title by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    var isTop3 by remember { mutableStateOf(false) }
    
    // For simplicity right now, default to today if not provided. Date Picker UI can be expanded later.
    val dueDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 32.dp), // Extra padding for system nav bar if needed
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Quick Add Task", style = MaterialTheme.typography.titleLarge)
            
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Task Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Priority:")
                SegmentedPriorityButtons(
                    selectedPriority = priority,
                    onPrioritySelected = { priority = it }
                )
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Mark as Top 3 for Today")
                Switch(checked = isTop3, onCheckedChange = { isTop3 = it })
            }
            
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        viewModel.addTask(
                            Task(
                                title = title.trim(),
                                notes = notes.trim(),
                                priority = priority,
                                isTop3 = isTop3,
                                dueDate = dueDate
                            )
                        )
                        onDismiss()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank()
            ) {
                Text("Save Task")
            }
        }
    }
}

@Composable
fun SegmentedPriorityButtons(
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Priority.entries.forEach { prio ->
            val isSelected = prio == selectedPriority
            FilterChip(
                selected = isSelected,
                onClick = { onPrioritySelected(prio) },
                label = { Text(prio.name) }
            )
        }
    }
}
