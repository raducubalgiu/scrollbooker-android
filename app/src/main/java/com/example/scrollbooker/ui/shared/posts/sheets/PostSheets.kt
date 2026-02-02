package com.example.scrollbooker.ui.shared.posts.sheets
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.shared.posts.sheets.bookings.BookingsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.bookings.BookingsSheetUser
import com.example.scrollbooker.ui.shared.posts.sheets.comments.CommentsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.moreOptions.MoreOptionsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.location.LocationSheet
import com.example.scrollbooker.ui.shared.posts.sheets.reviews.ReviewsSheet
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.titleMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostSheets(
    sheetState: SheetState,
    sheetContent: PostSheetsContent,
    onClose: () -> Unit
) {
    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        sheetState = sheetState,
        onDismissRequest = onClose,
        containerColor = Background,
        contentColor = OnBackground,
        dragHandle = {},
        contentWindowInsets = { BottomSheetDefaults.windowInsets },
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false
        ),
    ) {
        when (val content = sheetContent) {
            is PostSheetsContent.ReviewsSheet -> {
                ReviewsSheet(
                    userId = content.userId,
                    onClose = onClose,
                )
            }
            is PostSheetsContent.CommentsSheet -> {
                CommentsSheet(
                    postId = content.postId,
                    isSheetVisible = sheetState.isVisible,
                    onClose = onClose
                )
            }
            is PostSheetsContent.LocationSheet -> {
                LocationSheet(
                    onClose = onClose
                )
            }
            is PostSheetsContent.MoreOptionsSheet -> {
                MoreOptionsSheet(
                    onClose = onClose,
                    isOwnPost = content.isOwnPost
                )
            }
            is PostSheetsContent.BookingsSheet -> {
                val user = content.user

                BookingsSheet(
                    initialIndex = if(content.postId != null) 0 else 1,
                    postId = content.postId,
                    user = BookingsSheetUser(
                        id = user.id,
                        username = user.username,
                        fullName = user.fullName,
                        avatar = user.avatar,
                        profession = user.profession,
                        ratingsCount = user.ratingsCount,
                        ratingsAverage = user.ratingsAverage
                    ),
                    onClose = onClose
                )
            }
            is PostSheetsContent.PhoneSheet -> {
                SheetHeader(
                    title = stringResource(R.string.call),
                    onClose = onClose
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {}
                        .padding(BasePadding),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "+40 731 289 633",
                        style = titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.ic_call_outline),
                            contentDescription = null
                        )
                    }
                }
            }
            is PostSheetsContent.None -> Unit
        }
    }
}