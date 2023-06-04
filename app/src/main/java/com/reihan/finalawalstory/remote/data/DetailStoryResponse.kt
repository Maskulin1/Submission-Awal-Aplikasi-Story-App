package com.reihan.finalawalstory.remote.data

data class DetailStoryResponse(
    val error: Boolean,
    val message: String,
    val story: Story
)