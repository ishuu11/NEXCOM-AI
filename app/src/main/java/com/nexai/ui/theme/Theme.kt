package com.nexai.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val NexColors = lightColorScheme(
    primary = Violet,
    secondary = Teal,
    background = Canvas,
    surface = androidx.compose.ui.graphics.Color.White,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onBackground = Ink,
    onSurface = Ink,
)

@Composable
fun NexAiTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = NexColors, typography = Typography(), content = content)
}
