package uz.techie.mahsulot.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import uz.techie.mahsulot.MainActivity
import uz.techie.mahsulot.R

class MyFirebaseMessagingService: FirebaseMessagingService() {
    var notificationManager: NotificationManager? = null

    val CHANNEL_PRIMARY = "channel_fcm_messaging"
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.d("TAG", "onMessageReceived: $p0")

        p0.let {
            Log.d("TAG", "onMessageReceived: title ${it.notification?.title}")
            Log.d("TAG", "onMessageReceived: message ${it.notification?.body}")

            val title = it.notification?.title
            val message = it.notification?.body

            createNotification(title?:"", message?:"")

        }



    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }





    private fun createNotification(title:String, message:String){
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_PRIMARY,
                "channel_FCM_Messaging",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "app messaging channel"
            notificationManager!!.createNotificationChannel(channel)
        }




        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            0
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_PRIMARY)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.logo)
            .setColor(ContextCompat.getColor(this ,R.color.colorAccent))
            .setContentIntent(pendingIntent)


        notificationManager?.notify(110, builder.build())




    }


}