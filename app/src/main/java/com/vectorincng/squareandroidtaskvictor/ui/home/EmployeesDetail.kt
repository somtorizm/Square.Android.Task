package com.vectorincng.squareandroidtaskvictor.ui.home

import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.vectorincng.squareandroidtaskvictor.network.EmployeeFetcher


@Composable
fun EmployeesDetailsListItem(employees: EmployeeFetcher.EmployeeDataResponse.Employee) {
    Row(modifier = Modifier.padding(vertical = 10.dp)) {
        EmployeesImage(imageUrl = employees.imageUrl,
            imageHeight = 100.dp,
            modifier = Modifier)

        Spacer(modifier = Modifier.width(10.dp))

       Column(modifier = Modifier.weight(1f)) {
           Text(text = employees.name, style = MaterialTheme.typography.bodyMedium)
           Spacer(modifier = Modifier.height(10.dp))
           Text(text = employees.biography, style = MaterialTheme.typography.bodySmall)
           Spacer(modifier = Modifier.height(10.dp))
           Text(text = "Team: ${employees.team}", style = MaterialTheme.typography.labelSmall)
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
            .width(100.dp)
            .height(imageHeight)
            .clip(CircleShape)
    ) {
        if (isLoading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(placeholderColor)
                    .clip(CircleShape)
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


