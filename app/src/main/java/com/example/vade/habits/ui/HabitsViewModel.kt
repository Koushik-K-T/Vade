package com.example.vade.habits.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vade.habits.data.Habit
import com.example.vade.habits.data.HabitCheckIn
import com.example.vade.habits.data.HabitsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class HabitsUiState(
    val activeHabits: List<Habit> = emptyList(),
    val checkInsToday: Map<Long, HabitCheckIn> = emptyMap(),
    val isLoading: Boolean = true
)

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val repository: HabitsRepository
) : ViewModel() {

    private val today = LocalDate.now()
    
    private val habitsFlow = repository.getActiveHabits()
    private val checkInsFlow = repository.getCheckInsForDate(today)

    val uiState = combine(habitsFlow, checkInsFlow) { habits, checkIns ->
        val checkInMap = checkIns.associateBy { it.habitId }
        HabitsUiState(
            activeHabits = habits,
            checkInsToday = checkInMap,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HabitsUiState()
    )

    fun addHabit(habit: Habit) {
        viewModelScope.launch {
            repository.addHabit(habit)
        }
    }

    fun toggleCheckIn(habitId: Long) {
        viewModelScope.launch {
            val existing = repository.getCheckIn(habitId, today)
            val updated = if (existing != null) {
                // If it was completed, make it incomplete, and vice versa
                existing.copy(isCompleted = !existing.isCompleted, isSkipped = false)
            } else {
                HabitCheckIn(habitId = habitId, date = today, isCompleted = true)
            }
            repository.saveCheckIn(updated)
        }
    }
}
