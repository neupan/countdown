package com.neupan.countdown.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [MemorialDay::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memorialDayDao(): MemorialDayDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        /**
         * 数据库迁移：从版本 1 到版本 2
         * 添加公历和农历字段
         */
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // 创建新表
                db.execSQL("""
                    CREATE TABLE memorial_days_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        solarYear INTEGER NOT NULL,
                        solarMonth INTEGER NOT NULL,
                        solarDay INTEGER NOT NULL,
                        lunarYear INTEGER NOT NULL,
                        lunarMonth INTEGER NOT NULL,
                        lunarDay INTEGER NOT NULL,
                        isLunar INTEGER NOT NULL,
                        reminderDays TEXT NOT NULL
                    )
                """.trimIndent())
                
                // 将旧数据迁移到新表（将 year/month/day 复制到 solar 和 lunar 字段）
                db.execSQL("""
                    INSERT INTO memorial_days_new 
                    (id, name, solarYear, solarMonth, solarDay, lunarYear, lunarMonth, lunarDay, isLunar, reminderDays)
                    SELECT id, name, year, month, day, year, month, day, isLunar, reminderDays
                    FROM memorial_days
                """.trimIndent())
                
                // 删除旧表
                db.execSQL("DROP TABLE memorial_days")
                
                // 重命名新表
                db.execSQL("ALTER TABLE memorial_days_new RENAME TO memorial_days")
            }
        }
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "memorial_day_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

