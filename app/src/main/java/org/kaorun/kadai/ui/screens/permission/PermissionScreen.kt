@file:OptIn(ExperimentalFoundationStyleApi::class)

package org.kaorun.kadai.ui.screens.permission

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.fillWidth
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.kaorun.kadai.R
import org.kaorun.kadai.ui.icons.notification_settings
import org.kaorun.kadai.ui.screens.permission.components.PermissionCloseButton
import org.kaorun.kadai.ui.screens.permission.components.PermissionHeader
import org.kaorun.kadai.ui.screens.permission.components.PermissionItem
import org.kaorun.kadai.ui.screens.permission.components.PermissionItemsList
import org.kaorun.kadai.ui.screens.permission.utils.exactAlarmPermissionMissing
import org.kaorun.kadai.ui.screens.permission.utils.notificationPermissionMissing

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PermissionScreen(onContinue: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isCloseButtonVisible by rememberSaveable { mutableStateOf(false) }
    var isNotificationGranted by remember { mutableStateOf(!notificationPermissionMissing(context)) }
    var isAlarmGranted by remember { mutableStateOf(!exactAlarmPermissionMissing(context)) }
    val allGranted = isNotificationGranted && isAlarmGranted

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isNotificationGranted = !notificationPermissionMissing(context)
                isAlarmGranted = !exactAlarmPermissionMissing(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val notificationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted -> isNotificationGranted = !granted }

    PermissionScreenContent(
        isNotificationGranted = isNotificationGranted,
        isAlarmGranted = isAlarmGranted,
        isCloseButtonVisible = isCloseButtonVisible,
        isContinueButtonEnabled = allGranted,
        onShowCloseButton = {
            if (!isCloseButtonVisible) isCloseButtonVisible = true
        },
        onContinueButtonClick = onContinue,
        onClose = onContinue,
        onRequestNotification = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        },
        onRequestExactAlarm = {
            context.startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
        }
    )
}

@Composable
private fun PermissionScreenContent(
    isNotificationGranted: Boolean,
    isAlarmGranted: Boolean,
    isCloseButtonVisible: Boolean,
    isContinueButtonEnabled: Boolean,
    onShowCloseButton: () -> Unit,
    onContinueButtonClick: () -> Unit,
    onClose: () -> Unit,
    onRequestNotification: () -> Unit,
    onRequestExactAlarm: () -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current
    val items = listOf(
        PermissionItem(
            title = stringResource(R.string.notification_permission),
            isGranted = isNotificationGranted,
            onClick = onRequestNotification
        ),
        PermissionItem(
            title = stringResource(R.string.schedule_exact_alarms),
            isGranted = isAlarmGranted,
            onClick = onRequestExactAlarm
        )
    )

    Scaffold(containerColor = colorScheme.surfaceContainer) { padding ->
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .heightIn(min = maxHeight),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = padding.calculateTopPadding(),
                            start = padding.calculateStartPadding(layoutDirection),
                            end = padding.calculateEndPadding(layoutDirection)
                        )
                ) {
                    PermissionCloseButton(
                        onClose = onClose,
                        isVisible = isCloseButtonVisible
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        PermissionHeader(
                            icon = notification_settings,
                            title = stringResource(R.string.notification_permission_title),
                            summary = stringResource(R.string.notification_permission_summary)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        PermissionItemsList(
                            items = items.map { item ->
                                item.copy(onClick = {
                                    onShowCloseButton()
                                    item.onClick()
                                })
                            }
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = padding.calculateBottomPadding(),
                            start = padding.calculateStartPadding(layoutDirection),
                            end = padding.calculateEndPadding(layoutDirection)
                        )
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Button(
                        onClick = onContinueButtonClick,
                        modifier = Modifier.styleable {
                            height(ButtonDefaults.MediumContainerHeight)
                            fillWidth()
                        },
                        enabled = isContinueButtonEnabled,
                        shapes = ButtonDefaults.shapes()
                    ) {
                        Text(
                            text = stringResource(R.string.continue_button),
                            style = ButtonDefaults.textStyleFor(
                                buttonHeight = ButtonDefaults.MediumContainerHeight
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PermissionScreenPreview() {
    MaterialTheme {
        PermissionScreenContent(
            isNotificationGranted = true,
            isAlarmGranted = true,
            isContinueButtonEnabled = true,
            isCloseButtonVisible = true,
            onShowCloseButton = { },
            onContinueButtonClick = { },
            onClose = { },
            onRequestNotification = { },
            onRequestExactAlarm = { }
        )
    }
}