package com.omran.quran.data.model

data class PageResult<T>(
    val items: List<T>,
    val lastId: String?,
    val endReached: Boolean,
)

sealed class UiEvent {
    data class Message(val text: String) : UiEvent()
}
