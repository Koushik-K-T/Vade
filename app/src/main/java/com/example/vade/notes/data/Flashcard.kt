package com.example.vade.notes.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "decks")
data class Deck(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String = ""
)

@Entity(tableName = "cards")
data class Card(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val deckId: Long,
    val sourceNoteId: Long? = null,
    val front: String,
    val back: String,
    
    // Simple Spaced Repetition (Phase 2 version)
    val nextReviewDate: LocalDate = LocalDate.now(),
    val easeFactor: Float = 2.5f,
    val intervalDays: Int = 0 
)
