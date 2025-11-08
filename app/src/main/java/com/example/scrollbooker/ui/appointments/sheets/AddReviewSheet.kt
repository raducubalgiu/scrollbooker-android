package com.example.scrollbooker.ui.appointments.sheets
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.inputs.EditInput
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.entity.booking.review.data.remote.ReviewLabel
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.headlineMedium
import com.example.scrollbooker.ui.theme.titleMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReviewSheet(
    sheetState: SheetState,
    selectedRating: Int?,
    onRatingClick: (Int) -> Unit,
    onClose: () -> Unit
) {
    var review by rememberSaveable { mutableStateOf("") }
    val ratingLabel = selectedRating?.let { ReviewLabel.fromValue(it) }?.labelRes
    val ratingLabelRes = ratingLabel?.let { stringResource(it) }

    Sheet(
        modifier = Modifier.statusBarsPadding(),
        sheetState = sheetState,
        onClose = onClose
    ) {
        Column(Modifier.fillMaxSize()) {
            SheetHeader(
                onClose = onClose
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = SpacingXL)
                    .imePadding()
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(5) { index ->
                        val fill = selectedRating != null && index <= selectedRating

                        Box(
                            modifier = Modifier.size(50.dp).clickable { onRatingClick(index) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(37.5.dp),
                                imageVector = if(fill) Icons.Default.Star else Icons.Outlined.StarOutline,
                                contentDescription = null,
                                tint = if(fill) Primary else Divider
                            )
                        }
                    }
                }

                Spacer(Modifier.height(SpacingM))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${selectedRating?.plus(1)} ${stringResource(R.string.from5)}",
                        color = Color.Gray
                    )

                    Text(
                        modifier = Modifier.padding(horizontal = SpacingM),
                        text = "•"
                    )

                    Text(
                        text = ratingLabelRes ?: "",
                        style = titleMedium,
                        fontWeight = FontWeight.SemiBold

                    )
                }

                Spacer(Modifier.height(SpacingXL))

                Text(
                    style = headlineMedium,
                    color = OnBackground,
                    fontWeight = FontWeight.ExtraBold,
                    text = stringResource(R.string.howWasYourExperience)
                )

                Spacer(Modifier.height(SpacingXXS))

                Text(
                    style = bodyLarge,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    text = stringResource(R.string.howWasYourExperienceDescription),
                )

                Spacer(Modifier.height(SpacingXL))

                Column(Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.yourReview),
                        style = titleMedium
                    )

                    Spacer(Modifier.height(SpacingM))

                    EditInput(
                        value = review,
                        onValueChange = { review = it },
                        placeholder = stringResource(R.string.shareSomeDetailsAboutYourExperience),
                        singleLine = false,
                        minLines = 5,
                        maxLines = 5,
                        maxLength = 200
                    )

                    Spacer(Modifier.height(BasePadding))

                    MainButton(
                        modifier = Modifier.padding(bottom = BasePadding),
                        title = stringResource(R.string.add),
                        onClick = {},
                        enabled = false
                    )
                }
            }
        }
    }
}