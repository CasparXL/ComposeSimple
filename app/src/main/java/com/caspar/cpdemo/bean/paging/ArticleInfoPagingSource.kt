package com.caspar.cpdemo.bean.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.caspar.cpdemo.bean.ArticleInfoBean
import com.caspar.cpdemo.bean.InfoList
import com.caspar.cpdemo.di.domain.HomeRepository

class ArticleInfoPagingSource(private val repository: HomeRepository) :
    PagingSource<Int, InfoList>() {
    override fun getRefreshKey(state: PagingState<Int, InfoList>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, InfoList> {
        val nextPage = params.key ?: 1
        val response = repository.getRecommendContent(nextPage)
        return response.fold(onSuccess = {
            LoadResult.Page(
                data = it.data?.list ?: arrayListOf(),
                prevKey = if (it.data?.hasPre == true) nextPage - 1 else null,
                nextKey = if (it.data?.hasNext == true) nextPage + 1 else null
            )
        }, onFailure = {
            LoadResult.Error(it)
        })
    }
}