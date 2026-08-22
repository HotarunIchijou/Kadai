@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)

package org.kaorun.kadai.ui.screens.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.kaorun.kadai.R
import org.kaorun.kadai.ui.icons.close
import org.kaorun.kadai.ui.icons.error

@Composable
fun PermissionMissingCard(
    onClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(100.dp)),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.errorContainer,
            contentColor = colorScheme.onErrorContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = error,
                contentDescription = stringResource(R.string.error_icon),
                modifier = Modifier.size(IconButtonDefaults.largeIconSize)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.permissions_missing_title),
                    style = typography.titleLargeEmphasized
                )
            }

            val closeButtonDescription = stringResource(R.string.close)

            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                    TooltipAnchorPosition.Below
                ),
                tooltip = {
                    PlainTooltip(
                        modifier = Modifier.semantics {
                            liveRegion = LiveRegionMode.Assertive
                            paneTitle = closeButtonDescription
                        }
                    ) {
                        Text(closeButtonDescription)
                    }
                },
                state = rememberTooltipState()
            ) {
                IconButton(
                    onClick = onCloseClick,
                    shape = IconButtonDefaults.mediumRoundShape
                ) {
                    Icon(
                        imageVector = close,
                        contentDescription = stringResource(R.string.close),
                        modifier = Modifier.size(IconButtonDefaults.mediumIconSize)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PermissionMissingCardPreview() {
    PermissionMissingCard(
        onClick =  { },
        onCloseClick = { }
    )
}
