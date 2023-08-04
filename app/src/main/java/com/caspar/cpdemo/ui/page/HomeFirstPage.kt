package com.caspar.cpdemo.ui.page

import android.text.Html
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextField
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import coil.compose.rememberAsyncImagePainter
import com.caspar.cpdemo.R
import com.caspar.cpdemo.bean.InfoList
import com.caspar.cpdemo.ext.getLocalDataTime
import com.caspar.cpdemo.ext.timeFormatMillis
import com.caspar.cpdemo.utils.log.LogUtil
import com.caspar.cpdemo.viewmodel.homepage.HomeViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * 首页第一个界面[ProjectScreen.HOME_FISH_POND]
 */
@Composable
fun FishScreen(viewModel: HomeViewModel) {
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
        TopicList()
        FishList(viewModel)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun FishList(viewModel: HomeViewModel) {
    val list = viewModel.list.collectAsLazyPagingItems()
    val pullRefresh = rememberPullRefreshState(
        refreshing = list.loadState.refresh is LoadState.Loading,
        onRefresh = {
            viewModel.getList()
            list.refresh()
        })
    Box(
        modifier = Modifier
            .pullRefresh(pullRefresh)
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            itemsIndexed(list) { index, article ->
                Spacer(
                    modifier = Modifier
                        .background(Color.Gray.copy(alpha = 0.5F))
                        .height(5.dp)
                        .clickable {
                            viewModel.updateIndex(article?.id ?: "")
                        }
                        .fillMaxWidth()
                )
                ArticleList(article)
            }
            item {
                when (list.loadState.append) {
                    is LoadState.Error -> {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "加载失败,点击重新加载",
                                Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp)
                                    .clickable {
                                        list.retry()
                                    },
                                textAlign = TextAlign.Center,
                            )
                        }
                    }

                    LoadState.Loading -> {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "加载中",
                                Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }

                    is LoadState.NotLoading -> {
                        if (list.loadState.append.endOfPaginationReached) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "已经到底了，没有数据可以加载了",
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(15.dp),
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                }
            }
        }
        PullRefreshIndicator(
            modifier = Modifier
                .padding(15.dp)
                .size(30.dp)
                .align(Alignment.TopCenter),
            state = pullRefresh,
            refreshing = list.loadState.refresh is LoadState.Loading,
            contentColor = Color(0xFFFF6500),
            backgroundColor = Color.White
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ArticleList(article: InfoList?) {
    val s = rememberScrollState()
    Column(
        Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Row {
            Image(
                painter = rememberAsyncImagePainter(
                    model = article?.avatar ?: "",
                    placeholder = painterResource(id = R.drawable.image_loading_ic),
                    error = painterResource(id = R.drawable.image_loading_ic),
                ),
                contentDescription = "用户头像",
                modifier = Modifier
                    .padding(end = 15.dp)
                    .size(size = 40.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Red, CircleShape)
                    .clipToBounds()
            )
            Column {
                Text(text = article?.nickname ?: "", fontSize = 18.sp)
                val time =
                    article?.createTime.timeFormatMillis("yyyy-MM-dd HH:mm").getLocalDataTime()
                Text(
                    text = (article?.position ?: "游民").plus("·").plus(time.dayOfWeek.name)
                        .plus((" " + article?.createTime)),
                    fontSize = 10.sp,
                    color = colorResource(id = R.color.gray)
                )
            }
        }
        Text(
            text = Html.fromHtml(article?.content ?: "").toString(),
            modifier = Modifier.padding(15.dp),
            fontSize = 12.sp
        )
        val images = article?.images
        if (!images.isNullOrEmpty()) {
            val imageHeight = 120.dp
            //设置图片高度
            val height = when (images.size) {
                in 1..3 -> {
                    1
                }

                in 4..6 -> {
                    2
                }

                in 7..9 -> {
                    3
                }

                else -> {
                    1
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(if (images.size < 3) images.size else 3),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight.times(height))
                    .clip(RoundedCornerShape(15.dp))
                    .border(15.dp, Color.Transparent, RoundedCornerShape(15.dp))
                    .scrollable(s, Orientation.Horizontal, false)
                    .scrollable(s, Orientation.Vertical, false)
            ) {
                items(images.size) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = images[it] ?: "",
                            placeholder = painterResource(id = R.drawable.image_loading_ic),
                            error = painterResource(id = R.drawable.image_loading_ic),
                        ),
                        modifier = Modifier
                            .background(Color.Black)
                            .height(imageHeight)
                            .scrollable(s, Orientation.Horizontal, false)
                            .scrollable(s, Orientation.Vertical, false),
                        contentDescription = "用户头像",
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }
        Row(Modifier.padding(top = 15.dp)) {
            Text(
                text = "分享",
                modifier = Modifier
                    .weight(1F)
                    .fillMaxWidth()
                    .clickable(
                        indication = null,
                        interactionSource = remember {
                            MutableInteractionSource()
                        }
                    ) {
                        //LocalSoftwareKeyboardController.current 可以隐藏键盘，但必须当前界面有输入框，否则无效
                    },
                textAlign = TextAlign.Center
            )
            Text(
                text = "评论",
                modifier = Modifier
                    .weight(1F)
                    .fillMaxWidth()
                    .clickable {

                    },
                textAlign = TextAlign.Center
            )
            Text(
                text = "点赞",
                modifier = Modifier
                    .weight(1F)
                    .fillMaxWidth()
                    .clickable {

                    },
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TopicList(viewModel: HomeViewModel = hiltViewModel()) {
    val topicList = viewModel.topList.collectAsStateWithLifecycle()
    LazyRow {
        items(topicList.value.size, key = { topicList.value[it].id ?: "" }) {
            Column {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = topicList.value[it].cover,
                        placeholder = painterResource(id = R.drawable.image_loading_ic),
                    ),
                    contentDescription = "",
                    modifier = Modifier
                        .size(size = 45.dp)
                        .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                        .align(Alignment.CenterHorizontally),
                )
                Text(
                    text = topicList.value[it].topicName ?: "",
                    modifier = Modifier
                        .padding(vertical = 10.dp, horizontal = 15.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.getList()
    }
}

@Preview(showBackground = true)
@Composable
fun TestAir() {
    ArticleList(
        InfoList(
            avatar = "https://cdn.sunofbeaches.com/emoji/7.png",
            nickname = "test",
            createTime = "2023-02-09 09:59:00"
        )
    )
}