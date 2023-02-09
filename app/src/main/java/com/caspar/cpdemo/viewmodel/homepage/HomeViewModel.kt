package com.caspar.cpdemo.viewmodel.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.caspar.cpdemo.bean.ArticleInfo
import com.caspar.cpdemo.bean.FishPondTopicList
import com.caspar.cpdemo.bean.paging.ArticleInfoPagingSource
import com.caspar.cpdemo.di.domain.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {
    val list = Pager(PagingConfig(pageSize = 20)) {
        ArticleInfoPagingSource(repository)
    }.flow.cachedIn(viewModelScope)

    val topList = MutableStateFlow<List<FishPondTopicList>>(listOf())

    fun getList() {
        viewModelScope.launch {
            repository.loadTopicList().onSuccess {
                topList.emit(it)
            }
        }
    }

}