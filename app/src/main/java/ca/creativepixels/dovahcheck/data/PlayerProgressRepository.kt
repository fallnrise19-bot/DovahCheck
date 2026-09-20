package ca.creativepixels.dovahcheck.data

import ca.creativepixels.dovahcheck.data.model.CharacterProfile
import ca.creativepixels.dovahcheck.data.model.QuestProgress

/**
 * Character progress is intentionally separate from the master quest catalog.
 * This prevents one character's completion state from mutating shared Skyrim data.
 *
 * Persistence will be added behind this interface in the next implementation pass.
 */
interface PlayerProgressRepository {
    suspend fun characters(): List<CharacterProfile>
    suspend fun questProgress(characterId: String): List<QuestProgress>
    suspend fun saveQuestProgress(progress: QuestProgress)
}
