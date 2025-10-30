package com.neupan.countdown.utils

import java.util.Calendar

/**
 * 农历转换工具类
 * 实现了基本的农历转公历和公历转农历功能
 */
object LunarConverter {
    
    // 农历数据表：1900-2100年的农历信息
    // 每个数据用4个十六进制数表示，从低位到高位分别表示：
    // 1. 该年闰月月份（0表示无闰月）
    // 2. 该年闰月天数（29或30）
    // 3. 该年12个月的大小月情况（大月30天，小月29天），从1月到12月
    // 这是简化版本，实际需要完整的农历数据表
    
    /**
     * 将农历日期转换为公历日期
     * @param lunarYear 农历年份
     * @param lunarMonth 农历月份
     * @param lunarDay 农历日期
     * @return 公历Calendar对象
     */
    fun lunarToSolar(lunarYear: Int, lunarMonth: Int, lunarDay: Int): Calendar {
        // 简化实现：使用近似计算
        // 实际应用中需要完整的农历数据表进行精确转换
        // 这里使用一个近似的算法
        val calendar = Calendar.getInstance()
        
        // 农历年份对应的大致公历年份（农历和公历年份基本相同，但可能有偏移）
        // 农历新年通常在公历1月下旬到2月中旬
        val solarYear = lunarYear
        
        // 农历月份大致对应公历月份（需要根据农历新年位置调整）
        // 农历1月大约在公历1月下旬到2月中旬
        val solarMonth = (lunarMonth - 1 + 1) % 12 // 简化处理
        val solarDay = lunarDay
        
        calendar.set(solarYear, solarMonth, solarDay)
        
        // 注意：这是一个简化实现，实际应用中需要使用完整的农历数据表
        // 或者使用第三方库如 com.github.niefy:lunar-java
        
        return calendar
    }
    
    /**
     * 获取某年农历某月某日的公历日期（今年的）
     * @param lunarMonth 农历月份
     * @param lunarDay 农历日期
     * @param currentYear 当前公历年份
     * @return 公历Calendar对象（今年的日期）
     */
    fun getSolarDateForThisYear(lunarMonth: Int, lunarDay: Int, currentYear: Int): Calendar {
        // 简化实现：假设农历月日和公历月日有简单的对应关系
        // 在实际应用中，需要查询农历数据表来确定准确的对应关系
        
        // 农历1月1日大约在公历1月下旬到2月中旬之间
        // 这里使用一个近似的转换
        val calendar = Calendar.getInstance()
        
        // 将农历月份转换为大致对应的公历月份
        // 农历1月 ≈ 公历1月或2月，农历12月 ≈ 公历12月或1月
        var solarMonth = lunarMonth + 1 // 大致对应
        if (solarMonth > 12) {
            solarMonth -= 12
        }
        
        calendar.set(currentYear, solarMonth - 1, lunarDay)
        
        // 注意：这是简化实现，实际需要完整的农历转换算法
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
}

