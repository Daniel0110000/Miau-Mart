package com.daniel.miaumart.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val username: String,
    val profileImage: String
)