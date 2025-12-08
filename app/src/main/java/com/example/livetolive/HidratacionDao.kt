package com.example.livetolive

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface HidratacionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(h: Hidratacion)

    @Update
    suspend fun update(h: Hidratacion)

    @Delete
    suspend fun delete(h: Hidratacion)

    @Query("SELECT * FROM Hidratacion WHERE fecha = :fecha")
    fun getByDate(fecha: Date): Flow<Hidratacion?>

    @Query("SELECT * FROM Hidratacion ORDER BY fecha ASC")
    fun getAll(): Flow<List<Hidratacion>>
}
