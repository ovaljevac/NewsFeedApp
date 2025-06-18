package etf.ri.rma.newsfeedapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import etf.ri.rma.newsfeedapp.ui.theme.NewsFeedAppTheme
import etf.ri.rma.newsfeedapp.navigation.NewsFeedAppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsFeedAppTheme {
                Surface(
                    modifier = Modifier.
                        padding(WindowInsets.statusBars.asPaddingValues()),
                    color = Color(0xFFD2B48C)
                ) {
                    NewsFeedAppNavHost()
                }
            }
        }
    }
}

