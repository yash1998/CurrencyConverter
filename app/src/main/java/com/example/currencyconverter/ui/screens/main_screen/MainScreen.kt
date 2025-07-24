package com.example.currencyconverter.ui.screens.main_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconverter.data.remote.model.LatestConversionModel
import com.example.currencyconverter.ui.screens.components.CurrencyConverterGrid
import com.example.currencyconverter.ui.screens.components.CurrencyInputView
import com.example.currencyconverter.ui.screens.components.SelectCurrencyView
import com.example.currencyconverter.ui.screens.loader.Loader
import com.example.currencyconverter.ui.theme.SpaceMedium
import com.example.currencyconverter.ui.theme.SpaceSmall
import com.example.currencyconverter.utils.CurrencyNamesMap
import com.example.currencyconverter.utils.UiState
import com.example.currencyconverter.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Preview
@Composable
fun MainScreen(vm: MainScreenVM = hiltViewModel()) {
    val context = LocalContext.current
    var amountToConvertText by remember { mutableStateOf("0") }

    var selectedOption by remember { mutableStateOf("Select Currency") }

    val currencyNamesMap by vm.currencyNamesMap.collectAsState()
    val currencyConversionMap by vm.currencyConversionMap.collectAsState()

    val options = remember(currencyNamesMap) {
        when (currencyNamesMap) {
            is UiState.Success<*> -> {
                (currencyNamesMap as UiState.Success<CurrencyNamesMap>).data
            }

            is UiState.Loading -> emptyMap()
            else -> emptyMap()
        }
    }

    val conversionRates: LatestConversionModel? = remember(currencyConversionMap) {
        if (currencyConversionMap is UiState.Success<*>) {
            (currencyConversionMap as UiState.Success<LatestConversionModel>).data.also {
                if (options.get(it.base) != null) {
                    selectedOption = it.base
                }
//                context.showToast("Updating")
            }
        } else if (currencyConversionMap is UiState.Error) {
            context.showToast((currencyNamesMap as UiState.Error).message)
            null
        } else {
            null
        }
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            vm.getCurrencyList()
        }
        vm.pollLatestConversionRates()
    }

    Column(modifier = Modifier.padding(horizontal = SpaceMedium, vertical = SpaceSmall)) {
        CurrencyInputView(
            amountToConvertText,
            conversionRates != null,
            onValueChange = { newText -> amountToConvertText = newText })
        SelectCurrencyView(
            selectedOption,
            options.keys.toList(),
            onOptionSelected = { option -> selectedOption = option })
        if (conversionRates == null) {
            Loader(modifier = Modifier.fillMaxSize())
        } else {
            if (conversionRates.rates.get(selectedOption) != null) {
                CurrencyConverterGrid(
                    currencyNames = options,
                    currencyList = conversionRates.rates,
                    baseMultiplier = conversionRates.rates.get(selectedOption) ?: 0.0,
                    amountToConvert = amountToConvertText
                )
            }
        }
    }
}

