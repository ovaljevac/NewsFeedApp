package etf.ri.rma.newsfeedapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = NewsPrimaryDark,
    secondary = NewsAccent,
    tertiary = NewsPrimaryDark,
    background = DarkCanvas,
    surface = DarkSurface,
    onPrimary = NewsInk,
    onSecondary = NewsSurface,
    onBackground = NewsSurface,
    onSurface = NewsSurface,
    outline = NewsOutline
)

private val LightColorScheme = lightColorScheme(
    primary = NewsPrimary,
    secondary = NewsAccent,
    tertiary = NewsMuted,
    background = NewsCanvas,
    surface = NewsSurface,
    onPrimary = NewsSurface,
    onSecondary = NewsSurface,
    onBackground = NewsInk,
    onSurface = NewsInk,
    outline = NewsOutline
)

@Composable
fun NewsFeedAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
