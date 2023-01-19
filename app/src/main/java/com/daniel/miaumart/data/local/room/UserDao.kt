package com.daniel.miaumart.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserData(user: User)

    @Query("select * from user_data_table")
    fun getUserData(): Flow<List<User>>

}