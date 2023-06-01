package com.fphoenixcorneae.compose.https

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @desc：
 * @date：2023/03/22 10:06
 */
class RetrofitFactory {

    class Builder {
        var baseUrl: String = "https://www.baidu.com/"

        /** 请求头拦截器 */
        var headerInterceptor: Interceptor = HeaderInterceptor()

        /** 日志拦截器 */
        var loggingInterceptor: Interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        fun build(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(buildOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        private fun buildOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                // 添加请求header, 注意要设置在日志拦截器之前, 不然Log中会不显示header信息
                .addInterceptor(headerInterceptor)
                // 日志拦截器
                .addInterceptor(loggingInterceptor)
                .sslSocketFactory(SSLSocketClient.getSocketFactory(), SSLSocketClient.x509TrustManager)
                .hostnameVerifier(SSLSocketClient.hostnameVerifier)
                .build()
        }
    }
}