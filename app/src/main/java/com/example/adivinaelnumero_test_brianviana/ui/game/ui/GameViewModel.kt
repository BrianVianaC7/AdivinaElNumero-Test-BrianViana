package com.example.adivinaelnumero_test_brianviana.ui.game.ui

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.adivinaelnumero_test_brianviana.ui.game.domain.models.Difficulty
import com.example.adivinaelnumero_test_brianviana.ui.game.domain.models.GameState
import com.example.adivinaelnumero_test_brianviana.ui.game.domain.models.GameStatus
import com.example.adivinaelnumero_test_brianviana.ui.game.domain.models.Guess
import com.example.adivinaelnumero_test_brianviana.ui.game.domain.models.GuessResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

class GameViewModel : ViewModel() {

    private val _gameState = MutableStateFlow(
        GameState(
            difficulty = Difficulty.FACIL,
            secretNumber = 0,
            attemptsLeft = 0,
            guesses = emptyList(),
            status = GameStatus.JUGANDO,
            historyNumbers = emptyList()
        )
    )
    val gameState: StateFlow<GameState> = _gameState

    fun startNewGame(difficulty: Difficulty) {
        val secretNumber = Random.nextInt(difficulty.range.first, difficulty.range.last + 1)
        _gameState.value = GameState(
            difficulty = difficulty,
            secretNumber = secretNumber,
            attemptsLeft = difficulty.attempts,
            guesses = emptyList(),
            status = GameStatus.JUGANDO,
            historyNumbers = _gameState.value.historyNumbers
        )
    }

    fun clearHistory() {
        val state = _gameState.value
        _gameState.value = state.copy(
            guesses = emptyList(),
            historyNumbers = emptyList(),
            status = GameStatus.JUGANDO
        )
    }

    fun makeGuess(guess: Int) {
        val state = _gameState.value
        if (state.attemptsLeft <= 0 || state.status != GameStatus.JUGANDO) return

        val result = when {
            guess == state.secretNumber -> GuessResult.CORRECTO
            guess < state.secretNumber -> GuessResult.MENOR
            else -> GuessResult.MAYOR
        }

        val color = when {
            result == GuessResult.CORRECTO -> Color.Green
            else -> Color.Red
        }

        val newGuesses = state.guesses + Guess(guess, result, color)

        val newStatus = when {
            result == GuessResult.CORRECTO -> GameStatus.GANADO
            state.attemptsLeft - 1 <= 0 -> GameStatus.PERDIDO
            else -> GameStatus.JUGANDO
        }

        val newHistoryNumbers =
            if (result == GuessResult.CORRECTO || newStatus == GameStatus.PERDIDO) {
                state.historyNumbers + Guess(guess, result, color)
            } else {
                state.historyNumbers
            }

        _gameState.value = state.copy(
            attemptsLeft = state.attemptsLeft - 1,
            guesses = newGuesses,
            status = newStatus,
            historyNumbers = newHistoryNumbers
        )
    }
}