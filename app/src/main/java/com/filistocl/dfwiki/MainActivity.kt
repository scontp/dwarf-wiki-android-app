package com.filistocl.dfwiki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.filistocl.dfwiki.data.WikiRepository
import com.filistocl.dfwiki.model.Material
import com.filistocl.dfwiki.ui.CatalogScreen
import com.filistocl.dfwiki.ui.DetailScreen
import com.filistocl.dfwiki.ui.LandingScreen
import com.filistocl.dfwiki.ui.WikiViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Professional ViewModel initialization
        val viewModel: WikiViewModel by viewModels {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return WikiViewModel(WikiRepository(applicationContext)) as T
                }
            }
        }

        setContent {
            MaterialTheme {
                Surface {
                    AppNavigation(viewModel = viewModel)
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
private fun AppNavigation(viewModel: WikiViewModel) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Landing) }
    val materials by viewModel.materials

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
                materials = materials,
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
