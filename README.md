# Custom Reactions in Compose Chat SDK 

In this article, you'll learn how to add your custom reactions to Stream's Compose UI components. You'll use the [low-level Android client](https://github.com/GetStream/stream-chat-android/tree/main/stream-chat-android-client) library to send reactions.

With the new SDK v5, the ReactionsTypes API has been imporved to now support even more complext reactions. This version adds support for animated emojis or uploaded images just like other social media platforms. 

**Note**: The [Jetpack Compose UI components](https://getstream.io/blog/jetpack-compose-sdk/) are now stable. Check the full announcement [here](https://getstream.io/blog/android-v5-sdk-release/).  You can try out the new SDK in the [Jetpack Compose Chat Tutorial](https://getstream.io/chat/compose/tutorial/).

### Overriding Default Reactions

By Default the Stream Chat SDK has the following reactions: thumbs up/down, love, LOL. Users can react to messages with either of these reactions. As with all the components in the Chat SDK, you can override this default reactions and provide your own custom ones. There are two ways in which you can provide your own custom reactions:

- Using the `reactionIconFactory` property in `ChatTheme`.
- Using the `reactionTypes` property in the `SelectedReactionsMenu` or `SelectedMessageMenu`

First of all, you'll deep dive into the `ReactionIconFactory` class in the next section.

### Looking at `ReactionIconFactory`

This is a newly introduced class with the new version. This is the class that handles all the creation of the icons for all the reactions types. You can create your custom `ReactionIconFactory` by implementing this class as shown below:

```kotlin
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
```

To sum up what the code above does:

- You have a map of your custom reactions. Each reaction has it's `ReactionDrawable` which is a data class that holds the drawable resources for the reaction. It has the drawables for both the normal and selected state of the icon.
- The `ReactionIconFactory` requires you to override three methods:
  - `createReactionIcon` - creates a `ReactionIcon` for your reaction types. A `ReactionIcon` contains painters for your reaction in normal and selected states.
  - `createReactionIcons` - creates all the `ReactionIcon`'s for alll your reactions. It uses the `createReactionIcon` to create the icons.
  - `isReactionSupported` - checks in your reactions are supported by the SDK.

With this `CustomReactionIconFactory` you're now ready to support custom reactions. You'll be adding this `CustomReactionIconFactory` to `ChatTheme` next.

### Providing Custom Reactions to ChatTheme

You'll use the `reactionIconFactory` property to provide your custom reactions to `ChatTheme`.  

```kotlin
ChatTheme(
    reactionIconFactory = CustomReactionIconFactory()
) {
    // Your UI Components
}
```

When you run your app, you should see:

![custom reactions](images/custom_reactions.png "Custom Reactions.")

![message custom reactions](images/message_with_custom_reaction.png "Message with Custom Reactions.")

In addittion to using the `reactionIconFactory` in `ChatTheme` you can also provide your custom reactions using the `SelectedReactionsMenu` or  `SelectedMessageMenu`. You'll be looking at that in the next section.

### Adding Custom Reactions to SelectedReactionsMenu and SelectedMessageMenu

To begin with, you need to create a map of `ReactionIcon` as follows:

```kotlin
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

```

This is a `Composable` function that returns a map of your custom reactions. Each map has it's type and a `ReactionIcon`. Now you only need to add these to your `SelectedReactionsMenu` or `SelectedMessageMenu` as follows:

```kotlin
SelectedMessageMenu(
    modifier = Modifier
        .align(Alignment.Center)
        .padding(horizontal = 20.dp)
        .wrapContentSize(),
    shape = ChatTheme.shapes.attachment,
    messageOptions = defaultMessageOptionsState(
        selectedMessage,
        user,
        listViewModel.isInThread
    ),
    message = selectedMessage,
    onMessageAction = { action ->
        composerViewModel.performMessageAction(action)
        listViewModel.performMessageAction(action)
    },
    reactionTypes = customReactionIcons(),
    onDismiss = { listViewModel.removeOverlay() },
    onShowMoreReactionsSelected = {},
)
```

In the code snippet above, you provide your custom reactions using the `reactionTypes` property in the  `SelectedMessageMenu`. The same applies to `SelectedMessageMenu`

When you run the app, it will use your custom reactions even if you've not provide the custom `ReactionIconFactory`.

You've learned how to add your own custom reactions, in the next section, you'll learn how to add spring and size animations to the reactios you've added.

### Adding Animations to Reactions

The Stream Jetpack Compose UI components are highly customisable. It's easy for you to add new components to them or customize the existing components according to your own requirements.  With this it's easy to add animations to components and still take advantages of the features that the SDK offers.

#### Adding Spring Animation

```kotlin
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

    CustomReactionOptionItem(
        option = option,
        springValue = springValue,
        onReactionOptionSelected = { onReactionOptionSelected(option) })
}
```



#### Animating the Reaction Icon Size

### Conclusion

To learn more about Stream's Android SDK, go to the [GitHub repository](https://github.com/GetStream/stream-chat-android) which is a great starting point to all the available docs and samples.

You can get the full sample project with the examples shown in this tutorial [here]https://github.com/wangerekaharun/CustomComposeReactions).
