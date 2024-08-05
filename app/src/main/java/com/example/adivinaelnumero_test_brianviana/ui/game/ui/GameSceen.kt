package com.example.adivinaelnumero_test_brianviana.ui.game.ui

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.adivinaelnumero_test_brianviana.ui.game.domain.models.Difficulty
import com.example.adivinaelnumero_test_brianviana.ui.game.domain.models.GameStatus
import com.example.adivinaelnumero_test_brianviana.ui.game.domain.models.Guess
import com.example.adivinaelnumero_test_brianviana.ui.game.domain.models.GuessResult

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

            Row {
                GuessInput(
                    onGuessSubmitted = { viewModel.makeGuess(it) },
                    status = gameState.status,
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp)
                )

                ChooseDifficulty(
                    onDifficultySelected = { viewModel.startNewGame(it) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Intentos: ${gameState.attemptsLeft}",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))

            GameRecord(
                guesses = gameState.guesses,
                secretNumber = gameState.secretNumber,
                status = gameState.status,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                historyNumbers = gameState.historyNumbers,
                viewModel = viewModel
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
}

@Composable
fun GameRecord(
    guesses: List<Guess>,
    secretNumber: Int?,
    status: GameStatus,
    historyNumbers: List<Guess>,
    modifier: Modifier,
    viewModel: GameViewModel
) {


    Text("Historial", modifier)
    if (status == GameStatus.GANADO || status == GameStatus.PERDIDO) {
        Text("Numero correcto: $secretNumber")
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
            focusedElevation = 20.dp
        ),
        shape = CardDefaults.shape
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val (box1, box2, box3) = createRefs()
            ConstraintLayout(
                modifier = Modifier
                    .width(90.dp)
                    .height(180.dp)
                    .border(2.dp, Color(0xFFE91E63))
                    .padding(5.dp)
                    .constrainAs(box1) {
                        start.linkTo(parent.start)
                        end.linkTo(box2.start)
                    },
            ) {
                val (textRef, lazyColumnRef) = createRefs()
                Text(
                    text = "Menor que",
                    modifier = Modifier
                        .constrainAs(textRef) {
                            top.linkTo(parent.top, margin = 10.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
            ConstraintLayout(
                modifier = Modifier
                    .width(90.dp)
                    .height(180.dp)
                    .border(2.dp, Color(0xFFE91E63))
                    .padding(5.dp)
                    .constrainAs(box2) {
                        start.linkTo(box1.end)
                        end.linkTo(box3.start)
                    },
            ) {
                val (textRef, lazyColumnRef) = createRefs()
                Text(
                    text = "Mayor que",
                    modifier = Modifier
                        .constrainAs(textRef) {
                            top.linkTo(parent.top, margin = 10.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )

                LazyColumn(
                    modifier = Modifier
                        .constrainAs(lazyColumnRef) {
                            top.linkTo(textRef.bottom, margin = 20.dp)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(14) { // Cambia 14 por la cantidad de elementos que tengas
                        Text(text = "hola")
                    }
                }

            }
            ConstraintLayout(
                modifier = Modifier
                    .width(90.dp)
                    .height(180.dp)
                    .border(2.dp, Color(0xFFE91E63))
                    .padding(5.dp)
                    .constrainAs(box3) {
                        start.linkTo(box2.end)
                        end.linkTo(parent.end)
                    },
            ) {
                val (textRef, lazyColumnRef) = createRefs()
                Text(
                    text = "Historial",
                    modifier = Modifier
                        .constrainAs(textRef) {
                            top.linkTo(parent.top, margin = 10.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )


            }
        }
    }

}

