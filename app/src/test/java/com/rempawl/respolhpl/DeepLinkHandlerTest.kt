package com.rempawl.respolhpl

import android.content.Intent
import android.net.Uri
import com.rempawl.respolhpl.notification.NotificationHelper.Companion.KEY_NOTIFICATION_PAYLOAD
import com.rempawl.respolhpl.utils.extensions.getSerializable
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DeepLinkHandlerTest {

    private fun mockNotificationIntent(payload: HashMap<String, String>? = null): Intent =
        mockk<Intent> {
            every { data } returns Uri.EMPTY
            every { this@mockk.getSerializable<HashMap<String, String>>(KEY_NOTIFICATION_PAYLOAD) } returns payload
        }

    private fun mockUriParseMethod(expectedArgument: String, resultUri: Uri) {
        mockkStatic(Uri::class)
        every { Uri.parse(expectedArgument) } returns resultUri
    }

    private fun mockUri(
        host: String? = null,
        pathSegments: List<String>? = null,
    ): Uri = mockk {
        every { this@mockk.host } returns host
        every { this@mockk.pathSegments } returns pathSegments
    }

    private fun createSUT() = DeepLinkHandler()

    @Test
    fun `when notification with product deeplink, then return product deeplink`() {
        val url = "respolhpl://product/1401"
        val payload = hashMapOf(DeepLinkHandler.KEY_DEEPLINK to url)
        val uri = mockUri("product", listOf("1401"))
        mockUriParseMethod(url, uri)
        val intent = mockNotificationIntent(payload)

        val sut = createSUT()
        val result = sut.checkIntent(intent)

        result.run {
            assertIs<DeepLinkHandler.DeepLink.Product>(result)
            assertEquals(1401, result.id)
        }
        unmockkStatic(Uri::class)
    }
}