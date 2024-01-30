package com.rempawl.respolhpl.productDetails

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Fragment.observeOnStart(observers: suspend CoroutineScope.() -> Unit) =
    lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            observers()
        }
    }

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, duration).show()
}

fun Fragment.navigate(destination: NavDirections) = with(findNavController()) {
    currentDestination?.getAction(destination.actionId)
        ?.let { navigate(destination) }
}
