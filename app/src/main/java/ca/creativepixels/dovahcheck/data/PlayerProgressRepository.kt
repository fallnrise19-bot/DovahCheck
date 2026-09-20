package ca.creativepixels.dovahcheck.data

import ca.creativepixels.dovahcheck.data.model.CharacterProfile
import ca.creativepixels.dovahcheck.data.model.CollectionProgress
import ca.creativepixels.dovahcheck.data.model.QuestProgress
import ca.creativepixels.dovahcheck.data.model.QuestState

interface PlayerProgressRepository {
    suspend fun characters(): List<CharacterProfile>
    suspend fun activeCharacter(): CharacterProfile?
    suspend fun createCharacter(name: String, contentProfileName: String): CharacterProfile
    suspend fun selectCharacter(characterId: String)
    suspend fun updateCharacterPortrait(characterId: String, portraitPath: String?): CharacterProfile?
    suspend fun questProgress(characterId: String): List<QuestProgress>
    suspend fun setQuestState(characterId: String, questKey: String, state: QuestState)
    suspend fun collectionProgress(characterId: String, collectionKey: String): List<CollectionProgress>
    suspend fun setCollectionItemCollected(
        characterId: String,
        collectionKey: String,
        itemKey: String,
        collected: Boolean
    )
}
