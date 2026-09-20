package ca.creativepixels.dovahcheck.data.model

import kotlinx.serialization.Serializable

@Serializable
data class QuestCatalogFile(
    val schemaVersion: Int,
    val source: String,
    val recordCount: Int,
    val records: List<QuestRecord>
)

@Serializable
data class QuestRecord(
    val internalKey: String,
    val sourceRow: String? = null,
    val release: String? = null,
    val profile: String? = null,
    val section: String? = null,
    val subsection: String? = null,
    val title: String,
    val sourceTitle: String? = null,
    val contentClass: String? = null,
    val repeatable: String? = null,
    val branchGroup: String? = null,
    val completionRule: String? = null,
    val description: String? = null,
    val giver: String? = null,
    val verificationStatus: String? = null,
    val notes: String? = null,
    val questId: String? = null,
    val requiredIterations: String? = null,
    val routeContext: String? = null,
    val formId: String? = null
)

@Serializable
data class ContentProfileFile(
    val schemaVersion: Int,
    val profiles: List<ContentProfile>
)

@Serializable
data class ContentProfile(
    val name: String,
    val baseGame: String,
    val dawnguard: String,
    val hearthfire: String,
    val dragonborn: String,
    val freeCreationsSE: String,
    val anniversaryCreations: String,
    val recommendedUse: String? = null
)

@Serializable
data class CharacterProfile(
    val id: String,
    val name: String,
    val contentProfileName: String
)

@Serializable
data class QuestProgress(
    val characterId: String,
    val questKey: String,
    val state: QuestState = QuestState.NOT_FOUND,
    val completedIterations: Int = 0
)

@Serializable
enum class QuestState {
    NOT_FOUND,
    DISCOVERED,
    ACTIVE,
    COMPLETED;

    fun next(): QuestState = when (this) {
        NOT_FOUND -> DISCOVERED
        DISCOVERED -> ACTIVE
        ACTIVE -> COMPLETED
        COMPLETED -> NOT_FOUND
    }

    fun label(): String = when (this) {
        NOT_FOUND -> "Not Started"
        DISCOVERED -> "Discovered"
        ACTIVE -> "Active"
        COMPLETED -> "Completed"
    }
}

@Serializable
data class PlayerStateStore(
    val activeCharacterId: String? = null,
    val characters: List<CharacterProfile> = emptyList(),
    val progress: List<QuestProgress> = emptyList(),
    val collectionProgress: List<CollectionProgress> = emptyList()
)
