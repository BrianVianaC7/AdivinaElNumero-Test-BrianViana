package com.example.adivinaelnumero_test_brianviana.ui.game.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.adivinaelnumero_test_brianviana.ui.game.domain.models.Difficulty

@Composable
fun GameScreen(viewModel: GameViewModel = viewModel()) {
    val gameState by viewModel.gameState.collectAsState()

    Scaffold(
        topBar = { MyTopAppBar() }
    ) { innerPadding ->

        Column {
            ChooseDifficulty(onDifficultySelected = { difficulty ->
                viewModel.startNewGame(difficulty)
            }, modifier = Modifier.padding(innerPadding))
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

