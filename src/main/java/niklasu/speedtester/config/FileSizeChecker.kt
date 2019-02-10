package niklasu.speedtester.config

import com.google.inject.Inject
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.URL

class FileSizeChecker @Inject constructor(val client: OkHttpClient){

    fun getFileSize(url: URL): Long {
        val request = Request.Builder()
                .url(url)
                .head()
                .build()
        var responseHeaders: Headers
        try {
            client.newCall(request).execute().use({ response ->
                if (!response.isSuccessful) throw ValidationException("Unexpected code $response")
                responseHeaders = response.headers()
                val length = responseHeaders.get("Content-Length")
                try {
                    return length!!.toLong()
                } catch (e: NumberFormatException) {
                    throw ValidationException("$length was assumend to be the value for the \"Content-Length\" Header and could not be parsed", e)
                }
            })
        } catch (e: IOException) {
            throw ValidationException("Error while checking the file size of $url", e)
        } catch (e: NumberFormatException) {
            throw ValidationException("Error while checking the file size of $url", e)
        }
    }
}