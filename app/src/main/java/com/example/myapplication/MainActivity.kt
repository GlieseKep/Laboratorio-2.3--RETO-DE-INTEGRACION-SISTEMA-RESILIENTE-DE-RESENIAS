package com.example.myapplication

import android.os.Bundle
import android.util.Log

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.myapplication.viewmodel.ReviewViewModel

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "ReviewLifecycle"
    }

    // 1. onCreate
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate: Activity creada")

        setContent {

            MaterialTheme {

                val reviewViewModel: ReviewViewModel = viewModel()

                ReviewScreen(reviewViewModel)
            }
        }
    }

    // 2. onStart
    override fun onStart() {

        super.onStart()

        Log.d(TAG, "onStart: Activity visible")
    }

    // 3. onResume
    override fun onResume() {

        super.onResume()

        Log.d(TAG, "onResume: Activity interactiva")
    }

    // 4. onPause
    override fun onPause() {

        Log.d(TAG, "onPause: Activity pausada")

        super.onPause()
    }

    // 5. onStop
    override fun onStop() {

        Log.d(TAG, "onStop: Activity detenida")

        super.onStop()
    }

    // 6. onDestroy
    override fun onDestroy() {

        Log.d(TAG, "onDestroy: Activity destruida")

        super.onDestroy()
    }
}

@Composable
fun ReviewScreen(
    reviewViewModel: ReviewViewModel
) {

    // Bloquear la navegación hacia atrás
    // únicamente cuando existe texto
    BackHandler(
        enabled = reviewViewModel.comment.isNotEmpty()
    ) {
        // No ejecutar ninguna acción.
        // El gesto Atrás queda interceptado.
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.Top
        ) {

            // Título
            Text(
                text = "Sistema de Reseñas",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Califica tu experiencia con el producto",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            // Calificación
            Text(
                text = "Calificación: ${reviewViewModel.rating} / 5",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // Botones para seleccionar la calificación
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                for (value in 1..5) {

                    if (reviewViewModel.rating == value) {

                        Button(
                            onClick = {
                                reviewViewModel.updateRating(value)
                            }
                        ) {

                            Text(
                                text = value.toString()
                            )
                        }

                    } else {

                        OutlinedButton(
                            onClick = {
                                reviewViewModel.updateRating(value)
                            }
                        ) {

                            Text(
                                text = value.toString()
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            // Campo de comentario
            OutlinedTextField(
                value = reviewViewModel.comment,

                onValueChange = { newText ->

                    reviewViewModel.updateComment(newText)
                },

                label = {
                    Text("Comentario")
                },

                placeholder = {
                    Text("Escribe tu opinión del producto...")
                },

                modifier = Modifier.fillMaxWidth(),

                minLines = 4,

                maxLines = 6
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // Botón para guardar borrador
            Button(
                onClick = {

                    reviewViewModel.saveDraft()
                },

                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Guardar Borrador")
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // Confirmación de guardado
            if (reviewViewModel.draftSaved) {

                Text(
                    text = "Borrador guardado correctamente",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // Información sobre persistencia
            Text(
                text = "Tu calificación se conserva al rotar la pantalla. " +
                        "El comentario se restaura ante cambios de configuración " +
                        "y recuperación del proceso. Guarda el borrador para " +
                        "conservarlo después de cerrar la aplicación.",

                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}