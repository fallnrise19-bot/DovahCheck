package ca.creativepixels.dovahcheck.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ca.creativepixels.dovahcheck.ui.theme.LedgerVisualTheme

private val LedgerShape = RoundedCornerShape(16.dp)

@Composable
private fun artworkResource(assetKey: String?): Int {
    if (assetKey.isNullOrBlank()) return 0
    val context = LocalContext.current
    return context.resources.getIdentifier(assetKey, "drawable", context.packageName)
}

@Composable
fun LedgerHero(
    title: String,
    subtitle: String?,
    theme: LedgerVisualTheme,
    progress: String? = null,
    modifier: Modifier = Modifier
) {
    val artwork = artworkResource(theme.assetKey)
    val heroModifier = modifier
        .fillMaxWidth()
        .clip(LedgerShape)
        .then(if (artwork != 0) Modifier.height(190.dp) else Modifier)

    Box(
        modifier = heroModifier
            .background(
                Brush.horizontalGradient(
                    listOf(theme.surfaceDeep, theme.surface, theme.surfaceDeep)
                )
            )
    ) {
        if (artwork != 0) {
            Image(
                painter = painterResource(artwork),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Black.copy(alpha = 0.14f),
                                Color.Black.copy(alpha = 0.42f),
                                Color.Black.copy(alpha = 0.88f)
                            )
                        )
                    )
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 18.dp, vertical = 18.dp),
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
    val artwork = artworkResource(theme.assetKey)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = LedgerShape,
        colors = CardDefaults.cardColors(containerColor = theme.surface),
        border = BorderStroke(1.dp, theme.accentSoft.copy(alpha = 0.8f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (artwork != 0) 132.dp else 104.dp)
        ) {
            if (artwork != 0) {
                Image(
                    painter = painterResource(artwork),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0.82f),
                                    Color.Black.copy(alpha = 0.58f),
                                    Color.Black.copy(alpha = 0.18f)
                                )
                            )
                        )
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    theme.accent.copy(alpha = 0.14f),
                                    Color.Transparent,
                                    Color.Transparent
                                )
                            )
                        )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
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
                        color = theme.accent,
                        fontWeight = FontWeight.Bold
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
                            color = theme.bodyColor,
                            maxLines = 2
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
}

@Composable
fun LedgerArtworkTile(
    title: String,
    subtitle: String,
    theme: LedgerVisualTheme,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val artwork = artworkResource(theme.assetKey)

    Card(
        modifier = modifier
            .height(128.dp)
            .clickable(onClick = onClick),
        shape = LedgerShape,
        colors = CardDefaults.cardColors(containerColor = theme.surface),
        border = BorderStroke(1.dp, theme.accentSoft)
    ) {
        Box(Modifier.fillMaxSize()) {
            if (artwork != 0) {
                Image(
                    painter = painterResource(artwork),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0.16f),
                                    Color.Black.copy(alpha = 0.44f),
                                    Color.Black.copy(alpha = 0.86f)
                                )
                            )
                        )
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    theme.eyebrow,
                    style = MaterialTheme.typography.labelSmall,
                    color = theme.accent,
                    fontWeight = FontWeight.Bold
                )
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
