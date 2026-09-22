package com.example.vade.di

import android.content.Context
import androidx.room.Room
import com.example.vade.database.VadeDatabase
import com.example.vade.tasks.data.TaskDao
import com.example.vade.notes.data.NotesDao
import com.example.vade.habits.data.HabitsDao
import com.example.vade.money.data.MoneyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideVadeDatabase(@ApplicationContext context: Context): VadeDatabase {
        return Room.databaseBuilder(
            context,
            VadeDatabase::class.java,
            "vade_db"
        ).build()
    }

    @Provides
    fun provideTaskDao(database: VadeDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    fun provideNotesDao(database: VadeDatabase): NotesDao {
        return database.notesDao()
    }

    @Provides
    fun provideHabitsDao(database: VadeDatabase): HabitsDao {
        return database.habitsDao()
    }

    @Provides
    fun provideMoneyDao(database: VadeDatabase): MoneyDao {
        return database.moneyDao()
    }
}
