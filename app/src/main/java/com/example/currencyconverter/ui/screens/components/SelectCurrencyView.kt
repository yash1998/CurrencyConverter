package com.example.currencyconverter.ui.screens.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.currencyconverter.ui.theme.BorderSmall
import com.example.currencyconverter.ui.theme.LightBlue
import com.example.currencyconverter.ui.theme.SpaceSmall
import com.example.currencyconverter.ui.theme.SpaceXSmall
import com.example.currencyconverter.utils.showToast


@Composable
fun SelectCurrencyView(
    selectedOption: String, options: List<String>, onOptionSelected: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = SpaceSmall),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            modifier = Modifier
                .border(BorderSmall, Color.Black, RoundedCornerShape(SpaceXSmall))
                .clickable {
                    if (options.isEmpty()) {
                        context.showToast("Updating data...")
                    } else {
                        showDialog = true
                    }
                }
                .padding(SpaceSmall)) {
            Text(text = selectedOption, modifier = Modifier.padding(horizontal = SpaceSmall))
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "",
                tint = LightBlue
            )
        }
    }

    if (showDialog) {
        SelectCurrencyDialogView(
            selectedOption,
            options,
            onDismissDialog = { showDialog = false },
            onOptionSelected = { onOptionSelected(it) })
    }
}