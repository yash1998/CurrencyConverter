package com.example.currencyconverter.ui.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.currencyconverter.utils.addCommas
import com.example.currencyconverter.utils.removeCommas


@Composable
fun CurrencyInputView(amount: String, isEnabled: Boolean, onValueChange: (String) -> Unit) {
    TextField(
        enabled = isEnabled,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number,
            autoCorrectEnabled = false
        ),
        modifier = Modifier.fillMaxWidth(),
        value = amount.addCommas(),
        onValueChange = {
            onValueChange(it.removeCommas())
        },
        label = {
            Text(text = "Enter Amount to Convert")
        },
        placeholder = {
            Text(text = "Enter Amount to Convert")
        })
}
