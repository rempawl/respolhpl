package com.example.respolhpl.cart

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.respolhpl.R

class ConfirmDialog(
    private val title: String,
    private val onConfirm: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setPositiveButton(getString(R.string.confirm)) { _, _ -> onConfirm() }
                .setNegativeButton(getString(R.string.cancel)) { _, _ -> dismiss() }
                .create()
        } ?: throw IllegalStateException()

    }
}
