package com.caspar.cpdemo.ui.page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OtherPage() {
    val list = remember {
        mutableStateListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
    }
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
        LazyVerticalGrid(
            modifier = Modifier.background(Color.White),
            columns = GridCells.Fixed(2)
        ) {
            itemsIndexed(list) { index, data->
                Text(
                    text = "$data",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .combinedClickable(onLongClick = {
                            list[index] = "长按"
                        },
                        onClick = {
                            list[index] = "点击"
                        },
                        onDoubleClick = {
                            list[index] = "双击"
                        })
                        .padding(20.dp)
                )
            }
        }
    }
}
