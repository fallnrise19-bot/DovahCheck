@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package ca.creativepixels.dovahcheck.ui

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.creativepixels.dovahcheck.data.model.ShoutRecord
import ca.creativepixels.dovahcheck.data.model.ShoutWord
import ca.creativepixels.dovahcheck.ui.theme.LedgerVisualTheme
import ca.creativepixels.dovahcheck.ui.theme.themeForCategory

@Composable
fun CollectionsScreen(
    shouts: List<ShoutRecord>,
    collectedWordKeys: Set<String>,
    onBack: () -> Unit,
    onOpenShouts: () -> Unit
) {
    val theme = themeForCategory("collections")
    val wordCount = shouts.sumOf { it.words.size }
    val learnedWords = shouts.sumOf { shout ->
        shout.words.count { it.key in collectedWordKeys }
    }
    val completeShouts = shouts.count { shout ->
        shout.words.isNotEmpty() && shout.words.all { it.key in collectedWordKeys }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Collections") },
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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                LedgerHero(
                    title = "Collections",
                    subtitle = "The things Skyrim absolutely expects you to hoard.",
                    theme = theme,
                    progress = "$completeShouts / ${shouts.size} shouts complete · $learnedWords / $wordCount words"
                )
            }

            item {
                LedgerBrowseCard(
                    title = "Dragon Shouts",
                    subtitle = "Track every shout and each individual Word of Power.",
                    progress = "$completeShouts / ${shouts.size} shouts · $learnedWords / $wordCount words",
                    theme = theme,
                    onClick = onOpenShouts
                )
            }

            item {
                Text(
                    "Coming next",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            items(
                listOf(
                    "Dragon Priest Masks",
                    "Daedric Artifacts",
                    "Stones of Barenziah",
                    "Black Books",
                    "Standing Stones",
                    "Player Homes"
                )
            ) { title ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = theme.surface.copy(alpha = 0.72f)
                    ),
                    border = BorderStroke(1.dp, theme.accentSoft.copy(alpha = 0.35f))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            title,
                            style = MaterialTheme.typography.titleMedium,
                            color = theme.titleColor
                        )
                        Text(
                            "Inventory planned",
                            style = MaterialTheme.typography.labelMedium,
                            color = theme.bodyColor
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(20.dp)) }
        }
    }
}

@Composable
fun ShoutTrackerScreen(
    shouts: List<ShoutRecord>,
    collectedWordKeys: Set<String>,
    onBack: () -> Unit,
    onWordChanged: (wordKey: String, collected: Boolean) -> Unit
) {
    val theme = themeForCategory("collections")
    val totalWords = shouts.sumOf { it.words.size }
    val learnedWords = shouts.sumOf { shout ->
        shout.words.count { it.key in collectedWordKeys }
    }
    val completeShouts = shouts.count { shout ->
        shout.words.isNotEmpty() && shout.words.all { it.key in collectedWordKeys }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dragon Shouts") },
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
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                LedgerHero(
                    title = "Dragon Shouts",
                    subtitle = "Words are tracked individually, so half-learned shouts no longer lie about being finished.",
                    theme = theme,
                    progress = "$completeShouts / ${shouts.size} shouts · $learnedWords / $totalWords words"
                )
            }

            items(shouts, key = { it.key }) { shout ->
                ShoutCard(
                    shout = shout,
                    collectedWordKeys = collectedWordKeys,
                    theme = theme,
                    onWordChanged = onWordChanged
                )
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun ShoutCard(
    shout: ShoutRecord,
    collectedWordKeys: Set<String>,
    theme: LedgerVisualTheme,
    onWordChanged: (wordKey: String, collected: Boolean) -> Unit
) {
    val learned = shout.words.count { it.key in collectedWordKeys }
    val complete = shout.words.isNotEmpty() && learned == shout.words.size
    var locationRevealed by rememberSaveable(shout.key) { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = theme.surface),
        border = BorderStroke(
            1.dp,
            if (complete) theme.accent else theme.accentSoft.copy(alpha = 0.6f)
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        shout.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = theme.titleColor
                    )
                    Text(
                        shout.release,
                        style = MaterialTheme.typography.labelSmall,
                        color = theme.accent
                    )
                }
                Text(
                    "$learned / ${shout.words.size}",
                    style = MaterialTheme.typography.labelLarge,
                    color = theme.accent
                )
            }

            shout.words.forEach { word ->
                ShoutWordRow(
                    word = word,
                    collected = word.key in collectedWordKeys,
                    theme = theme,
                    onChanged = { collected ->
                        onWordChanged(word.key, collected)
                    }
                )
            }

            shout.acquisitionSummary
                .takeIf { it.isNotBlank() }
                ?.let { location ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                "Location",
                                style = MaterialTheme.typography.labelMedium,
                                color = theme.accent
                            )
                            Text(
                                if (locationRevealed) location else "Hidden to avoid spoilers",
                                style = MaterialTheme.typography.bodySmall,
                                color = theme.bodyColor
                            )
                        }
                        TextButton(onClick = { locationRevealed = !locationRevealed }) {
                            Text(
                                if (locationRevealed) "Hide" else "Reveal",
                                color = theme.accent
                            )
                        }
                    }
                }

            TextButton(
                onClick = {
                    val collectAll = !complete
                    shout.words.forEach { word ->
                        onWordChanged(word.key, collectAll)
                    }
                }
            ) {
                Text(
                    if (complete) "Clear this shout" else "Mark all words learned",
                    color = theme.accent
                )
            }
        }
    }
}

@Composable
private fun ShoutWordRow(
    word: ShoutWord,
    collected: Boolean,
    theme: LedgerVisualTheme,
    onChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onChanged(!collected) }
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = if (collected) {
                Icons.Outlined.CheckCircle
            } else {
                Icons.Outlined.RadioButtonUnchecked
            },
            contentDescription = if (collected) "Learned" else "Not learned",
            tint = if (collected) theme.accent else theme.bodyColor
        )
        Column {
            Text(
                word.word,
                style = MaterialTheme.typography.titleSmall,
                color = theme.titleColor
            )
            Text(
                word.translation,
                style = MaterialTheme.typography.bodySmall,
                color = theme.bodyColor
            )
        }
    }
}
