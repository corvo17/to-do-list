package com.corvo.demo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.corvo.demo.helper.Constants

@Database(entities = [TaskDto::class], version = 9, exportSchema = false)
abstract class LocalDB : RoomDatabase(){

    abstract fun getTaskDao(): TaskDao

    companion object{
        @Volatile private var instance: LocalDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance?: buildDataBase(context).also { instance = it }
        }

        fun buildDataBase(context: Context) = Room.databaseBuilder(context,
            LocalDB::class.java, Constants.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}