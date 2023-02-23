package com.unity.mynativeapp

import android.app.Application
import android.content.SharedPreferences
import androidx.annotation.Nullable
import com.unity.mynativeapp.ApplicationClass.Companion.AUTHORIZATION
import com.unity.mynativeapp.ApplicationClass.Companion.GRANT_TYPE
import com.unity.mynativeapp.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.unity.mynativeapp.ApplicationClass.Companion.sSharedPreferences
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

class ApplicationClass: Application() {
    companion object {
        lateinit var sSharedPreferences: SharedPreferences

        // JWT Token
        val AUTHORIZATION = "AUTHORIZATION"
        val X_ACCESS_TOKEN = "X_ACCESS_TOKEN"
        val X_REFRESH_TOKEN = "X_REFRESH_TOKEN"
        val GRANT_TYPE = "GRANT_TYPE"

        lateinit var okHttpClient: OkHttpClient
        val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
        val FORM_DATA: MediaType = "application/octet-stream".toMediaType()


        val API_URL = "http://175.114.240.162:8080/"

    }

    override fun onCreate() {
        super.onCreate()
        sSharedPreferences = applicationContext.getSharedPreferences("YOU_HAVE_TO", MODE_PRIVATE)

        okHttpClient = OkHttpClient.Builder()
                    .readTimeout(5000, TimeUnit.MILLISECONDS)
                    .connectTimeout(5000, TimeUnit.MILLISECONDS)
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addNetworkInterceptor(XAccessTokenInterceptor()) // JWT 자동 헤더 전송
                    .build()

        //okHttpClient = OkHttpClient.Builder()
        //            .readTimeout(5000, TimeUnit.MILLISECONDS)
        //            .connectTimeout(5000, TimeUnit.MILLISECONDS)
        //            .addInterceptor(AccessTokenAuthenticator())
        //            .build()

    }

}

class XAccessTokenInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val jwtToken: String? = sSharedPreferences.getString(AUTHORIZATION, null)
        val grantType = sSharedPreferences.getString(GRANT_TYPE, null)
        if (jwtToken != null) {
            builder.addHeader(AUTHORIZATION, "$grantType $jwtToken")
        }
        return chain.proceed(builder.build())
    }
}

/*class AccessTokenProvider {
    fun token(): String? {
        return sSharedPreferences.getString(X_ACCESS_TOKEN, null)
    }
    fun refreshToken(): String? {
    }
}
class AccessTokenAuthenticator(
    private val tokenProvider: AccessTokenProvider
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // We need to have a token in order to refresh it.
        val token = tokenProvider.token() ?: return null

        synchronized(this) {
            val newToken = tokenProvider.token()

            // Check if the request made was previously made as an authenticated request.
            if (response.request.header("Authorization") != null) {

                // If the token has changed since the request was made, use the new token.
                if (newToken != token) {
                    return response.request
                        .newBuilder()
                        .header("Authorization", "Bearer $newToken")
                        .build()
                }

                val updatedToken = tokenProvider.refreshToken() ?: return null

                // Retry the request with the new token.
                return response.request
                    .newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer $updatedToken")
                    .build()
            }
        }
        return null
    }
}*/
