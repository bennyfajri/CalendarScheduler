package com.beni.core.util

import com.google.android.material.textfield.TextInputLayout
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object ConstantFunction {
    fun Any.getErrorMessage(): String {
        return when (this) {
            is String -> {
                this.toString()
            }

            is Map<*, *> -> {
                this.values.joinToString {
                    (it as List<*>).first().toString()
                }
            }

            else -> {
                this.toString()
            }
        }
    }

    fun String.toRequestBodyParameter(): RequestBody {
        return this.toRequestBody("text/plain".toMediaType())
    }

    fun File.toMultipartBodyParamter(name: String): MultipartBody.Part {
        val imageFile = this.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(
            name,
            this.name,
            imageFile
        )
    }

    fun List<File>.toListMultipartBodyParamter(name: String): List<MultipartBody.Part> {
        val images: ArrayList<MultipartBody.Part> = ArrayList()
        for (i in this) {
            val imageFile = i.asRequestBody("image/jpeg".toMediaTypeOrNull())
            images.add(
                MultipartBody.Part.createFormData(
                    name,
                    i.name,
                    imageFile
                )
            )
        }
        return images
    }

    fun TextInputLayout.setInputError(message: String): Boolean {
        this.isErrorEnabled = true
        this.error = message
        this.requestFocus()
        return false
    }

    private const val FILENAME_FORMAT = "dd-MMM-yyyy"

    fun String.getErrorMessage(): String{
        val jObject = JSONObject(this)
        return jObject.getString("message")
    }

    fun Calendar.changeFormat(format: String): String {
        val dateFormat= SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(this.time)
    }
}