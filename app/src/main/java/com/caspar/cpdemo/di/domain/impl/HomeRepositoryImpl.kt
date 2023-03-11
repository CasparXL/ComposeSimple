package com.caspar.cpdemo.di.domain.impl

import com.caspar.cpdemo.bean.ArticleInfoBean
import com.caspar.cpdemo.bean.BaseBean
import com.caspar.cpdemo.bean.FishPond
import com.caspar.cpdemo.di.domain.HomeRepository
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
            api.get("/ct/moyu/list/recommend/${page}").body()
        }
    }

    override suspend fun loadTopicList(): Result<List<FishPond>> {
        return ktorResult {
            api.get("/ct/moyu/topic").body<BaseBean<List<FishPond>>>().data?: listOf()
        }
    }
}