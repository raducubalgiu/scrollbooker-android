package com.example.scrollbooker.ui.profile.tabs.about
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.customized.SchedulesSection
import com.example.scrollbooker.components.customized.SectionMap
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.rememberFlingBehavior
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfileAbout
import com.example.scrollbooker.navigation.navigators.UserProfileParam
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun ProfileAboutTab(
    isEmployee: Boolean,
    about: FeatureState<UserProfileAbout>,
    onNavigateToUserProfile: (param: UserProfileParam) -> Unit
) {
    val flingBehavior = rememberFlingBehavior()

    when (val aboutState = about) {
        is FeatureState.Error -> ErrorScreen()

        is FeatureState.Loading -> {
            LoadingScreen(
                modifier = Modifier.padding(top = 50.dp),
                arrangement = Arrangement.Top
            )
        }

        is FeatureState.Success -> {
            val data = aboutState.data

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = BasePadding),
                flingBehavior = flingBehavior
            ) {
                if (isEmployee) {
                    item(key = "profile_owner_section") {
                        ProfileInfoOwnerSection(
                            owner = data.owner,
                            onNavigateToUserProfile = onNavigateToUserProfile
                        )
                    }
                }

                item(key = "address_section") {
                    Text(
                        modifier = Modifier.padding(vertical = BasePadding),
                        text = stringResource(R.string.address),
                        fontWeight = FontWeight.SemiBold,
                        style = titleMedium,
                        fontSize = 18.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_location_outline),
                            contentDescription = null
                        )

                        Spacer(Modifier.width(BasePadding))

                        Text(text = data.location.address)
                    }
                }

                data.description?.let { descriptionText ->
                    item(key = "description_section") {
                        Text(
                            modifier = Modifier.padding(
                                top = SpacingXXL,
                                bottom = BasePadding
                            ),
                            text = stringResource(R.string.description),
                            style = titleMedium,
                            fontSize = 18.sp
                        )
                        Text(text = descriptionText)
                    }
                }

                data.location.mapUrl?.let { url ->
                    item(key = "map_section") {
                        Spacer(Modifier.height(BasePadding))
                        SectionMap(
                            mapUrl = url,
                            coordinates = data.location.coordinates,
                            fullName = data.owner.fullName,
                            displayDirectionsButton = false
                        )
                    }
                }

                item(key = "schedule_section") {
                    Spacer(Modifier.height(SpacingXL))

                    Text(
                        text = stringResource(R.string.schedule),
                        fontWeight = FontWeight.SemiBold,
                        style = titleMedium,
                        fontSize = 18.sp
                    )

                    Spacer(Modifier.height(BasePadding))

                    SchedulesSection(schedules = data.schedules)
                }

                item(key = "gallery_section") {
                    Spacer(Modifier.height(SpacingXL))

                    Text(
                        text = stringResource(R.string.photoGallery),
                        fontWeight = FontWeight.SemiBold,
                        style = titleMedium,
                        fontSize = 18.sp
                    )

                    Spacer(Modifier.height(BasePadding))

                    BusinessMediaGallery(mediaFiles = data.businessMedia)

                    Spacer(Modifier.height(BasePadding))
                }
            }
        }
    }
}
