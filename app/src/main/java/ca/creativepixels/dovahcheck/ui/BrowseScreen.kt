@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package ca.creativepixels.dovahcheck.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.creativepixels.dovahcheck.data.model.QuestRecord
import ca.creativepixels.dovahcheck.data.model.QuestState
import ca.creativepixels.dovahcheck.ui.theme.themeForCategory
import ca.creativepixels.dovahcheck.ui.theme.themeForRelease
import ca.creativepixels.dovahcheck.ui.theme.themeForSection

@Composable
fun CategoryBrowseScreen(
    category: QuestCategory,
    quests: List<QuestRecord>,
    progress: Map<String, QuestState>,
    onBack: () -> Unit,
    onSectionSelected: (release: String, section: String) -> Unit,
    onReleaseSelected: (release: String) -> Unit
) {
    val categoryTheme = themeForCategory(category.id)

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
                .padding(horizontal = 16.dp)
        ) {
            item {
                LedgerHero(
                    title = category.title,
                    subtitle = category.subtitle,
                    theme = categoryTheme
                )
                Spacer(Modifier.height(14.dp))
            }

            if (category.collectionsPlaceholder) {
                item {
                    LedgerBrowseCard(
                        title = "Collection trackers",
                        subtitle = "Shouts, Dragon Priest masks, Daedric artifacts, Stones of Barenziah and more.",
                        progress = "Next data module",
                        theme = categoryTheme,
                        onClick = {}
                    )
                    Spacer(Modifier.height(10.dp))
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

                LedgerBrowseCard(
                    title = target.title,
                    subtitle = target.subtitle ?: if (target.release == "Base Game") null else target.release,
                    progress = "$completed / ${targetQuests.size} completed",
                    theme = themeForSection(target.release, target.section),
                    onClick = { onSectionSelected(target.release, target.section) }
                )
                Spacer(Modifier.height(10.dp))
            }

            items(category.releaseGroups) { release ->
                val releaseQuests = quests.filter {
                    (it.release ?: "Base Game") == release && !it.isExcludedFromCompletion()
                }
                val completed = releaseQuests.count {
                    progress[it.internalKey] == QuestState.COMPLETED
                }

                LedgerBrowseCard(
                    title = release.displayReleaseName(),
                    subtitle = releaseSubtitle(release),
                    progress = "$completed / ${releaseQuests.size} completed",
                    theme = themeForRelease(release),
                    onClick = { onReleaseSelected(release) }
                )
                Spacer(Modifier.height(10.dp))
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

    val releaseTheme = themeForRelease(release)
    val eligibleRelease = quests.filter {
        (it.release ?: "Base Game") == release && !it.isExcludedFromCompletion()
    }
    val completedRelease = eligibleRelease.count {
        progress[it.internalKey] == QuestState.COMPLETED
    }

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
                .padding(horizontal = 16.dp)
        ) {
            item {
                LedgerHero(
                    title = release.displayReleaseName(),
                    subtitle = releaseSubtitle(release),
                    theme = releaseTheme,
                    progress = "$completedRelease / ${eligibleRelease.size} completed"
                )
                Spacer(Modifier.height(14.dp))
            }

            items(sections) { (section, sectionQuests) ->
                val eligible = sectionQuests.filterNot { it.isExcludedFromCompletion() }
                val completed = eligible.count {
                    progress[it.internalKey] == QuestState.COMPLETED
                }
                LedgerBrowseCard(
                    title = section,
                    subtitle = null,
                    progress = "$completed / ${eligible.size} completed",
                    theme = themeForSection(release, section),
                    onClick = { onSectionSelected(release, section) }
                )
                Spacer(Modifier.height(10.dp))
            }

            item { Spacer(Modifier.height(18.dp)) }
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
