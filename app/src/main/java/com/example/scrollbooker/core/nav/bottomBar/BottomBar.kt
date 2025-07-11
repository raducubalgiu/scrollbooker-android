import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.nav.bottomBar.BottomBarItem
import com.example.scrollbooker.core.nav.bottomBar.MainTab
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun BottomBar(
    drawerState: DrawerState,
    currentTab: MainTab,
    currentRoute: String?,
    onNavigate: (MainTab) -> Unit
) {
    val allTabs = MainTab.allTabs

    val isFeedTab = currentTab == MainTab.Feed
    val bottomBarRoutes = MainTab.allTabs.map { it.route }

    val dividerColor = if (isFeedTab) Color(0xFF3A3A3A) else Divider
    val containerColor = if(isFeedTab) Color(0xFF121212) else Background

    val isVisible = currentRoute in bottomBarRoutes && !drawerState.isAnimationRunning && drawerState.isClosed

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically { it },
        exit = fadeOut() + slideOutVertically { it }
    ) {
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
}