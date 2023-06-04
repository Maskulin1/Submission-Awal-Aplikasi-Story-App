package com.reihan.finalawalstory.remote.data

import com.google.gson.annotations.SerializedName

class GetStoryResponse (

    @field:SerializedName("listStory")
    val listStory: List<ListStory>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String

)