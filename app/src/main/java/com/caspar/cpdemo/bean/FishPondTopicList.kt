package com.caspar.cpdemo.bean

/**
 * 顶部推荐话题列表
 */
data class FishPondTopicList(
    val contentCount: Int? = null,
    val cover: String? = null,
    val description: String? = null,
    val followCount: Int? = null,
    val hasFollowed: Boolean? = null,
    val id: String? = null,
    val topicName: String? = null
)