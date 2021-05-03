package me.rerere.composediary.ui.page

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.material.timepicker.TimeFormat
import me.rerere.composediary.ComposeDiaryApp
import me.rerere.composediary.DiaryViewModel
import me.rerere.composediary.DiaryViewModelFactory
import me.rerere.composediary.model.Diary
import me.rerere.composediary.util.formatAsTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun EditPage(navController: NavController, diaryViewModel: DiaryViewModel) {
    val state = diaryViewModel.currentEditing
    var content by remember { mutableStateOf(diaryViewModel.currentEditing.content) }
    var date by remember {
        mutableStateOf(diaryViewModel.currentEditing.date)
    }
    // 载入完成后更新content
    LaunchedEffect(state) {
        content = state.content
        date = state.date
    }

    // context
    val context = LocalContext.current

    EditUI( content, date,state.id,{
        if(content.isEmpty()){
            Toast.makeText(context, "日记不能为空！", Toast.LENGTH_SHORT).show()
        }else {
            state.content = content
            diaryViewModel.update(state)
            Toast.makeText(context, "保存完成!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }, {
        content = it
    })
}

@Composable
fun EditUI(content: String,date: Long, id: Int,onSave: () -> Unit, onChange: (String)->Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "编辑日记: $id (${date.formatAsTime()})") },
                actions = {
                    IconButton(onClick = onSave) {
                        Icon(Icons.Default.Save, "Save")
                    }
                }
            )
        }
    ) {
        BasicTextField(value = content, onValueChange = onChange, Modifier.fillMaxSize())
    }
}