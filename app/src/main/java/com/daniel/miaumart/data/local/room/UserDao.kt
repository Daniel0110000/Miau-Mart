package com.daniel.miaumart.data.local.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserData(user: User)

    @Query("select * from user_data_table")
    fun getUserData(): Flow<List<User>>

    @Query("delete from user_data_table where username=:username")
    suspend fun deleteUserData(username: String)
}