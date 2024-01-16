package com.rempawl.respolhpl.utils

import androidx.core.text.HtmlCompat
import javax.inject.Inject

class HtmlParser @Inject constructor() {

    fun parse(html: String): String =
        HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
}
