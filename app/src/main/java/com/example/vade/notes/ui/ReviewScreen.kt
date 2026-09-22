package com.example.vade.notes.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vade.notes.data.Card

@Composable
fun ReviewScreen(
    deckId: Long? = null, // null means "review all due cards"
    viewModel: ReviewViewModel = hiltViewModel(),
    onFinish: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(deckId) {
        viewModel.loadDueCards(deckId)
    }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val currentCard = uiState.currentCard
    if (currentCard == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("All caught up!", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onFinish) { Text("Done") }
            }
        }
        return
    }

    var showAnswer by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Cards remaining: ${uiState.remainingCount}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clickable { showAnswer = true },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = if (showAnswer) currentCard.back else currentCard.front,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (showAnswer) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { viewModel.submitReview(1); showAnswer = false }) { Text("Again") }
                Button(onClick = { viewModel.submitReview(2); showAnswer = false }) { Text("Hard") }
                Button(onClick = { viewModel.submitReview(3); showAnswer = false }) { Text("Good") }
                Button(onClick = { viewModel.submitReview(4); showAnswer = false }) { Text("Easy") }
            }
        } else {
            Button(onClick = { showAnswer = true }, modifier = Modifier.fillMaxWidth()) {
                Text("Show Answer")
            }
        }
    }
}
