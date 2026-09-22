package com.example.vade.money.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vade.money.data.Budget
import com.example.vade.money.data.Category
import com.example.vade.money.data.Expense
import com.example.vade.money.data.MoneyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class MoneyUiState(
    val expensesToday: List<Expense> = emptyList(),
    val expensesThisMonth: List<Expense> = emptyList(),
    val categories: List<Category> = emptyList(),
    val budget: Budget? = null,
    val isLoading: Boolean = true
) {
    val totalSpentToday: Double
        get() = expensesToday.sumOf { it.amount }

    val totalSpentThisMonth: Double
        get() = expensesThisMonth.sumOf { it.amount }

    val budgetProgress: Float
        get() {
            if (budget == null || budget.overallLimit == 0.0) return 0f
            return (totalSpentThisMonth / budget.overallLimit).toFloat().coerceIn(0f, 1f)
        }
}

@HiltViewModel
class MoneyViewModel @Inject constructor(
    private val repository: MoneyRepository
) : ViewModel() {

    private val today = LocalDate.now()

    private val categoriesFlow = repository.getAllCategories()
    private val expensesTodayFlow = repository.getExpensesForDate(today)
    private val expensesMonthFlow = repository.getExpensesForCurrentMonth()
    private val budgetFlow = repository.getBudgetForCurrentMonth()

    val uiState = combine(
        categoriesFlow, expensesTodayFlow, expensesMonthFlow, budgetFlow
    ) { categories, todayExp, monthExp, budget ->
        MoneyUiState(
            categories = categories,
            expensesToday = todayExp,
            expensesThisMonth = monthExp,
            budget = budget,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MoneyUiState()
    )

    fun addExpense(amount: Double, categoryId: Long, note: String) {
        viewModelScope.launch {
            repository.addExpense(
                Expense(
                    amount = amount,
                    categoryId = categoryId,
                    note = note
                )
            )
        }
    }

    fun ensureDefaultCategories() {
        viewModelScope.launch {
            val current = repository.getAllCategories().stateIn(viewModelScope).value
            if (current.isEmpty()) {
                val defaults = listOf("Food", "Transport", "Bills", "Fun", "Study", "Health", "Other")
                defaults.forEach { name ->
                    repository.addCategory(Category(name = name))
                }
            }
        }
    }

    // Dummy method for budget setting
    fun setBudget(limit: Double) {
        viewModelScope.launch {
            val yearMonth = java.time.YearMonth.now().toString()
            repository.setBudgetForMonth(Budget(yearMonth, limit))
        }
    }
}
