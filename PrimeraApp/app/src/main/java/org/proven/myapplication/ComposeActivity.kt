package org.proven.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ComposeActivity : ComponentActivity() {

    private val modelGame = ModelGame()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DynamicGameScreen(modelGame)
        }
    }
}

@Composable
fun DynamicGameScreen(modelGame: ModelGame) {
    var playing by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf("") }
    var buttonVisible by remember { mutableStateOf(true) }
    var currentCubes by remember { mutableStateOf(modelGame.numCubes) }
    var isWinner by remember { mutableStateOf(false) }

    var cubeImages by remember { mutableStateOf(List(currentCubes) { R.drawable.cubilete }) }

    fun startGame() {
        modelGame.startNewGame()
        currentCubes = modelGame.numCubes
        playing = true
        buttonVisible = false
        isWinner = false
        resultMessage = "Tria un coco!"
        cubeImages = List(currentCubes) { R.drawable.cubilete }
        modelGame.shuffle()
    }

    fun nextLevel() {
        currentCubes = modelGame.numCubes
        playing = true
        buttonVisible = false
        isWinner = false
        resultMessage = "Tria un coco!"
        cubeImages = List(currentCubes) { R.drawable.cubilete }
        modelGame.shuffle()
    }

    fun onCubeClick(index: Int) {
        if (!playing) return
        playing = false

        val selectedPosition = index + 1
        val newImages = cubeImages.toMutableList()

        if (modelGame.checkSelection(selectedPosition)) {
            resultMessage = "You win!!!"
            isWinner = true
            newImages[index] = R.drawable.levantaok
        } else {
            resultMessage = "You lose!!!"
            isWinner = false
            newImages[index] = R.drawable.levantafail

            val correctIndex = modelGame.positionBall - 1
            if (correctIndex in newImages.indices) {
                newImages[correctIndex] = R.drawable.levantaok
            }
        }

        buttonVisible = true
        cubeImages = newImages
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.fondo),
                contentScale = ContentScale.Crop
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(200.dp))

            Text(
                text = "Nivell: $currentCubes",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 90.dp),
                modifier = Modifier.heightIn(max = 250.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                items(count = currentCubes) { i ->
                    Image(
                        painter = painterResource(id = cubeImages[i]),
                        contentDescription = "Cubilete",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(90.dp)
                            .padding(8.dp)
                            .clickable { onCubeClick(i) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(35.dp))

            if (buttonVisible) {
                Button(
                    onClick = { if (isWinner) nextLevel() else startGame() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text(if (isWinner) "SEGUIR (+1)" else "Start", fontSize = 16.sp)
                }
            } else {
                Spacer(modifier = Modifier.height(48.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = resultMessage,
                fontSize = 35.sp,
                color = Color.White
            )
        }
    }
}