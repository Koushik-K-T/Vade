package com.example.vade.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vade.tasks.data.Task
import com.example.vade.tasks.data.TaskRepository
import com.example.vade.notes.data.NotesRepository
import com.example.vade.habits.data.Habit
import com.example.vade.habits.data.HabitCheckIn
import com.example.vade.habits.data.HabitsRepository
import com.example.vade.money.data.Budget
import com.example.vade.money.data.Expense
import com.example.vade.money.data.MoneyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class TodayUiState(
    val top3Tasks: List<Task> = emptyList(),
    val scheduleTasks: List<Task> = emptyList(),
    val dueCardsCount: Int = 0,
    val habits: List<Habit> = emptyList(),
    val checkInsToday: Map<Long, HabitCheckIn> = emptyMap(),
    val expensesToday: List<Expense> = emptyList(),
    val expensesThisMonth: List<Expense> = emptyList(),
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
class TodayViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val notesRepository: NotesRepository,
    private val habitsRepository: HabitsRepository,
    private val moneyRepository: MoneyRepository
) : ViewModel() {

    private val today = LocalDate.now()
    
    private val top3Flow = taskRepository.getTop3Tasks()
    private val scheduleFlow = taskRepository.getTasksForDate(today)
    private val dueCardsFlow = notesRepository.getDueCardsCountFlow(today)
    private val habitsFlow = habitsRepository.getActiveHabits()
    private val checkInsFlow = habitsRepository.getCheckInsForDate(today)
    
    private val expensesTodayFlow = moneyRepository.getExpensesForDate(today)
    private val expensesMonthFlow = moneyRepository.getExpensesForCurrentMonth()
    private val budgetFlow = moneyRepository.getBudgetForCurrentMonth()

    val uiState = combine(
        top3Flow, scheduleFlow, dueCardsFlow, habitsFlow, checkInsFlow,
        expensesTodayFlow, expensesMonthFlow, budgetFlow
    ) { args: Array<Any?> ->
        val top3 = args[0] as List<Task>
        val schedule = args[1] as List<Task>
        val dueCards = args[2] as Int
        val habits = args[3] as List<Habit>
        val checkIns = args[4] as List<HabitCheckIn>
        val todayExp = args[5] as List<Expense>
        val monthExp = args[6] as List<Expense>
        val budget = args[7] as Budget?
        
        TodayUiState(
            top3Tasks = top3,
            scheduleTasks = schedule.filter { !it.isCompleted },
            dueCardsCount = dueCards,
            habits = habits,
            checkInsToday = checkIns.associateBy { it.habitId },
            expensesToday = todayExp,
            expensesThisMonth = monthExp,
            budget = budget,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TodayUiState()
    )
    
    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            val updated = task.copy(
                isCompleted = !task.isCompleted,
                completedDate = if (!task.isCompleted) LocalDate.now() else null
            )
            taskRepository.updateTask(updated)
        }
    }

    fun toggleHabitCheckIn(habitId: Long) {
        viewModelScope.launch {
            val existing = habitsRepository.getCheckIn(habitId, today)
            val updated = if (existing != null) {
                existing.copy(isCompleted = !existing.isCompleted, isSkipped = false)
            } else {
                HabitCheckIn(habitId = habitId, date = today, isCompleted = true)
            }
            habitsRepository.saveCheckIn(updated)
        }
    }
}
