package com.neupan.countdown.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.neupan.countdown.data.MemorialDay
import com.neupan.countdown.ui.viewmodel.MemorialDayViewModel
import com.neupan.countdown.utils.DateCalculator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MemorialDayViewModel,
    onAddMemorialDay: () -> Unit,
    onEditMemorialDay: (MemorialDay) -> Unit
) {
    val memorialDays by viewModel.memorialDays.collectAsState()
    var showDeleteDialog by remember { mutableStateOf<MemorialDay?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("纪念日倒计时") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddMemorialDay) {
                Icon(Icons.Default.Add, contentDescription = "添加纪念日")
            }
        }
    ) { paddingValues ->
        if (memorialDays.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "还没有纪念日\n点击右下角按钮添加",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(memorialDays, key = { it.id }) { memorialDay ->
                    MemorialDayCard(
                        memorialDay = memorialDay,
                        onDelete = { showDeleteDialog = memorialDay },
                        onEdit = { onEditMemorialDay(memorialDay) }
                    )
                }
            }
        }
        
        // 删除确认对话框
        showDeleteDialog?.let { memorialDay ->
            AlertDialog(
                onDismissRequest = { showDeleteDialog = null },
                title = { Text("删除纪念日") },
                text = { Text("确定要删除「${memorialDay.name}」吗？") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteMemorialDay(memorialDay)
                            showDeleteDialog = null
                        }
                    ) {
                        Text("删除")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = null }) {
                        Text("取消")
                    }
                }
            )
        }
    }
}

@Composable
fun MemorialDayCard(
    memorialDay: MemorialDay,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    val daysPassed = DateCalculator.getDaysPassed(memorialDay)
    val daysRemaining = DateCalculator.getDaysRemaining(memorialDay)
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = memorialDay.name,
                    style = MaterialTheme.typography.titleLarge
                )
                
                Text(
                    text = if (memorialDay.isLunar) "农历" else "公历",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "${memorialDay.year}年${memorialDay.month}月${memorialDay.day}日",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 正数天数
            daysPassed?.let { days ->
                Text(
                    text = "已经过去 $days 天",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            // 倒数天数
            Text(
                text = "距离下次${memorialDay.name}还剩 $daysRemaining 天",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEdit) {
                    Text("编辑")
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = onDelete) {
                    Text("删除", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

