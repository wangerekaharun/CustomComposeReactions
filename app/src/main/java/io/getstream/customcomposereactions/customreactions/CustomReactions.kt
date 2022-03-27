package io.getstream.customcomposereactions.customreactions

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.getstream.chat.android.client.models.Message
import io.getstream.chat.android.client.models.Reaction
import io.getstream.chat.android.common.state.MessageAction
import io.getstream.chat.android.common.state.React
import io.getstream.chat.android.compose.state.reactionoptions.ReactionOptionItemState
import io.getstream.chat.android.compose.ui.theme.ChatTheme

@Composable
internal fun CustomReactionOptions(
    message: Message,
    reactionTypes: Map<String, Int>,
    onMessageAction: (MessageAction) -> Unit,
) {
    ReactionOptions(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 20.dp),
        reactionTypes = reactionTypes,
        onReactionOptionSelected = {
            onMessageAction(
                React(
                    reaction = Reaction(messageId = message.id, type = it.type),
                    message = message
                )
            )
        },
        ownReactions = message.ownReactions,
    )
}

@Composable
fun ReactionOptions(
    ownReactions: List<Reaction>,
    onReactionOptionSelected: (ReactionOptionItemState) -> Unit,
    modifier: Modifier = Modifier,
    numberOfReactionsShown: Int = 5,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    reactionTypes: Map<String, Int> = ChatTheme.reactionTypes,
    itemContent: @Composable RowScope.(ReactionOptionItemState) -> Unit = { option ->
        DefaultReactionOptionItem(
            option = option,
            onReactionOptionSelected = onReactionOptionSelected
        )
    },
) {
    val options = reactionTypes.entries.map { (type, drawable) ->
        ReactionOptionItemState(
            painter = painterResource(id = drawable),
            isSelected = ownReactions.any { ownReaction -> ownReaction.type == type },
            type = type
        )
    }

    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement
    ) {
        options.take(numberOfReactionsShown).forEach { option ->
            key(option.type) {
                itemContent(option)
            }
        }
    }
}

/**
 * The default reaction option item.
 *
 * @param option The represented option.
 * @param onReactionOptionSelected The handler when the option is selected.
 */
@Composable
internal fun DefaultReactionOptionItem(
    option: ReactionOptionItemState,
    onReactionOptionSelected: (ReactionOptionItemState) -> Unit,
) {
    var animationState by remember { mutableStateOf(ReactionState.START) }

    val springValue: Float by animateFloatAsState(
        if (animationState == ReactionState.START) 0f else 1f,
        spring(dampingRatio = 0.10f, stiffness = Spring.StiffnessLow)
    )

    LaunchedEffect(Unit) {
        animationState = ReactionState.END
    }

    CustomReactionOptionItem (
        option = option,
        springValue = springValue,
        onReactionOptionSelected = { onReactionOptionSelected(option) }

    )
}

enum class ReactionState {
    START,
    END
}