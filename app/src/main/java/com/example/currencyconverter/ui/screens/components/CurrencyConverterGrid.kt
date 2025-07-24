package com.example.currencyconverter.ui.screens.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.currencyconverter.ui.theme.BorderSmall
import com.example.currencyconverter.ui.theme.SpaceSmall
import com.example.currencyconverter.ui.theme.TextSmall
import com.example.currencyconverter.utils.CurrencyConversionMap
import com.example.currencyconverter.utils.CurrencyNamesMap
import com.example.currencyconverter.utils.convert


@Composable
fun CurrencyConverterGrid(
    currencyNames: CurrencyNamesMap,
    currencyList: CurrencyConversionMap,
    baseMultiplier: Double,
    amountToConvert: String
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(SpaceSmall),
        verticalArrangement = Arrangement.spacedBy(SpaceSmall),
        horizontalArrangement = Arrangement.spacedBy(SpaceSmall)
    ) {
        items(currencyList.toList()) { pair ->
            CurrencyConverterView(currencyNames, pair, baseMultiplier, amountToConvert)
        }
    }
}

@Composable
fun CurrencyConverterView(
    currencyNames: CurrencyNamesMap,
    conversionRate: Pair<String, Double>,
    baseMultiplier: Double,
    amount: String = ""
) {
    val convertedAmt = amount.convert(baseMultiplier, conversionRate.second)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .border(BorderSmall, Color.Black, RoundedCornerShape(SpaceSmall))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(SpaceSmall),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier,
                text = "${conversionRate.first}(${currencyNames[conversionRate.first]})",
                textAlign = TextAlign.Center,
                maxLines = 3,
                color = Color.Black,
                lineHeight = TextSmall,
                fontSize = TextSmall
            )
            if (convertedAmt.isNotEmpty() && convertedAmt != "0.0") {
                Text(
                    color = Color.Black,
                    text = convertedAmt,
                    textAlign = TextAlign.Center,
                    fontSize = TextSmall
                )
            }
        }
    }
}
