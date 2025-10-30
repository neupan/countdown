package com.neupan.countdown

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neupan.countdown.data.AppDatabase
import com.neupan.countdown.data.MemorialDay
import com.neupan.countdown.ui.screens.AddMemorialDayDialog
import com.neupan.countdown.ui.screens.MainScreen
import com.neupan.countdown.notifications.NotificationHelper
import com.neupan.countdown.notifications.NotificationScheduler
import com.neupan.countdown.ui.theme.CountdownTheme
import com.neupan.countdown.ui.viewmodel.MemorialDayViewModel
import com.neupan.countdown.ui.viewmodel.MemorialDayViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        database = AppDatabase.getDatabase(applicationContext)
        
        // 创建通知通道
        NotificationHelper.createNotificationChannel(this)
        
        // 启动每日检查任务
        NotificationScheduler.scheduleDailyCheck(this)
        
        enableEdgeToEdge()
        setContent {
            CountdownTheme {
                MemorialDayApp(database = database)
            }
        }
    }
}

@Composable
fun MemorialDayApp(database: AppDatabase) {
    val viewModel: MemorialDayViewModel = viewModel(
        factory = MemorialDayViewModelFactory(database)
    )
    
    var showAddDialog by remember { mutableStateOf(false) }
    var editingMemorialDay by remember { mutableStateOf<MemorialDay?>(null) }
    
    MainScreen(
        viewModel = viewModel,
        onAddMemorialDay = {
            editingMemorialDay = null
            showAddDialog = true
        },
        onEditMemorialDay = { memorialDay ->
            editingMemorialDay = memorialDay
            showAddDialog = true
        }
    )
    
    if (showAddDialog) {
        AddMemorialDayDialog(
            memorialDay = editingMemorialDay,
            onDismiss = { showAddDialog = false },
            onSave = { memorialDay ->
                if (memorialDay.id == 0L) {
                    viewModel.insertMemorialDay(memorialDay)
                } else {
                    viewModel.updateMemorialDay(memorialDay)
                }
                showAddDialog = false
                editingMemorialDay = null
            }
        )
    }
}