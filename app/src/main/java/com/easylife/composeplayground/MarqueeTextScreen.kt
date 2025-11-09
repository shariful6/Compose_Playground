package com.easylife.composeplayground

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.util.regex.Pattern


@Composable
fun MarqueeTextScreen() {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            MarqueeText(
                text = "Rongy TV doesn't stream any of the channels included in this application https://google.com. All the streaming links are from third-party websites, available freely on the internet like https://anotherlink.com. We are just giving a way to stream and all content is the copyright of their owner."
            )
        }
    }
}

@Composable
fun MarqueeText(
    text: String,
    modifier: Modifier = Modifier,
    scrollDelay: Int = 500,
    velocity: Int = 100
) {
    val scrollState = rememberScrollState()
    var shouldScroll by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current

    val annotatedString = buildAnnotatedString {
        append(text)

        val urlPattern = Pattern.compile(
            "(https?://[^\\s]+)",
            Pattern.CASE_INSENSITIVE
        )
        val matcher = urlPattern.matcher(text)

        while (matcher.find()) {
            val url = matcher.group(1)
            if (url != null) {
                val startIndex = matcher.start(1)
                val endIndex = matcher.end(1)

                addStyle(
                    style = SpanStyle(
                        color = Color.Blue, // Style as you wish
                        textDecoration = TextDecoration.Underline
                    ),
                    start = startIndex,
                    end = endIndex
                )
                addStringAnnotation(
                    tag = "URL",
                    annotation = url,
                    start = startIndex,
                    end = endIndex
                )
            }
        }
    }

    LaunchedEffect(shouldScroll) {
        if (shouldScroll) {
            scrollState.animateScrollTo(
                value = scrollState.maxValue,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = calculateScrollDuration(
                            text = text,
                            velocity = velocity
                        ),
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Restart,
                    initialStartOffset = StartOffset(scrollDelay)
                )
            )
        }
    }

    ClickableText(
        text = annotatedString,
        modifier = modifier
            .padding(end = 24.dp)
            .horizontalScroll(scrollState, enabled = false)
            .onGloballyPositioned { layoutCoordinates ->
                val textWidth = layoutCoordinates.size.width
                val containerWidth = layoutCoordinates.parentLayoutCoordinates?.size?.width ?: 0
                shouldScroll = textWidth > containerWidth
            },
        overflow = TextOverflow.Clip,
        maxLines = 1,
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    uriHandler.openUri(annotation.item)
                }
        }
    )
}


private fun calculateScrollDuration(text: String, velocity: Int): Int {
    return (text.length * velocity)
}

