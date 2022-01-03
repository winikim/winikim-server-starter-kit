package net.yayoung.share.application.dto

data class PageResponse(
    val totalElements: Long,
    val page: Int,
    val size: Int,
    val elements: Any
)