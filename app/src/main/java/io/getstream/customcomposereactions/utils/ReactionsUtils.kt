package io.getstream.customcomposereactions.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import io.getstream.chat.android.compose.ui.util.ReactionDrawable
import io.getstream.chat.android.compose.ui.util.ReactionIcon
import io.getstream.customcomposereactions.R


@Composable
fun customReactionIcons(): Map<String, ReactionIcon> {
    return mapOf(
        "happy" to ReactionIcon(
            painter = painterResource(id = R.drawable.ic_baseline_emoji_emotions_24),
            selectedPainter = painterResource(id = R.drawable.ic_selected_baseline_emoji_emotions_24)
        ),
        "rocket" to ReactionIcon(
            painter = painterResource(id = R.drawable.ic_baseline_rocket_launch_24),
            selectedPainter = painterResource(id = R.drawable.ic_selected_baseline_rocket_launch_24),
        ),

        "sad" to ReactionIcon(
            painter = painterResource(id = R.drawable.ic_baseline_sentiment_very_dissatisfied_24),
            selectedPainter = painterResource(id = R.drawable.ic_selected_baseline_sentiment_very_dissatisfied_24)
        ),
        "plus_one" to ReactionIcon(
            painter = painterResource(id = R.drawable.ic_baseline_exposure_plus_1_24),
            selectedPainter = painterResource(id = R.drawable.ic_selected_baseline_exposure_plus_1_24)
        ),
        "celebration" to ReactionIcon(
            painter = painterResource(id = R.drawable.ic_baseline_celebration_24),
            selectedPainter = painterResource(id = R.drawable.ic_selected_baseline_celebration_24)
        )
    )


}


