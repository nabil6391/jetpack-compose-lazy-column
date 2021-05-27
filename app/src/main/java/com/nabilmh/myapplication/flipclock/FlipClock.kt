import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.ceil

@Composable
fun FlipClock(seconds: Int) {
    val animatedSeconds by animateFloatAsState(targetValue = seconds.toFloat())

    val currentSeconds = ceil(animatedSeconds).toInt()
    val nextSeconds = currentSeconds - 1
    val factor = currentSeconds.toFloat() - animatedSeconds
    val currentParts = getTimeParts(currentSeconds)
    val nextParts = getTimeParts(nextSeconds)

    Row {
        FlapSection(
            currentValue = currentParts.hours,
            nextValue = nextParts.hours,
            factor = if (currentParts.hours == nextParts.hours) 0F else factor
        )

        Spacer(modifier = Modifier.width(16.dp))

        FlapSection(
            currentValue = currentParts.minutes,
            nextValue = nextParts.minutes,
            factor = if (currentParts.minutes == nextParts.minutes) 0F else factor
        )

        Spacer(modifier = Modifier.width(16.dp))

        FlapSection(
            currentValue = currentParts.seconds,
            nextValue = nextParts.seconds,
            factor = if (currentParts.seconds == nextParts.seconds) 0F else factor
        )
    }
}

@Composable
fun FlapSection(
    currentValue: Int,
    nextValue: Int,
    factor: Float
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Flaps(
            currentText = currentValue.toString().padStart(2, '0'),
            nextText = nextValue.toString().padStart(2, '0'),
            factor = factor
        )
    }
}

fun getTimeParts(seconds: Int): TimeParts {
    val partHours = seconds / 60 / 60
    val partMinutes = (seconds - partHours * 60 * 60) / 60
    val partSeconds = seconds - partHours * 60 * 60 - partMinutes * 60

    return TimeParts(
        hours = partHours,
        minutes = partMinutes,
        seconds = partSeconds
    )
}

class TimeParts(
    val hours: Int,
    val minutes: Int,
    val seconds: Int
)
