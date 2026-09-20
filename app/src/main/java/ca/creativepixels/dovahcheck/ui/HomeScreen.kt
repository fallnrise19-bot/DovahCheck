package ca.creativepixels.dovahcheck.ui

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ca.creativepixels.dovahcheck.data.model.CharacterProfile
import ca.creativepixels.dovahcheck.data.model.QuestRecord
import ca.creativepixels.dovahcheck.data.model.QuestState
import ca.creativepixels.dovahcheck.data.model.ShoutRecord
import ca.creativepixels.dovahcheck.ui.theme.LedgerVisualTheme
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
    onOpenQuest: (release: String, section: String) -> Unit,
    onQuests: () -> Unit,
    onHolds: () -> Unit,
    onCollections: () -> Unit,
    onCharacters: () -> Unit
) {
    val eligible = quests.filterNot { it.isExcludedFromCompletion() }
    val completed = eligible.count { progress[it.internalKey] == QuestState.COMPLETED }
    val percentage = if (eligible.isEmpty()) 0 else (completed * 100 / eligible.size)
    val activeQuests = quests.filter {
        progress[it.internalKey] == QuestState.ACTIVE && !it.isExcludedFromCompletion()
    }.take(3)
    val theme = themeForSection("Base Game", "Main Quest")

    Scaffold(
        bottomBar = {
            LedgerBottomNav(
                selected = LedgerNavItem.HOME,
                onHome = {},
                onQuests = onQuests,
                onHolds = onHolds,
                onCollections = onCollections,
                onMore = onCharacters
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(Modifier.height(12.dp))
                LedgerHero(
                    title = "DovahCheck",
                    subtitle = "TRACK · EXPLORE · COMPLETE",
                    theme = theme,
                    progress = "Adventurer's Ledger"
                )
            }

            item {
                CharacterDashboardCard(
                    character = character,
                    percentage = percentage,
                    completed = completed,
                    total = eligible.size,
                    theme = theme,
                    onCharacters = onCharacters
                )
            }

            item {
                Text("At a glance", style = MaterialTheme.typography.titleLarge)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        label = "Quests",
                        value = "$completed/${eligible.size}",
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = "Holds",
                        value = categoryProgress("holds", quests, progress),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = "Factions",
                        value = categoryProgress("guilds", quests, progress),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        label = "DLC",
                        value = categoryProgress("dlc", quests, progress),
                        modifier = Modifier.weight(1f)
                    )
                    val totalWords = shouts.sumOf { it.words.size }
                    val learnedWords = shouts.sumOf { shout ->
                        shout.words.count { it.key in collectedShoutWordKeys }
                    }
                    StatCard(
                        label = "Shout words",
                        value = "$learnedWords/$totalWords",
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = "Active",
                        value = progress.values.count { it == QuestState.ACTIVE }.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Text("Continue journey", style = MaterialTheme.typography.titleLarge)
            }

            if (activeQuests.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCategorySelected("main-story") },
                        colors = CardDefaults.cardColors(containerColor = theme.surface),
                        border = BorderStroke(1.dp, theme.accentSoft)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                "No active quests",
                                style = MaterialTheme.typography.titleMedium,
                                color = theme.titleColor
                            )
                            Text(
                                "Open the Main Story or Quest Ledger when you're ready to choose your next problem.",
                                color = theme.bodyColor
                            )
                        }
                    }
                }
            } else {
                items(activeQuests, key = { it.internalKey }) { quest ->
                    ActiveQuestCard(
                        quest = quest,
                        theme = themeForSection(
                            quest.release ?: "Base Game",
                            quest.section ?: "Other"
                        ),
                        onClick = {
                            onOpenQuest(
                                quest.release ?: "Base Game",
                                quest.section ?: "Other"
                            )
                        }
                    )
                }
            }

            item {
                Text("Explore", style = MaterialTheme.typography.titleLarge)
                Text(
                    "The big sections from the original mockup now live here as quick entry points.",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DashboardShortcut(
                        title = "Main Story",
                        subtitle = categoryProgress("main-story", quests, progress),
                        theme = themeForCategory("main-story"),
                        modifier = Modifier.weight(1f),
                        onClick = { onCategorySelected("main-story") }
                    )
                    DashboardShortcut(
                        title = "Guilds",
                        subtitle = categoryProgress("guilds", quests, progress),
                        theme = themeForCategory("guilds"),
                        modifier = Modifier.weight(1f),
                        onClick = { onCategorySelected("guilds") }
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DashboardShortcut(
                        title = "Holds",
                        subtitle = categoryProgress("holds", quests, progress),
                        theme = themeForCategory("holds"),
                        modifier = Modifier.weight(1f),
                        onClick = onHolds
                    )
                    DashboardShortcut(
                        title = "Collections",
                        subtitle = "Shouts & more",
                        theme = themeForCategory("collections"),
                        modifier = Modifier.weight(1f),
                        onClick = onCollections
                    )
                }
            }

            item {
                TextButton(onClick = onQuests, modifier = Modifier.fillMaxWidth()) {
                    Text("Open full quest ledger")
                }
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun CharacterDashboardCard(
    character: CharacterProfile,
    percentage: Int,
    completed: Int,
    total: Int,
    theme: LedgerVisualTheme,
    onCharacters: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = theme.surface),
        border = BorderStroke(1.dp, theme.accentSoft)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier.size(84.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { percentage / 100f },
                    modifier = Modifier.fillMaxSize(),
                    color = theme.accent,
                    trackColor = theme.surfaceDeep
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "$percentage%",
                        style = MaterialTheme.typography.titleLarge,
                        color = theme.titleColor,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "$completed/$total",
                        style = MaterialTheme.typography.labelSmall,
                        color = theme.bodyColor
                    )
                }
            }

            Column(Modifier.weight(1f)) {
                Text(
                    character.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = theme.titleColor
                )
                Text(
                    character.contentProfileName,
                    style = MaterialTheme.typography.bodySmall,
                    color = theme.bodyColor
                )
                Text(
                    "Tracked completion",
                    style = MaterialTheme.typography.labelMedium,
                    color = theme.accent
                )
            }

            TextButton(onClick = onCharacters) {
                Icon(Icons.Outlined.People, contentDescription = null)
                Text("Switch")
            }
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ActiveQuestCard(
    quest: QuestRecord,
    theme: LedgerVisualTheme,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = theme.surface),
        border = BorderStroke(1.dp, theme.accentSoft)
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(
                "ACTIVE · ${quest.section ?: "Quest"}",
                style = MaterialTheme.typography.labelSmall,
                color = theme.accent
            )
            Text(
                quest.title,
                style = MaterialTheme.typography.titleMedium,
                color = theme.titleColor
            )
            quest.description?.takeIf { it.isNotBlank() }?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodySmall,
                    color = theme.bodyColor,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
private fun DashboardShortcut(
    title: String,
    subtitle: String,
    theme: LedgerVisualTheme,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(118.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = theme.surface),
        border = BorderStroke(1.dp, theme.accentSoft)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                theme.eyebrow,
                style = MaterialTheme.typography.labelSmall,
                color = theme.accent
            )
            Column {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    color = theme.titleColor
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = theme.bodyColor
                )
            }
        }
    }
}

private fun categoryProgress(
    categoryId: String,
    quests: List<QuestRecord>,
    progress: Map<String, QuestState>
): String {
    val category = BrowseTaxonomy.category(categoryId) ?: return "0/0"
    val eligible = category.questsForHub(quests).filterNot { it.isExcludedFromCompletion() }
    val completed = eligible.count { progress[it.internalKey] == QuestState.COMPLETED }
    return "$completed/${eligible.size}"
}
