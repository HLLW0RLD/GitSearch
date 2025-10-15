package com.example.gitsearch.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

val LocalNavController =
    staticCompositionLocalOf<NavController> { throw IllegalStateException("No NavController found") }

fun String.openUrl(context: Context) {
    val url = when {
        this.startsWith("http://") || this.startsWith("https://") -> this
        this.startsWith("www.") -> "https://$this"
        else -> "https://$this"
    }

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "Нет приложения для открытия ссылки", Toast.LENGTH_SHORT).show()
    }
}

internal fun Context.findActivity(): ComponentActivity {
    var context = this
    while (context is ContextWrapper) {
        if (context is ComponentActivity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Activity not found")
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.toReadableDate(): String {
    val outputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))
    return try {
        val instant = try {
            java.time.Instant.parse(this)
        } catch (e: Exception) {
            null
        }

        if (instant != null) {
            val zoned = instant.atZone(ZoneId.systemDefault()).toLocalDate()
            return zoned.format(outputFormatter)
        }

        val localDate = LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE)
        localDate.format(outputFormatter)
    } catch (e: Exception) {
        this
    }
}