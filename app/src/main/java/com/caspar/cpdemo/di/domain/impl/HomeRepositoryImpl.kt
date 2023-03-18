package com.caspar.cpdemo.di.domain.impl

import com.caspar.cpdemo.bean.ArticleInfoBean
import com.caspar.cpdemo.bean.BaseBean
import com.caspar.cpdemo.bean.FishPond
import com.caspar.cpdemo.di.domain.HomeRepository
import com.caspar.cpdemo.network.RequestRecommendContent
import com.caspar.cpdemo.network.RequestTopic
import com.caspar.cpdemo.network.util.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val api: HttpClient
) : HomeRepository {
    override suspend fun getRecommendContent(page: Int): Result<ArticleInfoBean> {
        return ktorResult {
            api.get(RequestRecommendContent.plus(page)).body()
        }
    }

    override suspend fun loadTopicList(): Result<List<FishPond>> {
        return ktorResult {
            api.get(RequestTopic).body<BaseBean<List<FishPond>>>().data ?: listOf()
        }
    }
}