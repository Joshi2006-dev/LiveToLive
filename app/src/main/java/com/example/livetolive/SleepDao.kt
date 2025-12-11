package com.example.livetolive

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface SleepDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(s: Sleep)

    @Update
    suspend fun update(s: Sleep)

    @Delete
    suspend fun delete(s: Sleep)

    @Query("SELECT * FROM Sleep WHERE fecha = :fecha")
    fun getByDate(fecha: Date): Flow<Sleep?>

    @Query("SELECT * FROM Sleep ORDER BY fecha ASC")
    fun getAll(): Flow<List<Sleep>>
}
