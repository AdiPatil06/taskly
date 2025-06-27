package com.app.taskly.task.ui.screen

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.taskly.R
import com.app.taskly.task.data.local.entity.Task
import com.app.taskly.task.data.local.enums.Priority
import com.app.taskly.task.data.local.enums.TaskStatus
import com.app.taskly.task.di.app
import com.app.taskly.task.ui.theme.hostFontFamilyBold
import com.app.taskly.task.viewmodel.TaskViewModel
import com.app.taskly.utils.DatePicker
import com.app.taskly.utils.TimePicker
import com.app.taskly.utils.getColorBasedOnPriority
import com.app.taskly.utils.getScreenWidth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: TaskViewModel, onClick: (String) -> Unit) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var showHeading by remember { mutableStateOf(false) }
    var showCard by remember { mutableStateOf(false) }
    var showTasks by remember { mutableStateOf(false) }
    var simpleTaskSheet by remember { mutableStateOf(false) }
    var recurringTaskSheet by remember { mutableStateOf(false) }

    val tasks = viewModel.allTasks.collectAsState(emptyList())
    val onGoingTasks = tasks.value.filter {
        it.status != TaskStatus.Completed && it.date == LocalDate.now().format(
            DateTimeFormatter.ofPattern("dd MMM, yyyy")
        )
    }
    val completedTasks = tasks.value.filter {
        it.status == TaskStatus.Completed && it.date == LocalDate.now().format(
            DateTimeFormatter.ofPattern("dd MMM, yyyy")
        )
    }

    BackHandler {
        (context as? ComponentActivity)?.finishAffinity()
    }

    LaunchedEffect(Unit) {
        showHeading = true
        delay(800)
        showCard = true
        delay(800)
        showTasks = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(scrollState)
        ) {
            // Heading
            AnimatedVisibility(
                visible = showHeading,
                enter = fadeIn()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Good",
                            style = MaterialTheme.typography.displayMedium.copy(
                                color = Color.Black,
                                fontSize = 26.sp,
                                fontFamily = hostFontFamilyBold
                            )
                        )
                        Text(
                            "Morning",
                            style = MaterialTheme.typography.displayMedium.copy(
                                color = MaterialTheme.colorScheme.secondary,
                                fontSize = 26.sp,
                                fontFamily = hostFontFamilyBold
                            )
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.tertiary),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.icon_calender),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(25.dp)
                                    .clickable { onClick("calender") }
                            )
                        }
                        Box(contentAlignment = Alignment.TopEnd) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.tertiary),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.icon_notification),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(25.dp)
                                        .clickable { onClick("notification") }
                                )
                            }
                            if (viewModel.countOfNotifications.intValue > 0) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.tertiary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        viewModel.countOfNotifications.intValue.toString(),
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Card
            AnimatedVisibility(
                visible = showCard,
                enter = fadeIn()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            "24 April 2024, Mon",
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = Color.White.copy(
                                    alpha = 0.8f
                                )
                            )
                        )
                        Text(
                            "Today's Progress",
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontFamily = hostFontFamilyBold,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(50.dp))
                        Text(
                            "${completedTasks.size}/${tasks.value.size} Tasks",
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = Color.White.copy(
                                    alpha = 0.8f
                                )
                            )
                        )
                        Text(
                            buildString {
                                append(
                                    ((completedTasks.size.toFloat() / tasks.value.size.toFloat()) * 100).toInt()
                                        .toString()
                                )
                                append("%")
                            },
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontFamily = hostFontFamilyBold,
                                color = Color.White,
                                fontSize = 40.sp
                            )
                        )
                        LinearProgressIndicator(
                            progress = {
                                if (tasks.value.isNotEmpty()) {
                                    completedTasks.size.toFloat() / tasks.value.size.toFloat()
                                } else 0f
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp)
                                .clip(RoundedCornerShape(20.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = showTasks,
                enter = fadeIn()
            ) {
                Column {
                    // Ongoing Tasks
                    Row(
                        modifier = Modifier.padding(top = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Ongoing",
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontFamily = hostFontFamilyBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = onGoingTasks.size.toString(),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontFamily = hostFontFamilyBold,
                                    color = Color.Black
                                )
                            )
                        }
                    }

                    // Ongoing Item
                    if (onGoingTasks.isEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "There are no ongoing tasks",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    color = Color.Gray,
                                ),
                            )
                        }
                    } else {
                        onGoingTasks.forEach {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 15.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(30.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .border(
                                                2.dp,
                                                MaterialTheme.colorScheme.secondary,
                                                RoundedCornerShape(10.dp)
                                            )
                                            .background(Color.Transparent)
                                            .clickable {
                                                viewModel.updateTask(
                                                    task = it,
                                                    TaskStatus.Completed
                                                )
                                            }
                                    )
                                    Column(verticalArrangement = Arrangement.Center) {
                                        Text(
                                            it.title,
                                            style = MaterialTheme.typography.headlineSmall.copy(
                                                fontFamily = hostFontFamilyBold,
                                                color = Color.Black
                                            )
                                        )
                                        Text(
                                            it.date,
                                            style = MaterialTheme.typography.headlineSmall.copy(
                                                color = Color.Gray
                                            )
                                        )
                                    }
                                }
                                Box(
                                    Modifier
                                        .size(100.dp, 30.dp)
                                        .clip(CircleShape)
                                        .background(getColorBasedOnPriority(it.priority)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        it.priority.toString(),
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            fontFamily = hostFontFamilyBold,
                                            color = Color.Black
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Completed Tasks
                    Row(
                        modifier = Modifier.padding(top = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Completed",
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontFamily = hostFontFamilyBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = completedTasks.size.toString(),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontFamily = hostFontFamilyBold,
                                    color = Color.Black
                                )
                            )
                        }
                    }

                    // Completed Item
                    if (completedTasks.isEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "There are no completed tasks",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    color = Color.Gray,
                                ),
                            )
                        }
                    } else {
                        completedTasks.forEach {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 15.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .border(
                                            2.dp,
                                            MaterialTheme.colorScheme.secondary,
                                            RoundedCornerShape(10.dp)
                                        )
                                        .background(Color.Transparent)
                                        .clickable {
                                            viewModel.updateTask(task = it, TaskStatus.Todo)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null
                                    )
                                }
                                Column(verticalArrangement = Arrangement.Center) {
                                    Text(
                                        it.title,
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            fontFamily = hostFontFamilyBold,
                                            color = Color.Gray,
                                            textDecoration = TextDecoration.LineThrough
                                        )
                                    )
                                    Text(
                                        it.date,
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            color = Color.Gray,
                                            textDecoration = TextDecoration.LineThrough
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        FloatingActionMenu(
            onClick = {
                when (it) {
                    app.constants.simpleTaskId.toString() -> simpleTaskSheet = true
                    app.constants.recurringTaskId.toString() -> recurringTaskSheet = true
                }
            }
        )
    }

    if (simpleTaskSheet) {
        SimpleTaskSheet(viewModel) {
            simpleTaskSheet = false
        }
    }

    if (recurringTaskSheet) {
        RecurringTaskSheet(viewModel) {
            recurringTaskSheet = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTaskSheet(viewModel: TaskViewModel, onDismiss: () -> Unit) {
    ModalBottomSheet(
        containerColor = Color.Black,
        onDismissRequest = { onDismiss() },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Add Task",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            SimpleTask(viewModel)

            Button(
                enabled = viewModel.checkForSimpleTask(),
                onClick = {
                    viewModel.addTask(
                        Task(
                            title = viewModel.simpleTaskName.value,
                            date = viewModel.simpleTaskDate.value,
                            time = viewModel.simpleTaskTime.value,
                            status = TaskStatus.Todo,
                            priority = viewModel.simpleTaskPriority.value
                        )
                    )
                    onDismiss()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Save")
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurringTaskSheet(viewModel: TaskViewModel, onDismiss: () -> Unit) {
    ModalBottomSheet(
        containerColor = Color.Black,
        onDismissRequest = { onDismiss() },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Add a Recurring Task",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            RecurringTask(viewModel)

            Button(
                enabled = viewModel.checkForRecurringTask(),
                onClick = {
                    viewModel.addRecurringTasks()
                    onDismiss()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Save")
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
fun FloatingActionMenu(onClick: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val animationDuration = 300

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = if (expanded) Color.Black.copy(alpha = 0.5f) else Color.Transparent),
        contentAlignment = Alignment.BottomEnd,
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            val rotation = remember { Animatable(0f) }

            if (expanded) {
                listOf(
                    R.drawable.icon_simple_task to app.constants.simpleTaskId,
                    R.drawable.icon_recurring_task to app.constants.recurringTaskId,
                ).forEach { pair ->
                    val alpha = remember { Animatable(0f) }
                    val translationY = remember { Animatable(100f) }
                    val scale = remember { Animatable(0f) }
                    val text = when (pair.second) {
                        app.constants.simpleTaskId -> "Simple Task"
                        app.constants.recurringTaskId -> "Recurring Task"
                        else -> "Simple Task"
                    }

                    if (expanded) {
                        coroutineScope.launch {
                            alpha.animateTo(1f, animationSpec = tween(300))
                        }
                        coroutineScope.launch {
                            translationY.animateTo(0f, animationSpec = tween(300))
                        }
                        coroutineScope.launch {
                            scale.animateTo(1f, animationSpec = tween(300))
                        }
                    }

                    AnimatedVisibility(
                        visible = true,
                        enter = slideInVertically(
                            initialOffsetY = { it / 2 },
                            animationSpec = tween(animationDuration)
                        ) + fadeIn(tween(animationDuration)),
                        exit = slideOutVertically(
                            targetOffsetY = { it / 2 },
                            animationSpec = tween(animationDuration)
                        ) + fadeOut(tween(animationDuration))
                    ) {
                        Card(
                            onClick = {
                                onClick(pair.second.toString())
                                expanded = false
                            },
                            modifier = Modifier
                                .graphicsLayer(
                                    alpha = alpha.value,
                                    translationY = translationY.value,
                                    scaleX = scale.value,
                                    scaleY = scale.value
                                ),
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(10.dp)
                            ) {
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
                                )
                                Image(
                                    painter = painterResource(pair.first),
                                    contentDescription = text,
                                    modifier = Modifier.size(30.dp),
                                    colorFilter = ColorFilter.tint(color = Color.White)
                                )
                            }
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    expanded = !expanded
                    coroutineScope.launch {
                        rotation.animateTo(
                            if (expanded) 45f else 0f,
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        )
                    }
                },
                modifier = Modifier
                    .clip(CircleShape),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier
                        .rotate(rotation.value)
                        .size(30.dp)
                )
            }
        }
    }
}

@Composable
fun SimpleTask(viewModel: TaskViewModel) {
    OutlinedTextField(
        value = viewModel.simpleTaskName.value,
        onValueChange = { viewModel.simpleTaskName.value = it },
        label = {
            Text(
                "Task Name",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(20.dp),
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DateTimeSelector(
            modifier = Modifier.width(getScreenWidth() * 0.5f),
            label = "Date",
            dateTime = viewModel.simpleTaskDate.value,
            onDateTimeSelect = { viewModel.simpleTaskDate.value = it },
            isDate = true
        )

        DateTimeSelector(
            modifier = Modifier.width(getScreenWidth() * 0.4f),
            label = "Time",
            dateTime = viewModel.simpleTaskTime.value,
            onDateTimeSelect = { viewModel.simpleTaskTime.value = it },
            isDate = false
        )
    }

    PriorityCards(
        selectedPriority = viewModel.simpleTaskPriority.value,
        onPrioritySelected = {
            viewModel.simpleTaskPriority.value = it
        }
    )
}

@Composable
fun RecurringTask(viewModel: TaskViewModel) {
    OutlinedTextField(
        value = viewModel.recurringTaskName.value,
        onValueChange = { viewModel.recurringTaskName.value = it },
        label = {
            Text(
                "Task Name",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(20.dp),
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DateTimeSelector(
            modifier = Modifier.width(getScreenWidth() * 0.45f),
            label = "Start Date",
            dateTime = viewModel.recurringTaskStartDate.value,
            onDateTimeSelect = { viewModel.recurringTaskStartDate.value = it },
            isDate = true
        )

        DateTimeSelector(
            modifier = Modifier.width(getScreenWidth() * 0.45f),
            label = "End Date",
            dateTime = viewModel.recurringTaskEndDate.value,
            onDateTimeSelect = { viewModel.recurringTaskEndDate.value = it },
            isDate = true
        )
    }

    DateTimeSelector(
        modifier = Modifier.width(getScreenWidth() * 0.3f),
        label = "Time",
        dateTime = viewModel.recurringTaskTime.value,
        onDateTimeSelect = { viewModel.recurringTaskTime.value = it },
        isDate = false
    )

    PriorityCards(
        selectedPriority = viewModel.recurringTaskPriority.value,
        onPrioritySelected = {
            viewModel.recurringTaskPriority.value = it
        }
    )
}

@Composable
fun DateTimeSelector(
    modifier: Modifier = Modifier,
    label: String,
    dateTime: String,
    onDateTimeSelect: (String) -> Unit,
    isDate: Boolean = true,
    dateFormat: String = "dd MMM, yyyy",
    minDate: Long = System.currentTimeMillis()
) {
    var showPicker by remember { mutableStateOf(false) }

    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        OutlinedButton(
            onClick = { showPicker = true },
            modifier = modifier,
            contentPadding = PaddingValues(end = 5.dp, start = 10.dp)
        ) {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    dateTime,
                    style = MaterialTheme.typography.headlineSmall
                )

                Button(
                    onClick = { showPicker = true },
                    contentPadding = PaddingValues(10.dp)
                ) {
                    if (isDate) {
                        Image(
                            painter = painterResource(R.drawable.icon_calender),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.icon_time),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }

    if (showPicker) {
        if (isDate) {
            DatePicker(
                onDateChanged = {
                    onDateTimeSelect(it)
                    showPicker = false
                },
                dateFormat = dateFormat,
                minDate = minDate,
                onCancel = { showPicker = false }
            )
        } else {
            TimePicker(
                onTimeSelected = { hour, minute ->
                    onDateTimeSelect("$hour:$minute")
                    showPicker = false
                },
                onDismiss = { showPicker = false }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PriorityCards(
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    var selected by remember { mutableStateOf(selectedPriority) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Priority",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Priority.entries.forEach { priority ->
                Card(
                    onClick = {
                        selected = priority
                        onPrioritySelected(priority)
                    },
                    colors = CardDefaults.cardColors(
                        containerColor = getColorBasedOnPriority(priority).copy(alpha = if (selected == priority) 1f else 0.4f),
                    ),
                    shape = CircleShape
                ) {
                    Text(
                        text = priority.toString(),
                        color = Color.Black,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    }
}
