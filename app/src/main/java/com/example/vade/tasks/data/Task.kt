package com.example.vade.tasks.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

enum class Priority { LOW, MEDIUM, HIGH }
enum class RepeatRule { NONE, DAILY, WEEKDAYS, WEEKLY, MONTHLY }

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val notes: String = "",
    val dueDate: LocalDate? = null,
    val dueTime: LocalTime? = null,
    val priority: Priority = Priority.MEDIUM,
    val repeatRule: RepeatRule = RepeatRule.NONE,
    val reminderTime: LocalTime? = null,
    val isCompleted: Boolean = false,
    val completedDate: LocalDate? = null,
    val isTop3: Boolean = false,
    val linkedNoteId: Long? = null
)
