package com.example.vade.habits.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HabitsDao {
    @Query("SELECT * FROM habits WHERE isActive = 1 ORDER BY displayOrder ASC")
    fun getActiveHabits(): Flow<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit)

    @Update
    suspend fun updateHabit(habit: Habit)
    
    @Delete
    suspend fun deleteHabit(habit: Habit)

    // --- CHECK INS ---
    @Query("SELECT * FROM habit_check_ins WHERE habitId = :habitId AND date = :date LIMIT 1")
    suspend fun getCheckIn(habitId: Long, date: LocalDate): HabitCheckIn?
    
    @Query("SELECT * FROM habit_check_ins WHERE date = :date")
    fun getCheckInsForDate(date: LocalDate): Flow<List<HabitCheckIn>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckIn(checkIn: HabitCheckIn)

    @Update
    suspend fun updateCheckIn(checkIn: HabitCheckIn)

    // --- ROUTINES ---
    @Query("SELECT * FROM routines")
    fun getAllRoutines(): Flow<List<Routine>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: Routine)
}
