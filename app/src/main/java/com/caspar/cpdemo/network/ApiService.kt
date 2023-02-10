package com.caspar.cpdemo.network

import com.caspar.cpdemo.bean.ArticleInfo
import com.caspar.cpdemo.bean.FishPondTopicList
import com.caspar.cpdemo.network.interceptor.Authenticated
import com.caspar.cpdemo.network.util.BaseBean
import com.caspar.cpdemo.network.util.BasePageBean
import retrofit2.http.*

/**
 * "CasparXL" 创建 2019/7/1.
 * 界面名称以及功能:Http的接口
 */
interface ApiService {

    /**
     * 获取推荐内容
     */
    @GET("ct/moyu/list/recommend/{page}")
    @Authenticated
    suspend fun getRecommendContent(@Path("page") page: Int): BasePageBean<List<ArticleInfo>>

    /**
     * 获取话题列表
     */
    @GET("ct/moyu/topic")
    suspend fun loadTopicList(): BaseBean<List<FishPondTopicList>>
}
