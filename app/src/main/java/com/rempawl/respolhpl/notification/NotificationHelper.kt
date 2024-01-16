package com.rempawl.respolhpl.notification

import android.Manifest.permission
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rempawl.respolhpl.MainActivity
import com.rempawl.respolhpl.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationHelper @Inject constructor(@ApplicationContext private val context: Context) {
    //TODO extract strings
    private val notificationManager by lazy {
        NotificationManagerCompat.from(context)
    }

    private val channel by lazy {
        NotificationChannelCompat.Builder(
            "notification channel",
            NotificationManagerCompat.IMPORTANCE_HIGH
        )
            .setName("notifications")
            .setDescription("App notifications")
            .build().apply {
                notificationManager.createNotificationChannel(this)
            }
    }

    fun showNotification(title: String?, body: String?, dataPayload: HashMap<String, String>?) {
        if (notificationManager.areNotificationsEnabled() && checkNotificationsPermission())
            notificationManager.notify(1, getNotification(title, body, dataPayload))
    }

    private fun checkNotificationsPermission() =
        if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
            checkSelfPermission(context, permission.POST_NOTIFICATIONS) == PERMISSION_GRANTED
        } else {
            true
        }

    private fun getNotification(
        title: String?,
        body: String?,
        dataPayload: HashMap<String, String>?
    ) = NotificationCompat.Builder(context, channel.id)
        .setContentTitle(title)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentText(body)
        .setAutoCancel(true)
        .setContentIntent(createPendingIntent(dataPayload))
        .build()

    private fun createPendingIntent(dataPayload: HashMap<String, String>?): PendingIntent? {
        val intent = Intent(context, MainActivity::class.java).putExtra(
            KEY_NOTIFICATION_PAYLOAD,
            dataPayload
        )

        return PendingIntent.getActivity(
            context, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        const val KEY_NOTIFICATION_PAYLOAD = "notification_payload"
    }
}