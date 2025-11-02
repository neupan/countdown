package com.neupan.countdown.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 纪念日实体类
 * @param id 唯一标识符
 * @param name 纪念日名称
 * @param solarYear 公历年份（用户选择时的公历年份）
 * @param solarMonth 公历月份
 * @param solarDay 公历日期
 * @param lunarYear 农历年份（保存对应的农历年份）
 * @param lunarMonth 农历月份
 * @param lunarDay 农历日期
 * @param isLunar 是否为农历纪念日（true时使用农历日期计算，false时使用公历日期计算）
 * @param reminderDays 提前提醒天数列表，例如 "3,1" 表示提前3天和1天提醒
 */
@Entity(tableName = "memorial_days")
data class MemorialDay(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val solarYear: Int,
    val solarMonth: Int,
    val solarDay: Int,
    val lunarYear: Int,
    val lunarMonth: Int,
    val lunarDay: Int,
    val isLunar: Boolean = false,
    val reminderDays: String = "" // 存储为逗号分隔的字符串，例如 "3,1"
) {
    // 为了兼容旧代码，添加便捷属性
    val year: Int get() = if (isLunar) lunarYear else solarYear
    val month: Int get() = if (isLunar) lunarMonth else solarMonth
    val day: Int get() = if (isLunar) lunarDay else solarDay
}

