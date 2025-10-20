package com.example.scrollbooker.ui.shared.posts.sheets.products
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.shared.posts.sheets.products.components.BookingSheetHeader
import com.example.scrollbooker.ui.shared.posts.sheets.products.tabs.CalendarTab
import com.example.scrollbooker.ui.shared.posts.sheets.products.tabs.ConfirmTab
import com.example.scrollbooker.ui.shared.posts.sheets.products.tabs.ProductsTab
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.launch

@Composable
fun ProductsSheet(
    userId: Int,
    onClose: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val steps = listOf(
        "Servicii",
        "Calendar",
        "Confirma"
    )

    val pagerState = rememberPagerState { steps.size }
    val currentStep = pagerState.currentPage

    Column(modifier = Modifier.fillMaxSize()) {
        BookingSheetHeader(
            stepTitle = steps[currentStep],
            fullName = "Frizeria Figaro",
            avatar = "",
            profession = "Frizerie",
            onClose = onClose,
            totalSteps = steps.size,
            currentStep = currentStep
        )

        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            VerticalPager(
                state = pagerState,
                beyondViewportPageCount = 0,
                userScrollEnabled = false
            ) { page ->
                when(page) {
                    0 -> ProductsTab(
                        userId = userId,
                        onNext = { scope.launch { pagerState.animateScrollToPage(page + 1) } },
                    )
                    1 -> CalendarTab(
                        userId = userId,
                        onSelectSlot = {}
                    )
                    2 -> ConfirmTab()
                }
            }
        }

        Column {
            HorizontalDivider(color = Divider, thickness = 0.55.dp)

            AnimatedContent(
                targetState = currentStep > 0,
                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
                label = "HeaderTransition"
            ) { target ->
                if(target) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(BasePadding),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MainButton(
                            modifier = Modifier.weight(0.5f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SurfaceBG,
                                contentColor = OnSurfaceBG
                            ),
                            title = "Inapoi",
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            }
                        )

                        Spacer(Modifier.width(SpacingS))

                        MainButton(
                            modifier = Modifier.weight(0.5f),
                            title = "Rezerva",
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(BasePadding),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Nici un produs selectat",
                            style = bodyLarge,
                            color = Color.Gray
                        )

                        Button(
                            shape = ShapeDefaults.Medium,
                            contentPadding = PaddingValues(
                                vertical = BasePadding,
                                horizontal = SpacingXL
                            ),
                            //enabled = false,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.next),
                                style = titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}