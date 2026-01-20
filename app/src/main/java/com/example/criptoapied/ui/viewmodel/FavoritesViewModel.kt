package com.example.criptoapied.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.criptoapied.data.local.FavoritesRepository
import com.example.criptoapied.data.model.Crypto
import com.example.criptoapied.data.repository.CryptoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estados para la pantalla de Favoritos
 */
sealed class FavoritesUiState {
    object Loading : FavoritesUiState()
    data class Success(val cryptos: List<Crypto>) : FavoritesUiState()
    object Empty : FavoritesUiState()
    data class Error(val message: String) : FavoritesUiState()
}

/**
 * 💾 ViewModel para gestionar criptomonedas favoritas
 */
class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val cryptoRepository = CryptoRepository()
    private val favoritesRepository = FavoritesRepository(application.applicationContext)

    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    /**
     * Carga todas las criptos favoritas desde la API
     * usando los IDs guardados en SharedPreferences
     */
    fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = FavoritesUiState.Loading

            val favoriteIds = favoritesRepository.getFavoriteIds()

            if (favoriteIds.isEmpty()) {
                _uiState.value = FavoritesUiState.Empty
                return@launch
            }

            try {
                // Obtener la lista completa de cryptos
                cryptoRepository.getCryptoList()
                    .onSuccess { allCryptos ->
                        // Filtrar solo los favoritos y marcarlos
                        val favorites = allCryptos
                            .filter { it.id in favoriteIds }
                            .map { it.copy(isFavorite = true) }

                        if (favorites.isEmpty()) {
                            _uiState.value = FavoritesUiState.Empty
                        } else {
                            _uiState.value = FavoritesUiState.Success(favorites)
                        }
                    }
                    .onFailure { error ->
                        _uiState.value = FavoritesUiState.Error(
                            error.message ?: "Error al cargar favoritos"
                        )
                    }
            } catch (e: Exception) {
                _uiState.value = FavoritesUiState.Error(
                    e.message ?: "Error desconocido"
                )
            }
        }
    }

    /**
     * Elimina una cripto de favoritos
     */
    fun removeFavorite(cryptoId: String) {
        favoritesRepository.removeFavorite(cryptoId)
        // Recargar la lista de favoritos
        loadFavorites()
    }

    /**
     * Refresca la lista de favoritos
     */
    fun refresh() {
        loadFavorites()
    }
}
