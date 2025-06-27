package com.app.taskly.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.app.taskly.task.data.local.enums.Priority
import com.app.taskly.task.ui.theme.highPriority
import com.app.taskly.task.ui.theme.lowPriority
import com.app.taskly.task.ui.theme.mediumPriority
import com.app.taskly.task.ui.theme.urgentPriority
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

@Composable
fun getScreenWidth() : Dp {
    return LocalConfiguration.current.screenWidthDp.dp
}

fun getColorBasedOnPriority(priority: Priority) : Color {
    return when(priority) {
        Priority.Low -> lowPriority
        Priority.Medium -> mediumPriority
        Priority.High -> highPriority
        Priority.Urgent -> urgentPriority
    }
}

@Composable
fun LottieLoader(isPlaying: Boolean, rawFile: Int, modifier: Modifier = Modifier){
    val lottieCompose by rememberLottieComposition(
        LottieCompositionSpec.RawRes(rawFile))

    LottieAnimation(
        composition = lottieCompose,
        iterations = LottieConstants.IterateForever,
        clipToCompositionBounds = true,
        isPlaying = isPlaying,
        speed = 1f,
        contentScale = ContentScale.Fit,
        modifier = modifier
    )
}

@SuppressLint("SimpleDateFormat")
@Composable
fun DatePicker(
    onDateChanged: (String) -> Unit,
    dateFormat: String,
    minDate: Long? = null,
    maxDate: Long? = null,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val dateNow: LocalDate = LocalDate.now()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            val formattedDate =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(selectedDate.toString())?.let {
                    SimpleDateFormat(dateFormat, Locale.getDefault()).format(
                        it
                    )
                }
            if (formattedDate != null) {
                onDateChanged(formattedDate)
            }
        },
        dateNow.year,
        dateNow.monthValue - 1,
        dateNow.dayOfMonth,
    )

    minDate?.let {
        datePickerDialog.datePicker.minDate = it
    }
    maxDate?.let {
        datePickerDialog.datePicker.maxDate = it
    }

    datePickerDialog.setOnCancelListener {
        onCancel()
    }

    datePickerDialog.show()
}

@Composable
fun SetStatusBarColor(color: Color, darkIcons: Boolean = true) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = color,
        darkIcons = darkIcons
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker(
    onTimeSelected: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(
                onClick = { onDismiss() },
            ) {
                Text("Dismiss", color = Color.Black)
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onTimeSelected(timePickerState.hour, timePickerState.minute) },
            ) {
                Text("OK", color = Color.Black)
            }
        },
        text = {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = Color.Black,
                    timeSelectorSelectedContainerColor = Color.Black,
                    timeSelectorUnselectedContainerColor = Color.Black,
                    timeSelectorSelectedContentColor = Color.White,
                    timeSelectorUnselectedContentColor = Color.White
                )
            )
        }
    )
}
