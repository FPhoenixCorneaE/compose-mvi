package com.fphoenixcorneae.basic.mvi

import kotlin.random.Random

/**
 * @desc：表示副作用视图，用于表示与用户界面响应相关的副作用。
 * @date：2023/05/25 15:28
 */
sealed interface UiEffect {
    /** 显示内容 */
    object ShowContent : UiEffect

    /** 显示吐司 */
    data class ShowToast(val message: String) : UiEffect {
        /**
         * 重写回调对象的 equals() 和 hashCode()
         * 让每次数据都不一样。
         */
        override fun equals(other: Any?): Boolean {
            return false
        }

        override fun hashCode(): Int {
            return Random.nextInt(Int.MAX_VALUE)
        }
    }

    /** 显示加载中 */
    data class ShowLoading(val message: String? = null, val icon: Any? = null) : UiEffect

    /** 显示空白 */
    data class ShowEmpty(val message: String?, val icon: Any? = null) : UiEffect

    /** 显示错误 */
    data class ShowError(val message: String?, val icon: Any? = null) : UiEffect

    /** 显示无网络 */
    data class ShowNoNetwork(val message: String?, val icon: Any? = null) : UiEffect
}
