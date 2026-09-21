package ca.creativepixels.dovahcheck.ui.theme

import androidx.compose.ui.graphics.Color

data class LedgerVisualTheme(
    val key: String,
    val eyebrow: String,
    val accent: Color,
    val accentSoft: Color,
    val surface: Color,
    val surfaceDeep: Color,
    val titleColor: Color = Color(0xFFF2EEE5),
    val bodyColor: Color = Color(0xFFD0CBC0),
    val assetKey: String? = null
)

private val Home = LedgerVisualTheme(
    key = "home",
    eyebrow = "DRAGONBORN",
    accent = Color(0xFFC4D1D8),
    accentSoft = Color(0xFF657782),
    surface = Color(0xFF252A2E),
    surfaceDeep = Color(0xFF111416),
    assetKey = "category_holds"
)

private val Nordic = LedgerVisualTheme(
    key = "nordic",
    eyebrow = "DRAGONBORN",
    accent = Color(0xFFC4D1D8),
    accentSoft = Color(0xFF657782),
    surface = Color(0xFF252A2E),
    surfaceDeep = Color(0xFF111416),
    assetKey = "theme_main_story"
)

private val Companions = LedgerVisualTheme(
    key = "companions",
    eyebrow = "JORRVASKR",
    accent = Color(0xFFC39A62),
    accentSoft = Color(0xFF74583A),
    surface = Color(0xFF2C241D),
    surfaceDeep = Color(0xFF17120E),
    assetKey = "faction_companions"
)

private val College = LedgerVisualTheme(
    key = "college",
    eyebrow = "COLLEGE OF WINTERHOLD",
    accent = Color(0xFFAECDE2),
    accentSoft = Color(0xFF4B6A80),
    surface = Color(0xFF18232C),
    surfaceDeep = Color(0xFF0A1117),
    assetKey = "faction_college_winterhold"
)

private val Thieves = LedgerVisualTheme(
    key = "thieves",
    eyebrow = "RAGGED FLAGON",
    accent = Color(0xFFB49A67),
    accentSoft = Color(0xFF66583B),
    surface = Color(0xFF24231E),
    surfaceDeep = Color(0xFF11110F),
    assetKey = "faction_thieves_guild"
)

private val Brotherhood = LedgerVisualTheme(
    key = "brotherhood",
    eyebrow = "WE KNOW",
    accent = Color(0xFFB85652),
    accentSoft = Color(0xFF6D2E2C),
    surface = Color(0xFF291B1B),
    surfaceDeep = Color(0xFF130A0A),
    assetKey = "faction_dark_brotherhood"
)

private val Bards = LedgerVisualTheme(
    key = "bards",
    eyebrow = "BARDS COLLEGE",
    accent = Color(0xFFD5B878),
    accentSoft = Color(0xFF755B31),
    surface = Color(0xFF30261F),
    surfaceDeep = Color(0xFF18110D),
    assetKey = "faction_bards_college"
)

private val Blades = LedgerVisualTheme(
    key = "blades",
    eyebrow = "THE BLADES",
    accent = Color(0xFFC88A78),
    accentSoft = Color(0xFF71473B),
    surface = Color(0xFF30201D),
    surfaceDeep = Color(0xFF150D0B),
    assetKey = "faction_blades"
)

private val Daedric = LedgerVisualTheme(
    key = "daedric",
    eyebrow = "OBLIVION",
    accent = Color(0xFFC06A8D),
    accentSoft = Color(0xFF6F3751),
    surface = Color(0xFF2A1B27),
    surfaceDeep = Color(0xFF120A10),
    assetKey = "theme_daedric"
)

private val Imperial = LedgerVisualTheme(
    key = "imperial",
    eyebrow = "IMPERIAL LEGION",
    accent = Color(0xFFD1A56B),
    accentSoft = Color(0xFF81403D),
    surface = Color(0xFF302221),
    surfaceDeep = Color(0xFF160D0C),
    assetKey = "civil_war_imperial"
)

private val Stormcloak = LedgerVisualTheme(
    key = "stormcloak",
    eyebrow = "STORMCLOAKS",
    accent = Color(0xFF98B6D3),
    accentSoft = Color(0xFF3F5F80),
    surface = Color(0xFF1D2731),
    surfaceDeep = Color(0xFF0C1116),
    assetKey = "civil_war_stormcloak"
)

private val Dawnguard = LedgerVisualTheme(
    key = "dawnguard",
    eyebrow = "DAWNGUARD",
    accent = Color(0xFFD29962),
    accentSoft = Color(0xFF7E5739),
    surface = Color(0xFF292622),
    surfaceDeep = Color(0xFF12100E),
    assetKey = "dlc_dawnguard"
)

private val Dragonborn = LedgerVisualTheme(
    key = "dragonborn_dlc",
    eyebrow = "SOLSTHEIM",
    accent = Color(0xFFA6B08B),
    accentSoft = Color(0xFF5A6248),
    surface = Color(0xFF25271F),
    surfaceDeep = Color(0xFF10110D),
    assetKey = "dlc_dragonborn"
)

private val DragonbornMain = LedgerVisualTheme(
    key = "dragonborn_main",
    eyebrow = "FIRST DRAGONBORN",
    accent = Color(0xFFB7B0D5),
    accentSoft = Color(0xFF625B7D),
    surface = Color(0xFF211F2A),
    surfaceDeep = Color(0xFF0B0A10),
    assetKey = "dlc_dragonborn_miraak"
)

private val Hearthfire = LedgerVisualTheme(
    key = "hearthfire",
    eyebrow = "HEARTHFIRE",
    accent = Color(0xFFD2AE76),
    accentSoft = Color(0xFF765B38),
    surface = Color(0xFF30271D),
    surfaceDeep = Color(0xFF171109),
    assetKey = "dlc_hearthfire"
)

