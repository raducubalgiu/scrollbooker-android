import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.navigation.LocalTabsController
import com.example.scrollbooker.navigation.bottomBar.BottomBarItem
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.BackgroundDark
import com.example.scrollbooker.ui.theme.Divider
import androidx.compose.runtime.getValue

@Composable
fun BottomBar(
    appointmentsNumber: Int,
    notificationsNumber: Int,
) {
    val tabs = LocalTabsController.current
    val currentTab by tabs.currentTab.collectAsState()

    val allTabs = MainTab.allTabs
    val isFeedTab = currentTab == MainTab.Feed

    val dividerColor = if (isFeedTab) Color(0xFF3A3A3A) else Divider
    val containerColor = if(isFeedTab) BackgroundDark else Background

    Column(modifier = Modifier.height(90.dp)) {
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
                        notificationsNumber = notificationsNumber,
                        modifier = Modifier.then(Modifier.weight(1f)),
                        onNavigate = { tabs.setTab(tab) },
                        isSelected = currentTab == tab,
                        isFeedTab = isFeedTab,
                        tab = tab
                    )
                }
            }
        }
    }
}