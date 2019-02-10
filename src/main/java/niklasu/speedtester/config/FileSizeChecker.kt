package niklasu.speedtester.config

import com.google.inject.Inject
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.URL

class FileSizeChecker @Inject constructor(private val client: OkHttpClient) {

    /**
     *
     * @throws IOException
     * @throws NumberFormatException
     * @return file size given by the Content-Length response header
     */
    @Throws(IOException::class)
    fun getFileSize(url: URL): Long {
        val request = Request.Builder()
                .url(url)
                .head()
                .build()
        client.newCall(request).execute().use({ response ->
            if (!response.isSuccessful) throw ValidationException("Unexpected code $response")
            val length = response.headers().get("Content-Length")
            return length!!.toLong()
        })
    }
}