package com.android.sdvdemo.ui

import android.car.Car
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SdvHomeView : Fragment() {

    private val viewModel: SdvHomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {

        lifecycle.addObserver(viewModel)

        setContent {
            MaterialTheme {
                SdvHome(viewModel = viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requestPermissionsIfNeeded()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_SPEED -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.connectCarApi()
                } else {
                    Log.d("SdvHomeView", "onRequestPermissionsResult: Permission not granted to access car speed")
                }
                return
            }
            else -> {}
        }
    }

    private fun requestPermissionsIfNeeded() {
        Log.d("SdvHomeView", "requestPermissionsIfNeeded")
        when {
            context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Car.PERMISSION_SPEED
                )
            } == PackageManager.PERMISSION_GRANTED -> {
                Log.d("SdvHomeView", "requestPermissionsIfNeeded: Permission already granted")
                viewModel.connectCarApi()
            }
            shouldShowRequestPermissionRationale("android.car.permission.CAR_SPEED") -> {
                Log.d("SdvHomeView", "shouldShowRequestPermissionRationale")
            }
            else -> {
                Log.d("SdvHomeView", "android.car.permission.CAR_SPEED permission requested")
                requestPermissions(arrayOf("android.car.permission.CAR_SPEED"), REQUEST_CODE_SPEED)
            }
        }
    }

    private companion object {
        const val REQUEST_CODE_SPEED = 1
    }
}
