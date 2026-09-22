package com.example.vade.notes.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vade.notes.data.Card
import com.example.vade.notes.data.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class ReviewUiState(
    val cards: List<Card> = emptyList(),
    val currentIndex: Int = 0,
    val isLoading: Boolean = true
) {
    val currentCard: Card? get() = cards.getOrNull(currentIndex)
    val remainingCount: Int get() = cards.size - currentIndex
}

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val repository: NotesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    fun loadDueCards(deckId: Long?) {
        viewModelScope.launch {
            val allDue = repository.getDueCards(LocalDate.now())
            val filtered = if (deckId != null) {
                allDue.filter { it.deckId == deckId }
            } else {
                allDue
            }
            // Shuffle them so the order isn't entirely predictable
            _uiState.value = ReviewUiState(cards = filtered.shuffled(), isLoading = false)
        }
    }

    fun submitReview(rating: Int) {
        val current = _uiState.value.currentCard ?: return
        
        // Simple Phase 2 SRS algorithm
        val newInterval = when (rating) {
            1 -> 0 // Again
            2 -> maxOf(1, (current.intervalDays * 1.2).toInt())
            3 -> maxOf(1, (current.intervalDays * 2.5).toInt())
            4 -> maxOf(1, (current.intervalDays * 3.5).toInt())
            else -> 1
        }
        
        val updatedCard = current.copy(
            intervalDays = newInterval,
            nextReviewDate = LocalDate.now().plusDays(newInterval.toLong())
        )
        
        viewModelScope.launch {
            repository.updateCard(updatedCard)
            _uiState.value = _uiState.value.copy(currentIndex = _uiState.value.currentIndex + 1)
        }
    }
}
