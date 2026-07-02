package com.example.scrollbooker.entity.social.cloudflare.data.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.example.scrollbooker.entity.social.cloudflare.data.mappers.toDomain
import com.example.scrollbooker.entity.social.cloudflare.data.remote.CloudflareApiService
import com.example.scrollbooker.entity.social.cloudflare.data.remote.CloudflareDirectUploadRequest
import com.example.scrollbooker.entity.social.cloudflare.domain.model.CloudflareDirectUpload
import com.example.scrollbooker.entity.social.cloudflare.domain.repository.CloudflareRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.InputStream
import java.util.concurrent.TimeUnit

class CloudflareRepositoryImpl(
    private val apiService: CloudflareApiService,
    private val context: Context
): CloudflareRepository {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.MINUTES)
        .build()

    override suspend fun getUploadUrl(
        request: CloudflareDirectUploadRequest
    ): CloudflareDirectUpload =
        withContext(Dispatchers.IO) {
            apiService.uploadToCloudflare(request).toDomain()
        }

    override suspend fun uploadVideo(
        uploadUrl: String,
        videoUri: Uri,
        onProgress: (Double) -> Unit
    ): Unit = withContext(Dispatchers.IO) {

        val totalBytes = getFileSize(context, videoUri)
        if (totalBytes <= 0) {
            throw IllegalArgumentException(
                "The video file size is zero or invalid."
            )
        }

        val fileRequestBody = object : RequestBody() {
            override fun contentType() = "video/mp4".toMediaType()
            override fun contentLength(): Long = totalBytes

            override fun writeTo(sink: BufferedSink) {
                val inputStream: InputStream = context.contentResolver.openInputStream(videoUri)
                    ?: throw IllegalArgumentException("Could not open the video file.")

                val buffer = ByteArray(8192)
                var bytesWritten = 0L

                inputStream.use { stream ->
                    var bytesRead = stream.read(buffer)
                    while (bytesRead != -1) {
                        sink.write(buffer, 0, bytesRead)
                        bytesWritten += bytesRead

                        val percentage = (bytesWritten.toDouble() / totalBytes.toDouble()) * 100
                        onProgress(percentage)

                        bytesRead = stream.read(buffer)
                    }
                }
            }
        }

        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", "video.mp4", fileRequestBody)
            .build()

        val request = Request.Builder()
            .url(uploadUrl)
            .post(multipartBody)
            .build()

        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                val errorBody = response.body?.string() ?: "No details available"
                throw IllegalStateException("Cloudflare Direct Upload failed (${response.code}): $errorBody")
            }
        }
    }

    private fun getFileSize(context: Context, uri: Uri): Long {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (cursor.moveToFirst() && sizeIndex != -1) {
                return cursor.getLong(sizeIndex)
            }
        }
        return context.contentResolver.openAssetFileDescriptor(uri, "r")?.use { it.length } ?: -1L
    }
}