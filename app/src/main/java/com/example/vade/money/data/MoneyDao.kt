package com.example.vade.money.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface MoneyDao {
    // --- CATEGORIES ---
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)
    
    @Update
    suspend fun updateCategory(category: Category)

    // --- EXPENSES ---
    @Query("SELECT * FROM expenses ORDER BY date DESC, id DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE date = :date")
    fun getExpensesForDate(date: LocalDate): Flow<List<Expense>>
    
    @Query("SELECT * FROM expenses WHERE date >= :startDate AND date <= :endDate")
    fun getExpensesBetweenDates(startDate: LocalDate, endDate: LocalDate): Flow<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    // --- BUDGETS ---
    @Query("SELECT * FROM budgets WHERE monthYear = :monthYear LIMIT 1")
    fun getBudgetForMonth(monthYear: String): Flow<Budget?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: Budget)
}
