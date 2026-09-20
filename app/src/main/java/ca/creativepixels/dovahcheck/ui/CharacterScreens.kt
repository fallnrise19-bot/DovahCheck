@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package ca.creativepixels.dovahcheck.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.creativepixels.dovahcheck.data.model.CharacterProfile
import ca.creativepixels.dovahcheck.data.model.ContentProfile

@Composable
fun CharacterSetupScreen(
    profiles: List<ContentProfile>,
    onCreate: (name: String, profile: ContentProfile) -> Unit,
    onBack: (() -> Unit)? = null
) {
    var name by remember { mutableStateOf("") }
    var selected by remember(profiles) { mutableStateOf(profiles.firstOrNull()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New adventurer") },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Each character gets completely separate quest progress.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Character name") },
                    singleLine = true
                )
            }

            item {
                Text("Which Skyrim are you playing?", style = MaterialTheme.typography.titleMedium)
            }

            items(profiles) { profile ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selected = profile }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        RadioButton(
                            selected = selected?.name == profile.name,
                            onClick = { selected = profile }
                        )
                        Column {
                            Text(profile.name, style = MaterialTheme.typography.titleMedium)
                            profile.recommendedUse
                                ?.takeIf { it.isNotBlank() }
                                ?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        val profile = selected ?: return@Button
                        onCreate(name.trim(), profile)
                    },
                    enabled = name.isNotBlank() && selected != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Begin ledger")
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun CharacterPickerScreen(
    characters: List<CharacterProfile>,
    activeCharacterId: String?,
    onSelect: (CharacterProfile) -> Unit,
    onCreateNew: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Characters") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(characters) { character ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(character) }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(character.name, style = MaterialTheme.typography.titleLarge)
                        Text(character.contentProfileName)
                        if (character.id == activeCharacterId) {
                            Text(
                                "Current character",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = onCreateNew,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Create another character")
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
