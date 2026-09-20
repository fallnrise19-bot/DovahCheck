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
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.creativepixels.dovahcheck.data.model.QuestRecord
import ca.creativepixels.dovahcheck.data.model.QuestState
import ca.creativepixels.dovahcheck.ui.theme.LedgerVisualTheme
import ca.creativepixels.dovahcheck.ui.theme.themeForSection

@Composable
fun QuestSectionScreen(
    release: String,
    section: String,
    quests: List<QuestRecord>,
    progress: Map<String, QuestState>,
    onBack: () -> Unit,
    onStateChange: (QuestRecord, QuestState) -> Unit
) {
    val repeatableQuests = quests.filter { it.isRadiantOrRepeatable() }
    val coreQuests = quests.filterNot { it.isRadiantOrRepeatable() }
    var repeatablesExpanded by remember(section, release) { mutableStateOf(false) }
    val theme = themeForSection(release, section)
    val completed = quests.count { progress[it.internalKey] == QuestState.COMPLETED }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(section) },
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
                    title = section,
                    subtitle = if (release == "Base Game") null else release,
                    theme = theme,
                    progress = "$completed / ${quests.size} completed"
                )
            }

            if (coreQuests.isNotEmpty()) {
                item {
                    Text(
                        "Quests",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                items(coreQuests, key = { it.internalKey }) { quest ->
                    QuestCard(
                        quest = quest,
                        state = progress[quest.internalKey] ?: QuestState.NOT_FOUND,
                        theme = theme,
                        onStateChange = onStateChange
                    )
                }
            }

            if (repeatableQuests.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(4.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { repeatablesExpanded = !repeatablesExpanded },
                        colors = CardDefaults.cardColors(containerColor = theme.surface),
                        border = BorderStroke(1.dp, theme.accentSoft)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(
                                    "Radiant & Repeatable",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = theme.titleColor
                                )
                                Text(
                                    "${repeatableQuests.size} quest templates. Complete each required template once unless a finite count is shown.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = theme.bodyColor
                                )
                            }
                            Icon(
                                if (repeatablesExpanded) Icons.Outlined.ExpandLess
                                else Icons.Outlined.ExpandMore,
                                contentDescription = if (repeatablesExpanded) "Collapse" else "Expand",
                                tint = theme.accent
                            )
                        }
                    }
                }

                if (repeatablesExpanded) {
                    items(repeatableQuests, key = { "repeatable-" + it.internalKey }) { quest ->
                        QuestCard(
                            quest = quest,
                            state = progress[quest.internalKey] ?: QuestState.NOT_FOUND,
                            theme = theme,
                            onStateChange = onStateChange
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(20.dp)) }
        }
    }
}

@Composable
private fun QuestCard(
    quest: QuestRecord,
    state: QuestState,
    theme: LedgerVisualTheme,
    onStateChange: (QuestRecord, QuestState) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = theme.surface.copy(alpha = 0.92f)),
        border = BorderStroke(
            1.dp,
            if (state == QuestState.COMPLETED) theme.accent else theme.accentSoft.copy(alpha = 0.55f)
        )
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    quest.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = theme.titleColor,
                    modifier = Modifier.weight(1f)
                )
                TextButton(onClick = { onStateChange(quest, state.next()) }) {
                    Text(
                        state.label(),
                        color = when (state) {
                            QuestState.NOT_FOUND -> theme.bodyColor
                            QuestState.DISCOVERED -> theme.accentSoft
                            QuestState.ACTIVE -> theme.accent
                            QuestState.COMPLETED -> theme.accent
                        }
                    )
                }
            }

            quest.description
                ?.takeIf { it.isNotBlank() }
                ?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodySmall,
                        color = theme.bodyColor
                    )
                }

            quest.routeContext
                ?.takeIf { it.isNotBlank() }
                ?.let {
                    Text(
                        it,
                        color = theme.accent,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

            if (quest.repeatable == "Finite" && !quest.requiredIterations.isNullOrBlank()) {
                Text(
                    "Required completions: " + quest.requiredIterations,
                    style = MaterialTheme.typography.labelSmall,
                    color = theme.accent
                )
            }
        }
    }
}

private fun QuestRecord.isRadiantOrRepeatable(): Boolean {
    val text = listOfNotNull(subsection, contentClass, completionRule)
        .joinToString(" ")
        .lowercase()

    return repeatable == "Yes" ||
        repeatable == "Finite" ||
        "radiant" in text ||
        "repeatable" in text ||
        "bounty" in text
}
