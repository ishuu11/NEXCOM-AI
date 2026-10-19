package com.nexai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.nexai.data.AssistantPreferences
import com.nexai.data.AssistantPreferencesRepository
import com.nexai.ui.NexAiApp
import com.nexai.ui.theme.NexAiTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val preferences by lazy { AssistantPreferencesRepository(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val assistant by preferences.preferences.collectAsState(initial = AssistantPreferences())
            NexAiTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NexAiApp(
                        preferences = assistant,
                        onPreferencesChange = { update ->
                            lifecycleScope.launch { preferences.update(update) }
                        },
                    )
                }
            }
        }
    }
}
