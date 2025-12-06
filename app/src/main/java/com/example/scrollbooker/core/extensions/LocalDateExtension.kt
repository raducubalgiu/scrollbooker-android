import android.os.Build
import androidx.annotation.RequiresApi
import org.threeten.bp.LocalDate
import org.threeten.bp.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toPrettyDate(): String {
    val locale = Locale.getDefault()

    val dayName = this.dayOfWeek
        .getDisplayName(TextStyle.FULL, locale)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }

    val monthShort = this.month
        .getDisplayName(TextStyle.SHORT, locale)
        .lowercase(locale)
        .replace(".", "")
        .replaceFirstChar { it.lowercase(locale) }

    return "$dayName ${this.dayOfMonth} $monthShort"
}