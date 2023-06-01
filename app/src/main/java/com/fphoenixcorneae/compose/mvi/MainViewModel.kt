package com.fphoenixcorneae.compose.mvi

import com.fphoenixcorneae.compose.https.ApiException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

/**
 * @desc：
 * @date：2023/05/25 17:51
 */
class MainViewModel : BaseViewModel<DefaultAction>() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    override fun processIntent(action: DefaultAction) {
        when (action) {
            DefaultAction.Initialize -> launchIo {
                showLoading()
                delay(2000)
                dispatchResult(ActionResult.Success(listOf("显示空白页", "显示错误页", "显示加载页", "显示无网络页")))
            }
            DefaultAction.Refresh -> launchIo {
                showLoading()
                delay(2000)
                if (Random.nextInt(4) == 1) {
                    dispatchResult(ActionResult.Success(listOf("显示空白页", "显示错误页", "显示加载页", "显示无网络页")))
                } else if (Random.nextInt(4) == 2) {
                    dispatchResult(ActionResult.Nothing)
                } else {
                    dispatchResult(ActionResult.Failure(ApiException(500, "服务器错误！")))
                }
            }
        }
    }
}

/**
 * @desc：
 * @date：2023/05/25 17:51
 */
data class MainUiState(
    val `data`: Any? = null,
)