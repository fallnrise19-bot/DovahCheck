package ca.creativepixels.dovahcheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ca.creativepixels.dovahcheck.ui.DovahCheckApp
import ca.creativepixels.dovahcheck.ui.theme.DovahCheckTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DovahCheckTheme {
                DovahCheckApp()
            }
        }
    }
}
