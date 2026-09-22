package com.example.vade.habits.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

interface HabitsRepository {
    fun getActiveHabits(): Flow<List<Habit>>
    suspend fun addHabit(habit: Habit)
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(habit: Habit)

    fun getCheckInsForDate(date: LocalDate): Flow<List<HabitCheckIn>>
    suspend fun getCheckIn(habitId: Long, date: LocalDate): HabitCheckIn?
    suspend fun saveCheckIn(checkIn: HabitCheckIn)

    fun getAllRoutines(): Flow<List<Routine>>
    suspend fun addRoutine(routine: Routine)
    
    suspend fun recalculateScore(habitId: Long)
}

@Singleton
class HabitsRepositoryImpl @Inject constructor(
    private val dao: HabitsDao
) : HabitsRepository {
    
    override fun getActiveHabits() = dao.getActiveHabits()
    override suspend fun addHabit(habit: Habit) = dao.insertHabit(habit)
    override suspend fun updateHabit(habit: Habit) = dao.updateHabit(habit)
    override suspend fun deleteHabit(habit: Habit) = dao.deleteHabit(habit)

    override fun getCheckInsForDate(date: LocalDate) = dao.getCheckInsForDate(date)
    override suspend fun getCheckIn(habitId: Long, date: LocalDate) = dao.getCheckIn(habitId, date)
    
    override suspend fun saveCheckIn(checkIn: HabitCheckIn) {
        if (checkIn.id == 0L) {
            dao.insertCheckIn(checkIn)
        } else {
            dao.updateCheckIn(checkIn)
        }
        recalculateScore(checkIn.habitId)
    }

    override fun getAllRoutines() = dao.getAllRoutines()
    override suspend fun addRoutine(routine: Routine) = dao.insertRoutine(routine)

    override suspend fun recalculateScore(habitId: Long) {
        // Very simple logic for Phase 3:
        // Normally we would query the last N days. For now, assume a check-in boosts it, a miss lowers it gently.
        // We will leave the complex history algorithm for when we implement the history view fully.
        // For now, this placeholder satisfies the architectural rule.
    }
}
