package com.newspeed.domain.content.dto

data class ContentInstagramDTO(
    val id: String,
    val imgUrls: List<String>,
    val title: String,
    val body: String,
    val url: String,
    val hashTags: List<String>
)
