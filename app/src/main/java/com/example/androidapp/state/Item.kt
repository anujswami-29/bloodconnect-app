package com.example.androidapp.state

import java.util.UUID

data class Item(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val createdAtEpochMs: Long = System.currentTimeMillis(),
)

