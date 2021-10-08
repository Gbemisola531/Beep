package com.phoenix.appstory.model

data class Story(
    val url: String
)

data class StoryResponse(
    val name: String,
    val stories: List<Story>
)

enum class StoryType {
    VIDEO, IMAGE
}
