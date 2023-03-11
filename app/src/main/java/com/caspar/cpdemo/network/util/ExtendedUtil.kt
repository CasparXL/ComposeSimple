package com.caspar.cpdemo.network.util


import com.caspar.cpdemo.utils.log.LogUtil
import com.google.gson.JsonParseException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException
import java.text.ParseException

/**
 * 默认协程全部拦截,使用方式launch(DefaultErrorHandelCoroutine),
 * 当launch代码块内部出现异常,会中断后续代码块,届时，请在launch的invokeOnCompletion代码块中读取该异常并做相应处理
 */
val DefaultErrorHandelCoroutine = CoroutineExceptionHandler { _, _ -> }

/**
 * 解析数据异常
 */
private fun exportError(throwable: Throwable): Pair<Int, String> {
    LogUtil.e(throwable)
    return when (throwable) {
        is ConnectException, is UnknownHostException -> -1 to NetException.CONNECT_ERROR
        is InterruptedIOException -> -1 to NetException.CONNECT_TIMEOUT
        is JsonParseException, is JSONException, is ParseException -> -1 to NetException.PARSE_ERROR
        else -> -1 to NetException.UNKNOWN_ERROR
    }
}

/**
 * Ktor解析类型
 */
suspend fun <T> ktorResult(block: suspend () -> T): Result<T> {
    return withContext(Dispatchers.IO) {
        try {
            return@withContext Result.success(block.invoke())
        } catch (e: Exception) {
            e.printStackTrace()
            val exportError = exportError(e)
            return@withContext Result.failure(Exception(exportError.second))
        }
    }
}



