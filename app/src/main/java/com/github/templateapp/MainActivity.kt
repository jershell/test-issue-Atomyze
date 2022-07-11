package com.github.templateapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.github.templateapp.data.DataSource
import com.github.templateapp.repository.CurrencyRepository
import com.github.templateapp.screen.HomeScreen
import com.github.templateapp.screenmodel.HomeScreenModel
import com.github.templateapp.ui.theme.TemplateAppTheme
import org.kodein.di.*

val LocalDi = compositionLocalOf<DI> { error("No di provided!") }

class MainActivity : ComponentActivity(), DIAware {
    override val di by DI.lazy {
        bindSingleton { CurrencyRepository(DataSource) }
        bindProvider { HomeScreenModel(instance()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(LocalDi provides di) {
                TemplateAppTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background,
                        content = {
                            Navigator(screen = HomeScreen())
                        }
                    )
                }
            }
        }
    }
}
