package com.daniel.miaumart.ui.adapters

interface FavoritesItemClickListener {
    fun onItemClickListener(pid: String, category: String)
    fun onDeleteClickListener(favoriteId: String)
}