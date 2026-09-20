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

data class CharacterProfile(
    val id: String,
    val name: String,
    val contentProfileName: String
)

data class QuestProgress(
    val characterId: String,
    val questKey: String,
    val state: QuestState = QuestState.NOT_FOUND,
    val completedIterations: Int = 0
)

enum class QuestState {
    NOT_FOUND,
    DISCOVERED,
    ACTIVE,
    COMPLETED
}
