package com.fphoenixcorneae.compose.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/** 副作用视图 */
private val effectChannel by lazy { Channel<UiEffect>() }
val uiEffect: Flow<UiEffect>
    get() = effectChannel.receiveAsFlow()

/**
 * @desc：处理用户输入并通过在其内部处理结果以更新UI中的视图状态
 * @date：2023/05/25 15:12
 */
abstract class BaseViewModel<A> : ViewModel(), IAction<A>, IResult {
    /** 用户意图 */
    private val actionChannel = Channel<A>()

    /** 用户意图结果 */
    private val resultChannel = Channel<ActionResult>()

    protected fun <T> sendHttpRequest(
        loadingMsg: String? = null,
        judgeEmpty: ((T) -> Boolean)? = null,
        isOk: ((T) -> Boolean)? = null,
        call: suspend () -> T,
    ) = viewModelScope.launch {
        runCatching {
            showLoading(message = loadingMsg)
            call()
        }.onSuccess {

        }.onFailure {
            showError(getErrorMessage(it))
        }
    }

    private fun getErrorMessage(t: Throwable) = when (t) {
        is ConnectException,
        is UnknownHostException,
        -> "网络连接失败"
        is SocketTimeoutException -> "网络连接超时"
        is JsonParseException -> "数据解析错误"
        else -> "未知错误"
    }

    override fun dispatchIntent(action: A) {
        launch { actionChannel.send(action) }
    }

    override fun dispatchResult(result: ActionResult) {
        launch { resultChannel.send(result) }
    }

    /**
     * 根据自己的项目需求，可以重写该方法
     */
    override fun processResult(result: ActionResult) {
        when (result) {
            is ActionResult.Failure -> launch { showError(result.cause) }
            ActionResult.Nothing -> launch { showEmpty() }
            is ActionResult.Success<*> -> launch { showContent(result.data) }
        }
    }

    private inline fun <reified T> showContent(message: T?) {
        launch { effectChannel.send(UiEffect.ShowContent(message)) }
    }

    fun showLoading(message: String? = null) {
        launch { effectChannel.send(UiEffect.ShowLoading(message = message)) }
    }

    fun showError(message: String?) {
        launch { effectChannel.send(UiEffect.ShowError(message = message)) }
    }

    fun showEmpty(message: String? = null) {
        launch { effectChannel.send(UiEffect.ShowEmpty(message = message)) }
    }

    fun showNoNetwork(message: String? = null) {
        launch { effectChannel.send(UiEffect.ShowNoNetwork(message = message)) }
    }

    fun launch(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch { block() }

    fun launchDefault(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch(Dispatchers.Default) { block() }

    fun launchIo(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch(Dispatchers.IO) { block() }

    fun launchMain(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch(Dispatchers.Main) { block() }

    init {
        launch { actionChannel.receiveAsFlow().collect { processIntent(it) } }
        launch { resultChannel.receiveAsFlow().collect { processResult(it) } }
    }
}