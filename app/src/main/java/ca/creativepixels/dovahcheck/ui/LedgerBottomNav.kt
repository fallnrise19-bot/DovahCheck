package ca.creativepixels.dovahcheck.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

enum class LedgerNavItem {
    HOME,
    QUESTS,
    HOLDS,
    COLLECTIONS,
    MORE
}

@Composable
fun LedgerBottomNav(
    selected: LedgerNavItem,
    onHome: () -> Unit,
    onQuests: () -> Unit,
    onHolds: () -> Unit,
    onCollections: () -> Unit,
    onMore: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selected == LedgerNavItem.HOME,
            onClick = onHome,
            icon = { Icon(Icons.Outlined.Home, contentDescription = null) },
            label = { NavLabel("Home") }
        )
        NavigationBarItem(
            selected = selected == LedgerNavItem.QUESTS,
            onClick = onQuests,
            icon = { Icon(Icons.Outlined.ListAlt, contentDescription = null) },
            label = { NavLabel("Quests") }
        )
        NavigationBarItem(
            selected = selected == LedgerNavItem.HOLDS,
            onClick = onHolds,
            icon = { Icon(Icons.Outlined.Map, contentDescription = null) },
            label = { NavLabel("Holds") }
        )
        NavigationBarItem(
            selected = selected == LedgerNavItem.COLLECTIONS,
            onClick = onCollections,
            icon = { Icon(Icons.Outlined.CollectionsBookmark, contentDescription = null) },
            label = { NavLabel("Collections") }
        )
        NavigationBarItem(
            selected = selected == LedgerNavItem.MORE,
            onClick = onMore,
            icon = { Icon(Icons.Outlined.MoreHoriz, contentDescription = null) },
            label = { NavLabel("More") }
        )
    }
}

@Composable
private fun NavLabel(text: String) {
    Text(
        text = text,
        maxLines = 1,
        softWrap = false,
        fontSize = 9.sp
    )
}
