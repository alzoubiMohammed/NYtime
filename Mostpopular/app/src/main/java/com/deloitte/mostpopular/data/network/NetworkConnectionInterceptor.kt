package com.deloitte.mostpopular.data.network

import android.content.Context
import com.deloitte.mostpopular.ui.util.extension.isNetworkAvailable
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(private val mContext: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isNetworkConnected()) {
            throw NoConnectivityException()
        }
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }


    private fun isNetworkConnected(): Boolean {
        return mContext.isNetworkAvailable()
    }

}