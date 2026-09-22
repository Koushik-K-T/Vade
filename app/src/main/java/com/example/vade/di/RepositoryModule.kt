package com.example.vade.di

import com.example.vade.tasks.data.TaskRepository
import com.example.vade.tasks.data.TaskRepositoryImpl
import com.example.vade.notes.data.NotesRepository
import com.example.vade.notes.data.NotesRepositoryImpl
import com.example.vade.habits.data.HabitsRepository
import com.example.vade.habits.data.HabitsRepositoryImpl
import com.example.vade.money.data.MoneyRepository
import com.example.vade.money.data.MoneyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindNotesRepository(
        notesRepositoryImpl: NotesRepositoryImpl
    ): NotesRepository

    @Binds
    @Singleton
    abstract fun bindHabitsRepository(
        habitsRepositoryImpl: HabitsRepositoryImpl
    ): HabitsRepository

    @Binds
    @Singleton
    abstract fun bindMoneyRepository(
        moneyRepositoryImpl: MoneyRepositoryImpl
    ): MoneyRepository
}
