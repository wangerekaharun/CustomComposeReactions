package io.getstream.customcomposereactions.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import io.getstream.chat.android.compose.ui.util.ReactionDrawable
import io.getstream.chat.android.compose.ui.util.ReactionIcon
import io.getstream.customcomposereactions.R


@Composable
fun CustomReactions(): Map<String, ReactionIcon> {
    return mapOf(
        "reaction_one" to ReactionIcon(
            painter = painterResource(id = R.drawable.ic_baseline_emoji_emotions_24),
            selectedPainter = painterResource(id = R.drawable.ic_baseline_emoji_emotions_24)
        ),
        "reaction_two" to ReactionIcon(
            painter = painterResource(id = R.drawable.ic_baseline_rocket_launch_24),
            selectedPainter = painterResource(id = R.drawable.ic_baseline_rocket_launch_24),
        ),

        "reaction_three" to ReactionIcon(
            painter = painterResource(id = R.drawable.ic_baseline_sentiment_very_dissatisfied_24),
            selectedPainter = painterResource(id = R.drawable.ic_baseline_sentiment_very_dissatisfied_24)
        ),
        "reaction_four" to ReactionIcon(
            painter = painterResource(id = R.drawable.ic_baseline_exposure_plus_1_24),
            selectedPainter = painterResource(id = R.drawable.ic_baseline_exposure_plus_1_24)
        ),
        "reaction_five" to ReactionIcon(
            painter = painterResource(id = R.drawable.ic_baseline_celebration_24),
            selectedPainter = painterResource(id = R.drawable.ic_baseline_celebration_24)
        )
    )


}


