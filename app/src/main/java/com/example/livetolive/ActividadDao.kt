package com.example.livetolive

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface ActividadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(a: Actividad)

    @Update
    suspend fun update(a: Actividad)

    @Delete
    suspend fun delete(a: Actividad)

    @Query("SELECT * FROM Actividad WHERE fecha = :fecha")
    fun getByDate(fecha: Date): Flow<Actividad?>

    @Query("SELECT * FROM Actividad ORDER BY fecha ASC")
    fun getAll(): Flow<List<Actividad>>
}
