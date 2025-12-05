import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R

@Composable
fun rememberLocationsCountText(count: Int): String {
    return when {
        count == 0 -> {
            stringResource(R.string.search_locations_zero)
        }
        count == 1 -> {
            stringResource(R.string.search_locations_one, count)
        }
        count in 1..19 -> {
            stringResource(R.string.search_locations_over_1_20, count)
        }
        count in 20..999 -> {
            stringResource(R.string.search_locations_over_20_999, count)
        }
        else -> {
            stringResource(R.string.search_locations_over_1000)
        }
    }
}