package com.vectorincng.squareandroidtaskvictor.employeesdetails

import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.vectorincng.squareandroidtaskvictor.data.EmployeesResponse

@Composable
fun EmployeesDetailsScreen() {

}



@Composable
fun EmployeesDetailsContent() {

}


@Composable
fun EmployeesDetailsListItem(employeesResponse: EmployeesResponse,) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        EmployeesImage(imageUrl = employeesResponse.imageUrl,
            imageHeight = 100.dp,
            modifier = Modifier)

        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = employeesResponse.name, style = MaterialTheme.typography.headlineMedium)
            Text(text = employeesResponse.biography, style = MaterialTheme.typography.bodyMedium)
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun EmployeesImage(
    imageUrl: String,
    imageHeight: Dp,
    modifier: Modifier = Modifier,
    placeholderColor: Color = MaterialTheme.colorScheme.onSurface.copy(0.2f)
) {
    var isLoading by remember { mutableStateOf(true) }
    Box(
        modifier
            .fillMaxWidth()
            .height(imageHeight)
    ) {
        if (isLoading) {
            // TODO: Update this implementation once Glide releases a version
            // that contains this feature: https://github.com/bumptech/glide/pull/4934
            Box(
                Modifier
                    .fillMaxSize()
                    .background(placeholderColor)
            )
        }
        GlideImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
        ) {
            it.addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean,
                ): Boolean {
                    isLoading = false
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean,
                ): Boolean {
                    isLoading = false
                    return false
                }
            })
        }
    }
}


