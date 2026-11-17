package org.proven.jocdelesparelles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ModelGame())
    val uiState: StateFlow<ModelGame> = _uiState.asStateFlow()

    init {
        startGame()
    }

    fun startGame() {
        // Deixem 3 imatges perquè siguin 6 cartes en total (graella de 3x2) i encaixin perfecte
        val images = listOf(
            R.drawable.eugenio,
            R.drawable.faemino,
            R.drawable.chiquito
        )
        val backgrounds = listOf(
            R.drawable.fondo1,
            R.drawable.fondo2,
            R.drawable.fondo3,
            R.drawable.fondo4
        )
        
        // Duplicem les imatges per fer les parelles i barregem
        val gameCards = (images + images).shuffled().mapIndexed { index, imageRes ->
            Card(id = index, imageRes = imageRes)
        }
        
        _uiState.value = ModelGame(
            cards = gameCards,
            backgroundRes = backgrounds.random()
        )
    }

    fun onCardClicked(index: Int) {
        val state = _uiState.value
        if (state.isProcessing || state.cards[index].isFaceUp || state.cards[index].isMatched) return

        val newCards = state.cards.toMutableList()
        newCards[index] = newCards[index].copy(isFaceUp = true)

        if (state.firstSelectedCardIndex == null) {
            // Primer intent de la parella
            _uiState.update { 
                it.copy(
                    cards = newCards,
                    firstSelectedCardIndex = index,
                    clicks = it.clicks + 1
                )
            }
        } else {
            // Segon intent de la parella
            _uiState.update {
                it.copy(
                    cards = newCards,
                    isProcessing = true,
                    clicks = it.clicks + 1
                )
            }
            checkMatch(state.firstSelectedCardIndex, index)
        }
    }

    private fun checkMatch(firstIndex: Int, secondIndex: Int) {
        viewModelScope.launch {
            val state = _uiState.value
            val firstCard = state.cards[firstIndex]
            val secondCard = state.cards[secondIndex]

            if (firstCard.imageRes == secondCard.imageRes) {
                // Hi ha coincidència
                delay(500) // Petita pausa per veure la carta
                _uiState.update { currentState ->
                    val newCards = currentState.cards.toMutableList()
                    newCards[firstIndex] = newCards[firstIndex].copy(isMatched = true)
                    newCards[secondIndex] = newCards[secondIndex].copy(isMatched = true)
                    
                    val newMatchedPairs = currentState.matchedPairs + 1
                    currentState.copy(
                        cards = newCards,
                        firstSelectedCardIndex = null,
                        isProcessing = false,
                        isGameWon = newMatchedPairs == 3 // Són 3 parelles en total ara
                    )
                }
            } else {
                // No hi ha coincidència, girem les cartes
                delay(1000)
                _uiState.update { currentState ->
                    val newCards = currentState.cards.toMutableList()
                    newCards[firstIndex] = newCards[firstIndex].copy(isFaceUp = false)
                    newCards[secondIndex] = newCards[secondIndex].copy(isFaceUp = false)
                    
                    currentState.copy(
                        cards = newCards,
                        firstSelectedCardIndex = null,
                        isProcessing = false
                    )
                }
            }
        }
    }
}
