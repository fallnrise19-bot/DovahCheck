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

private data class SectionBucket(
    val release: String,
    val section: String,
    val quests: List<QuestRecord>
)

@Composable
fun HomeScreen(
    character: CharacterProfile,
    quests: List<QuestRecord>,
    progress: Map<String, QuestState>,
    onSectionSelected: (release: String, section: String) -> Unit,
    onCharacters: () -> Unit
) {
    val eligible = quests.filterNot {
        it.completionRule?.contains("exclude", ignoreCase = true) == true
    }
    val completed = eligible.count { progress[it.internalKey] == QuestState.COMPLETED }

    val sections = quests
        .groupBy { (it.release ?: "Base Game") to (it.section ?: "Other") }
        .map { (key, entries) -> SectionBucket(key.first, key.second, entries) }
        .sortedWith(compareBy<SectionBucket> { it.release != "Base Game" }.thenBy { it.section })

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
                    Column(Modifier.padding(18.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
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
                Spacer(Modifier.height(6.dp))
                Text("Quest sections", style = MaterialTheme.typography.headlineSmall)
            }

            items(sections) { bucket ->
                val sectionEligible = bucket.quests.filterNot {
                    it.completionRule?.contains("exclude", ignoreCase = true) == true
                }
                val sectionCompleted = sectionEligible.count {
                    progress[it.internalKey] == QuestState.COMPLETED
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSectionSelected(bucket.release, bucket.section) }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(bucket.section, style = MaterialTheme.typography.titleMedium)
                        if (bucket.release != "Base Game") {
                            Text(
                                bucket.release,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text("$sectionCompleted / ${sectionEligible.size} completed")
                    }
                }
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}
