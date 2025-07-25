import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.navigation.bottomBar.BottomBarItem
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun BottomBar(
    appointmentsNumber: Int,
    currentTab: MainTab,
    currentRoute: String?,
    onNavigate: (MainTab) -> Unit
) {
    val allTabs = MainTab.allTabs
    val isFeedTab = currentTab == MainTab.Feed

    val isVisible = remember(currentTab, currentRoute) {
        currentTab in MainTab.allTabs
    }

    val dividerColor = if (isFeedTab) Color(0xFF3A3A3A) else Divider
    val containerColor = if(isFeedTab) Color(0xFF121212) else Background

    if(isVisible) {
        Column(Modifier.height(90.dp)) {
            HorizontalDivider(color = dividerColor, thickness = 1.dp)
            NavigationBar(
                tonalElevation = 0.dp,
                modifier = Modifier.fillMaxWidth(),
                containerColor = containerColor
            ) {
                Row(modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 5.dp)
                ) {
                    allTabs.forEach { tab ->
                        BottomBarItem(
                            appointmentsNumber = appointmentsNumber,
                            modifier = Modifier.then(Modifier.weight(1f)),
                            onNavigate = { onNavigate(tab) },
                            isSelected = currentTab == tab,
                            isFeedTab = isFeedTab,
                            tab = tab
                        )
                    }
                }
            }
        }
    }

//    AnimatedVisibility(
//        visible = isVisible,
//        enter = fadeIn() + slideInVertically { it },
//        exit = fadeOut() + slideOutVertically { it }
//    ) {
//        Column(Modifier.height(90.dp)) {
//            HorizontalDivider(color = dividerColor, thickness = 1.dp)
//            NavigationBar(
//                tonalElevation = 0.dp,
//                modifier = Modifier.fillMaxWidth(),
//                containerColor = containerColor
//            ) {
//                Row(modifier = Modifier
//                    .fillMaxSize()
//                    .padding(vertical = 5.dp)
//                ) {
//                    allTabs.forEach { tab ->
//                        BottomBarItem(
//                            appointmentsNumber = appointmentsNumber,
//                            modifier = Modifier.then(Modifier.weight(1f)),
//                            onNavigate = { onNavigate(tab) },
//                            isSelected = currentTab == tab,
//                            isFeedTab = isFeedTab,
//                            tab = tab
//                        )
//                    }
//                }
//            }
//        }
//    }
}