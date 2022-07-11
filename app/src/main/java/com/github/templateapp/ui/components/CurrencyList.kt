@file:OptIn(ExperimentalMaterialApi::class)

package com.github.templateapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.templateapp.R
import com.github.templateapp.screenmodel.HomeScreenModel
import com.github.templateapp.ui.fmt.fmtCurrency
import com.github.templateapp.ui.fmt.fmtForeignCurrency

@Composable
fun ColumnScope.CurrencyList(value: List<HomeScreenModel.Companion.Item>) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .weight(1f)
            .padding(16.dp)
    ) {

        items(value, key = { it.id }) { currency ->
            Spacer(Modifier.height(4.dp))
            Card(
                onClick = {},
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        content = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${currency.name} X ${currency.faceValue}",
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                if (currency.recap != null && currency.recap > 0.0) {
                                    Text(text = fmtForeignCurrency(currency.recap, currency))
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row() {
                                    Text(text = stringResource(id = R.string.today))
                                    Spacer(Modifier.width(4.dp))
                                    Text(text = fmtCurrency(currency.value))
                                }
                                Spacer(Modifier.height(4.dp))
                                Row() {
                                    Text(text = stringResource(id = R.string.prev))
                                    Spacer(Modifier.width(4.dp))
                                    Text(text = fmtCurrency(currency.previousValue))
                                }
                            }
                        }
                    )
                }
            )
            Spacer(Modifier.height(4.dp))
        }
    }
}