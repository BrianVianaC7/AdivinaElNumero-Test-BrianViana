package com.example.adivinaelnumero_test_brianviana.ui.game.domain.models

data class GameState(
    val difficulty: Difficulty,
    val secretNumber: Int,
    val attemptsLeft: Int,
    val guesses: List<Guess>,
    val status: GameStatus,
    val historyNumbers: List<Guess>
)