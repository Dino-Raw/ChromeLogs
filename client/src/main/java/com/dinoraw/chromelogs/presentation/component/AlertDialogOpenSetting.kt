package com.dinoraw.chromelogs.presentation.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.dinoraw.chromelogs.util.openAccessibilityServiceSetting

@Composable
fun AlertDialogOpenSetting(
    navigateUp: () -> Unit = { },
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    Box(modifier) {
        val context = LocalContext.current
        AlertDialog(
            title = { Text(text = "Accessibility service") },
            text = { Text(text = "Enable accessibility service in settings") },
            onDismissRequest = { navigateUp() },
            confirmButton = {
                TextButton(
                    onClick = {
                        context.openAccessibilityServiceSetting()
                        navigateUp()
                    }
                ) { Text("Open Setting") }
            },
            dismissButton = {
                TextButton(
                    onClick = { navigateUp() }
                ) { Text("Close") }
            }
        )
    }
}