package com.example.adivinaelnumero_test_brianviana.ui.game.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.adivinaelnumero_test_brianviana.ui.game.domain.models.Difficulty
import com.example.adivinaelnumero_test_brianviana.ui.game.domain.models.GameStatus

@Composable
fun GameScreen(viewModel: GameViewModel = viewModel()) {
    val gameState by viewModel.gameState.collectAsState()

    Scaffold(
        topBar = { MyTopAppBar() }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp)
        ) {
            Text(
                text = "Estado: ${gameState.status.name}",
                Modifier.align(Alignment.CenterHorizontally)
            )


            ChooseDifficulty(
                onDifficultySelected = { difficulty ->
                    viewModel.startNewGame(difficulty)
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(10.dp)
            )

            GuessInput(
                onGuessSubmitted = { viewModel.makeGuess(it) },
                status = gameState.status,
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
            )

        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar() {
    CenterAlignedTopAppBar(
        title = { Text(text = "Adivina el numero") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFE91E63),
            titleContentColor = Color(0xFFFFFFFF),
            actionIconContentColor = Color(0xFFFFFFFF)
        )
    )
}

@Composable
fun ChooseDifficulty(onDifficultySelected: (Difficulty) -> Unit, modifier: Modifier) {

    var selectedText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val options = Difficulty.entries.toTypedArray()


    Column(modifier = modifier) {

        OutlinedTextField(
            value = selectedText,
            onValueChange = { selectedText = it },
            enabled = false,
            readOnly = true,
            modifier = Modifier
                .clickable { expanded = true }
                .fillMaxWidth(),
            label = { Text(text = "Opciones") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Rounded.ArrowDropDown,
                    contentDescription = "Clear text"
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            options.forEach { options ->
                DropdownMenuItem(text = { Text(text = options.nombre) }, onClick = {
                    expanded = false
                    selectedText = options.nombre
                    onDifficultySelected(options)
                })
            }
        }

    }
}

@Composable
fun GuessInput(onGuessSubmitted: (Int) -> Unit, status: GameStatus, modifier: Modifier) {
    var guess by remember { mutableStateOf("") }
    val isGameJUGANDO = status == GameStatus.JUGANDO

    fun submitGuess() {
        val guessNumber = guess.toIntOrNull()
        if (guessNumber != null) {
            onGuessSubmitted(guessNumber)
            guess = ""
        }
    }
    OutlinedTextField(
        value = guess,
        onValueChange = { guess = it },
        modifier = modifier,
        label = { Text(text = "Numero") },
        placeholder = { Text(text = "####", color = Color.Gray) },
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { submitGuess() }),
        enabled = isGameJUGANDO,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFE91E63),
            focusedLabelColor = Color(0xFFE91E63)
        )
    )
    Log.e("GuessInput", "isGameJUGANDO: $isGameJUGANDO")
    Log.e("GuessInput", "guess: $guess")
}

