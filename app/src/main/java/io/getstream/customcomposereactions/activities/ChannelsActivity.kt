package io.getstream.customcomposereactions.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.livedata.ChatDomain
import io.getstream.customcomposereactions.R
import io.getstream.customcomposereactions.ui.theme.CustomComposeReactionsTheme

class ChannelsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val client = ChatClient.Builder("b67pax5b2wdq", applicationContext)
            .logLevel(ChatLogLevel.ALL)
            .build()
        ChatDomain.Builder(client, applicationContext).build()

        val user = User(
            id = "tutorial-droid",
            extraData = mutableMapOf(
                "name" to "Tutorial Droid",
                "image" to "https://bit.ly/2TIt8NR",
            ),
        )

        client.connectUser(
            user = user,
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoidHV0b3JpYWwtZHJvaWQifQ.NhEr0hP9W9nwqV7ZkdShxvi02C5PR7SJE7Cs4y7kyqg"
        ).enqueue()

        setContent {
            ChatTheme {
                ChannelsList(onBackPressed = this::finish)
            }
        }
    }
}

@Composable
fun ChannelsList(onBackPressed: () -> Unit) {
    val context = LocalContext.current

    ChannelsScreen(
        title = stringResource(id = R.string.app_name),
        onItemClick = { channel ->

        },
        onBackPressed = { onBackPressed.invoke() }
    )
}