package com.health.healthmonitorwearapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import com.health.healthmonitorwearapp.services.DataSyncManager
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator

@Composable
fun PairingScreen(onSuccess: () -> Unit) {
    var token by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scalingListState = rememberScalingLazyListState()

    ScalingLazyColumn(
        state = scalingListState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                "🔗 Pair with your account",
                color = Color.White,
                fontSize = 16.sp
            )
        }

        item {
            BasicTextField(
                value = token,
                onValueChange = { token = it.trim() },
                singleLine = true,
                textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(44.dp)
                            .background(Color.DarkGray, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 12.dp)
                    ) {
                        if (token.isBlank()) {
                            Text("Enter token", color = Color.Gray, fontSize = 14.sp)
                        }
                        innerTextField()
                    }
                }
            )
        }

        item {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        isLoading = true
                        DataSyncManager.linkWearDevice(
                            token,
                            context,
                            onSuccess = {
                                isLoading = false
                                onSuccess()
                            },
                            onError = {
                                errorMessage = it
                                isLoading = false
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Link")
                }
            }
        }

        item {
            errorMessage?.let {
                Text(it, color = Color.Red, fontSize = 12.sp)
            }
        }
    }
}
