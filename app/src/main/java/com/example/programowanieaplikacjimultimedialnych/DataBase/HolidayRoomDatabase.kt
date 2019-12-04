package com.example.programowanieaplikacjimultimedialnych.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.programowanieaplikacjimultimedialnych.model.MultimediaPath
import com.example.programowanieaplikacjimultimedialnych.model.Post


// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Post::class, MultimediaPath::class), version = 2, exportSchema = false) //export Schema w normalnej apce inaczej
abstract class HolidayRoomDatabase : RoomDatabase() {

    abstract fun holidayDao(): HolidayDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: HolidayRoomDatabase? = null

        fun getDatabase(context: Context): HolidayRoomDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HolidayRoomDatabase::class.java,
                    "holiday_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}