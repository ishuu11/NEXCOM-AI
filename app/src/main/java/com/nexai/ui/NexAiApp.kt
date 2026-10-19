package com.nexai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardVoice
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nexai.data.AssistantPreferences
import com.nexai.ui.theme.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private enum class AppTab { Home, Activity, Settings }

@Composable
fun NexAiApp(
    preferences: AssistantPreferences,
    onPreferencesChange: ( (AssistantPreferences) -> AssistantPreferences ) -> Unit,
) {
    var selectedTab by remember { mutableStateOf(AppTab.Home) }
    Scaffold(
        containerColor = Canvas,
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
                listOf(AppTab.Home, AppTab.Activity, AppTab.Settings).forEach { tab ->
                    val icon = when (tab) {
                        AppTab.Home -> Icons.Default.Home
                        AppTab.Activity -> Icons.Default.History
                        AppTab.Settings -> Icons.Default.Settings
                    }
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = { Icon(icon, contentDescription = null) },
                        label = { Text(tab.name) },
                    )
                }
            }
        },
    ) { insets ->
        when (selectedTab) {
            AppTab.Home -> HomeScreen(
                preferences,
                onOpenSettings = { selectedTab = AppTab.Settings },
                modifier = Modifier.padding(insets),
            )
            AppTab.Activity -> ActivityScreen(Modifier.padding(insets))
            AppTab.Settings -> SettingsScreen(
                preferences = preferences,
                onPreferencesChange = onPreferencesChange,
                modifier = Modifier.padding(insets),
            )
        }
    }
}

@Composable
private fun HomeScreen(
    preferences: AssistantPreferences,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(start = 22.dp, end = 22.dp, top = 18.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    val date = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.getDefault()))
                    val greeting = when (LocalTime.now().hour) {
                        in 5..11 -> "Good morning"
                        in 12..16 -> "Good afternoon"
                        else -> "Good evening"
                    }
                    Text(date.uppercase(Locale.getDefault()), style = MaterialTheme.typography.labelMedium, color = Muted)
                    Spacer(Modifier.height(5.dp))
                    Text(greeting, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Ink)
                }
                Box(
                    modifier = Modifier.size(46.dp).clip(CircleShape).background(Color.White).clickable(onClick = onOpenSettings),
                    contentAlignment = Alignment.Center,
                ) { Text("I", color = Violet, fontWeight = FontWeight.Bold) }
            }
        }
        item {
            AssistantCard(preferences)
        }
        item {
            SectionHeading("Quick actions", "Your everyday shortcuts")
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ActionTile(Icons.Default.Language, "Translate a phrase", "Coming soon", Blue, Modifier.fillMaxWidth())
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ActionTile(Icons.Default.Bolt, "Set a timer", "Coming soon", Orange, Modifier.weight(1f))
                    ActionTile(Icons.Default.Tune, "Make it yours", "Open Settings", Violet, Modifier.weight(1f)) {
                        onOpenSettings()
                    }
                }
            }
        }
        item {
            SectionHeading("Built for your voice", "Ready for what comes next")
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Color.White).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Default.KeyboardVoice, null, tint = Teal, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("English · Hindi · Hinglish", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = Ink)
                    Text("Your assistant, your way", style = MaterialTheme.typography.bodySmall, color = Muted)
                }
                Icon(Icons.Default.ChevronRight, null, tint = Muted)
            }
        }
    }
}

@Composable
private fun AssistantCard(preferences: AssistantPreferences) {
    Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(28.dp)).background(
            Brush.linearGradient(listOf(DeepNavy, Color(0xFF293B72), Violet)),
        ).padding(22.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(38.dp).clip(CircleShape).background(Color.White.copy(alpha = .12f)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Mic, null, tint = Color.White, modifier = Modifier.size(19.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(preferences.assistantName, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.SemiBold)
                Text("YOUR PERSONAL ASSISTANT", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = .7f))
            }
            Box(Modifier.clip(CircleShape).background(Color.White.copy(alpha = .14f)).padding(horizontal = 10.dp, vertical = 6.dp)) {
                Text("FOUNDATION", style = MaterialTheme.typography.labelSmall, color = Color.White)
            }
        }
        Spacer(Modifier.height(28.dp))
        Text("Say something", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(Modifier.height(4.dp))
        Text("Voice conversations are coming soon.", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = .75f))
        Spacer(Modifier.height(20.dp))
        Row(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(Color.White.copy(alpha = .1f)).padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(Modifier.size(34.dp).clip(CircleShape).background(Color.White.copy(alpha = .12f)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Mic, null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text("Activation phrase", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = .68f))
                Text(preferences.activationPhrase, style = MaterialTheme.typography.titleSmall, color = Color.White, fontWeight = FontWeight.Medium)
            }
            Text("NOT ACTIVE", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = .72f))
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {},
            enabled = false,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(disabledContainerColor = Color.White, disabledContentColor = DeepNavy),
        ) {
            Icon(Icons.Default.Mic, null, modifier = Modifier.size(19.dp))
            Spacer(Modifier.width(9.dp))
            Text("Voice controls coming soon", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun SectionHeading(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Ink)
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Muted)
    }
}

