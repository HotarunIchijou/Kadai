package org.kaorun.kadai.ui.screens.settings.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.lerp
import androidx.compose.ui.unit.dp
import org.kaorun.kadai.R
import org.kaorun.kadai.ui.icons.arrow_back

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    onBack: () -> Unit,
) {
    val expandedStyle = MaterialTheme.typography.headlineLargeEmphasized
    val collapsedStyle = MaterialTheme.typography.titleLargeEmphasized

    val titleStyle = lerp(
        start = expandedStyle,
        stop = collapsedStyle,
        fraction = scrollBehavior.state.collapsedFraction
    )
    LargeFlexibleTopAppBar(
        title = {
            Text(
                text = title,
                style = titleStyle
            )
        },
        modifier = Modifier.padding(start = 16.dp, end = 8.dp),
        navigationIcon = {
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                    TooltipAnchorPosition.Below
                ),
                tooltip = {
                    PlainTooltip {
                        Text(stringResource(R.string.back))
                    }
                },
                state = rememberTooltipState(),
            ) {
                IconButton(
                    onClick = { onBack() },
                    shapes = IconButtonDefaults.shapes(),
                    modifier = Modifier.padding(end = 8.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                ) {
                    Icon(
                        imageVector = arrow_back,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        scrollBehavior = scrollBehavior
    )
}