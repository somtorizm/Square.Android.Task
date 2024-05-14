package com.vectorincng.squareandroidtaskvictor.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vectorincng.squareandroidtaskvictor.R
import com.vectorincng.squareandroidtaskvictor.ui.theme.SquareAndroidTaskVictorTheme
import com.vectorincng.squareandroidtaskvictor.viewmodels.HomeScreenUiState
import com.vectorincng.squareandroidtaskvictor.viewmodels.HomeViewModel

@Composable
fun HomeScreenReady(modifier: Modifier, viewModel: HomeViewModel = hiltViewModel()) {

    val homeScreenUiState by viewModel.state.collectAsStateWithLifecycle()
    when(val uiState = homeScreenUiState) {
        is HomeScreenUiState.Error -> HomeScreenError({
            viewModel.refresh(true)
        })
        HomeScreenUiState.Loading -> HomeScreenLoading()

        is HomeScreenUiState.Ready -> {
            Surface(Modifier.padding(10.dp)) {
                if (uiState.featuredEmployees.isNotEmpty()) {
                    LazyColumn {
                        items(uiState.featuredEmployees.size) { item ->
                            EmployeesDetailsListItem(uiState.featuredEmployees[item])
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeScreenLoading(modifier: Modifier = Modifier) {
    Surface(modifier.fillMaxSize()) {
        Box {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun HomeScreenError(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Surface(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(
                text = stringResource(id = R.string.an_error_has_occurred),
                modifier = Modifier.padding(16.dp)
            )
            Button(onClick = onRetry) {
                Text(text = stringResource(id = R.string.retry_label))
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenErrorPreview() {
    SquareAndroidTaskVictorTheme {
        HomeScreenError(onRetry = {})
    }
}
