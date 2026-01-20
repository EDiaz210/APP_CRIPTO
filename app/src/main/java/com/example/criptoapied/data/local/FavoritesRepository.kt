package com.example.criptoapied.data.local

import android.content.Context
import android.content.SharedPreferences

/**
 * 💾 Repositorio local para manejar criptomonedas favoritas
 * Utiliza SharedPreferences para persistencia local
 */
class FavoritesRepository(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        FAVORITES_FILE,
        Context.MODE_PRIVATE
    )

    /**
     * Obtiene la lista de IDs de criptos favoritas
     */
    fun getFavoriteIds(): Set<String> {
        return sharedPreferences.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
    }

    /**
     * Agrega una cripto a favoritos
     */
    fun addFavorite(cryptoId: String) {
        val favorites = getFavoriteIds().toMutableSet()
        favorites.add(cryptoId)
        sharedPreferences.edit().putStringSet(FAVORITES_KEY, favorites).apply()
    }

    /**
     * Elimina una cripto de favoritos
     */
    fun removeFavorite(cryptoId: String) {
        val favorites = getFavoriteIds().toMutableSet()
        favorites.remove(cryptoId)
        sharedPreferences.edit().putStringSet(FAVORITES_KEY, favorites).apply()
    }

    /**
     * Verifica si una cripto es favorita
     */
    fun isFavorite(cryptoId: String): Boolean {
        return getFavoriteIds().contains(cryptoId)
    }

    /**
     * Toggle: agrega o quita de favoritos
     */
    fun toggleFavorite(cryptoId: String) {
        if (isFavorite(cryptoId)) {
            removeFavorite(cryptoId)
        } else {
            addFavorite(cryptoId)
        }
    }

    companion object {
        private const val FAVORITES_FILE = "favorites_prefs"
        private const val FAVORITES_KEY = "favorite_cryptos"
    }
}
