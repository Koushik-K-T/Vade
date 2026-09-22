package com.example.vade.notes.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface NotesDao {
    // --- NOTES ---
    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    // --- DECKS ---
    @Query("SELECT * FROM decks ORDER BY name ASC")
    fun getAllDecks(): Flow<List<Deck>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeck(deck: Deck)

    // --- CARDS ---
    @Query("SELECT * FROM cards WHERE deckId = :deckId")
    fun getCardsForDeck(deckId: Long): Flow<List<Card>>

    @Query("SELECT * FROM cards WHERE nextReviewDate <= :date")
    suspend fun getDueCards(date: LocalDate): List<Card>

    @Query("SELECT COUNT(*) FROM cards WHERE nextReviewDate <= :date")
    fun getDueCardsCountFlow(date: LocalDate): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: Card)

    @Update
    suspend fun updateCard(card: Card)
}
