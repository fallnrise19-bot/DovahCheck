package ca.creativepixels.dovahcheck.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import ca.creativepixels.dovahcheck.data.LocalPlayerProgressRepository
import ca.creativepixels.dovahcheck.data.QuestCatalogRepository
import ca.creativepixels.dovahcheck.data.model.CharacterProfile
import ca.creativepixels.dovahcheck.data.model.ContentProfile
import ca.creativepixels.dovahcheck.data.model.QuestRecord
import ca.creativepixels.dovahcheck.data.model.QuestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private sealed interface AppScreen {
    data object Home : AppScreen
    data object Characters : AppScreen
    data object CreateCharacter : AppScreen
    data class Category(val id: String) : AppScreen
    data class Release(val release: String) : AppScreen
    data class Section(
        val release: String,
        val section: String,
        val parentCategoryId: String? = null,
        val parentRelease: String? = null
    ) : AppScreen
}

@Composable
fun DovahCheckApp() {
    val context = LocalContext.current
    val catalogRepository = remember(context) { QuestCatalogRepository(context) }
    val playerRepository = remember(context) { LocalPlayerProgressRepository(context) }
    val scope = rememberCoroutineScope()

    var quests by remember { mutableStateOf<List<QuestRecord>?>(null) }
    var contentProfiles by remember { mutableStateOf<List<ContentProfile>>(emptyList()) }
    var characters by remember { mutableStateOf<List<CharacterProfile>>(emptyList()) }
    var activeCharacter by remember { mutableStateOf<CharacterProfile?>(null) }
    var progress by remember { mutableStateOf<Map<String, QuestState>>(emptyMap()) }
    var screen by remember { mutableStateOf<AppScreen?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    suspend fun refreshPlayerState(character: CharacterProfile? = null) {
        val resolvedCharacter = character ?: playerRepository.activeCharacter()
        characters = playerRepository.characters()
        activeCharacter = resolvedCharacter
        progress = if (resolvedCharacter == null) {
            emptyMap()
        } else {
            playerRepository.questProgress(resolvedCharacter.id).associate { it.questKey to it.state }
        }
    }

    LaunchedEffect(catalogRepository, playerRepository) {
        runCatching {
            val catalog = withContext(Dispatchers.IO) { catalogRepository.loadQuestCatalog() }
            val profiles = withContext(Dispatchers.IO) { catalogRepository.loadContentProfiles() }
            quests = catalog.records
            contentProfiles = profiles.profiles
            refreshPlayerState()
        }.onSuccess {
            screen = if (activeCharacter == null) AppScreen.CreateCharacter else AppScreen.Home
        }.onFailure {
            error = it.message ?: "Unable to load DovahCheck."
        }
    }

    val filteredQuests = quests.orEmpty().filterForProfile(activeCharacter?.contentProfileName)

    when {
        error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(error!!, color = MaterialTheme.colorScheme.error)
        }

        quests == null || screen == null -> Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        screen == AppScreen.CreateCharacter -> CharacterSetupScreen(
            profiles = contentProfiles,
            onCreate = { name, profile ->
                scope.launch {
                    val character = playerRepository.createCharacter(name, profile.name)
                    refreshPlayerState(character)
                    screen = AppScreen.Home
                }
            },
            onBack = if (characters.isEmpty()) null else ({ screen = AppScreen.Characters })
        )

        screen == AppScreen.Characters -> CharacterPickerScreen(
            characters = characters,
            activeCharacterId = activeCharacter?.id,
            onSelect = { character ->
                scope.launch {
                    playerRepository.selectCharacter(character.id)
                    refreshPlayerState(character)
                    screen = AppScreen.Home
                }
            },
            onCreateNew = { screen = AppScreen.CreateCharacter },
            onBack = { screen = AppScreen.Home }
        )

        screen is AppScreen.Category -> {
            val categoryScreen = screen as AppScreen.Category
            val category = BrowseTaxonomy.category(categoryScreen.id)

            if (category == null) {
                screen = AppScreen.Home
            } else {
                CategoryBrowseScreen(
                    category = category,
                    quests = filteredQuests,
                    progress = progress,
                    onBack = { screen = AppScreen.Home },
                    onSectionSelected = { release, section ->
                        screen = AppScreen.Section(
                            release = release,
                            section = section,
                            parentCategoryId = category.id
                        )
                    },
                    onReleaseSelected = { release ->
                        screen = AppScreen.Release(release)
                    }
                )
            }
        }

        screen is AppScreen.Release -> {
            val releaseScreen = screen as AppScreen.Release
            ReleaseBrowseScreen(
                release = releaseScreen.release,
                quests = filteredQuests,
                progress = progress,
                onBack = { screen = AppScreen.Category("dlc") },
                onSectionSelected = { release, section ->
                    screen = AppScreen.Section(
                        release = release,
                        section = section,
                        parentCategoryId = "dlc",
                        parentRelease = release
                    )
                }
            )
        }

        screen is AppScreen.Section -> {
            val sectionScreen = screen as AppScreen.Section
            val visibleQuests = filteredQuests
                .filter {
                    (it.release ?: "Base Game") == sectionScreen.release &&
                        (it.section ?: "Other") == sectionScreen.section
                }
                .filterNot { it.isExcludedFromCompletion() }

            QuestSectionScreen(
                release = sectionScreen.release,
                section = sectionScreen.section,
                quests = visibleQuests,
                progress = progress,
                onBack = {
                    screen = when {
                        sectionScreen.parentRelease != null ->
                            AppScreen.Release(sectionScreen.parentRelease)
                        sectionScreen.parentCategoryId != null ->
                            AppScreen.Category(sectionScreen.parentCategoryId)
                        else -> AppScreen.Home
                    }
                },
                onStateChange = { quest, state ->
                    val character = activeCharacter
                    if (character != null) {
                        scope.launch {
                            playerRepository.setQuestState(character.id, quest.internalKey, state)
                            progress = progress.toMutableMap().apply {
                                if (state == QuestState.NOT_FOUND) remove(quest.internalKey)
                                else put(quest.internalKey, state)
                            }
                        }
                    }
                }
            )
        }

        else -> {
            val character = activeCharacter
            if (character == null) {
                CharacterSetupScreen(
                    profiles = contentProfiles,
                    onCreate = { name, profile ->
                        scope.launch {
                            val created = playerRepository.createCharacter(name, profile.name)
                            refreshPlayerState(created)
                            screen = AppScreen.Home
                        }
                    }
                )
            } else {
                HomeScreen(
                    character = character,
                    quests = filteredQuests,
                    progress = progress,
                    onCategorySelected = { categoryId ->
                        val category = BrowseTaxonomy.category(categoryId)
                        val onlyTarget = category?.targets?.singleOrNull()

                        if (onlyTarget != null && category.releaseGroups.isEmpty()) {
                            screen = AppScreen.Section(
                                release = onlyTarget.release,
                                section = onlyTarget.section,
                                parentCategoryId = category.id
                            )
                        } else {
                            screen = AppScreen.Category(categoryId)
                        }
                    },
                    onCharacters = { screen = AppScreen.Characters }
                )
            }
        }
    }
}

private fun List<QuestRecord>.filterForProfile(profileName: String?): List<QuestRecord> {
    val allowed = when (profileName) {
        "Original Skyrim (2011)" -> setOf("Base Game")
        "Legendary Edition" -> setOf("Base Game", "Dawnguard", "Hearthfire", "Dragonborn")
        "Special Edition (current)" -> setOf(
            "Base Game",
            "Dawnguard",
            "Hearthfire",
            "Dragonborn",
            "Free Creations (Special Edition)"
        )
        "Anniversary Edition / Upgrade" -> null
        else -> null
    }
    return if (allowed == null) this else filter { (it.release ?: "Base Game") in allowed }
}
