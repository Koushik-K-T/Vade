package com.example.vade.money.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val icon: String = "",
    val color: String = "", // Hex code
    val monthlyLimit: Double? = null
)

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val categoryId: Long,
    val note: String = "",
    val date: LocalDate = LocalDate.now(),
    val linkedTaskId: Long? = null,
    val linkedNoteId: Long? = null
)

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey val monthYear: String, // e.g. "2026-09"
    val overallLimit: Double
)
