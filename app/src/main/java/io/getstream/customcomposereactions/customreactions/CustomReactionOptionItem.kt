package io.getstream.customcomposereactions.customreactions

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import io.getstream.chat.android.compose.state.reactionoptions.ReactionOptionItemState
import io.getstream.chat.android.compose.ui.theme.ChatTheme

@Composable
fun CustomReactionOptionItem(
    option: ReactionOptionItemState,
    springValue: Float,
    onReactionOptionSelected: (ReactionOptionItemState) -> Unit
) {
    var currentState by remember { mutableStateOf(ReactionButtonState.IDLE) }
    val normalIconSize = 50.dp
    val animatedIconSize = 80.dp
    val sizeAnimation by animateDpAsState(
        if(currentState == ReactionButtonState.ACTIVE) 50.1.dp else 50.dp,
        animationSpec = keyframes {
            durationMillis = 500
            animatedIconSize.at(100)
            normalIconSize.at(200)
        },
        finishedListener = {
            onReactionOptionSelected(option)
        }
    )

    Icon(
        modifier = Modifier
            .size(size = sizeAnimation)
            .scale(springValue)
            .offset(x = (-15).dp + (15 * springValue).dp)
            .rotate(-45f + (45 * springValue))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false),
                onClick = {
                    currentState = if(currentState == ReactionButtonState.IDLE)
                        ReactionButtonState.ACTIVE else ReactionButtonState.IDLE
                }
            ),
        painter = option.painter,
        contentDescription = option.type,
        tint = if (option.isSelected) ChatTheme.colors.primaryAccent else ChatTheme.colors.textLowEmphasis
    )
}

enum class ReactionButtonState {
    IDLE, ACTIVE
}