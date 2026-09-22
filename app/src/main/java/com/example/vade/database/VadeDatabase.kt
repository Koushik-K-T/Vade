package com.example.vade.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vade.tasks.data.Task
import com.example.vade.tasks.data.TaskDao
import com.example.vade.notes.data.Note
import com.example.vade.notes.data.Deck
import com.example.vade.notes.data.Card
import com.example.vade.notes.data.NotesDao
import com.example.vade.habits.data.Habit
import com.example.vade.habits.data.HabitCheckIn
import com.example.vade.habits.data.Routine
import com.example.vade.habits.data.HabitsDao
import com.example.vade.money.data.Category
import com.example.vade.money.data.Expense
import com.example.vade.money.data.Budget
import com.example.vade.money.data.MoneyDao

@Database(
    entities = [
        Task::class, 
        Note::class, Deck::class, Card::class,
        Habit::class, HabitCheckIn::class, Routine::class,
        Category::class, Expense::class, Budget::class
    ], 
    version = 1, 
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class VadeDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun notesDao(): NotesDao
    abstract fun habitsDao(): HabitsDao
    abstract fun moneyDao(): MoneyDao
}
