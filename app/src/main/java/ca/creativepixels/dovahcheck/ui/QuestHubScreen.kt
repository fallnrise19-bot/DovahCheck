@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package ca.creativepixels.dovahcheck.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.creativepixels.dovahcheck.data.model.QuestRecord
import ca.creativepixels.dovahcheck.data.model.QuestState
import ca.creativepixels.dovahcheck.ui.theme.themeForCategory
import ca.creativepixels.dovahcheck.ui.theme.themeForSection

@Composable
fun QuestHubScreen(
    quests: List<QuestRecord>,
    progress: Map<String, QuestState>,
    onCategorySelected: (String) -> Unit,
    onHome: () -> Unit,
    onHolds: () -> Unit,
    onCollections: () -> Unit,
    onMore: () -> Unit
) {
    val questCategories = BrowseTaxonomy.categories.filter {
        it.id !in setOf("holds", "collections")
    }
    val eligible = quests.filterNot { it.isExcludedFromCompletion() }
    val completed = eligible.count { progress[it.internalKey] == QuestState.COMPLETED }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Quests") })
        },
        bottomBar = {
            LedgerBottomNav(
                selected = LedgerNavItem.QUESTS,
                onHome = onHome,
                onQuests = {},
                onHolds = onHolds,
                onCollections = onCollections,
                onMore = onMore
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
                    title = "Quest Ledger",
                    subtitle = "Storylines, factions, Daedric bargains, wars and all the trouble in between.",
                    theme = themeForSection("Base Game", "Main Quest"),
                    progress = "$completed / ${eligible.size} currently tracked"
                )
                Spacer(Modifier.height(14.dp))
            }

            items(questCategories, key = { it.id }) { category ->
                val categoryQuests = category.questsForHub(quests)
                    .filterNot { it.isExcludedFromCompletion() }
                val categoryCompleted = categoryQuests.count {
                    progress[it.internalKey] == QuestState.COMPLETED
                }

                LedgerBrowseCard(
                    title = category.title,
                    subtitle = category.subtitle,
                    progress = "$categoryCompleted / ${categoryQuests.size} completed",
                    theme = themeForCategory(category.id),
                    onClick = { onCategorySelected(category.id) }
                )
                Spacer(Modifier.height(10.dp))
            }

            item { Spacer(Modifier.height(18.dp)) }
        }
    }
}

internal fun QuestCategory.questsForHub(quests: List<QuestRecord>): List<QuestRecord> {
    val directTargets = targets.map { it.release to it.section }.toSet()
    val allowedReleases = releaseGroups.toSet()

    return quests.filter { quest ->
        val release = quest.release ?: "Base Game"
        val section = quest.section ?: "Other"
        (release to section) in directTargets || release in allowedReleases
    }
}
