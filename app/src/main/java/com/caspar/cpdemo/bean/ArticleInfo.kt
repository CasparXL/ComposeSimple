package com.caspar.cpdemo.bean

/**
 * 首页帖子列表
 */
data class ArticleInfo(
    val avatar: String? = null,
    val commentCount: Int? = null,
    val company: String? = null,
    val content: String? = null,
    val createTime: String? = null,
    val hasThumbUp: Boolean? = null,
    val id: String? = null,
    val images: List<String?>? = null,
    val linkCover: String? = null,
    val linkTitle: String? = null,
    val linkUrl: String? = null,
    val nickname: String? = null,
    val position: String? = null,
    val thumbUpCount: Int? = null,
    val thumbUpList: List<String?>? = null,
    val topicId: String? = null,
    val topicName: Any? = null,
    val userId: String? = null,
    val vip: Boolean? = null
)