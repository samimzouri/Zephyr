package com.example.zephyr.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

// Importa los colores definidos en el archivo `color.kt`
import com.example.zephyr.ui.theme.*

// Cambiaremos el LightColorScheme para incluir estos colores
private val DarkColorScheme = darkColorScheme(
    primary = PastelOrangeDark,  // Ajusta el tono si deseas un tema oscuro
    secondary = PastelOrangeMain,
    tertiary = PastelOrangeLight
)

private val LightColorScheme = lightColorScheme(
    primary = PastelOrangeMain,         // Naranja pastel como color principal
    secondary = PastelOrangeDark,   // Naranja oscuro para contraste
    tertiary = PastelOrangeLight,   // Naranja más claro para detalles
    background = BackgroundLight,   // Fondo claro
    surface = Color.White,          // Fondo blanco para superficies
    onPrimary = Color.White,        // Texto blanco sobre el color primario
    onSecondary = Color.White,      // Texto blanco sobre el color secundario
    onBackground = TextPrimary,     // Texto en marrón suave sobre el fondo claro
    onSurface = TextPrimary         // Texto en marrón sobre las superficies
)

@Composable
fun ZephyrTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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
