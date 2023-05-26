package com.fphoenixcorneae.basic.mvi

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
                if (Random.nextInt(4) == 1) {
                    dispatchResult(ActionResult.Success("获取数据成功！"))
                } else if (Random.nextInt(4) == 2) {
                    dispatchResult(ActionResult.Nothing)
                } else {
                    dispatchResult(ActionResult.Failure("服务器错误！"))
                }
            }
            DefaultAction.Refresh -> showLoading()
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