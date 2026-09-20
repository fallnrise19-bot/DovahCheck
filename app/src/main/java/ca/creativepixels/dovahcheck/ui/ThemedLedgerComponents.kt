package ca.creativepixels.dovahcheck.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ca.creativepixels.dovahcheck.ui.theme.LedgerVisualTheme

private val LedgerShape = RoundedCornerShape(16.dp)

@Composable
fun LedgerHero(
    title: String,
    subtitle: String?,
    theme: LedgerVisualTheme,
    progress: String? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(LedgerShape)
            .background(
                Brush.horizontalGradient(
                    listOf(theme.surfaceDeep, theme.surface, theme.surfaceDeep)
                )
            )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = theme.eyebrow,
                style = MaterialTheme.typography.labelMedium,
                color = theme.accent,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = theme.titleColor,
                fontWeight = FontWeight.SemiBold
            )
            subtitle?.takeIf { it.isNotBlank() }?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = theme.bodyColor
                )
            }
            progress?.takeIf { it.isNotBlank() }?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelLarge,
                    color = theme.accent
                )
            }
        }
    }
}

@Composable
fun LedgerBrowseCard(
    title: String,
    subtitle: String?,
    progress: String?,
    theme: LedgerVisualTheme,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = LedgerShape,
        colors = CardDefaults.cardColors(containerColor = theme.surface),
        border = BorderStroke(1.dp, theme.accentSoft.copy(alpha = 0.8f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            theme.accent.copy(alpha = 0.14f),
                            Color.Transparent,
                            Color.Transparent
                        )
                    )
                )
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(theme.accent)
                    .padding(horizontal = 2.dp, vertical = 22.dp)
            )
            Column(Modifier.weight(1f)) {
                Text(
                    text = theme.eyebrow,
                    style = MaterialTheme.typography.labelSmall,
                    color = theme.accent
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = theme.titleColor
                )
                subtitle?.takeIf { it.isNotBlank() }?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = theme.bodyColor
                    )
                }
                progress?.takeIf { it.isNotBlank() }?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelLarge,
                        color = theme.accent
                    )
                }
            }
            Icon(
                Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = theme.accent
            )
        }
    }
}
