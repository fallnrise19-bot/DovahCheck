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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import ca.creativepixels.dovahcheck.data.QuestCatalogRepository
import ca.creativepixels.dovahcheck.data.model.QuestRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun DovahCheckApp() {
    val context = LocalContext.current
    val repository = remember(context) { QuestCatalogRepository(context) }

    var quests by remember { mutableStateOf<List<QuestRecord>?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(repository) {
        runCatching {
            withContext(Dispatchers.IO) { repository.loadQuestCatalog().records }
        }.onSuccess {
            quests = it
        }.onFailure {
            error = it.message ?: "Unable to load quest catalog."
        }
    }

    when {
        error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(error!!, color = MaterialTheme.colorScheme.error)
        }
        quests == null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        else -> HomeScreen(quests.orEmpty())
    }
}
