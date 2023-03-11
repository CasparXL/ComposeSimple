package com.caspar.cpdemo.di

import com.caspar.cpdemo.network.*
import com.caspar.cpdemo.utils.log.LogUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.gson.*

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun ktorInit(): HttpClient {
        return HttpClient(Android) {
            //Gson解析
            install(ContentNegotiation){
                gson {
                    /*setPrettyPrinting()
                    setLenient()*/
                }
            }
            //默认请求参数
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = BASE_URL
                    //port = 你的端口
                }
            }
            //用于判断网络请求建立成功以后的逻辑
            HttpResponseValidator {
                this.validateResponse {
                    //网络请求建立成功以后,返回的返回体
                    when (it.status.value) {
                        401 -> {

                        }
                    }
                }
                this.handleResponseExceptionWithRequest { cause, request ->
                    LogUtil.d("是否有异常->${cause},req->${request.url.host}")
                }
            }
            //基础配置(超时相关)
            engine {
                socketTimeout = 100_000
                connectTimeout = 100_000
            }
            //日志打印
            install(Logging) {
                logger = MessageLengthLimitingLogger(delegate = object : Logger {
                    override fun log(message: String) {
                        LogUtil.d(message)
                    }
                })
                level = LogLevel.ALL
            }
            //用户Token处理相关
            install(Auth){
                this.bearer {
                    //正常加载的token
                    loadTokens {
                        BearerTokens("initial_access_token", "initial_refresh_token")
                    }
                    //动态刷新token的方式
                    /*refreshTokens {
                        val response = client.get("https://example.com/get_token")
                        if (response.status == HttpStatusCode.Unauthorized) {
                            // logout
                            null
                        } else {
                            // get token from a response
                            BearerTokens("new_access_token", "new_refresh_token")
                        }
                    }*/
                }
            }

        }
    }


}