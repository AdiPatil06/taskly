package com.app.taskly.task.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.taskly.R
import com.app.taskly.task.ui.theme.hostFontFamilyBold
import com.app.taskly.task.ui.theme.notificationCardColor
import com.app.taskly.task.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(viewModel: TaskViewModel, onBackClick: () -> Unit) {
    val notifications = viewModel.allNotifications.collectAsState(emptyList())

    BackHandler {
        onBackClick()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        item {
            Column {
                Text(
                    "My",
                    style = MaterialTheme.typography.displayMedium.copy(
                        color = Color.Black,
                        fontSize = 26.sp,
                        fontFamily = hostFontFamilyBold
                    )
                )
                Text(
                    "Notifications",
                    style = MaterialTheme.typography.displayMedium.copy(
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 26.sp,
                        fontFamily = hostFontFamilyBold
                    )
                )
                Spacer(Modifier.height(20.dp))
            }
        }

        items(notifications.value, key = { it.id }) { notification ->
            val dismissState = rememberSwipeToDismissBoxState(
                positionalThreshold = { it * 0.4f }
            )

            if (dismissState.currentValue == SwipeToDismissBoxValue.StartToEnd &&
                dismissState.targetValue != SwipeToDismissBoxValue.Settled
            ) {
                viewModel.requestDelete(notification)
            }


            SwipeToDismissBox(
                dismissState,
                backgroundContent = {
                    val color = when (dismissState.dismissDirection) {
                        SwipeToDismissBoxValue.StartToEnd -> Color.Red
                        else -> Color.Transparent
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(15.dp))
                            .background(color)
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.White
                        )
                    }
                },
                content = {
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = notificationCardColor)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.icon_notification),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                            Column {
                                Text(
                                    notification.title,
                                    style = MaterialTheme.typography.displaySmall.copy(
                                        color = Color.Black,
                                        fontFamily = hostFontFamilyBold
                                    )
                                )
                                Text(
                                    notification.message,
                                    style = MaterialTheme.typography.displaySmall.copy(
                                        color = Color.Black
                                    )
                                )
                            }
                        }
                    }
                },
            )
            Spacer(Modifier.height(10.dp))
        }

//        items(notifications.value) {
//            Card(
//                shape = RoundedCornerShape(15.dp),
//                modifier = Modifier.fillMaxWidth(),
//                colors = CardDefaults.cardColors(containerColor = notificationCardColor)
//            ) {
//                Row(
//                    modifier = Modifier.padding(10.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(10.dp)
//                ) {
//                    Image(
//                        painter = painterResource(R.drawable.icon_notification),
//                        contentDescription = null,
//                        modifier = Modifier.size(30.dp)
//                    )
//                    Column {
//                        Text(
//                            it.title,
//                            style = MaterialTheme.typography.displaySmall.copy(
//                                color = Color.Black,
//                                fontFamily = hostFontFamilyBold
//                            )
//                        )
//                        Text(
//                            it.message,
//                            style = MaterialTheme.typography.displaySmall.copy(
//                                color = Color.Black
//                            )
//                        )
//                    }
//                }
//            }
//            Spacer(Modifier.height(10.dp))
//        }
    }
}