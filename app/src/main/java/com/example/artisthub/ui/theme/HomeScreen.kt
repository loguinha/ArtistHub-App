package com.example.artisthub.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.artisthub.ui.theme.ArtistHubTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onStoreClick: (storeId: String) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("ArtistHub") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Tela Inicial (Home)")

            // Botão de exemplo para navegar para uma loja
            Button(onClick = { onStoreClick("loja_da_ana") }) {
                Text("Visitar Lojinha da Ana")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ArtistHubTheme {
        HomeScreen(onStoreClick = {})
    }
}