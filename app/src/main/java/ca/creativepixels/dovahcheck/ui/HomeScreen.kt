package ca.creativepixels.dovahcheck.ui

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
import ca.creativepixels.dovahcheck.data.model.ShoutRecord
import ca.creativepixels.dovahcheck.ui.theme.themeForCategory
import ca.creativepixels.dovahcheck.ui.theme.themeForSection

@Composable
fun HomeScreen(
    character: CharacterProfile,
    quests: List<QuestRecord>,
    progress: Map<String, QuestState>,
    shouts: List<ShoutRecord>,
    collectedShoutWordKeys: Set<String>,
    onCategorySelected: (String) -> Unit,
    onCharacters: () -> Unit
) {
    val eligible = quests.filterNot { it.isExcludedFromCompletion() }
    val completed = eligible.count { progress[it.internalKey] == QuestState.COMPLETED }
    val overallTheme = themeForSection("Base Game", "Main Quest")

    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(Modifier.height(16.dp))
                LedgerHero(
                    title = "DovahCheck",
                    subtitle = "Adventurer's Ledger",
                    theme = overallTheme,
                    progress = "$completed / ${eligible.size} completed"
                )
            }

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
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
                Text("Your Skyrim", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "Choose a chapter. The section artwork can slot into these themed cards as we finish the asset set.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            items(BrowseTaxonomy.categories) { category ->
                val categoryQuests = category.questsForCategory(quests)
                    .filterNot { it.isExcludedFromCompletion() }
                val categoryCompleted = categoryQuests.count {
                    progress[it.internalKey] == QuestState.COMPLETED
                }

                val progressText = if (category.id == "collections") {
                    val wordCount = shouts.sumOf { it.words.size }
                    val learned = shouts.sumOf { shout ->
                        shout.words.count { it.key in collectedShoutWordKeys }
                    }
                    "$learned / $wordCount shout words learned"
                } else {
                    "$categoryCompleted / ${categoryQuests.size} completed"
                }

                LedgerBrowseCard(
                    title = category.title,
                    subtitle = category.subtitle,
                    progress = progressText,
                    theme = themeForCategory(category.id),
                    onClick = { onCategorySelected(category.id) }
                )
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
