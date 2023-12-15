package com.beni.core.data

import com.beni.core.data.local.LocalDataSource
import com.beni.core.data.remote.RemoteDataSource
import com.beni.core.data.remote.request.ExampleRequest
import com.beni.core.util.ConstantFunction.toListMultipartBodyParamter
import com.beni.core.util.ConstantFunction.toMultipartBodyParamter
import com.beni.core.util.ConstantFunction.toRequestBodyParameter
import java.io.File
import javax.inject.Inject

class SampleRepository @Inject constructor(
    private val remoteData: RemoteDataSource,
    private val localData: LocalDataSource
) {
    fun sampleGetRequest() = remoteData.sampleGetRequest()

    fun sampleInsertData(
        header: String,
        insertRequest: ExampleRequest
    ) = remoteData.sampleInsertData(header, insertRequest)

    fun sampleInsertData(
        header: String,
        field1: String,
        field2: String,
    ) = remoteData.sampleInsertData(header, field1, field2)

    fun sampleInsertSingleImage(
        header: String,
        image: File,
        field1: String,
        field2: String,
    ) = remoteData.sampleInsertSingleImage(
        header,
        image.toMultipartBodyParamter("image"),
        field1.toRequestBodyParameter(),
        field2.toRequestBodyParameter()
    )

    fun sampleInsertMultipleImage(
        header: String,
        image: List<File>,
        field1: String,
        field2: String,
    ) = remoteData.sampleInsertMultipleImage(
        header,
        image.toListMultipartBodyParamter("images[]"),
        field1.toRequestBodyParameter(),
        field2.toRequestBodyParameter()
    )
}