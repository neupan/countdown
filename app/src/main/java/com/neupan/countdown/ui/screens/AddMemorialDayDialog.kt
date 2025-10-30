package com.neupan.countdown.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.neupan.countdown.data.MemorialDay
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
    var year by remember { 
        mutableStateOf(
            memorialDay?.year ?: calendar.get(Calendar.YEAR)
        ) 
    }
    var month by remember { 
        mutableStateOf(
            memorialDay?.month ?: calendar.get(Calendar.MONTH) + 1
        ) 
    }
    var day by remember { 
        mutableStateOf(
            memorialDay?.day ?: calendar.get(Calendar.DAY_OF_MONTH)
        ) 
    }
    var reminderDaysText by remember { 
        mutableStateOf(
            memorialDay?.reminderDays ?: "3,1"
        ) 
    }
    
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
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = year.toString(),
                        onValueChange = { 
                            it.toIntOrNull()?.let { y -> 
                                if (y > 1900 && y < 2100) year = y
                            } 
                        },
                        label = { Text("年") },
                        modifier = Modifier.weight(1f)
                    )
                    
                    OutlinedTextField(
                        value = month.toString(),
                        onValueChange = { 
                            it.toIntOrNull()?.let { m -> 
                                if (m >= 1 && m <= 12) month = m
                            } 
                        },
                        label = { Text("月") },
                        modifier = Modifier.weight(1f)
                    )
                    
                    OutlinedTextField(
                        value = day.toString(),
                        onValueChange = { 
                            it.toIntOrNull()?.let { d -> 
                                if (d >= 1 && d <= 31) day = d
                            } 
                        },
                        label = { Text("日") },
                        modifier = Modifier.weight(1f)
                    )
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
                        onSave(
                            memorialDay?.copy(
                                name = name,
                                year = year,
                                month = month,
                                day = day,
                                isLunar = isLunar,
                                reminderDays = reminderDaysText
                            ) ?: MemorialDay(
                                name = name,
                                year = year,
                                month = month,
                                day = day,
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
}