@Composable
private fun ActionTile(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    accent: Color,
    modifier: Modifier,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.clip(RoundedCornerShape(20.dp)).background(Color.White)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(40.dp).clip(RoundedCornerShape(14.dp)).background(accent.copy(alpha = .11f)), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = accent, modifier = Modifier.size(21.dp))
        }
        Spacer(Modifier.width(11.dp))
        Column {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = Ink, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = Muted)
        }
    }
}

@Composable
private fun ActivityScreen(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Activity", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Ink)
        Text("Your conversations and actions will appear here.", style = MaterialTheme.typography.bodyMedium, color = Muted)
        Spacer(Modifier.height(18.dp))
        Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(Color.White).padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.History, null, tint = Violet)
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Nothing here yet", fontWeight = FontWeight.SemiBold, color = Ink)
                Text("Start a voice conversation when Nex is ready.", color = Muted, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun SettingsScreen(
    preferences: AssistantPreferences,
    onPreferencesChange: ((AssistantPreferences) -> AssistantPreferences) -> Unit,
    modifier: Modifier = Modifier,
) {
    var editName by remember { mutableStateOf(false) }
    var editPhrase by remember { mutableStateOf(false) }
    Column(
        modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(22.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Your assistant", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Ink)
            Text("Make Nex feel like yours.", style = MaterialTheme.typography.bodyMedium, color = Muted)
        }
        SettingRow("Assistant name", preferences.assistantName, "Personalize how you address it") { editName = true }
        SettingRow("Activation phrase", preferences.activationPhrase, "Choose what you’ll say to get started") { editPhrase = true }
        Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Color.White).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Voice language", fontWeight = FontWeight.SemiBold, color = Ink)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("English", "Hindi", "Hinglish").forEach { language ->
                    val active = preferences.language == language
                    Row(
                        Modifier.clip(CircleShape).background(if (active) Violet.copy(alpha = .1f) else Canvas)
                            .border(1.dp, if (active) Violet.copy(alpha = .3f) else Color.Transparent, CircleShape)
                            .clickable { onPreferencesChange { it.copy(language = language) } }
                            .padding(horizontal = 12.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (active) { Icon(Icons.Default.Check, null, tint = Violet, modifier = Modifier.size(14.dp)); Spacer(Modifier.width(4.dp)) }
                        Text(language, style = MaterialTheme.typography.labelMedium, color = if (active) Violet else Ink)
                    }
                }
            }
        }
        Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Color.White).padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Background activation", fontWeight = FontWeight.SemiBold, color = Ink)
                    Text("Hands-free listening when supported", style = MaterialTheme.typography.bodySmall, color = Muted)
                }
                Switch(
                    checked = preferences.backgroundActivationEnabled,
                    onCheckedChange = { enabled -> onPreferencesChange { it.copy(backgroundActivationEnabled = enabled) } },
                )
            }
            if (preferences.backgroundActivationEnabled) {
                Spacer(Modifier.height(8.dp))
                Text("Background activation is a saved preference. Voice listening is not available in this foundation release.", style = MaterialTheme.typography.bodySmall, color = Orange)
            }
        }
        Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Color.White).padding(16.dp)) {
            Text("Connect a device", fontWeight = FontWeight.SemiBold, color = Ink)
            Spacer(Modifier.height(4.dp))
            Text("USB-C AI hardware support is planned for a future release.", style = MaterialTheme.typography.bodySmall, color = Muted)
        }
    }
    if (editName) EditNameDialog(preferences.assistantName, { editName = false }) { name ->
        onPreferencesChange { it.copy(assistantName = name.ifBlank { "Nex" }) }; editName = false
    }
    if (editPhrase) EditNameDialog("${preferences.activationPhrase}", { editPhrase = false }, "Activation phrase") { phrase ->
        onPreferencesChange { it.copy(activationPhrase = phrase.ifBlank { "Hey Nex" }) }; editPhrase = false
    }
}

@Composable
private fun SettingRow(title: String, value: String, hint: String, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Color.White).clickable(onClick = onClick).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(title, fontWeight = FontWeight.SemiBold, color = Ink)
            Text(value, color = Violet, style = MaterialTheme.typography.bodyMedium)
            Text(hint, color = Muted, style = MaterialTheme.typography.bodySmall)
        }
        Icon(Icons.Default.ChevronRight, null, tint = Muted)
    }
}

@Composable
private fun EditNameDialog(initial: String, onDismiss: () -> Unit, title: String = "Assistant name", onSave: (String) -> Unit) {
    var value by remember(initial) { mutableStateOf(initial) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { OutlinedTextField(value = value, onValueChange = { value = it }, singleLine = true, label = { Text(title) }) },
        confirmButton = { TextButton(onClick = { onSave(value.trim()) }) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}
