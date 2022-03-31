package com.sutrix.uatest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sutrix.uatest.model.models.Burger

@Database(
    entities = [Burger::class],
    version = 1,
    exportSchema = false
)

abstract class BurgerDatabase : RoomDatabase() {
    abstract fun getBurgerDao(): BurgerDao

    companion object {
        private const val DB_NAME = "burger_database.db"

        @Volatile
        private var instance: BurgerDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            BurgerDatabase::class.java,
            DB_NAME
        ).allowMainThreadQueries().build()
    }
}