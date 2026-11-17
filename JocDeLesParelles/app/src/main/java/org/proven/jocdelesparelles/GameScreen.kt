package org.proven.jocdelesparelles

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameScreen() {
    val viewModel = remember { GameViewModel() }
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = uiState.backgroundRes),
            contentDescription = "Fons del joc",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Joc de les Parelles",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF9800), // Color taronja com a la imatge verda
                modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3), // 3 columnes per a que càpiguen bé les cartes
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(uiState.cards) { index, card ->
                    CardItem(card = card, onClick = { viewModel.onCardClicked(index) })
                }
            }

            if (uiState.isGameWon) {
                Text(
                    text = "Has Guanyat!",
                    fontSize = 28.sp,
                    color = Color.Green,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Button(
                onClick = { viewModel.startGame() },
                modifier = Modifier.padding(bottom = 32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.Black
                )
            ) {
                Text(text = "VALIDATE", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CardItem(card: Card, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(0.65f) // Proporció de carta més allargada
            .clip(RoundedCornerShape(8.dp))
            .border(3.dp, Color.White, RoundedCornerShape(8.dp)) // Vora blanca com a la imatge
            .clickable(onClick = onClick)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        if (card.isFaceUp || card.isMatched) {
            Image(
                painter = painterResource(id = card.imageRes),
                contentDescription = "Carta cap amunt",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Carta cap avall",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
