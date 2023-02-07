package com.caspar.cpdemo.ui.page

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.caspar.cpdemo.ui.theme.ComposeDemoTheme

@Composable
fun Greeting(
    name: String,
) {
    Text(text = name, modifier = Modifier
        .padding(top = 20.dp)
        .fillMaxSize())
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeDemoTheme {
        Greeting("Android")
    }
}