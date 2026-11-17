package org.proven.jocdelesparelles

data class Card(
    val id: Int,
    val imageRes: Int,
    var isFaceUp: Boolean = false,
    var isMatched: Boolean = false
)

data class ModelGame(
    val cards: List<Card> = emptyList(),
    val firstSelectedCardIndex: Int? = null,
    val clicks: Int = 0,
    val matchedPairs: Int = 0,
    val isProcessing: Boolean = false,
    val isGameWon: Boolean = false,
    val backgroundRes: Int = R.drawable.fondo1
)
