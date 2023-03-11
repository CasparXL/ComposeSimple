package com.caspar.cpdemo.bean

import kotlin.collections.List

data class ArticleInfoBean(
    val code: Int? = null,
    val `data`: Data? = null,
    val message: String? = null,
    val success: Boolean? = null
)

data class Data(
    val currentPage: Int? = null,
    val hasNext: Boolean? = null,
    val hasPre: Boolean? = null,
    val list: List<InfoList>? = null,
    val pageSize: Int? = null,
    val total: Int? = null,
    val totalPage: Int? = null
)

data class InfoList(
    val avatar: String? = null,
    val commentCount: Int? = null,
    val company: String? = null,
    val content: String? = null,
    val createTime: String? = null,
    val hasThumbUp: Boolean? = null,
    val id: String? = null,
    val images: List<String>? = null,
    val linkCover: Any? = null,
    val linkTitle: Any? = null,
    val linkUrl: Any? = null,
    val nickname: String? = null,
    val position: String? = null,
    val thumbUpCount: Int? = null,
    val thumbUpList: List<String>? = null,
    val topicId: String? = null,
    val topicName: String? = null,
    val userId: String? = null,
    val vip: Boolean? = null
)