package com.vectorincng.squareandroidtaskvictor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.vectorincng.squareandroidtaskvictor.ui.home.HomeScreenReady
import com.vectorincng.squareandroidtaskvictor.ui.theme.SquareAndroidTaskVictorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SquareAndroidTaskVictorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreenReady(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
