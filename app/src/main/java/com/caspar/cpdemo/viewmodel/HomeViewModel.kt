package com.caspar.cpdemo.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caspar.cpdemo.bean.ArticleInfo
import com.caspar.cpdemo.bean.FishPondTopicList
import com.caspar.cpdemo.di.domain.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    val list = MutableStateFlow<List<ArticleInfo>>(listOf())
    var swipeRefreshState = MutableStateFlow(false)
    val topList = MutableStateFlow<List<FishPondTopicList>>(listOf())

    fun getList() {
        viewModelScope.launch {
            swipeRefreshState.emit(true)
            repository.loadTopicList().onSuccess {
                swipeRefreshState.emit(false)
                topList.emit(it)
            }
        }
    }

    init {
        getList()
    }
}