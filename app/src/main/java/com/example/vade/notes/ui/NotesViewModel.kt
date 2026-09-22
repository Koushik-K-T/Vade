package com.example.vade.notes.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vade.notes.data.Card
import com.example.vade.notes.data.Deck
import com.example.vade.notes.data.Note
import com.example.vade.notes.data.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: NotesRepository
) : ViewModel() {

    val notes = repository.getAllNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val decks = repository.getAllDecks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addNote(note: Note) {
        viewModelScope.launch {
            repository.addNote(note)
        }
    }

    fun addDeck(deck: Deck) {
        viewModelScope.launch {
            repository.addDeck(deck)
        }
    }

    fun addCard(card: Card) {
        viewModelScope.launch {
            repository.addCard(card)
        }
    }
    
    // Very simple spacing logic for phase 2. 
    // Later we will migrate to FSRS (Phase 6).
    fun submitReview(card: Card, rating: Int) {
        // rating: 1 = Again, 2 = Hard, 3 = Good, 4 = Easy
        val newInterval = when (rating) {
            1 -> 0 // Again -> Review today
            2 -> maxOf(1, (card.intervalDays * 1.2).toInt())
            3 -> maxOf(1, (card.intervalDays * 2.5).toInt())
            4 -> maxOf(1, (card.intervalDays * 3.5).toInt())
            else -> 1
        }
        
        val updatedCard = card.copy(
            intervalDays = newInterval,
            nextReviewDate = LocalDate.now().plusDays(newInterval.toLong())
        )
        
        viewModelScope.launch {
            repository.updateCard(updatedCard)
        }
    }
}
