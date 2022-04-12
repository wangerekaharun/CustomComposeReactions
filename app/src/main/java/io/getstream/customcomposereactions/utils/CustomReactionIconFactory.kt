package io.getstream.customcomposereactions.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import io.getstream.chat.android.compose.ui.util.ReactionDrawable
import io.getstream.chat.android.compose.ui.util.ReactionIcon
import io.getstream.chat.android.compose.ui.util.ReactionIconFactory
import io.getstream.customcomposereactions.R

class CustomReactionIconFactory(
    private val customReactions: Map<String, ReactionDrawable> = mapOf(
        "happy" to ReactionDrawable(
            iconResId = R.drawable.ic_baseline_emoji_emotions_24,
            selectedIconResId = R.drawable.ic_selected_baseline_emoji_emotions_24
        ),
        "rocket" to ReactionDrawable(
            iconResId = R.drawable.ic_baseline_rocket_launch_24,
            selectedIconResId = R.drawable.ic_selected_baseline_rocket_launch_24
        ),
        "sad" to ReactionDrawable(
            iconResId = R.drawable.ic_baseline_emoji_emotions_24,
            selectedIconResId = R.drawable.ic_selected_baseline_sentiment_very_dissatisfied_24
        ),
        "plus_one" to ReactionDrawable(
            iconResId = R.drawable.ic_baseline_exposure_plus_1_24,
            selectedIconResId = R.drawable.ic_selected_baseline_exposure_plus_1_24
        ),
        "celebration" to ReactionDrawable(
            iconResId = R.drawable.ic_baseline_celebration_24,
            selectedIconResId = R.drawable.ic_selected_baseline_celebration_24
        )
    )
) : ReactionIconFactory {
    @Composable
    override fun createReactionIcon(type: String): ReactionIcon {
        val reactionDrawable = requireNotNull(customReactions[type])
        return ReactionIcon(
            painter = painterResource(reactionDrawable.iconResId),
            selectedPainter = painterResource(reactionDrawable.selectedIconResId)
        )
    }

    @Composable
    override fun createReactionIcons(): Map<String, ReactionIcon> {
        return customReactions.mapValues {
            createReactionIcon(it.key)
        }
    }

    override fun isReactionSupported(type: String): Boolean {
        return customReactions.containsKey(type)
    }
}