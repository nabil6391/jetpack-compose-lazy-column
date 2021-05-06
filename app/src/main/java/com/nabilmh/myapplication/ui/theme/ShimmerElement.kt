package com.nabilmh.myapplication.ui.theme

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Shimmer(
    elementHeight: Dp,
    padding: Dp = 16.dp,
    content: @Composable() (Modifier) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val elementWidthPx = with(LocalDensity.current) { (maxWidth - (padding * 2)).toPx() }
        val elementHeightPx = with(LocalDensity.current) { (elementHeight - padding).toPx() }
        val gradientWidth: Float = (0.2f * elementHeightPx)

        val infiniteTransition = rememberInfiniteTransition()
        val xElementShimmer = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = (elementWidthPx + gradientWidth),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                    easing = LinearEasing,
                    delayMillis = 500
                ),
                repeatMode = RepeatMode.Restart
            )
        )
        val yElementShimmer = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = (elementHeightPx + gradientWidth),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                    easing = LinearEasing,
                    delayMillis = 500
                ),
                repeatMode = RepeatMode.Restart
            )
        )

        val colors = listOf(Purple200.copy(alpha = .9f), Purple200.copy(alpha = .3f), Purple200.copy(alpha = .9f),)

        val brush = Brush.linearGradient(
            colors,
            start = Offset(xElementShimmer.value - gradientWidth,  yElementShimmer.value - gradientWidth),
            end = Offset(xElementShimmer.value,  yElementShimmer.value)
        )

        val modifier = Modifier.background(brush = brush)
        content(modifier)
    }
}
