package com.dinoraw.chromelogs.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dinoraw.chromelogs.domain.model.ClientGestureData

@Composable
fun LogItem(
    clientGestureData: ClientGestureData,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier,
    ) {
        Column (modifier.padding(4.dp)) {
            Text(text = "Client IP: ${clientGestureData.ip}")
            Text(text = "Gesture ID: ${clientGestureData.id}")
            Text(text = "Max Width: ${clientGestureData.gestureServiceResult.maxWidth}")
            Text(text = "Max Height: ${clientGestureData.gestureServiceResult.maxHeight}")
            Text(text = "Vertical gesture: from ${clientGestureData.gestureServiceResult.startOfVerticalSwipe} to ${clientGestureData.gestureServiceResult.endOfVerticalSwipe}")
            Text(text = "Horizontal gesture: from ${clientGestureData.gestureServiceResult.startOfHorizontalSwipe} to ${clientGestureData.gestureServiceResult.endOfHorizontalSwipe }")
        }
    }
}