package com.example.androidapp.state

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class ItemsViewModel : ViewModel() {
    private val _items = mutableStateListOf<Item>()
    val items: List<Item> get() = _items

    init {
        _items.addAll(
            listOf(
                Item(title = "Welcome! Tap + to add an item."),
                Item(title = "Tap an item to open details."),
            ),
        )
    }

    fun add(title: String) {
        val trimmed = title.trim()
        if (trimmed.isEmpty()) return
        _items.add(0, Item(title = trimmed))
    }

    fun remove(id: String) {
        _items.removeAll { it.id == id }
    }

    fun find(id: String): Item? = _items.firstOrNull { it.id == id }
}

