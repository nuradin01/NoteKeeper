package com.example.notekeeper

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class ReminderNotification(private val context: Context) {


    private val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    fun showNotification(notePosition: Int) {
        val activityIntent = Intent(context, NoteActivity::class.java)
        activityIntent.putExtra(NOTE_POSITION, notePosition)
//        val pendingIntent = PendingIntent.getActivity(
//            context,
//            1,
//            activityIntent,
//           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT else 0
//        )

        val pendingIntent = TaskStackBuilder.create(context)
            .addNextIntentWithParentStack(activityIntent)
            .getPendingIntent(
                1,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT else 0
            )
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_round_note_24)
            .setContentTitle("Review ${DataManager.notes[notePosition].title}")
            .setContentText(DataManager.notes[notePosition].text)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        nm.notify(1, notification)

    }
}