package com.caspar.cpdemo.bean.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.caspar.cpdemo.bean.ArticleInfo
import com.caspar.cpdemo.di.domain.HomeRepository

class ArticleInfoPagingSource(private val repository: HomeRepository) :
    PagingSource<Int, ArticleInfo>() {
    override fun getRefreshKey(state: PagingState<Int, ArticleInfo>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleInfo> {
        val nextPage = params.key ?: 1
        val response = repository.getRecommendContent(nextPage)
        return response.fold(onSuccess = {
            LoadResult.Page(
                data = it.list ?: arrayListOf(),
                prevKey = if (it.hasPre == true) nextPage - 1 else null,
                nextKey = if (it.hasNext == true) nextPage + 1 else null
            )
        }, onFailure = {
            LoadResult.Error(it)
        })
    }
}