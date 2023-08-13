package com.example.respolhpl

import android.content.Intent
import android.net.Uri
import com.example.respolhpl.notification.NotificationHelper
import com.example.respolhpl.utils.extensions.getSerializable
import javax.inject.Inject

class DeepLinkHandler @Inject constructor() {

    fun checkIntent(intent: Intent): DeepLink? {
        return intent.getSerializable<HashMap<String, String>>(
            NotificationHelper.KEY_NOTIFICATION_PAYLOAD,
        )?.let { handleNotification(it) }
    }

    private fun handleNotification(payload: HashMap<String, String>): DeepLink? {
        return if (payload.containsKey(KEY_DEEPLINK)) {
            getDeeplinkFromPayload(payload)
        } else {
            null
        }
    }

    private fun getDeeplinkFromPayload(payload: HashMap<String, String>) =
        payload[KEY_DEEPLINK]?.let {
            val uri = Uri.parse(it)
            getDeeplinkFromUri(uri)
        }

    private fun getDeeplinkFromUri(uri: Uri) = when (uri.host) {
        "product" -> DeepLink.Product(uri.pathSegments.first().toInt())
        else -> null
    }


    sealed interface DeepLink {
        data class Product(val id: Int) : DeepLink
    }

    companion object {
        const val KEY_DEEPLINK = "deeplink"
    }
}