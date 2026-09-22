package com.example.vade.notes.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

interface NotesRepository {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun addNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)

    fun getAllDecks(): Flow<List<Deck>>
    suspend fun addDeck(deck: Deck)
    
    fun getCardsForDeck(deckId: Long): Flow<List<Card>>
    suspend fun getDueCards(date: LocalDate): List<Card>
    fun getDueCardsCountFlow(date: LocalDate): Flow<Int>
    suspend fun addCard(card: Card)
    suspend fun updateCard(card: Card)
}

@Singleton
class NotesRepositoryImpl @Inject constructor(
    private val dao: NotesDao
) : NotesRepository {
    override fun getAllNotes() = dao.getAllNotes()
    override suspend fun addNote(note: Note) = dao.insertNote(note)
    override suspend fun updateNote(note: Note) = dao.updateNote(note)
    override suspend fun deleteNote(note: Note) = dao.deleteNote(note)

    override fun getAllDecks() = dao.getAllDecks()
    override suspend fun addDeck(deck: Deck) = dao.insertDeck(deck)

    override fun getCardsForDeck(deckId: Long) = dao.getCardsForDeck(deckId)
    override suspend fun getDueCards(date: LocalDate) = dao.getDueCards(date)
    override fun getDueCardsCountFlow(date: LocalDate) = dao.getDueCardsCountFlow(date)
    override suspend fun addCard(card: Card) = dao.insertCard(card)
    override suspend fun updateCard(card: Card) = dao.updateCard(card)
}
