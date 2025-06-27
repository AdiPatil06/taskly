package com.app.taskly.task.ui.screen

import android.icu.util.Calendar
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.taskly.task.ui.theme.hostFontFamilyBold
import com.app.taskly.task.viewmodel.TaskViewModel
import com.app.taskly.utils.SetStatusBarColor
import com.app.taskly.utils.getColorBasedOnPriority
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle.*
import java.util.Locale
import kotlin.text.*

fun getMonthName(month: Int): String {
    val months = arrayOf(
        "January", "February", "March", "April", "May", "June", "July", "August",
        "September", "October", "November", "December"
    )
    return months[month]
}

@Composable
fun CalenderScreen(viewModel: TaskViewModel, onBackClick: () -> Unit){
    val tasks = viewModel.allTasks.collectAsState(emptyList())
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val calendar = Calendar.getInstance()

    var changeMonthYear by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableStateOf(selectedDate.month.getDisplayName(FULL, Locale.getDefault())) }
    var selectedYear by remember { mutableStateOf(selectedDate.year.toString()) }

    val tasksForTheDay = tasks.value.filter { it.date == selectedDate.format(
        DateTimeFormatter.ofPattern("dd MMM, yyyy")) }

    BackHandler {
        onBackClick()
    }

    val state = rememberLazyListState()

    LaunchedEffect(selectedMonth, selectedYear) {
        selectedDate = selectedDate.withYear(selectedYear.toInt())
            .withMonth(Month.valueOf(selectedMonth.uppercase()).value)
            .withDayOfMonth(
                if (selectedMonth == LocalDate.now().month.getDisplayName(FULL, Locale.getDefault(),
                                ).lowercase(Locale.ROOT).replaceFirstChar { it.uppercase() } &&
                    selectedYear == LocalDate.now().year.toString()
                ) {
                    LocalDate.now().dayOfMonth
                } else {
                    1
                },
            )
        state.animateScrollToItem(index = selectedDate.dayOfMonth - 1, scrollOffset = -50)
    }

    LaunchedEffect(selectedDate) {
        state.animateScrollToItem(index = selectedDate.dayOfMonth - 1, scrollOffset = -50)
    }

    SetStatusBarColor(
        color = Color.Black,
        darkIcons = false
    )

    if(changeMonthYear) {
        DatePickerDialog(
            LocalContext.current,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                selectedMonth = getMonthName(month)
                selectedYear = year.toString()
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                changeMonthYear = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = calendar.timeInMillis
            setTitle("Select Month and Year")
        }.show()
    }

    Column(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier.fillMaxSize().background(color = Color.Black)){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ){
                Box(Modifier.size(40.dp).clip(CircleShape).background(color = Color.White).clickable { onBackClick() }, contentAlignment = Alignment.Center){
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(25.dp)
                    )
                }
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    text = "$selectedMonth $selectedYear",
                    style = MaterialTheme.typography.displayMedium.copy(fontFamily = hostFontFamilyBold)
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp).clickable { changeMonthYear = true }
                )
            }

            DatePickerRow(
                state = state,
                daysInMonth = selectedDate.lengthOfMonth(),
                selectedDate = selectedDate,
                onDateSelected = { newDate ->
                    selectedDate = newDate
                }
            )

            Column(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)).background(color = Color.White)){
                Column(modifier = Modifier.padding(20.dp)){
                    val numberOfTasks = tasksForTheDay.size
                    Text(
                        text = "$numberOfTasks meetings",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray),
                    )
                    Text(
                        text = selectedDate.dayOfWeek.getDisplayName(FULL, Locale.getDefault()),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.Black, fontFamily = hostFontFamilyBold,
                            fontSize = 40.sp,
                        ),
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        items(24) { hour ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "$hour:00",
                                    modifier = Modifier,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color.Black,
                                        fontFamily = hostFontFamilyBold
                                    )
                                )
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                ) {
                                    val timeTask = tasksForTheDay.filter { it.time.split(":").first() == hour.toString() }
                                    if(timeTask.isNotEmpty()) {
                                        timeTask.forEach {
                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth().padding(bottom = 5.dp),
                                                elevation = CardDefaults.elevatedCardElevation(),
                                                colors = CardDefaults.cardColors(containerColor = getColorBasedOnPriority(it.priority))
                                            ) {
                                                Text(
                                                    text = it.title,
                                                    modifier = Modifier.padding(8.dp),
                                                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
                                                )
                                            }
                                        }
                                    } else {
                                        HorizontalDivider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 10.dp, top = 5.dp)
                                                .height(1.dp)
                                                .background(Color.Gray),
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun DatePickerRow(
    state: LazyListState,
    daysInMonth: Int,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    LazyRow(
        state = state,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        items(daysInMonth) { dayIndex ->
            val date = selectedDate.withDayOfMonth(dayIndex + 1)
            val isSelected = date == selectedDate

            Column(
                modifier = Modifier
                    .padding(
                        start = if (dayIndex == 0) 20.dp else 0.dp,
                        end = if (dayIndex == daysInMonth - 1) 20.dp else 0.dp
                    )
                    .clickable { onDateSelected(date) }
            ) {
                Text(
                    text = date.dayOfWeek.getDisplayName(SHORT, Locale.getDefault())
                        .lowercase(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White.copy(alpha = if (isSelected) 1f else 0.5f)
                    )
                )
                Text(
                    text = date.dayOfMonth.toString(),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontFamily = hostFontFamilyBold,
                        fontSize = 40.sp,
                        color = Color.White.copy(alpha = if (isSelected) 1f else 0.5f)
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
