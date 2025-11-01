package com.neupan.countdown.utils

import com.nlf.calendar.Lunar
import com.nlf.calendar.Solar
import java.util.Calendar

/**
 * 农历转换工具类
 * 使用 lunar-java 库实现精确的农历转公历转换
 */
object LunarConverter {
    
    /**
     * 将农历日期转换为公历日期
     * @param lunarYear 农历年份
     * @param lunarMonth 农历月份
     * @param lunarDay 农历日期
     * @return 公历Calendar对象
     */
    fun lunarToSolar(lunarYear: Int, lunarMonth: Int, lunarDay: Int): Calendar {
        // 使用 lunar-java 库进行精确转换
        val lunar = Lunar.fromYmd(lunarYear, lunarMonth, lunarDay)
        val solar = lunar.solar
        
        val calendar = Calendar.getInstance()
        calendar.set(solar.year, solar.month - 1, solar.day, 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        
        return calendar
    }
    
    /**
     * 获取某年农历某月某日的公历日期
     * @param lunarMonth 农历月份（1-12）
     * @param lunarDay 农历日期
     * @param solarYear 公历年份
     * @return 公历Calendar对象
     */
    fun getSolarDateForThisYear(lunarMonth: Int, lunarDay: Int, solarYear: Int): Calendar {
        // 尝试当前公历年对应的农历年
        try {
            val lunar = Lunar.fromYmd(solarYear, lunarMonth, lunarDay)
            val solar = lunar.solar
            
            // 检查转换后的公历年份
            if (solar.year == solarYear) {
                val calendar = Calendar.getInstance()
                calendar.set(solar.year, solar.month - 1, solar.day, 0, 0, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                return calendar
            }
        } catch (e: Exception) {
            // 如果转换失败，可能是因为农历年份不对
        }
        
        // 尝试下一个农历年
        try {
            val lunar = Lunar.fromYmd(solarYear + 1, lunarMonth, lunarDay)
            val solar = lunar.solar
            
            if (solar.year == solarYear) {
                val calendar = Calendar.getInstance()
                calendar.set(solar.year, solar.month - 1, solar.day, 0, 0, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                return calendar
            }
        } catch (e: Exception) {
            // 忽略
        }
        
        // 尝试上一个农历年
        try {
            val lunar = Lunar.fromYmd(solarYear - 1, lunarMonth, lunarDay)
            val solar = lunar.solar
            
            if (solar.year == solarYear) {
                val calendar = Calendar.getInstance()
                calendar.set(solar.year, solar.month - 1, solar.day, 0, 0, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                return calendar
            }
        } catch (e: Exception) {
            // 忽略
        }
        
        // 如果都失败，返回一个默认值（农历日期对应到公历大致位置）
        val calendar = Calendar.getInstance()
        calendar.set(solarYear, lunarMonth, lunarDay, 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar
    }
    
    /**
     * 检查今年的农历日期是否已过
     * @param lunarMonth 农历月份
     * @param lunarDay 农历日期
     * @param currentDate 当前日期
     * @return 如果已过返回true，否则返回false
     */
    fun isLunarDatePassed(lunarMonth: Int, lunarDay: Int, currentDate: Calendar): Boolean {
        val thisYearDate = getSolarDateForThisYear(lunarMonth, lunarDay, currentDate.get(Calendar.YEAR))
        return currentDate.after(thisYearDate)
    }
    
    /**
     * 将公历日期转换为农历日期
     * @param solarCalendar 公历Calendar对象
     * @return Triple(年, 月, 日)
     */
    fun solarToLunar(solarCalendar: Calendar): Triple<Int, Int, Int> {
        val solar = Solar.fromYmd(
            solarCalendar.get(Calendar.YEAR),
            solarCalendar.get(Calendar.MONTH) + 1,
            solarCalendar.get(Calendar.DAY_OF_MONTH)
        )
        val lunar = solar.lunar
        
        return Triple(lunar.year, lunar.month, lunar.day)
    }
}

