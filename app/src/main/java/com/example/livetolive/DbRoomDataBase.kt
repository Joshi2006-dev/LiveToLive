package com.example.livetolive
import android.content.Context
import androidx.room.*
import java.util.*

@Database(
    entities = [Hidratacion::class, Sleep::class, Actividad::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun hidratacionDao(): HidratacionDao
    abstract fun sleepDao(): SleepDao
    abstract fun actividadDao(): ActividadDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "LiveToLiveDB"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
