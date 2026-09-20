package ca.creativepixels.dovahcheck.data

import android.content.Context
import android.net.Uri
import java.io.File

object CharacterPortraitStore {
    fun savePortrait(
        context: Context,
        characterId: String,
        sourceUri: Uri
    ): String? {
        return runCatching {
            val directory = File(context.filesDir, "character_portraits").apply { mkdirs() }
            val destination = File(directory, "$characterId.jpg")

            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                destination.outputStream().use { output ->
                    input.copyTo(output)
                }
            } ?: return null

            destination.absolutePath
        }.getOrNull()
    }

    fun deletePortrait(path: String?) {
        if (path.isNullOrBlank()) return
        runCatching { File(path).delete() }
    }
}
