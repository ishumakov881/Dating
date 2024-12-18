package com.lds.quickdeal.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.unit.dp
import com.lds.quickdeal.android.utils.AttachFileType

@Composable
fun MakeDropdownMenu(
    title: String,
    subtitle: String,
    filetype: AttachFileType,
    onRemoveClicked: () -> Unit
) {

    val icon = when (filetype) {
        AttachFileType.FILE -> Icons.Filled.AttachFile
        AttachFileType.PHOTO -> Icons.Filled.Photo
        AttachFileType.VIDEO -> Icons.Filled.VideoFile
        else -> Icons.Filled.Attachment
    }

    var menuExpanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart), // Размещаем содержимое влево
            verticalAlignment = Alignment.CenterVertically // Центрируем по вертикали
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Attach",
                tint = Color.Red
            )
            Text(
                text = title,
                modifier = Modifier
                    .clickable { menuExpanded = true }
                    .padding(4.dp)
            )
            Text(
                text = "[$subtitle]",
                modifier = Modifier
                    .padding(4.dp)
            )
        }

        // Отображение имени файла

        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    menuExpanded = false
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