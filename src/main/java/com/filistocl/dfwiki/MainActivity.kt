package com.filistocl.dfwiki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.filistocl.dfwiki.data.WikiRepository
import com.filistocl.dfwiki.model.Material
import com.filistocl.dfwiki.ui.CatalogScreen
import com.filistocl.dfwiki.ui.DetailScreen
import com.filistocl.dfwiki.ui.LandingScreen

class MainActivity : ComponentActivity() {

    private lateinit var repository: WikiRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = WikiRepository(applicationContext)

        setContent {
            MaterialTheme {
                Surface {
                    AppNavigation(repository = repository)
                }
            }
        }
    }
}

sealed class Screen {
    data object Landing : Screen()
    data object Catalog : Screen()
    data class Detail(val material: Material) : Screen()
}

@Composable
private fun AppNavigation(repository: WikiRepository) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Landing) }

    when (val screen = currentScreen) {
        is Screen.Landing -> {
            LandingScreen(
                onEnterWiki = {
                    currentScreen = Screen.Catalog
                }
            )
        }

        is Screen.Catalog -> {
            CatalogScreen(
                repository = repository,
                onMaterialClick = { material ->
                    currentScreen = Screen.Detail(material)
                },
                onBack = {
                    currentScreen = Screen.Landing
                }
            )
        }

        is Screen.Detail -> {
            DetailScreen(
                material = screen.material,
                onBack = {
                    currentScreen = Screen.Catalog
                }
            )
        }
    }
}