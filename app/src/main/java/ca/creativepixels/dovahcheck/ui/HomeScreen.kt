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
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.creativepixels.dovahcheck.data.model.QuestRecord

@Composable
fun HomeScreen(quests: List<QuestRecord>) {
    val sections = quests
        .groupBy { it.section ?: "Other" }
        .map { (name, entries) -> name to entries.size }
        .sortedByDescending { it.second }

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
                Text(
                    text = "DovahCheck",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Adventurer's Ledger",
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
                        Column {
                            Text("No character selected", style = MaterialTheme.typography.titleMedium)
                            Text("Character profiles and saved progress are the next foundation step.")
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
                            Text("${quests.size} tracked records", style = MaterialTheme.typography.titleMedium)
                            Text("Loaded from the versioned Skyrim data catalog.")
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(6.dp))
                Text("Quest sections", style = MaterialTheme.typography.headlineSmall)
            }

            items(sections.take(18)) { (name, count) ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(name)
                        Text(count.toString(), color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}
