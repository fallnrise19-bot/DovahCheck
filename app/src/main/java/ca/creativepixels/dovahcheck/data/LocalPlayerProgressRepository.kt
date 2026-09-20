package ca.creativepixels.dovahcheck.data

import android.content.Context
import ca.creativepixels.dovahcheck.data.model.CharacterProfile
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

    private companion object {
        const val KEY_STATE = "player_state_v1"
    }
}
