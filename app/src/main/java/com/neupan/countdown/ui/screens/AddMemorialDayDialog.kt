package com.neupan.countdown.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.neupan.countdown.data.MemorialDay
import com.neupan.countdown.utils.LunarConverter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemorialDayDialog(
    memorialDay: MemorialDay? = null,
    onDismiss: () -> Unit,
    onSave: (MemorialDay) -> Unit
) {
    val calendar = remember { Calendar.getInstance() }
    
    var name by remember { mutableStateOf(memorialDay?.name ?: "") }
    var isLunar by remember { mutableStateOf(memorialDay?.isLunar ?: false) }
    
    // 用户选择的总是公历日期（从 DatePicker 选择）
    var solarYear by remember { 
        mutableStateOf(
            memorialDay?.solarYear ?: calendar.get(Calendar.YEAR)
        ) 
    }
    var solarMonth by remember { 
        mutableStateOf(
            memorialDay?.solarMonth ?: calendar.get(Calendar.MONTH) + 1
        ) 
    }
    var solarDay by remember { 
        mutableStateOf(
            memorialDay?.solarDay ?: calendar.get(Calendar.DAY_OF_MONTH)
        ) 
    }
    var reminderDaysText by remember { 
        mutableStateOf(
            memorialDay?.reminderDays ?: "3,1"
        ) 
    }
    
    var showDatePicker by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (memorialDay == null) "添加纪念日" else "编辑纪念日") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("纪念日名称") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                // 日期选择按钮
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "选择日期",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${solarYear}年${solarMonth}月${solarDay}日 (公历)",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "选择日期",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("日期类型：")
                    Spacer(modifier = Modifier.width(8.dp))
                    Row {
                        FilterChip(
                            selected = !isLunar,
                            onClick = { isLunar = false },
                            label = { Text("公历") }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        FilterChip(
                            selected = isLunar,
                            onClick = { isLunar = true },
                            label = { Text("农历") }
                        )
                    }
                }
                
                OutlinedTextField(
                    value = reminderDaysText,
                    onValueChange = { reminderDaysText = it },
                    label = { Text("提前提醒天数（用逗号分隔，如：3,1）") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    supportingText = { Text("将在距离纪念日这些天数时提醒") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        // 根据用户选择的公历日期，计算对应的农历日期
                        val solarCalendar = Calendar.getInstance().apply {
                            set(solarYear, solarMonth - 1, solarDay)
                        }
                        val (lunarYear, lunarMonth, lunarDay) = LunarConverter.solarToLunar(solarCalendar)
                        
                        onSave(
                            memorialDay?.copy(
                                name = name,
                                solarYear = solarYear,
                                solarMonth = solarMonth,
                                solarDay = solarDay,
                                lunarYear = lunarYear,
                                lunarMonth = lunarMonth,
                                lunarDay = lunarDay,
                                isLunar = isLunar,
                                reminderDays = reminderDaysText
                            ) ?: MemorialDay(
                                name = name,
                                solarYear = solarYear,
                                solarMonth = solarMonth,
                                solarDay = solarDay,
                                lunarYear = lunarYear,
                                lunarMonth = lunarMonth,
                                lunarDay = lunarDay,
                                isLunar = isLunar,
                                reminderDays = reminderDaysText
                            )
                        )
                        onDismiss()
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text("保存")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
    
    // 日期选择器对话框
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = Calendar.getInstance().apply {
                set(solarYear, solarMonth - 1, solarDay)
            }.timeInMillis
        )
        
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedCalendar = Calendar.getInstance().apply {
                                timeInMillis = millis
                            }
                            solarYear = selectedCalendar.get(Calendar.YEAR)
                            solarMonth = selectedCalendar.get(Calendar.MONTH) + 1
                            solarDay = selectedCalendar.get(Calendar.DAY_OF_MONTH)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("取消")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

