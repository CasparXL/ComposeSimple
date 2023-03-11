package com.caspar.cpdemo.bean

/**
 * 顶部推荐话题列表
 */
data class BaseBean<T>(
    val code: Int? = null,
    val `data`: T? = null,
    val message: String? = null,
    val success: Boolean? = null
)

data class FishPond(
    val contentCount: Int? = null,
    val cover: String? = null,
    val description: String? = null,
    val followCount: Int? = null,
    val hasFollowed: Boolean? = null,
    val id: String? = null,
    val topicName: String? = null
)