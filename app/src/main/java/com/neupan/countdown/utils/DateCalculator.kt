package com.neupan.countdown.utils

import com.neupan.countdown.data.MemorialDay
import java.util.Calendar

/**
 * 日期计算工具类
 */
object DateCalculator {
    
    /**
     * 计算纪念日的正数天数（已经过去的天数）
     * @param memorialDay 纪念日
     * @return 已经过去的天数，如果纪念日还没到今年，返回null
     */
    fun getDaysPassed(memorialDay: MemorialDay): Int? {
        val today = Calendar.getInstance()
        val thisYearDate = getThisYearDate(memorialDay, today.get(Calendar.YEAR))
        
        if (today.before(thisYearDate)) {
            // 今年的纪念日还没到，返回null
            return null
        }
        
        val diff = today.timeInMillis - thisYearDate.timeInMillis
        return (diff / (1000 * 60 * 60 * 24)).toInt()
    }
    
    /**
     * 计算距离今年纪念日还剩的天数
     * @param memorialDay 纪念日
     * @return 还剩的天数，如果今年的纪念日已过，返回明年纪念日还剩的天数
     */
    fun getDaysRemaining(memorialDay: MemorialDay): Int {
        val today = Calendar.getInstance()
        val currentYear = today.get(Calendar.YEAR)
        
        val thisYearDate = getThisYearDate(memorialDay, currentYear)
        
        if (today.after(thisYearDate) || today == thisYearDate) {
            // 今年的纪念日已过或就是今天，计算明年的
            val nextYearDate = getThisYearDate(memorialDay, currentYear + 1)
            val diff = nextYearDate.timeInMillis - today.timeInMillis
            return (diff / (1000 * 60 * 60 * 24)).toInt()
        } else {
            // 今年的纪念日还没到
            val diff = thisYearDate.timeInMillis - today.timeInMillis
            return (diff / (1000 * 60 * 60 * 24)).toInt()
        }
    }
    
    /**
     * 获取今年的纪念日日期
     * @param memorialDay 纪念日
     * @param year 年份
     * @return Calendar对象
     */
    fun getThisYearDate(memorialDay: MemorialDay, year: Int): Calendar {
        val calendar = Calendar.getInstance()
        
        if (memorialDay.isLunar) {
            // 农历日期
            val lunarDate = LunarConverter.getSolarDateForThisYear(
                memorialDay.month,
                memorialDay.day,
                year
            )
            return lunarDate
        } else {
            // 公历日期
            calendar.set(year, memorialDay.month - 1, memorialDay.day)
            return calendar
        }
    }
    
    /**
     * 检查是否在提醒日期范围内
     * @param memorialDay 纪念日
     * @return 如果在提醒日期范围内返回true
     */
    fun isInReminderRange(memorialDay: MemorialDay): Boolean {
        val daysRemaining = getDaysRemaining(memorialDay)
        val reminderDays = memorialDay.reminderDays
            .split(",")
            .filter { it.isNotBlank() }
            .mapNotNull { it.toIntOrNull() }
        
        return reminderDays.contains(daysRemaining)
    }
}

