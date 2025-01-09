package com.lds.quickdeal.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SpeechToTextButton(
    context: Context, onSpeechRecognized: (String) -> Unit
) {
    // Состояние для хранения результата
    val recognizedText = remember { mutableStateOf("") }

    // Лаунчер для получения результата
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val data = result.data
            val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!matches.isNullOrEmpty()) {
                recognizedText.value = matches[0]
                onSpeechRecognized(recognizedText.value)
            }
        }
    }

    Button(
        onClick = {
            try {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        //Locale.getDefault()
                        "ru-RU"
                    )
                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Говорите...")
                }
                speechLauncher.launch(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    "Ваше устройство не поддерживает распознавание речи",
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
        modifier = Modifier.padding(top = 8.dp)
    ) {
        //Text("Надиктовать содержание")
        Row(
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Mic,
                contentDescription = "Надиктовать содержание",
                tint = Color.White
            )
            Text(
                text = "Надиктовать содержание",
                style = MaterialTheme.typography.button
            )
        }
    }

    // Отображение надиктованного текста
//    if (recognizedText.value.isNotEmpty()) {
//        Text(
//            text = "Распознанный текст: ${recognizedText.value}",
//            modifier = Modifier.padding(top = 16.dp)
//        )
//    }
}
