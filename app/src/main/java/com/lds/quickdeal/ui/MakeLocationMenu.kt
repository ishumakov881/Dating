package com.lds.quickdeal.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MakeLocationMenu(
    title: String,
    subtitle: String,

    onRemoveClicked: () -> Unit
) {


    var menuExpanded0 by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = "Attach",
                tint = Color.Red
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .clickable { menuExpanded0 = true }
                    .padding(4.dp)

            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(4.dp)
            )
        }

        // Отображение имени файла

        DropdownMenu(
            expanded = menuExpanded0,
            onDismissRequest = { menuExpanded0 = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    menuExpanded0 = false
                    onRemoveClicked()
                },
                text = { Text("Удалить") },
                modifier = Modifier.padding(horizontal = 16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.DeleteForever,
                        contentDescription = "Menu Icon"
                    )
                },
                contentPadding = PaddingValues(horizontal = 12.dp)
            )
        }
    }

}