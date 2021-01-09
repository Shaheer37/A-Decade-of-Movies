package com.shaheer.adecadeofmovies.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class NetworkConnectionInterceptor @Inject constructor(
        private val context: Context
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected()) {
            throw NoConnectivityException()
            // Throwing our custom exception 'NoConnectivityException'
        }

        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

    fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            capabilities?.let {
                when{
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }?: false

        }else{
            val netInfo = connectivityManager.activeNetworkInfo
            netInfo != null && netInfo.isConnected
        }

    }
}

class NoConnectivityException: IOException() {
    override val message: String?
        get() = "No Internet Connection"
}