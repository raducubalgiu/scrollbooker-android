package com.example.scrollbooker.screens.auth.collectClientDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.InputRadio
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.core.enums.GenderTypeEnum
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun CollectClientGenderScreen(
    onBack: () -> Unit,
    onNext: () -> Unit,
) {
    FormLayout(
        headLine = stringResource(R.string.chooseYourGender),
        subHeadLine = stringResource(R.string.genderLabelDescription),
        buttonTitle = stringResource(R.string.nextStep),
        onBack = onBack,
        onNext = onNext
    ) {
        val genders = listOf(
            GenderTypeEnum.MALE,
            GenderTypeEnum.FEMALE,
            GenderTypeEnum.OTHER
        )
        var selectedGender by remember { mutableStateOf(GenderTypeEnum.OTHER) }

        LazyColumn {
            itemsIndexed(genders) { index, gender ->
                InputRadio(
                    selected = gender.key == selectedGender.key,
                    onSelect = { selectedGender = gender },
                    headLine = if(gender == GenderTypeEnum.OTHER) stringResource(R.string.preferNotToSay) else gender.key
                )

                if(index < genders.size) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = SpacingXXL)
                            .height(0.55.dp)
                            .background(Divider.copy(alpha = 0.5f))
                    )
                }
            }
        }
    }
}