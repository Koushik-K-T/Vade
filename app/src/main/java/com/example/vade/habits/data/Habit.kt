package com.example.vade.habits.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

// We'll keep schedules simple: Daily, or a specific list of weekdays (1=Mon, 7=Sun)
enum class HabitScheduleType { DAILY, SPECIFIC_DAYS }

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val icon: String = "", // Could be an emoji string
    val scheduleType: HabitScheduleType = HabitScheduleType.DAILY,
    val scheduleDaysOfWeek: String = "", // Comma-separated integers e.g. "1,3,5" for Mon/Wed/Fri
    val reminderTime: LocalTime? = null,
    val isActive: Boolean = true,
    val displayOrder: Int = 0,
    val consistencyScore: Float = 1.0f // 0.0 to 1.0
)

@Entity(tableName = "habit_check_ins")
data class HabitCheckIn(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val habitId: Long,
    val date: LocalDate,
    val isCompleted: Boolean = false,
    val isSkipped: Boolean = false, // Skipped means explicitly exempted for the day
    val note: String = ""
)

// A simple Routine grouping for morning/evening
@Entity(tableName = "routines")
data class Routine(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val timeOfDay: String = "Morning", // Or "Evening"
    val habitIds: String = "" // Comma-separated habit IDs
)
