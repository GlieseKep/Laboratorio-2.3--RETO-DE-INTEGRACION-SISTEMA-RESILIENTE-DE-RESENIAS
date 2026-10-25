package com.example.myapplication.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Instancia única de DataStore asociada al Context
private val Context.dataStore by preferencesDataStore(
    name = "review_preferences"
)

class DraftRepository(
    private val context: Context
) {

    companion object {
        private val DRAFT_CONTENT =
            stringPreferencesKey("draft_content")
    }

    // Flujo que permite leer el borrador almacenado
    val draftContent: Flow<String> =
        context.dataStore.data.map { preferences ->

            preferences[DRAFT_CONTENT] ?: ""
        }

    // Función para guardar el borrador en disco
    suspend fun saveDraft(comment: String) {

        context.dataStore.edit { preferences ->

            preferences[DRAFT_CONTENT] = comment
        }
    }
}