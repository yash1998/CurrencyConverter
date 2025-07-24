package com.example.currencyconverter.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun SelectCurrencyDialogView(
    selectedOption: String,
    options: List<String>,
    onDismissDialog: () -> Unit,
    onOptionSelected: (String) -> Unit
) {
    if (options.isEmpty()) {
        CircularProgressIndicator()
    } else {
        AlertDialog(onDismissRequest = { onDismissDialog() }, confirmButton = {
            Button(onClick = { onDismissDialog() }) {
                Text("OK")
            }
        }, text = {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(options) { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOptionSelected(option) }) {
                        RadioButton(
                            selected = (option == selectedOption), onClick = {
                                onOptionSelected(option)
                            })
                        Text(text = option)
                    }
                }
            }
        })
    }
}