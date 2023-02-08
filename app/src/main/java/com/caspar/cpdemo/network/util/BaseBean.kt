package com.caspar.cpdemo.network.util

class BaseBean<T>(
    val data: T? = null,
    val code: Int? = 0,
    val success: Boolean? = null,
    val message: String? = null,
) {
    fun getResult(): Result<T> {
        return if (success == true) {
            Result.success(data!!)
        } else {
            Result.failure(Exception("$message:$code"))
        }
    }
}
class BasePageBean<T>(
    val data: PageBean<T>? = null,
    val code: Int? = 0,
    val success: Boolean? = null,
    val message: String? = null,
) {
    fun getResult(): Result<PageBean<T>> {
        return if (success == true) {
            Result.success(data!!)
        } else {
            Result.failure(Exception("$message:$code"))
        }
    }
}
data class PageBean<T>(
    val list: T? = null,
    val currentPage: Int? = null,
    val hasNext: Boolean? = null,
    val hasPre: Boolean? = null,
    val pageSize: Int? = null,
    val total: Int? = null,
    val totalPage: Int? = null
)
