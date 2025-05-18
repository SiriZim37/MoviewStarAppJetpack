package com.siri.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DefaultErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "Error Icon",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Button(onClick = onRetry) {
                Text(text = "Retry")
            }
        }
    }
}

//@Composable
//fun DefaultErrorView(
//    message: String = "ไม่พบข้อมูล",
//    onRetry: () -> Unit = {}
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(24.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Icon(
//            imageVector = Icons.Filled.CloudOff, // ใช้ไอคอนที่มีใน material-icons-extended
//            contentDescription = null,
//            tint = Color.Gray,
//            modifier = Modifier.size(72.dp)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text(
//            text = message,
//            color = Color.White,
//            style = MaterialTheme.typography.bodyLarge
//        )
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        Button(onClick = onRetry) {
//            Text("ลองใหม่อีกครั้ง")
//        }
//    }
//}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewDefaultErrorView() {
    DefaultErrorView(
        message = "ไม่สามารถโหลดข้อมูลได้",
        onRetry = {}
    )
}
