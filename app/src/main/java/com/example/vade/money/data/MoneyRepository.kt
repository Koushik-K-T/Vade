package com.example.vade.money.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject
import javax.inject.Singleton

interface MoneyRepository {
    fun getAllCategories(): Flow<List<Category>>
    suspend fun addCategory(category: Category)

    fun getAllExpenses(): Flow<List<Expense>>
    fun getExpensesForDate(date: LocalDate): Flow<List<Expense>>
    fun getExpensesForCurrentMonth(): Flow<List<Expense>>
    suspend fun addExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)

    fun getBudgetForCurrentMonth(): Flow<Budget?>
    suspend fun setBudgetForMonth(budget: Budget)
    
    suspend fun exportExpensesToCsv(): String
}

@Singleton
class MoneyRepositoryImpl @Inject constructor(
    private val dao: MoneyDao
) : MoneyRepository {
    
    override fun getAllCategories() = dao.getAllCategories()
    
    override suspend fun addCategory(category: Category) = dao.insertCategory(category)

    override fun getAllExpenses() = dao.getAllExpenses()
    
    override fun getExpensesForDate(date: LocalDate) = dao.getExpensesForDate(date)
    
    override fun getExpensesForCurrentMonth(): Flow<List<Expense>> {
        val today = LocalDate.now()
        val startOfMonth = today.withDayOfMonth(1)
        val endOfMonth = today.withDayOfMonth(today.lengthOfMonth())
        return dao.getExpensesBetweenDates(startOfMonth, endOfMonth)
    }

    override suspend fun addExpense(expense: Expense) = dao.insertExpense(expense)
    
    override suspend fun deleteExpense(expense: Expense) = dao.deleteExpense(expense)

    override fun getBudgetForCurrentMonth(): Flow<Budget?> {
        val yearMonth = YearMonth.now().toString()
        return dao.getBudgetForMonth(yearMonth)
    }

    override suspend fun setBudgetForMonth(budget: Budget) = dao.insertBudget(budget)
    
    override suspend fun exportExpensesToCsv(): String {
        // A placeholder for the actual CSV building logic based on dao.getAllExpenses()
        return "id,amount,categoryId,note,date\n" 
    }
}
