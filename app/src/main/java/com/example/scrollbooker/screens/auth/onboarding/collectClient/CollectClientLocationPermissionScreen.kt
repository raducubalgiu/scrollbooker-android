package com.example.scrollbooker.screens.auth.onboarding.collectClient

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import com.example.scrollbooker.components.core.layout.FormLayout

@SuppressLint("MissingPermission")
@Composable
fun CollectClientLocationPermissionScreen(
    viewModel: CollectClientLocationPermissionViewModel,
    onNext: () -> Unit
) {
//    val context = LocalContext.current
//    val permission = Manifest.permission.ACCESS_FINE_LOCATION
//
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//        if(isGranted) {
//            if(ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
//                val locationClient = LocationServices.getFusedLocationProviderClient(context)
//
//                locationClient.lastLocation.addOnSuccessListener { location ->
//                    Timber.tag("Location!").e("LAT: ${location.latitude}")
//                    Timber.tag("Location!").e("LAT: ${location.longitude}")
//
////                    viewModel.submitLocationUpdate(
////                        hasPermission = true,
////                        lat = location.latitude,
////                        lng = location.longitude,
////                        onSuccess = {},
////                        onError = {}
////                    )
//                }
//            }
//        } else {
//            val permanentlyDenied = !ActivityCompat.shouldShowRequestPermissionRationale(
//                context as Activity,
//                permission
//            )
//            if (permanentlyDenied) {
//                context.startActivity(
//                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
//                        data = Uri.fromParts("package", context.packageName, null)
//                    }
//                )
//            } else {
//                viewModel.submitLocationUpdate(
//                    hasPermission = false,
//                    lat = null,
//                    lng = null,
//                    onSuccess = {},
//                    onError = {}
//                )
//            }
//        }
//    }
//
    FormLayout(
        enableBack = false,
        isEnabled = true,
        isLoading = false,
        headLine = "Permisiunea locației",
        subHeadLine = "Ne ajută să îți oferim servicii din apropiere și să îți personalizăm experiența",
        buttonTitle = "Save",
        onBack = {},
        onNext = {
            //launcher.launch(permission)
        }
    ) {

    }
}