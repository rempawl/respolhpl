package com.rempawl.respolhpl

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.rempawl.respolhpl.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainActivityViewModel>()

    @Inject
    lateinit var deepLinkHandler: DeepLinkHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.setupBinding()
        setContentView(binding.root)
        requestNotificationsPermission()
        checkIntentForDeepLinks()
    }

    private fun checkIntentForDeepLinks() {
        deepLinkHandler.getDeeplinkFrom(intent)?.let { deepLink ->
            when (deepLink) {
                is DeepLinkHandler.DeepLink.Product -> {
                    findNavController(R.id.nav_host_fragment)
                        .navigate(
                            R.id.product_details,
                            bundleOf(KEY_PRODUCT_ID to deepLink.id)
                        )
                }
            }
        }
    }

    private fun requestNotificationsPermission() {
        // todo one time event..
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED && VERSION.SDK_INT >= VERSION_CODES.TIRAMISU
        ) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1237)
        }
    }

    private fun ActivityMainBinding.setupBinding() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isConnected.collectLatest { connectionInfo.isVisible = !it }
            }
        }
    }

    companion object {
        const val KEY_PRODUCT_ID = "productId"
    }
}
