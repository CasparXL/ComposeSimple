package com.caspar.cpdemo.ui.page

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun OtherPage() {
    Column {
        Text(
            text = "扩展页面",
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Spacer(
            modifier = Modifier
                .background(Color.Gray)
                .fillMaxWidth()
                .height(1.dp)
        )
        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(10) {
                Text(text = "${it + 1}", textAlign = TextAlign.Center, modifier = Modifier.clickable {

                }.padding(20.dp))
            }
        }
    }
}