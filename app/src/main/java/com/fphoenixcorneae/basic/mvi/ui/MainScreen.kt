package com.fphoenixcorneae.basic.mvi.ui

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fphoenixcorneae.basic.mvi.DefaultAction
import com.fphoenixcorneae.basic.mvi.MainViewModel
import com.fphoenixcorneae.basic.mvi.UiEffect
import com.fphoenixcorneae.basic.mvi.uiEffect

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
) {
    val uiEffect by uiEffect.collectAsState(UiEffect.ShowContent)
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
                        viewModel.dispatchIntent(DefaultAction.Initialize)
                    }
                    is UiEffect.ShowError -> ErrorScreen((uiEffect as UiEffect.ShowError).message) {
                        viewModel.dispatchIntent(DefaultAction.Initialize)
                    }
                    is UiEffect.ShowLoading -> LoadingScreen((uiEffect as UiEffect.ShowLoading).message)
                    is UiEffect.ShowNoNetwork -> NoNetworkScreen((uiEffect as UiEffect.ShowNoNetwork).message) {
                        viewModel.dispatchIntent(DefaultAction.Initialize)
                    }
                    UiEffect.ShowContent, is UiEffect.ShowToast -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            Text(text = "显示空白页", modifier = Modifier.clickable { viewModel.showEmpty("暂时还没有数据哦~") })
                            Text(text = "显示错误页", modifier = Modifier.clickable { viewModel.showError("这是一个美丽的错误！") })
                            Text(text = "显示加载页", modifier = Modifier.clickable { viewModel.showLoading("拼命加载中...") })
                            Text(
                                text = "显示无网络页",
                                modifier = Modifier.clickable { viewModel.showNoNetwork("网络电波无法到达哟~") })
                            Text(text = "显示吐司", modifier = Modifier.clickable { viewModel.showToast("客官不要调皮，乖乖到碗里来！") })
                        }
                        if (uiEffect is UiEffect.ShowToast) {
                            Toast.makeText(
                                LocalContext.current,
                                (uiEffect as UiEffect.ShowToast).message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    )
}
