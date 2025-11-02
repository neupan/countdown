package com.neupan.countdown.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MemorialDayDao {
    @Query("SELECT * FROM memorial_days ORDER BY solarMonth, solarDay")
    fun getAllMemorialDays(): Flow<List<MemorialDay>>
    
    @Query("SELECT * FROM memorial_days WHERE id = :id")
    suspend fun getMemorialDayById(id: Long): MemorialDay?
    
    @Insert
    suspend fun insertMemorialDay(memorialDay: MemorialDay): Long
    
    @Update
    suspend fun updateMemorialDay(memorialDay: MemorialDay)
    
    @Delete
    suspend fun deleteMemorialDay(memorialDay: MemorialDay)
}

