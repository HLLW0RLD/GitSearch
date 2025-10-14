package com.example.gitsearch.utils

import android.R.id.input
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController
import com.example.gitsearch.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val LENGTH_LONG = Toast.LENGTH_LONG
val LENGTH_SHORT = Toast.LENGTH_SHORT

val LocalNavController =
    staticCompositionLocalOf<NavController> { throw IllegalStateException("No NavController found") }


//fun Any.toast(msg: Any?, duration: Int = LENGTH_SHORT) {
//    Toast.makeText(
//        App.Companion.appInstance,
//        if (msg == null) this.toString() else msg.toString(),
//        duration)
//        .show()
//}

internal fun Context.findActivity(): ComponentActivity {
    var context = this
    while (context is ContextWrapper) {
        if (context is ComponentActivity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Activity not found")
}


//fun CoroutineScope.debounceLaunch(
//    timeoutMs: Long = 500L,
//    block: suspend CoroutineScope.() -> Unit
//): Job {
//    var job: Job? = null
//    job?.cancel()
//    return launch {
//        delay(timeoutMs)
//        block()
//    }
//}