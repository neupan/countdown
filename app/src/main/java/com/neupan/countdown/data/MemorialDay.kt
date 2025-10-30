package com.neupan.countdown.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 纪念日实体类
 * @param id 唯一标识符
 * @param name 纪念日名称
 * @param year 年份（用于农历计算，公历时忽略）
 * @param month 月份
 * @param day 日期
 * @param isLunar 是否为农历日期
 * @param reminderDays 提前提醒天数列表，例如 [3, 1] 表示提前3天和1天提醒
 */
@Entity(tableName = "memorial_days")
data class MemorialDay(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val isLunar: Boolean = false,
    val reminderDays: String = "" // 存储为逗号分隔的字符串，例如 "3,1"
)

