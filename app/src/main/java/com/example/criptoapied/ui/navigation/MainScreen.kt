package com.example.criptoapied.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.criptoapied.ui.screens.CryptoDetailScreen
import com.example.criptoapied.ui.screens.CryptoListScreen
import com.example.criptoapied.ui.screens.FavoritesScreen
import com.example.criptoapied.ui.screens.TrendingScreen
import com.example.criptoapied.ui.viewmodel.CryptoDetailViewModel

/**
 * 🗺️ Pantalla principal de navegación
 *
 * Maneja la navegación entre:
 * - Lista de criptomonedas (Mercado)
 * - Criptomonedas en Tendencia
 * - Detalle de cada criptomoneda
 *
 * Utiliza un BottomNavigationBar para navegar entre Mercado y Trending
 * Los clics en una crypto llevan a la pantalla de detalle
 */
@Composable
fun MainScreen() {
    // Estados de navegación
    val currentScreen = remember { mutableStateOf(NavigationScreen.Market) }
    val selectedCryptoId = remember { mutableStateOf<String?>(null) }

    // ViewModels
    val cryptoDetailViewModel: CryptoDetailViewModel = viewModel()

    // Si hay un crypto seleccionado, mostramos el detalle
    if (selectedCryptoId.value != null) {
        CryptoDetailScreen(
            cryptoId = selectedCryptoId.value!!,
            viewModel = cryptoDetailViewModel,
            onBackClick = {
                selectedCryptoId.value = null
            }
        )
    } else {
        // Si no, mostramos el contenido principal con navegación
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.background
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Mercado") },
                        label = { Text("Mercado") },
                        selected = currentScreen.value == NavigationScreen.Market,
                        onClick = { currentScreen.value = NavigationScreen.Market }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Star, contentDescription = "Trending") },
                        label = { Text("Trending") },
                        selected = currentScreen.value == NavigationScreen.Trending,
                        onClick = { currentScreen.value = NavigationScreen.Trending }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoritos") },
                        label = { Text("Favoritos") },
                        selected = currentScreen.value == NavigationScreen.Favorites,
                        onClick = { currentScreen.value = NavigationScreen.Favorites }
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues)
            ) {
                // Contenido principal según la pantalla seleccionada
                when (currentScreen.value) {
                    NavigationScreen.Market -> {
                        CryptoListScreen(
                            onCryptoClick = { cryptoId ->
                                selectedCryptoId.value = cryptoId
                            }
                        )
                    }
                    NavigationScreen.Trending -> {
                        TrendingScreen(
                            onCryptoClick = { cryptoId ->
                                selectedCryptoId.value = cryptoId
                            }
                        )
                    }
                    NavigationScreen.Favorites -> {
                        FavoritesScreen(
                            onCryptoClick = { cryptoId ->
                                selectedCryptoId.value = cryptoId
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Enum que define las pantallas de navegación disponibles
 */
enum class NavigationScreen {
    Market,    // Lista de cryptos por capitalización de mercado
    Trending,  // Cryptos más buscadas en las últimas 24h
    Favorites  // Cryptos marcadas como favoritas
}