private val Creations = LedgerVisualTheme(
    key = "creations",
    eyebrow = "CREATIONS",
    accent = Color(0xFFB6A7D8),
    accentSoft = Color(0xFF635779),
    surface = Color(0xFF25212D),
    surfaceDeep = Color(0xFF100D15),
    assetKey = "theme_creations"
)

private val SideQuest = LedgerVisualTheme(
    key = "side_quests",
    eyebrow = "ADVENTURER'S LEDGER",
    accent = Color(0xFFB9A57A),
    accentSoft = Color(0xFF6A5B3C),
    surface = Color(0xFF29271F),
    surfaceDeep = Color(0xFF11100D),
    assetKey = "theme_side_quests"
)


private val GuildsCategory = LedgerVisualTheme(
    key = "guilds_category",
    eyebrow = "GUILDS & FACTIONS",
    accent = Color(0xFFB9A57A),
    accentSoft = Color(0xFF6A5B3C),
    surface = Color(0xFF25231E),
    surfaceDeep = Color(0xFF11100D),
    assetKey = "guilds_category"
)

private val HoldsCategory = LedgerVisualTheme(
    key = "holds_category",
    eyebrow = "THE HOLDS",
    accent = Color(0xFFD0B878),
    accentSoft = Color(0xFF75613B),
    surface = Color(0xFF29251D),
    surfaceDeep = Color(0xFF11100C),
    assetKey = "category_holds"
)

private val Collections = LedgerVisualTheme(
    key = "collections",
    eyebrow = "COLLECTIONS",
    accent = Color(0xFFB8C9BD),
    accentSoft = Color(0xFF52665A),
    surface = Color(0xFF202923),
    surfaceDeep = Color(0xFF0D120F),
    assetKey = "category_collections"
)

private fun hold(
    key: String,
    eyebrow: String,
    accent: Long,
    soft: Long,
    surface: Long,
    deep: Long,
    assetKey: String
) = LedgerVisualTheme(
    key = key,
    eyebrow = eyebrow,
    accent = Color(accent),
    accentSoft = Color(soft),
    surface = Color(surface),
    surfaceDeep = Color(deep),
    assetKey = assetKey
)

private val HoldThemes = mapOf(
    "Whiterun" to hold("whiterun", "WHITERUN HOLD", 0xFFD0B878, 0xFF75613B, 0xFF302B20, 0xFF15120D, "hold_whiterun"),
    "The Rift" to hold("rift", "THE RIFT", 0xFFC28B63, 0xFF75503B, 0xFF30231D, 0xFF150E0A, "hold_riften"),
    "The Reach" to hold("reach", "THE REACH", 0xFFAAB0A6, 0xFF5D665C, 0xFF252925, 0xFF10120F, "hold_markarth"),
    "Haafingar" to hold("haafingar", "HAAFINGAR", 0xFFD1BF91, 0xFF756A4B, 0xFF2F2A22, 0xFF15120E, "category_holds"),
    "Eastmarch" to hold("eastmarch", "EASTMARCH", 0xFF9EBAC6, 0xFF4C6975, 0xFF202A2F, 0xFF0C1215, "hold_windhelm"),
    "Falkreath Hold" to hold("falkreath", "FALKREATH HOLD", 0xFF91AD83, 0xFF4F6746, 0xFF20281E, 0xFF0C110B, "hold_falkreath"),
    "Hjaalmarch" to hold("hjaalmarch", "HJAALMARCH", 0xFF9FA6A8, 0xFF555D60, 0xFF24282A, 0xFF0E1112, "hold_morthal"),
    "The Pale" to hold("pale", "THE PALE", 0xFFC7D2D8, 0xFF6B7B83, 0xFF232A2E, 0xFF0C1012, "hold_dawnstar"),
    "Winterhold" to hold("winterhold", "WINTERHOLD", 0xFFB3D1DE, 0xFF557486, 0xFF1E2930, 0xFF0A1115, "hold_winterhold")
)

fun themeForHome(): LedgerVisualTheme = Home

fun themeForCategory(categoryId: String): LedgerVisualTheme = when (categoryId) {
    "main-story" -> Nordic
    "guilds" -> GuildsCategory
    "holds" -> HoldsCategory
    "daedric" -> Daedric
    "civil-war" -> Imperial
    "side-quests" -> SideQuest
    "dlc" -> Dragonborn
    "collections" -> Collections
    else -> SideQuest
}

fun themeForRelease(release: String): LedgerVisualTheme = when (release) {
    "Dawnguard" -> Dawnguard
    "Dragonborn" -> Dragonborn
    "Hearthfire" -> Hearthfire
    "Free Creations (Special Edition)",
    "Creation Club / Anniversary" -> Creations
    else -> Nordic
}

fun themeForSection(release: String, section: String): LedgerVisualTheme {
    HoldThemes[section]?.let { return it }

    if (release == "Dragonborn" && section == "Main Quest") {
        return DragonbornMain
    }

    if (release != "Base Game") {
        return themeForRelease(release)
    }

    return when (section) {
        "Main Quest" -> Nordic
        "The Companions" -> Companions
        "College of Winterhold" -> College
        "Thieves Guild" -> Thieves
        "The Dark Brotherhood" -> Brotherhood
        "Bard's College" -> Bards
        "Blades" -> Blades
        "Daedric Quests" -> Daedric
        "Imperial Legion" -> Imperial
        "Stormcloaks" -> Stormcloak
        else -> SideQuest
    }
}
