package com.caspar.cpdemo.ui.page

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.caspar.cpdemo.R
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        FishList()
    }
}

@Composable
private fun FishList() {
    val listState = rememberLazyListState()
    val swipeRefreshState = rememberSwipeRefreshState(false)
    val scope = rememberCoroutineScope()
    val currentPosition = remember { derivedStateOf { listState.firstVisibleItemIndex } }.value
    val pageSize = remember { derivedStateOf { listState.layoutInfo } }.value.visibleItemsInfo.size
    val pageCount = remember { derivedStateOf { listState.layoutInfo } }.value.totalItemsCount
    Log.e("浪", "当前下标: ${currentPosition},一页能展示多少条数据:${pageSize},总数据:${pageCount}", )
    if (currentPosition + pageSize == pageCount && !listState.isScrollInProgress && pageCount!=0){
        Log.e("浪", "滚动到底部了")
    }
    SwipeRefresh(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        state = swipeRefreshState,
        onRefresh = {
            scope.launch {
                swipeRefreshState.isRefreshing = true
                delay(2000)
                swipeRefreshState.isRefreshing = false
            }
        },
        indicator = { state, refreshTrigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = refreshTrigger,
                backgroundColor = Color.Green,
                contentColor = Color.DarkGray
            )
        },
    ) {
        Column {
            Text(
                text = "推荐话题",
                modifier = Modifier
                    .padding(15.dp),
                textAlign = TextAlign.Center,
            )
            Column {
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
                LazyColumn(state = listState) {
                    items(100, key = { it }) {
                        Text(
                            text = "鱼塘${it + 1}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(vertical = 10.dp, horizontal = 15.dp),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}