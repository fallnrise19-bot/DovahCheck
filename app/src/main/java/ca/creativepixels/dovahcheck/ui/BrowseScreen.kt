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
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.creativepixels.dovahcheck.data.model.QuestRecord
import ca.creativepixels.dovahcheck.data.model.QuestState

@Composable
fun CategoryBrowseScreen(
    category: QuestCategory,
    quests: List<QuestRecord>,
    progress: Map<String, QuestState>,
    onBack: () -> Unit,
    onSectionSelected: (release: String, section: String) -> Unit,
    onReleaseSelected: (release: String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(category.title) },
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
                Text(
                    category.subtitle,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(6.dp))
            }

            if (category.collectionsPlaceholder) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(18.dp)) {
                            Text(
                                "Collection trackers are next",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                "Shouts, Dragon Priest masks, Daedric artifacts, Stones of Barenziah and the rest will live here instead of being mixed into the quest ledger."
                            )
                        }
                    }
                }
            }

            items(category.targets) { target ->
                val targetQuests = quests.filter {
                    (it.release ?: "Base Game") == target.release &&
                        (it.section ?: "Other") == target.section &&
                        !it.isExcludedFromCompletion()
                }
                val completed = targetQuests.count {
                    progress[it.internalKey] == QuestState.COMPLETED
                }

                BrowseCard(
                    title = target.title,
                    subtitle = target.subtitle ?: if (target.release == "Base Game") null else target.release,
                    progress = "$completed / ${targetQuests.size} completed",
                    onClick = { onSectionSelected(target.release, target.section) }
                )
            }

            items(category.releaseGroups) { release ->
                val releaseQuests = quests.filter {
                    (it.release ?: "Base Game") == release && !it.isExcludedFromCompletion()
                }
                val completed = releaseQuests.count {
                    progress[it.internalKey] == QuestState.COMPLETED
                }

                BrowseCard(
                    title = release.displayReleaseName(),
                    subtitle = releaseSubtitle(release),
                    progress = "$completed / ${releaseQuests.size} completed",
                    onClick = { onReleaseSelected(release) }
                )
            }

            item { Spacer(Modifier.height(18.dp)) }
        }
    }
}

@Composable
fun ReleaseBrowseScreen(
    release: String,
    quests: List<QuestRecord>,
    progress: Map<String, QuestState>,
    onBack: () -> Unit,
    onSectionSelected: (release: String, section: String) -> Unit
) {
    val sections = quests
        .filter { (it.release ?: "Base Game") == release }
        .groupBy { it.section ?: "Other" }
        .toList()
        .sortedBy { it.first }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(release.displayReleaseName()) },
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
            items(sections) { (section, sectionQuests) ->
                val eligible = sectionQuests.filterNot { it.isExcludedFromCompletion() }
                val completed = eligible.count {
                    progress[it.internalKey] == QuestState.COMPLETED
                }
                BrowseCard(
                    title = section,
                    subtitle = null,
                    progress = "$completed / ${eligible.size} completed",
                    onClick = { onSectionSelected(release, section) }
                )
            }

            item { Spacer(Modifier.height(18.dp)) }
        }
    }
}

@Composable
private fun BrowseCard(
    title: String,
    subtitle: String?,
    progress: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                subtitle?.takeIf { it.isNotBlank() }?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    progress,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Icon(Icons.Outlined.ChevronRight, contentDescription = null)
        }
    }
}

internal fun QuestRecord.isExcludedFromCompletion(): Boolean =
    completionRule?.contains("exclude", ignoreCase = true) == true

private fun String.displayReleaseName(): String = when (this) {
    "Free Creations (Special Edition)" -> "Special Edition Creations"
    "Creation Club / Anniversary" -> "Anniversary Creations"
    else -> this
}

private fun releaseSubtitle(release: String): String? = when (release) {
    "Dawnguard" -> "Vampire hunters, Volkihar and the Forgotten Vale"
    "Dragonborn" -> "Solstheim, Miraak and the Black Books"
    "Hearthfire" -> "Homesteads and family systems"
    "Free Creations (Special Edition)" -> "Fishing and Saints & Seducers"
    "Creation Club / Anniversary" -> "Anniversary bundled Creation quests"
    else -> null
}
