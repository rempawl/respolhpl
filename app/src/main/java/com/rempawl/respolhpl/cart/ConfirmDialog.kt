package com.rempawl.respolhpl.cart

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.rempawl.respolhpl.R

class ConfirmDialog : DialogFragment() {
    private var title: String = ""
    private var onConfirm: (() -> Unit)? = null

    companion object {
        fun newInstance(title: String, onConfirm: () -> Unit): ConfirmDialog {
            return ConfirmDialog().apply {
                this.onConfirm = onConfirm
                this.title = title
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                    onConfirm?.invoke()
                    dismissAllowingStateLoss()
                }
                .setNegativeButton(getString(R.string.cancel)) { _, _ -> dismissAllowingStateLoss() }
                .create()
        } ?: throw IllegalStateException()
    }
}
