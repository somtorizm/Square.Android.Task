package com.vectorincng.squareandroidtaskvictor.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vectorincng.squareandroidtaskvictor.R
import com.vectorincng.squareandroidtaskvictor.ui.theme.SquareAndroidTaskVictorTheme
import com.vectorincng.squareandroidtaskvictor.viewmodels.HomeScreenUiState
import com.vectorincng.squareandroidtaskvictor.viewmodels.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenReady(modifier: Modifier, viewModel: HomeViewModel = hiltViewModel()) {

    val homeScreenUiState by viewModel.state.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState()
    val pullToRefreshState = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    if (pullToRefreshState.isRefreshing) {
        viewModel.refresh(true)
    }

    when(val uiState = homeScreenUiState) {
        is HomeScreenUiState.Error -> {
            HomeScreenError({
                viewModel.refresh(true)
            }, errorMessage = uiState.errorMessage.toString())

            pullToRefreshState.endRefresh()
        }
        HomeScreenUiState.Loading -> HomeScreenLoading()

        is HomeScreenUiState.Ready -> {
            pullToRefreshState.endRefresh()

            Surface(modifier.padding(10.dp)) {
                Box(
                    modifier = Modifier
                        .nestedScroll(pullToRefreshState.nestedScrollConnection)
                ) {
                    Scaffold(
                        floatingActionButton = {
                            ExtendedFloatingActionButton(
                                text = { Text("Sort list") },
                                icon = { Icon(Icons.Filled.List, contentDescription = "") },
                                onClick = {
                                    showBottomSheet = true
                                }
                            )
                        },
                        content = { innerPadding ->
                            if (showBottomSheet) {
                                ModalBottomSheet(
                                    onDismissRequest = {
                                        showBottomSheet = false
                                    },
                                    sheetState = sheetState
                                ) {
                                    BottomSheetDialog(onClick = {
                                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                                            if (!sheetState.isVisible) {
                                                showBottomSheet = false
                                            }
                                        }
                                    })
                                }
                            }

                            LazyColumn(modifier.padding(innerPadding)) {
                                items(uiState.featuredEmployees.size) { item ->
                                    key(uiState.featuredEmployees[item].id) {
                                        EmployeesDetailsListItem(uiState.featuredEmployees[item])
                                    }
                                }
                            }
                        })

                    PullToRefreshContainer(
                        modifier = Modifier.align(Alignment.TopCenter),
                        state = pullToRefreshState
                    )
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
private fun HomeScreenError(onRetry: () -> Unit, modifier: Modifier = Modifier, errorMessage: String) {
    Surface(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
        ) {
            Icon(imageVector = Icons.Filled.Info, contentDescription = "Error")

            Text(
                text = stringResource(id = R.string.an_error_has_occurred),
                modifier = Modifier.padding(10.dp)
            )

            Text(
                text = errorMessage,
                modifier = Modifier.padding(10.dp)
            )
            Button(onClick = onRetry) {
                Text(text = stringResource(id = R.string.retry_label))
            }
        }
    }
}

@Composable
fun BottomSheetDialog(onClick: () -> Unit, modifier: Modifier = Modifier, viewModel: HomeViewModel = hiltViewModel()) {
    Surface(modifier = modifier.height(150.dp)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Sort By",
                modifier = Modifier
                    .padding(5.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    viewModel.sortListName("name" )
                    onClick()
                }) {
                    Text(text = "Name")
                }

                Button(onClick = {
                    viewModel.sortListName("team")
                    onClick()
                }) {
                    Text(text = "Team")
                }

                Button(onClick = {
                    viewModel.sortListName(null)
                    onClick()
                }) {
                    Text(text = "Employee Type")
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenErrorPreview() {
    SquareAndroidTaskVictorTheme {
        HomeScreenError(onRetry = {}, errorMessage = "No network")
    }
}
