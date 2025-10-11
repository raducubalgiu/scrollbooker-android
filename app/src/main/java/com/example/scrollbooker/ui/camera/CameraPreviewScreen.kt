package com.example.scrollbooker.ui.camera
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun CameraPreviewScreen(
    viewModel: CameraViewModel,
    //controller: LifecycleCameraController,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
//    AndroidView(
//        factory = {
//            PreviewView(it).apply {
//                this.controller = controller
//                controller.bindToLifecycle(lifecycleOwner)
//            }
//        },
//        modifier = modifier
//    )
}