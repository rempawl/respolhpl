package com.rempawl.respolhpl.utils.extensions

import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.WindowInsets
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.rempawl.respolhpl.R
import kotlin.math.roundToInt

fun Resources.dpToPx() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, displayMetrics)

fun Int.dpToPx(resources: Resources): Int = resources.dpToPx().times(this).toInt()

fun Int.pxToDp(resources: Resources): Int = (this / resources.dpToPx()).roundToInt()

fun View.addStatusBarPaddingForAndroid15(insets: WindowInsets): WindowInsets {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
        val topPadding = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            .pxToDp(resources) + resources.getDimension(R.dimen.def_margin).roundToInt()
        this.updateLayoutParams<ConstraintLayout.LayoutParams> {
            height =
                resources.getDimension(R.dimen.toolbar_height).roundToInt() + topPadding
        }
        this.updatePadding(top = topPadding)
    }
    return insets
}
