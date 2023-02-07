package com.caspar.cpdemo.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


/**
 * 初始化Application
 */
@HiltAndroidApp
class BaseApplication : Application() {

    companion object {
        //Application上下文
        lateinit var context: Application

        //全局使用的协程，因为官方不推荐使用GlobalScope，因此在Application中创建一个全局的协程以便于非Activity，ViewModel的类使用协程
        var job = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

}
