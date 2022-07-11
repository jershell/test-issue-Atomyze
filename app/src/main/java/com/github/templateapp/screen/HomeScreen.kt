package com.github.templateapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.templateapp.screenmodel.HomeScreenModel
import com.github.templateapp.ui.components.CurrencyList
import com.github.templateapp.ui.components.Input
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeScreen : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val screenModel = rememberScreenModel<HomeScreenModel>()
        val isLoading by screenModel.isLoading.collectAsState()
        // TODO В следующей серии мы обработам ошибки
        val errors by screenModel.errors.collectAsState()
        val allItems by screenModel.all.collectAsState()
        val timer by screenModel.timer.collectAsState()

        var quantity by remember {
            mutableStateOf(TextFieldValue(
                screenModel.quantity.value.let { q ->
                    if (q > 0.0) q.toString() else ""
                }
            ))
        }

        LaunchedEffect(key) {
            screenModel.getCurrencies()

            launch {
                // бесконечный цикл плохо, но не тут. Во время утилизации экрана коррутина остановится.
                while (true) {
                    screenModel.tick()
                    delay(200)
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            content = {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isLoading),
                    onRefresh = { screenModel.refresh() },
                    content = {
                        Column(Modifier.fillMaxSize()) {
                            TopAppBar {
                                IconButton(onClick = { }) {
                                    Icon(Icons.Filled.Menu, contentDescription = "menu")
                                }

                                Spacer(modifier = Modifier.width(2.dp))

                                Row(Modifier.weight(1f)) {
                                    Input(
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        value = quantity,
                                        onValueChange = { value ->
                                            quantity = value

                                            val newValue = value.text.toDoubleOrNull()

                                            if (newValue != null) {
                                                screenModel.setQuantity(newValue)
                                            } else {
                                                screenModel.setQuantity(0.0)
                                            }
                                        },
                                        singleLine = true,
                                        maxLines = 1,
                                    )
                                }


                                IconButton(onClick = { screenModel.refresh(silent = true) }) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            Icons.Filled.Refresh,
                                            "refresh",
                                            tint = Color.White
                                        )
                                        CircularProgressIndicator(
                                            progress = timer,
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                            if (isLoading && allItems.isEmpty()) {
                                Row(
                                    Modifier
                                        .fillMaxSize()
                                        .weight(1f), horizontalArrangement = Arrangement.Center
                                ) {
                                    Spacer(Modifier.height(12.dp))
                                    Text(text = "Loading...")
                                }
                            } else {
                                CurrencyList(allItems)
                            }
                        }
                    }
                )
            }
        )
    }
}