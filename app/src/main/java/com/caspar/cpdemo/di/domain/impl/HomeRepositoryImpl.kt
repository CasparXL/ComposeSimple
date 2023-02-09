package com.caspar.cpdemo.di.domain.impl

import androidx.paging.PagingSource
import com.caspar.cpdemo.bean.ArticleInfo
import com.caspar.cpdemo.bean.FishPondTopicList
import com.caspar.cpdemo.di.BodyOkHttpClient
import com.caspar.cpdemo.di.domain.HomeRepository
import com.caspar.cpdemo.network.ApiService
import com.caspar.cpdemo.network.util.BasePageBean
import com.caspar.cpdemo.network.util.PageBean
import com.caspar.cpdemo.network.util.basePageResult
import com.caspar.cpdemo.network.util.baseResult
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    @BodyOkHttpClient private val api: ApiService
) : HomeRepository {
    override suspend fun getRecommendContent(page: Int): Result<PageBean<List<ArticleInfo>>> {
        val baseResult = basePageResult {
            api.getRecommendContent(page)
        }
        return baseResult
    }

    override suspend fun loadTopicList(): Result<List<FishPondTopicList>> {
        val baseResult = baseResult {
            api.loadTopicList()
        }
        return baseResult.fold(onSuccess = {
            Result.success(it)
        }, onFailure = {
            Result.failure(it)
        })
    }
}