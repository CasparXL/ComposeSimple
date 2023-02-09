package com.caspar.cpdemo.di.domain

import com.caspar.cpdemo.bean.ArticleInfo
import com.caspar.cpdemo.bean.FishPondTopicList
import com.caspar.cpdemo.network.util.BasePageBean
import com.caspar.cpdemo.network.util.PageBean

/**
 * 用户相关请求仓库
 */
interface HomeRepository {
    /**
     * 获取列表数据
     */
    suspend fun getRecommendContent(page: Int): Result<PageBean<List<ArticleInfo>>>

    /**
     * 获取话题列表
     */
    suspend fun loadTopicList(): Result<List<FishPondTopicList>>
}