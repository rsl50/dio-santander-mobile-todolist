package br.com.dio.todolist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.dio.todolist.dao.TaskDAO
import br.com.dio.todolist.model.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getTaskDAO(): TaskDAO

    companion object {
        private const val DATABASE_NAME = "todolist.db"

        // Centralize instance creation for easier re-use
        fun getInstance(context: Context): AppDatabase {
            return context.let {
                Room
                        .databaseBuilder(it, AppDatabase::class.java, DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build()
            }
        }
    }
}