package ca.creativepixels.dovahcheck.data

import android.content.Context
import ca.creativepixels.dovahcheck.data.model.CharacterProfile
import ca.creativepixels.dovahcheck.data.model.CollectionProgress
import ca.creativepixels.dovahcheck.data.model.PlayerStateStore
import ca.creativepixels.dovahcheck.data.model.QuestProgress
import ca.creativepixels.dovahcheck.data.model.QuestState
import java.util.UUID
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LocalPlayerProgressRepository(
    context: Context,
    private val json: Json = Json { ignoreUnknownKeys = true }
) : PlayerProgressRepository {

    private val preferences =
        context.getSharedPreferences("dovahcheck_player_state", Context.MODE_PRIVATE)

    @Synchronized
    private fun load(): PlayerStateStore {
        val raw = preferences.getString(KEY_STATE, null) ?: return PlayerStateStore()
        return runCatching { json.decodeFromString<PlayerStateStore>(raw) }
            .getOrDefault(PlayerStateStore())
    }

    @Synchronized
    private fun save(state: PlayerStateStore) {
        preferences.edit()
            .putString(KEY_STATE, json.encodeToString(state))
            .apply()
    }

    override suspend fun characters(): List<CharacterProfile> = load().characters

    override suspend fun activeCharacter(): CharacterProfile? {
        val state = load()
        return state.characters.firstOrNull { it.id == state.activeCharacterId }
    }

    override suspend fun createCharacter(
        name: String,
        contentProfileName: String
    ): CharacterProfile {
        val character = CharacterProfile(
            id = UUID.randomUUID().toString(),
            name = name.trim(),
            contentProfileName = contentProfileName
        )
        val current = load()
        save(
            current.copy(
                activeCharacterId = character.id,
                characters = current.characters + character
            )
        )
        return character
    }

    override suspend fun selectCharacter(characterId: String) {
        val current = load()
        if (current.characters.none { it.id == characterId }) return
        save(current.copy(activeCharacterId = characterId))
    }

    override suspend fun updateCharacterPortrait(
        characterId: String,
        portraitPath: String?
    ): CharacterProfile? {
        val current = load()
        val existing = current.characters.firstOrNull { it.id == characterId } ?: return null
        val updatedCharacter = existing.copy(portraitPath = portraitPath)
        save(
            current.copy(
                characters = current.characters.map {
                    if (it.id == characterId) updatedCharacter else it
                }
            )
        )
        return updatedCharacter
    }

    override suspend fun questProgress(characterId: String): List<QuestProgress> =
        load().progress.filter { it.characterId == characterId }

    override suspend fun setQuestState(
        characterId: String,
        questKey: String,
        state: QuestState
    ) {
        val current = load()
        val remaining = current.progress.filterNot {
            it.characterId == characterId && it.questKey == questKey
        }
        val updated = if (state == QuestState.NOT_FOUND) {
            remaining
        } else {
            remaining + QuestProgress(
                characterId = characterId,
                questKey = questKey,
                state = state
            )
        }
        save(current.copy(progress = updated))
    }

    override suspend fun collectionProgress(
        characterId: String,
        collectionKey: String
    ): List<CollectionProgress> =
        load().collectionProgress.filter {
            it.characterId == characterId && it.collectionKey == collectionKey
        }

    override suspend fun setCollectionItemCollected(
        characterId: String,
        collectionKey: String,
        itemKey: String,
        collected: Boolean
    ) {
        val current = load()
        val remaining = current.collectionProgress.filterNot {
            it.characterId == characterId &&
                it.collectionKey == collectionKey &&
                it.itemKey == itemKey
        }
        val updated = if (collected) {
            remaining + CollectionProgress(
                characterId = characterId,
                collectionKey = collectionKey,
                itemKey = itemKey
            )
        } else {
            remaining
        }
        save(current.copy(collectionProgress = updated))
    }

    private companion object {
        const val KEY_STATE = "player_state_v1"
    }
}
