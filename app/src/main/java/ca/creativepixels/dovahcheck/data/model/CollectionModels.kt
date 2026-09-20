package ca.creativepixels.dovahcheck.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ShoutCatalogFile(
    val schemaVersion: Int,
    val source: String,
    val shoutCount: Int,
    val wordCount: Int,
    val shouts: List<ShoutRecord>
)

@Serializable
data class ShoutRecord(
    val key: String,
    val release: String,
    val name: String,
    val acquisitionSummary: String = "",
    val source: String? = null,
    val words: List<ShoutWord>
)

@Serializable
data class ShoutWord(
    val key: String,
    val word: String,
    val translation: String
)

@Serializable
data class CollectionProgress(
    val characterId: String,
    val collectionKey: String,
    val itemKey: String
)
