package com.ppb.travellin.services.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ppb.travellin.TravellinApps

class NotifReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra("TITLE")
        val msg = intent?.getStringExtra("MESSAGE")
        val app = context?.applicationContext as TravellinApps
        if (msg != null && title != null) {
            app.showNotification(title, msg)
        }

    }
}