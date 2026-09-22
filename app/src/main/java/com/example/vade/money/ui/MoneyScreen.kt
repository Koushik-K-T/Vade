package com.example.vade.money.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vade.money.data.Category
import com.example.vade.money.data.Expense

@Composable
fun MoneyScreen(viewModel: MoneyViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.ensureDefaultCategories()
    }

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
                Text("Money", style = MaterialTheme.typography.headlineMedium)
            }

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("This Month: $${"%.2f".format(uiState.totalSpentThisMonth)}", style = MaterialTheme.typography.titleLarge)
                        val limit = uiState.budget?.overallLimit ?: 0.0
                        Text("Budget: $${"%.2f".format(limit)}", style = MaterialTheme.typography.bodyMedium)
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { uiState.budgetProgress },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Today's Expenses", style = MaterialTheme.typography.titleMedium)
            }

            if (uiState.expensesToday.isEmpty()) {
                item { Text("No expenses today.", style = MaterialTheme.typography.bodyMedium) }
            } else {
                items(uiState.expensesToday) { expense ->
                    val catName = uiState.categories.find { it.id == expense.categoryId }?.name ?: "Unknown"
                    ExpenseCard(expense, catName)
                }
            }
        }

        if (showAddDialog) {
            AddExpenseDialog(
                categories = uiState.categories,
                onDismiss = { showAddDialog = false },
                onSave = { amount, categoryId, note ->
                    viewModel.addExpense(amount, categoryId, note)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun ExpenseCard(expense: Expense, categoryName: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(categoryName, style = MaterialTheme.typography.bodyLarge)
                if (expense.note.isNotBlank()) {
                    Text(expense.note, style = MaterialTheme.typography.bodySmall)
                }
            }
            Text("$${"%.2f".format(expense.amount)}", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseDialog(
    categories: List<Category>,
    onDismiss: () -> Unit,
    onSave: (Double, Long, String) -> Unit
) {
    var amountStr by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedCat by remember { mutableStateOf<Category?>(categories.firstOrNull()) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Expense") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it },
                    label = { Text("Amount ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                // Simple Dropdown for Category
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedCat?.name ?: "Select Category",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.name) },
                                onClick = {
                                    selectedCat = cat
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note (Optional)") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountStr.toDoubleOrNull()
                    if (amount != null && selectedCat != null) {
                        onSave(amount, selectedCat!!.id, note)
                    }
                }
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
