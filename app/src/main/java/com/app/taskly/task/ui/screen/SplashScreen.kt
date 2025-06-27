package com.app.taskly.task.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.taskly.R
import com.app.taskly.task.di.app
import com.app.taskly.task.ui.theme.hostFontFamilyBold
import com.app.taskly.utils.LottieLoader
import com.app.taskly.utils.getScreenWidth
import kotlin.coroutines.coroutineContext

@Composable
fun SplashScreen(onClick: () -> Unit){
    Column(
        modifier = Modifier.fillMaxSize().background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        LottieLoader(
            isPlaying = true,
            rawFile = R.raw.splash_animation,
            modifier = Modifier.size(getScreenWidth() * 1f).padding(horizontal = 20.dp)
        )
        Box (
            modifier = Modifier
                .fillMaxSize()
                .clip(
                    RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                )
                .background(color = MaterialTheme.colorScheme.primary),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ){
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Manage",
                        style = MaterialTheme.typography.displayLarge.copy(fontFamily = hostFontFamilyBold, fontSize = 50.sp)
                    )
                    Text(
                        text = "Your",
                        style = MaterialTheme.typography.displayLarge.copy(fontFamily = hostFontFamilyBold, fontSize = 50.sp)
                    )
                    Text(
                        text = "Tasks",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontFamily = hostFontFamilyBold,
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 50.sp
                        ),
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Get Started",
                        style = MaterialTheme.typography.displayLarge.copy(fontFamily = hostFontFamilyBold)
                    )
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(color = MaterialTheme.colorScheme.tertiary)
                            .clickable {
                                onClick()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }
    }
}