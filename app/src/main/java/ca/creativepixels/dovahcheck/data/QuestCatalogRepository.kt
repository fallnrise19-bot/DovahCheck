package ca.creativepixels.dovahcheck.data

import android.content.Context
import ca.creativepixels.dovahcheck.data.model.ContentProfileFile
import ca.creativepixels.dovahcheck.data.model.QuestCatalogFile
import kotlinx.serialization.json.Json

class QuestCatalogRepository(
    private val context: Context,
    private val json: Json = Json { ignoreUnknownKeys = true }
) {
    fun loadQuestCatalog(): QuestCatalogFile =
        context.assets.open("data/quests.v1.json")
            .bufferedReader()
            .use { json.decodeFromString<QuestCatalogFile>(it.readText()) }

    fun loadContentProfiles(): ContentProfileFile =
        context.assets.open("data/content_profiles.v1.json")
            .bufferedReader()
            .use { json.decodeFromString<ContentProfileFile>(it.readText()) }
}
