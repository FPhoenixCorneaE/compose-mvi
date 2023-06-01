package com.fphoenixcorneae.compose.mvi.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fphoenixcorneae.compose.ext.toast
import com.fphoenixcorneae.compose.ext.toastCenter
import com.fphoenixcorneae.compose.https.ApiException
import com.fphoenixcorneae.compose.mvi.DefaultAction
import com.fphoenixcorneae.compose.mvi.MainViewModel
import com.fphoenixcorneae.compose.mvi.UiEffect
import com.fphoenixcorneae.compose.mvi.uiEffect

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
) {
    val uiEffect by uiEffect.collectAsState(UiEffect.ShowLoading())
    DisposableEffect(key1 = Unit) {
        viewModel.dispatchIntent(DefaultAction.Initialize)
        onDispose { }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(
                title = { Text(text = "My App") },
                modifier = Modifier.background(MaterialTheme.colorScheme.onPrimary),
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Menu, "")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Search, "")
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Favorite, "")
                    }
                },
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.White),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { }, modifier = Modifier.width(IntrinsicSize.Max)) {
                    Icon(Icons.Filled.Home, "")
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Filled.Favorite, "")
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Filled.Settings, "")
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.tertiary,
        content = {
            Box(modifier = Modifier.padding(it)) {
                when (uiEffect) {
                    is UiEffect.ShowEmpty -> EmptyScreen((uiEffect as UiEffect.ShowEmpty).message) {
                        viewModel.dispatchIntent(DefaultAction.Refresh)
                    }
                    is UiEffect.ShowError -> ErrorScreen((uiEffect as UiEffect.ShowError).t?.message) {
                        viewModel.dispatchIntent(DefaultAction.Refresh)
                    }
                    is UiEffect.ShowLoading -> LoadingScreen((uiEffect as UiEffect.ShowLoading).message)
                    is UiEffect.ShowNoNetwork -> NoNetworkScreen((uiEffect as UiEffect.ShowNoNetwork).message) {
                        viewModel.dispatchIntent(DefaultAction.Refresh)
                    }
                    is UiEffect.ShowContent<*> -> {
                        val data = (uiEffect as UiEffect.ShowContent<List<String>>).result
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            Text(
                                text = data?.getOrNull(0).orEmpty(),
                                modifier = Modifier.clickable { viewModel.showEmpty("暂时还没有数据哦~") })
                            Text(
                                text = data?.getOrNull(1).orEmpty(),
                                modifier = Modifier.clickable { viewModel.showError(ApiException(404, "这是一个美丽的错误！")) })
                            Text(
                                text = data?.getOrNull(2).orEmpty(),
                                modifier = Modifier.clickable { viewModel.showLoading("拼命加载中...") })
                            Text(
                                text = data?.getOrNull(3).orEmpty(),
                                modifier = Modifier.clickable { viewModel.showNoNetwork("网络电波无法到达哟~") })
                        }
                        LaunchedEffect(key1 = Unit){
                            "获取数据成功！".toast()
                        }
                    }
                }
            }
        }
    )
}
