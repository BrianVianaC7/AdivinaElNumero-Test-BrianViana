package com.example.adivinaelnumero_test_brianviana.ui.game.domain.models

enum class Difficulty(val range: IntRange, val attempts: Int, val nombre: String) {
    FACIL(1..10, 5, "Fácil"),
    MEDIO(1..20, 8, "Medio"),
    AVANZADO(1..100, 15, "Avanzado"),
    EXTREMO(1..1000, 25, "Extremo")
}