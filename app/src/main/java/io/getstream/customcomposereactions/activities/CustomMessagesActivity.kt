package io.getstream.customcomposereactions.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Message
import io.getstream.chat.android.client.models.Reaction
import io.getstream.chat.android.common.state.MessageMode
import io.getstream.chat.android.common.state.React
import io.getstream.chat.android.compose.state.messages.SelectedMessageOptionsState
import io.getstream.chat.android.compose.state.messages.SelectedMessageReactionsState
import io.getstream.chat.android.compose.ui.components.messageoptions.defaultMessageOptionsState
import io.getstream.chat.android.compose.ui.components.selectedmessage.SelectedMessageMenu
import io.getstream.chat.android.compose.ui.components.selectedmessage.SelectedReactionsMenu
import io.getstream.chat.android.compose.ui.messages.attachments.AttachmentsPicker
import io.getstream.chat.android.compose.ui.messages.composer.MessageComposer
import io.getstream.chat.android.compose.ui.messages.header.MessageListHeader
import io.getstream.chat.android.compose.ui.messages.list.MessageList
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.messages.AttachmentsPickerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageComposerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageListViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory
import io.getstream.chat.android.offline.ChatDomain
import io.getstream.customcomposereactions.customreactions.CustomReactionOptions
import io.getstream.customcomposereactions.utils.customReactions

class CustomMessagesActivity : ComponentActivity() {
    private val factory by lazy {
        MessagesViewModelFactory(
            context = this,
            chatClient = ChatClient.instance(),
            chatDomain = ChatDomain.instance(),
            channelId = intent.getStringExtra(KEY_CHANNEL_ID) ?: "",
            enforceUniqueReactions = true,
            messageLimit = 40
        )
    }

    private val listViewModel by viewModels<MessageListViewModel>(factoryProducer = { factory })
    private val attachmentsPickerViewModel by viewModels<AttachmentsPickerViewModel>(factoryProducer = { factory })
    private val composerViewModel by viewModels<MessageComposerViewModel>(factoryProducer = { factory })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ChatTheme(
                reactionTypes = customReactions
            ) {
                CustomUi {
                    onBackPressed()
                }
            }
        }
    }

    @Composable
    fun CustomUi(onBackPressed: () -> Unit) {
        val isShowingAttachments = attachmentsPickerViewModel.isShowingAttachments
        val selectedMessageState = listViewModel.currentMessagesState.selectedMessageState
        val user by listViewModel.user.collectAsState()

        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    MessageComposer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        viewModel = composerViewModel,
                        onAttachmentsClick = { attachmentsPickerViewModel.changeAttachmentState(true) },
                        onCancelAction = {
                            listViewModel.dismissAllMessageActions()
                            composerViewModel.dismissMessageActions()
                        }
                    )
                },
                content = {
                    Column(modifier = Modifier.fillMaxSize()) {
                        MessageListHeader(
                            channel = listViewModel.channel,
                            currentUser = user,
                            messageMode = listViewModel.messageMode,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            onBackPressed = onBackPressed,
                            onHeaderActionClick = {},
                        )

                        MessageList(
                            modifier = Modifier
                                .padding(it)
                                .background(ChatTheme.colors.appBackground),
                            viewModel = listViewModel,
                            onThreadClick = { message ->
                                composerViewModel.setMessageMode(MessageMode.MessageThread(message))
                                listViewModel.openMessageThread(message)
                            }
                        )
                    }
                }
            )

            if (isShowingAttachments) {
                AttachmentsPicker(
                    attachmentsPickerViewModel = attachmentsPickerViewModel,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .height(350.dp),
                    onAttachmentsSelected = { attachments ->
                        attachmentsPickerViewModel.changeAttachmentState(false)
                        composerViewModel.addSelectedAttachments(attachments)
                    },
                    onDismiss = {
                        attachmentsPickerViewModel.changeAttachmentState(false)
                        attachmentsPickerViewModel.dismissAttachments()
                    }
                )
            }

            if (selectedMessageState != null) {
                val selectedMessage = selectedMessageState.message
                if (selectedMessageState is SelectedMessageOptionsState) {
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
                        onMessageAction = { },
                        onDismiss = { listViewModel.removeOverlay() },
                        onShowMoreReactionsSelected = {},
                        headerContent = {
                            CustomReactionOptions(
                                message = selectedMessage,
                                reactionTypes = customReactions ,
                                onMessageAction = { action ->
                                    composerViewModel.performMessageAction(action)
                                    listViewModel.performMessageAction(action)
                                }
                            )
                        }
                    )
                } else if (selectedMessageState is SelectedMessageReactionsState) {
                    SelectedReactionsMenu(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 20.dp)
                            .wrapContentSize(),
                        shape = ChatTheme.shapes.attachment,
                        message = selectedMessage,
                        currentUser = user,
                        reactionTypes = customReactions,
                        onMessageAction = { },
                        onDismiss = {
                            listViewModel.removeOverlay()
                        },
                        onShowMoreReactionsSelected = {},
                        headerContent = {
                            CustomReactionOptions(
                                message = selectedMessage,
                                reactionTypes = customReactions ,
                                onMessageAction = { action ->
                                    when(action){
                                        is React -> {
                                            sendReactions(selectedMessage, action.reaction, listViewModel)
                                        }
                                        else -> {
                                            composerViewModel.performMessageAction(action)
                                            listViewModel.performMessageAction(action)
                                        }
                                    }
                                }
                            )
                        }
                    )

                }
            }
        }
    }

    private fun sendReactions(
        message: Message,
        reaction: Reaction,
        listViewModel: MessageListViewModel
    ) {
        if (message.ownReactions.any { it.messageId == reaction.messageId && it.type == reaction.type }) {
            ChatClient.instance().deleteReaction(message.id, reaction.type).enqueue { result ->
                if (result.isSuccess) {
                    listViewModel.removeOverlay()
                    Log.e("Message", "Reaction Deleted")
                } else {
                    Log.d("Message", "Deleting reaction Failed: ${result.error().message}")
                }
            }
        } else {
            ChatClient.instance().sendReaction(reaction).enqueue { result ->
                if (result.isSuccess) {
                    listViewModel.removeOverlay()
                    val sentReaction = result.data()
                    Log.d("Message", "Message Reaction score is: ${sentReaction.type}")
                } else {
                    Log.d("Message", "Adding reaction Failed: ${result.error().message}")
                }
            }
        }
    }


    companion object {
        private const val KEY_CHANNEL_ID = "channelId"

        fun getIntent(context: Context, channelId: String): Intent {
            return Intent(context, CustomMessagesActivity::class.java).apply {
                putExtra(KEY_CHANNEL_ID, channelId)
            }
        }
    }
}