package com.mirz.common.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mirz.approval.core.common.R
import com.mirz.common.ui.theme.AppTheme
import com.mirz.common.ui.theme.NavyDark
import com.mirz.common.ui.theme.TealLight
import com.mirz.common.ui.theme.TealMint
import kotlin.math.roundToInt

private val ThumbSize = 48.dp
private val ButtonHeight = 56.dp
private const val ApproveThresholdFraction = 0.80f

@Composable
fun AppSlideButton(
    onApproved: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
) {
    var trackWidthPx by remember { mutableFloatStateOf(0f) }
    var dragOffsetPx by remember { mutableFloatStateOf(0f) }
    var isApproved by remember { mutableStateOf(false) }

    val thumbSizePx = with(androidx.compose.ui.platform.LocalDensity.current) { ThumbSize.toPx() }
    val paddingPx = with(androidx.compose.ui.platform.LocalDensity.current) { 4.dp.toPx() }
    val maxDrag = (trackWidthPx - thumbSizePx - paddingPx * 2).coerceAtLeast(0f)

    val animatedOffset by animateFloatAsState(
        targetValue = if (isApproved) maxDrag else dragOffsetPx.coerceIn(0f, maxDrag),
        animationSpec = tween(durationMillis = 300),
        label = "thumb_offset",
    )

    val bgColor by animateColorAsState(
        targetValue = if (isApproved) TealLight else TealMint,
        animationSpec = tween(durationMillis = 300),
        label = "bg_color",
    )

    LaunchedEffect(isApproved) {
        if (isApproved) onApproved()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(ButtonHeight)
            .clip(RoundedCornerShape(28.dp))
            .background(bgColor)
            .onGloballyPositioned { trackWidthPx = it.size.width.toFloat() },
    ) {
        // Track label
        Text(
            text = if (isApproved) stringResource(R.string.approved) else stringResource(R.string.slide_to_approve),
            modifier = Modifier.align(Alignment.Center),
            color = Color.White.copy(alpha = if (isApproved) 1f else 0.85f),
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
        )

        // Draggable thumb
        Box(
            modifier = Modifier
                .padding(4.dp)
                .offset { IntOffset((animatedOffset + paddingPx).roundToInt(), 0) }
                .size(ThumbSize)
                .clip(CircleShape)
                .background(NavyDark)
                .draggable(
                    state = rememberDraggableState { delta ->
                        if (!isApproved && isEnabled) {
                            dragOffsetPx = (dragOffsetPx + delta).coerceIn(0f, maxDrag)
                            if (dragOffsetPx >= maxDrag * ApproveThresholdFraction) {
                                isApproved = true
                            }
                        }
                    },
                    orientation = Orientation.Horizontal,
                    enabled = isEnabled && !isApproved,
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (isApproved) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Approved",
                    tint = Color.White,
                )
            } else {
                Text(
                    text = ">>",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppSlideButtonPreview() {
    AppTheme {
        AppSlideButton(
            onApproved = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
