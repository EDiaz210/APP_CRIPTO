package com.example.criptoapied.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.criptoapied.data.model.Crypto
import com.example.criptoapied.ui.viewmodel.FavoritesUiState
import com.example.criptoapied.ui.viewmodel.FavoritesViewModel
import java.text.NumberFormat
import java.util.Locale

/**
 * ⭐ Pantalla de Criptomonedas Favoritas
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = viewModel(),
    onCryptoClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "⭐ Favoritos",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                ),
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Refrescar"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            when (val state = uiState) {
                is FavoritesUiState.Loading -> {
                    LoadingContent()
                }
                is FavoritesUiState.Success -> {
                    FavoritesList(
                        cryptos = state.cryptos,
                        onCryptoClick = onCryptoClick,
                        onRemoveFavorite = { cryptoId ->
                            viewModel.removeFavorite(cryptoId)
                        }
                    )
                }
                is FavoritesUiState.Empty -> {
                    EmptyFavoritesContent()
                }
                is FavoritesUiState.Error -> {
                    ErrorContent(
                        message = state.message,
                        onRetry = { viewModel.refresh() }
                    )
                }
            }
        }
    }
}

/**
 * Lista de criptos favoritas
 */
@Composable
fun FavoritesList(
    cryptos: List<Crypto>,
    onCryptoClick: (String) -> Unit,
    onRemoveFavorite: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(cryptos, key = { it.id }) { crypto ->
            FavoriteCryptoCard(
                crypto = crypto,
                onCryptoClick = { onCryptoClick(crypto.id) },
                onRemoveFavorite = { onRemoveFavorite(crypto.id) }
            )
        }
    }
}

/**
 * Tarjeta de cripto favorita con opción de eliminar
 */
@Composable
fun FavoriteCryptoCard(
    crypto: Crypto,
    onCryptoClick: () -> Unit,
    onRemoveFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCryptoClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo + Nombre + Símbolo
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Logo
                AsyncImage(
                    model = crypto.image,
                    contentDescription = crypto.name,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                // Nombre y símbolo
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = crypto.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = crypto.symbol.uppercase(),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Precio
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    text = "$${NumberFormat.getInstance(Locale.US).format(crypto.currentPrice)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                if (crypto.priceChangePercentage24h != null) {
                    Text(
                        text = "${if (crypto.priceChangePercentage24h > 0) "+" else ""}${String.format(Locale.US, "%.2f", crypto.priceChangePercentage24h)}%",
                        fontSize = 12.sp,
                        color = if (crypto.priceChangePercentage24h > 0) Color.Green else Color.Red
                    )
                }
            }

            // Botón eliminar de favoritos
            IconButton(
                onClick = onRemoveFavorite,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar de favoritos",
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * Contenido cuando no hay favoritos
 */
@Composable
fun EmptyFavoritesContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Star,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .padding(bottom = 16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Sin favoritos",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Marca criptomonedas como favoritas para verlas aquí",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
