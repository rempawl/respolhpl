package com.rempawl.respolhpl.notification

import com.rempawl.respolhpl.utils.log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseMessagingServiceImpl : FirebaseMessagingService() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        log { "kruci messaging token= $token" }
        // todo someday send token to server to associate with user..
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        with(message) {
            notificationHelper.showNotification(
                title = notification?.title,
                body = notification?.body,
                dataPayload = HashMap(data)
            )
        }
    }
}