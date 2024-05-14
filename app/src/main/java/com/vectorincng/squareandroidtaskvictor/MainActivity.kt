package com.vectorincng.squareandroidtaskvictor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vectorincng.squareandroidtaskvictor.home.HomeScreenReady
import com.vectorincng.squareandroidtaskvictor.ui.theme.SquareAndroidTaskVictorTheme
import com.vectorincng.squareandroidtaskvictor.viewmodels.HomeScreenUiState
import com.vectorincng.squareandroidtaskvictor.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SquareAndroidTaskVictorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, viewModel: HomeViewModel = hiltViewModel(),
) {
    val homeScreenUiState by viewModel.state.collectAsStateWithLifecycle()
    when(val uiState = homeScreenUiState) {
        is HomeScreenUiState.Error -> {

        }
        HomeScreenUiState.Loading -> {

        }
        is HomeScreenUiState.Ready -> {
            HomeScreenReady(uiState)

        }
    }

    val coroutineScope = rememberCoroutineScope()

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SquareAndroidTaskVictorTheme {
        Greeting("Android")
    }
}