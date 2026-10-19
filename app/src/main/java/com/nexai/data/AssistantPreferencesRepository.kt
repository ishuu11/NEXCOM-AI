package com.nexai.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "assistant_preferences")

data class AssistantPreferences(
    val assistantName: String = "Nex",
    val activationPhrase: String = "Hey Nex",
    val language: String = "English",
    val backgroundActivationEnabled: Boolean = false,
)

class AssistantPreferencesRepository(private val context: Context) {
    val preferences: Flow<AssistantPreferences> = context.dataStore.data.map { values ->
        AssistantPreferences(
            assistantName = values[ASSISTANT_NAME] ?: "Nex",
            activationPhrase = values[ACTIVATION_PHRASE] ?: "Hey Nex",
            language = values[LANGUAGE] ?: "English",
            backgroundActivationEnabled = values[BACKGROUND_ACTIVATION] ?: false,
        )
    }

    suspend fun update(transform: (AssistantPreferences) -> AssistantPreferences) {
        context.dataStore.edit { values ->
            val current = AssistantPreferences(
                assistantName = values[ASSISTANT_NAME] ?: "Nex",
                activationPhrase = values[ACTIVATION_PHRASE] ?: "Hey Nex",
                language = values[LANGUAGE] ?: "English",
                backgroundActivationEnabled = values[BACKGROUND_ACTIVATION] ?: false,
            )
            transform(current).let { updated ->
                values[ASSISTANT_NAME] = updated.assistantName
                values[ACTIVATION_PHRASE] = updated.activationPhrase
                values[LANGUAGE] = updated.language
                values[BACKGROUND_ACTIVATION] = updated.backgroundActivationEnabled
            }
        }
    }

    private companion object {
        val ASSISTANT_NAME = stringPreferencesKey("assistant_name")
        val ACTIVATION_PHRASE = stringPreferencesKey("activation_phrase")
        val LANGUAGE = stringPreferencesKey("language")
        val BACKGROUND_ACTIVATION = booleanPreferencesKey("background_activation_enabled")
    }
}
