package com.example.reviewresilience.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.reviewresilience.data.DraftRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ReviewViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    // Repositorio de persistencia
    private val repository = DraftRepository(application)

    // Calificación observable por Compose
    var rating by mutableStateOf(1)
        private set

    // Comentario respaldado por SavedStateHandle
    var comment by mutableStateOf(
        savedStateHandle.get<String>("comment") ?: ""
    )
        private set

    // Indica si el borrador se guardó correctamente
    var draftSaved by mutableStateOf(false)
        private set

    init {
        // Recuperar el borrador cuando no existe
        // un comentario restaurado desde SavedStateHandle
        if (comment.isEmpty()) {
            loadDraft()
        }
    }

    // Actualizar calificación
    fun updateRating(newRating: Int) {
        rating = newRating.coerceIn(1, 5)
    }

    // Actualizar comentario
    fun updateComment(newComment: String) {

        comment = newComment

        // Guardar el estado para una posible
        // recreación de la Activity o del proceso
        savedStateHandle["comment"] = newComment

        draftSaved = false
    }

    // Guardar borrador en DataStore
    fun saveDraft() {

        viewModelScope.launch {

            repository.saveDraft(comment)

            draftSaved = true
        }
    }

    // Recuperar borrador de DataStore
    private fun loadDraft() {

        viewModelScope.launch {

            val savedComment = repository.draftContent.first()

            // Evitar sobrescribir texto escrito mientras
            // se estaba recuperando el borrador
            if (comment.isEmpty() && savedComment.isNotEmpty()) {

                updateComment(savedComment)
            }
        }
    }
}