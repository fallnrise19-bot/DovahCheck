package ca.creativepixels.dovahcheck.ui

data class QuestTarget(
    val release: String,
    val section: String,
    val title: String = section,
    val subtitle: String? = null
)

data class QuestCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val targets: List<QuestTarget> = emptyList(),
    val releaseGroups: List<String> = emptyList(),
    val collectionsPlaceholder: Boolean = false
)

object BrowseTaxonomy {
    val categories = listOf(
        QuestCategory(
            id = "main-story",
            title = "Main Story",
            subtitle = "The Dragonborn's central questline",
            targets = listOf(
                QuestTarget("Base Game", "Main Quest", "Main Quest")
            )
        ),
        QuestCategory(
            id = "guilds",
            title = "Guilds & Factions",
            subtitle = "Companions, College, thieves, assassins and more",
            targets = listOf(
                QuestTarget("Base Game", "The Companions", "The Companions"),
                QuestTarget("Base Game", "College of Winterhold", "College of Winterhold"),
                QuestTarget("Base Game", "Thieves Guild", "Thieves Guild"),
                QuestTarget("Base Game", "The Dark Brotherhood", "Dark Brotherhood"),
                QuestTarget("Base Game", "Bard's College", "Bards College"),
                QuestTarget("Base Game", "Blades", "The Blades")
            )
        ),
        QuestCategory(
            id = "holds",
            title = "Hold Quests",
            subtitle = "Nine Holds, each kept in its own ledger",
            targets = listOf(
                QuestTarget("Base Game", "Whiterun"),
                QuestTarget("Base Game", "The Rift", "The Rift"),
                QuestTarget("Base Game", "The Reach", "The Reach"),
                QuestTarget("Base Game", "Haafingar"),
                QuestTarget("Base Game", "Eastmarch"),
                QuestTarget("Base Game", "Falkreath Hold", "Falkreath"),
                QuestTarget("Base Game", "Hjaalmarch"),
                QuestTarget("Base Game", "The Pale", "The Pale"),
                QuestTarget("Base Game", "Winterhold")
            )
        ),
        QuestCategory(
            id = "daedric",
            title = "Daedric Quests",
            subtitle = "Deals, artifacts and generally excellent judgment",
            targets = listOf(
                QuestTarget("Base Game", "Daedric Quests", "Daedric Quests")
            )
        ),
        QuestCategory(
            id = "civil-war",
            title = "Civil War",
            subtitle = "Imperial Legion and Stormcloak routes",
            targets = listOf(
                QuestTarget("Base Game", "Imperial Legion"),
                QuestTarget("Base Game", "Stormcloaks")
            )
        ),
        QuestCategory(
            id = "side-quests",
            title = "Side Quests",
            subtitle = "Dungeons, favors, bounties and other trouble",
            targets = listOf(
                QuestTarget("Base Game", "Dungeon Misc", "Dungeon Quests"),
                QuestTarget("Base Game", "Favors", "Favors & Miscellaneous"),
                QuestTarget("Base Game", "Bounty Quests", "Bounty Quests"),
                QuestTarget("Base Game", "Blackreach", "Blackreach"),
                QuestTarget("Base Game", "Tutorial", "Tutorials")
            )
        ),
        QuestCategory(
            id = "dlc",
            title = "DLC & Creations",
            subtitle = "Dawnguard, Dragonborn, Hearthfire and Creations",
            releaseGroups = listOf(
                "Dawnguard",
                "Dragonborn",
                "Hearthfire",
                "Free Creations (Special Edition)",
                "Creation Club / Anniversary"
            )
        ),
        QuestCategory(
            id = "collections",
            title = "Collections",
            subtitle = "Shouts, artifacts, masks, stones and more",
            collectionsPlaceholder = true
        )
    )

    fun category(id: String): QuestCategory? = categories.firstOrNull { it.id == id }
}
