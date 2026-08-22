package moe.ouom.neriplayer.ui.component.playback

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import moe.ouom.neriplayer.R

internal enum class RetroPlaybackModeGlyph(@DrawableRes val drawableRes: Int) {
    Shuffle(R.drawable.ic_retro_shuffle),
    Repeat(R.drawable.ic_retro_repeat),
    RepeatOne(R.drawable.ic_retro_repeat_one)
}

private const val RetroPlaybackModeVisualScale = 0.88f

@Composable
internal fun RetroPlaybackModeIcon(
    icon: RetroPlaybackModeGlyph,
    contentDescription: String?,
    tint: Color,
    size: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(icon.drawableRes),
        contentDescription = contentDescription,
        tint = tint,
        modifier = modifier.then(Modifier.size(size * RetroPlaybackModeVisualScale))
    )
}
