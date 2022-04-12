package io.getstream.customcomposereactions.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import io.getstream.chat.android.compose.ui.util.ReactionDrawable
import io.getstream.chat.android.compose.ui.util.ReactionIcon
import io.getstream.chat.android.compose.ui.util.ReactionIconFactory
import io.getstream.customcomposereactions.R

class CustomReactionsFactory(
    private val customReactions: Map<String, ReactionDrawable> = mapOf(
        "reaction_one" to ReactionDrawable(
            iconResId = R.drawable.ic_baseline_emoji_emotions_24,
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
        TODO("Not yet implemented")
    }
}