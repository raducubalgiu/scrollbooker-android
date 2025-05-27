package com.example.scrollbooker.core.nav.host

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.inputs.SearchBar
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun AppDrawer() {
    var search by remember { mutableStateOf("") }

    BoxWithConstraints {
        val drawerWidth = maxWidth * 0.7f

        ModalDrawerSheet(
            modifier = Modifier.width(drawerWidth),
            drawerContainerColor = SurfaceBG
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(BasePadding)
            ) {
                SearchBar(
                    value = search,
                    containerColor = Background,
                    contentColor = OnSurfaceBG,
                    onValueChange = { search = it },
                    placeholder = "Ce cauti?",
                    onSearch = {}
                )

                HorizontalDivider(modifier = Modifier
                    .padding(vertical = BasePadding),
                    color = Divider
                )
            }
        }
    }
}