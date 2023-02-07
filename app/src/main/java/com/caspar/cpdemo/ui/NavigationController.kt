package com.caspar.cpdemo.ui

import androidx.navigation.NavController


/**
 * App的界面
 */
object ProjectScreen {
    const val SPLASH = "splash"
    const val MAIN = "main"
}

/**
 * 跳转新界面,并从中移除旧的堆栈
 * @param startPage 要跳转的界面
 * @param finishPages 要关闭的旧界面
 */
fun NavController.navigationAndFinish(startPage: String, vararg finishPages: String){
    graph.remove(graph)
    popBackStack()
    navigate(startPage)
    backQueue.removeIf { finishPages.contains(it.destination.route) }
}

