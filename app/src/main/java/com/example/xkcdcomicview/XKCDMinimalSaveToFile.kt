package com.example.xkcdcomicview

import com.google.gson.annotations.SerializedName

data class XKCDMinimalSaveToFile(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("explanation") val explanation: String,
    @SerializedName("imagebytearray") val imagebytearray: ByteArray
)