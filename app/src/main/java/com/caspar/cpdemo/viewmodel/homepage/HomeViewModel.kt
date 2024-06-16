package com.caspar.cpdemo.viewmodel.homepage

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.caspar.cpdemo.bean.FishPond
import com.caspar.cpdemo.bean.paging.ArticleInfoPagingSource
import com.caspar.cpdemo.di.domain.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {
    val snackBarHost = SnackbarHostState()
    val topList = MutableStateFlow<List<FishPond>>(listOf())

    //点击选中条目
    private val infoIndex = MutableStateFlow(listOf(""))

    //使用combine对Flow进行链接，其中任意Flow发生改变，会刷新整个与Flow相关的所有UI
    val list = Pager(PagingConfig(pageSize = 20)) {
        ArticleInfoPagingSource(repository)
    }.flow.cachedIn(viewModelScope).combine(infoIndex) { list, changedItem ->
        list.map { ar ->
            if (changedItem.contains(ar.id)) {
                ar.copy(nickname = "测试")
            } else {
                ar
            }
        }
    }

    /**
     * 更新数据源,由于关联了page相关，所以UI也会重新更改
     */
    fun updateIndex(index: String) {
        viewModelScope.launch {
            if (!infoIndex.value.contains(index)) {
                infoIndex.emit(infoIndex.value.toMutableList().apply {
                    add(index)
                })
            } else {
                snackBarHost.showSnackbar(message = "请勿重新点击")
            }
        }
    }

    fun getList() {
        viewModelScope.launch {
            repository.loadTopicList().onSuccess {
                topList.emit(it)
            }
        }
    }

}