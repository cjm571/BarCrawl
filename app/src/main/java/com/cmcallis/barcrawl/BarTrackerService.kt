package com.cmcallis.barcrawl

import android.app.*
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat

const val FOREGROUND_ID = 1

class BarTrackerService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        // Create the NotificationChannel
        val id = getString(R.string.channel_id)
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance).apply {
            description = descriptionText
        }

        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        // Create an intent to open the Main Activity if the user taps the notification
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val builder = NotificationCompat.Builder(this, id
        )
            .setSmallIcon(android.R.drawable.ic_dialog_map)
            .setContentTitle(getText(R.string.notification_title))
            .setContentText(getText(R.string.notification_message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
            .build()

        startForeground(FOREGROUND_ID, builder)

        try {
            Toast.makeText(this, "Bar Tracker Service running.", Toast.LENGTH_SHORT).show()
        } catch (e: InterruptedException) {
            // Restore interrupt status.
            Thread.currentThread().interrupt()
        }

        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "Bar Tracker Service done running.", Toast.LENGTH_SHORT).show()

        super.onDestroy()
    }
}