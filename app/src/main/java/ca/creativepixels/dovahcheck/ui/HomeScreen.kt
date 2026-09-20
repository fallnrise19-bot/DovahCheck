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
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.creativepixels.dovahcheck.data.model.CharacterProfile
import ca.creativepixels.dovahcheck.data.model.QuestRecord
import ca.creativepixels.dovahcheck.data.model.QuestState

@Composable
fun HomeScreen(
    character: CharacterProfile,
    quests: List<QuestRecord>,
    progress: Map<String, QuestState>,
    onCategorySelected: (String) -> Unit,
    onCharacters: () -> Unit
) {
    val eligible = quests.filterNot { it.isExcludedFromCompletion() }
    val completed = eligible.count { progress[it.internalKey] == QuestState.COMPLETED }

    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(Modifier.height(20.dp))
                Text("DovahCheck", style = MaterialTheme.typography.headlineLarge)
                Text(
                    "Adventurer's Ledger",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(18.dp))
            }

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Icon(Icons.Outlined.Person, contentDescription = null)
                        Column(Modifier.weight(1f)) {
                            Text(character.name, style = MaterialTheme.typography.titleLarge)
                            Text(character.contentProfileName)
                        }
                        TextButton(onClick = onCharacters) {
                            Icon(Icons.Outlined.People, contentDescription = null)
                            Text("Characters")
                        }
                    }
                }
            }

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Icon(Icons.Outlined.AutoStories, contentDescription = null)
                        Column {
                            Text(
                                "$completed completed",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text("${eligible.size} currently countable quest records")
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(4.dp))
                Text("Your Skyrim", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "Pick a chapter instead of scrolling through the entire province.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            items(BrowseTaxonomy.categories) { category ->
                val categoryQuests = category.questsForCategory(quests)
                    .filterNot { it.isExcludedFromCompletion() }
                val categoryCompleted = categoryQuests.count {
                    progress[it.internalKey] == QuestState.COMPLETED
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCategorySelected(category.id) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(category.title, style = MaterialTheme.typography.titleMedium)
                            Text(
                                category.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (category.collectionsPlaceholder) {
                                Text(
                                    "Tracker coming next",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            } else {
                                Text(
                                    "$categoryCompleted / ${categoryQuests.size} completed",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        Icon(Icons.Outlined.ChevronRight, contentDescription = null)
                    }
                }
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

private fun QuestCategory.questsForCategory(quests: List<QuestRecord>): List<QuestRecord> {
    if (collectionsPlaceholder) return emptyList()

    val directTargets = targets.map { it.release to it.section }.toSet()
    val allowedReleases = releaseGroups.toSet()

    return quests.filter { quest ->
        val release = quest.release ?: "Base Game"
        val section = quest.section ?: "Other"
        (release to section) in directTargets || release in allowedReleases
    }
}
