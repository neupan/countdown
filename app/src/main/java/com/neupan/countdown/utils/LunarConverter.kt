package com.neupan.countdown.utils

import android.util.Log
import com.nlf.calendar.Lunar
import com.nlf.calendar.Solar
import java.util.Calendar

/**
 * 农历转换工具类
 * 使用 lunar-java 库实现精确的农历转公历转换
 */
object LunarConverter {
    val TAG = "LunarConverter"
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
        var curYear = solarYear;
        var isFound = false;
        var lunarDate = Lunar()
        while (!isFound) {
            try {
                lunarDate = Lunar.fromYmd(curYear, lunarMonth, lunarDay);
                isFound = true;
            } catch (e: Exception) {
                Log.e(TAG, "error: $e")
                curYear++;
            }
        }
        val solar = lunarDate.solar
        Log.d(TAG, "getSolarDateForThisYear lunar: $lunarDate")
        Log.d(TAG, "getSolarDateForThisYear solar: $solar")
        val calendar = Calendar.getInstance()
        calendar.set(solar.year, solar.month - 1, solar.day, 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar
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
        Log.d(TAG, "lunar: $lunar")
        Log.d(TAG, "lunar.year: ${lunar.year}")
        Log.d(TAG, "lunar.month: ${lunar.month}")
        Log.d(TAG, "lunar.day: ${lunar.day}")
        return Triple(lunar.year, lunar.month, lunar.day)
    }
}

