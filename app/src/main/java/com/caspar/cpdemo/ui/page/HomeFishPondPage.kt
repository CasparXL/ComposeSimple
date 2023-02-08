package com.caspar.cpdemo.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.caspar.cpdemo.R

/**
 * 首页第一个界面[ProjectScreen.HOME_FISH_POND]
 */
@Composable
fun FishScreen() {
    Column {
        Text(
            text = "鱼塘",
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
        Text(
            text = "推荐话题",
            modifier = Modifier
                .padding(15.dp),
            textAlign = TextAlign.Center,
        )
        LazyColumn {
            item {
                LazyRow {
                    items(20) {
                        Column {
                            Icon(
                                painter = painterResource(id = R.drawable.home_found_on_ic),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(size = 45.dp)
                                    .align(Alignment.CenterHorizontally),
                            )
                            Text(
                                text = "鱼塘${it}",
                                modifier = Modifier
                                    .padding(vertical = 10.dp, horizontal = 15.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
            items(100) {
                Text(
                    text = "鱼塘${it+1}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 15.dp),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}