package com.neupan.countdown.utils

import com.neupan.countdown.data.MemorialDay
import java.util.Calendar
import android.util.Log
import com.nlf.calendar.Solar

/**
 * 日期计算工具类
 */
object DateCalculator {

    val TAG = "DateCalculator"
    /**
     * 计算距离纪念日已经过去的天数
     * @param memorialDay 纪念日
     * @return 已经过去的天数，如果纪念日还没到，返回null
     */
    fun getDaysPassed(memorialDay: MemorialDay): Int? {
        val memoriaSolorDate = Solar.fromYmd(memorialDay.solarYear, memorialDay.solarMonth, memorialDay.solarDay);
        val today = Calendar.getInstance();
        val todaySolarDate = Solar.fromYmd(today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH));
        if (todaySolarDate.isBefore(memoriaSolorDate)) {
            return null
        }
        return todaySolarDate.subtract(memoriaSolorDate)
    }
    
    /**
     * 计算距离下一个纪念日还剩的天数
     * @param memorialDay 纪念日
     * @return 还剩的天数，如果今年的纪念日已过，返回明年纪念日还剩的天数
     */
    fun getDaysRemaining(memorialDay: MemorialDay): Int {
        val today = Calendar.getInstance()
        val currentYear = today.get(Calendar.YEAR)
        val todaySolarDate = Solar.fromYmd(today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH));
        val thisYearDate = getThisYearDate(memorialDay, currentYear)
        val thisYearSolarDate = Solar.fromYmd(thisYearDate.get(Calendar.YEAR), thisYearDate.get(Calendar.MONTH) + 1, thisYearDate.get(Calendar.DAY_OF_MONTH));
        Log.d(TAG, "getDaysRemaining todaySolarDate: $todaySolarDate")
        Log.d(TAG, "getDaysRemaining thisYearSolarDate: $thisYearSolarDate")

        if (todaySolarDate.isAfter(thisYearSolarDate)) {
            // 今年的纪念日已过或就是今天，计算明年的
            val nextYearDate = getThisYearDate(memorialDay, currentYear + 1)
            val nextYearSolarDate = Solar.fromYmd(nextYearDate.get(Calendar.YEAR), nextYearDate.get(Calendar.MONTH) + 1, nextYearDate.get(Calendar.DAY_OF_MONTH));
            return nextYearSolarDate.subtract(todaySolarDate)
        } else {
            // 今年的纪念日还没到
            return thisYearSolarDate.subtract(todaySolarDate)
        }
    }
    
    /**
     * 获取指定年份的纪念日日期
     * @param memorialDay 纪念日
     * @param year 年份
     * @return Calendar对象
     */
    fun getThisYearDate(memorialDay: MemorialDay, year: Int): Calendar {
        val calendar = Calendar.getInstance()
        
        if (memorialDay.isLunar) {
            // 农历纪念日：根据保存的农历月日，计算指定年份对应的公历日期
            val lunarDate = LunarConverter.getSolarDateForThisYear(
                memorialDay.lunarMonth,
                memorialDay.lunarDay,
                year
            )
            return lunarDate
        } else {
            // 公历纪念日：直接使用保存的公历日期
            var curYear = year;
            var isFound = false;
            var solarDate = Solar()
            while (!isFound) {
                try {
                    solarDate = Solar.fromYmd(curYear, memorialDay.solarMonth, memorialDay.solarDay);
                    isFound = true;
                } catch (e: Exception) {
                    Log.e(TAG, "error: $e")
                    curYear++;
                }
            }
            Log.d(TAG, "getThisYearDate solarDate: $solarDate")
            calendar.set(solarDate.getYear(), solarDate.getMonth() - 1, solarDate.getDay(), 0, 0, 0)
            calendar.set(Calendar.MILLISECOND, 0)
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

