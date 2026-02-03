package com.example.scrollbooker.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

@Composable
fun rememberCollapsingNestedScroll(
    headerHeightPx: Int,
    headerOffset: Float,
    onHeaderOffsetChanged: (Float) -> Unit,
): NestedScrollConnection {

    val currentOffset = rememberUpdatedState(headerOffset)
    val onOffsetChanged = rememberUpdatedState(onHeaderOffsetChanged)

    val min = remember(headerHeightPx) { -headerHeightPx.toFloat() }
    val max = 0f

    return remember(headerHeightPx) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y

                val offset = currentOffset.value
                if (delta < 0f && offset > min) {
                    val newOffset = (offset + delta).coerceIn(min, max)
                    val consumed = newOffset - offset
                    onOffsetChanged.value(newOffset)
                    return Offset(0f, consumed)
                }
                return Offset.Zero
            }

            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y

                val offset = currentOffset.value
                if (delta > 0f && offset < max) {
                    val newOffset = (offset + delta).coerceIn(min, max)
                    val consumedByHeader = newOffset - offset
                    onOffsetChanged.value(newOffset)
                    return Offset(0f, consumedByHeader)
                }
                return Offset.Zero
            }
        }
    }
}