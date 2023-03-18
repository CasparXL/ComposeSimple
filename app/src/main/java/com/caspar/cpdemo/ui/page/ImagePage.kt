package com.caspar.cpdemo.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import com.caspar.cpdemo.R

@Composable
fun ImagePage(string: String) {
    Box(Modifier.fillMaxSize()) {
        Image(
            painter = rememberAsyncImagePainter(
                model = string,
                placeholder = painterResource(id = R.drawable.image_loading_ic),
                error = painterResource(id = R.drawable.image_loading_ic),
            ), contentDescription = ""
        )
    }
}