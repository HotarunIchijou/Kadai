@file:OptIn(ExperimentalFoundationStyleApi::class)

package org.kaorun.kadai.ui.screens.permission

import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
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
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.kaorun.kadai.R.string
import org.kaorun.kadai.ui.icons.notification_settings
import org.kaorun.kadai.ui.screens.permission.components.PermissionCloseButton
import org.kaorun.kadai.ui.screens.permission.components.PermissionHeader
import org.kaorun.kadai.ui.screens.permission.components.PermissionItem
import org.kaorun.kadai.ui.screens.permission.components.PermissionItemsList
import org.kaorun.kadai.ui.screens.permission.utils.isExactAlarmPermissionGranted
import org.kaorun.kadai.ui.screens.permission.utils.isNotificationPermissionGranted

@SuppressLint("ObsoleteSdkInt")
@Composable
fun PermissionScreen(
    onContinue: () -> Unit,
    onBack: () -> Unit,
    canGoBack: Boolean = false
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val permissionDeniedString = stringResource(string.notification_permission_denied)
    var currentToast: Toast? by remember { mutableStateOf(null) }
    var isNotificationGranted by remember {
        mutableStateOf(isNotificationPermissionGranted(context))
    }
    var isAlarmGranted by remember {
        mutableStateOf(isExactAlarmPermissionGranted(context))
    }

    var isNotificationItemClicked by rememberSaveable { mutableStateOf(isNotificationGranted) }
    var isExactAlarmItemClicked by rememberSaveable { mutableStateOf(isAlarmGranted) }
    val isCloseButtonVisible = canGoBack || (isNotificationItemClicked && isExactAlarmItemClicked)

    val allGranted = isNotificationGranted && isAlarmGranted

    fun refreshPermissions() {
        isNotificationGranted = isNotificationPermissionGranted(context)
        isAlarmGranted = isExactAlarmPermissionGranted(context)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Event.ON_RESUME) refreshPermissions()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val notificationLauncher = rememberLauncherForActivityResult(
        contract = RequestPermission()
    ) { granted ->
        refreshPermissions()

        if (!granted &&
            !ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                permission.POST_NOTIFICATIONS
            )
        ) {
            currentToast?.cancel()
            currentToast = Toast.makeText(
                context,
                permissionDeniedString,
                Toast.LENGTH_SHORT
            ).also { it.show() }

            context.startActivity(
                Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            )
        }
    }

    val requestNotification = {
        when {
            VERSION.SDK_INT < VERSION_CODES.TIRAMISU -> {
                context.startActivity(
                    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                )
            }

            else -> notificationLauncher.launch(permission.POST_NOTIFICATIONS)
        }
    }
    val requestExactAlarm = {
        if (VERSION.SDK_INT >= VERSION_CODES.S) {
            context.startActivity(
                Intent(
                    Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                    "package:${context.packageName}".toUri()
                )
            )
        }
    }

    val notificationTitle = stringResource(string.notification_permission)
    val alarmTitle = stringResource(string.schedule_exact_alarms)

    val items = remember(
        isNotificationGranted,
        isAlarmGranted,
        notificationTitle,
        alarmTitle
    ) {
        listOf(
            PermissionItem(
                title = notificationTitle,
                isGranted = isNotificationGranted,
                onClick = {
                    requestNotification()
                    isNotificationItemClicked = true
                }
            ),
            PermissionItem(
                title = alarmTitle,
                isGranted = isAlarmGranted,
                onClick = {
                    requestExactAlarm()
                    isExactAlarmItemClicked = true
                }
            )
        )
    }

    PermissionScreenContent(
        items = items,
        isCloseButtonVisible = isCloseButtonVisible,
        isContinueButtonEnabled = allGranted,
        onContinue = onContinue,
        onBack = onBack,
        canGoBack = canGoBack
    )
}

@Composable
private fun PermissionScreenContent(
    items: List<PermissionItem>,
    isCloseButtonVisible: Boolean,
    isContinueButtonEnabled: Boolean,
    onContinue: () -> Unit,
    onBack: () -> Unit,
    canGoBack: Boolean
) {
    val layoutDirection = LocalLayoutDirection.current

    Scaffold(
        containerColor = colorScheme.surfaceContainer
    ) { padding ->
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
                    Box(modifier = Modifier.padding(start = 20.dp, top = 8.dp)) {
                        PermissionCloseButton(
                            onClose = if (canGoBack) onBack
                            else onContinue,
                            isVisible = isCloseButtonVisible
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        PermissionHeader(
                            icon = notification_settings,
                            title = stringResource(string.notification_permission_title),
                            summary = stringResource(string.notification_permission_summary)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        PermissionItemsList(items = items)
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
                        onClick = onContinue,
                        modifier = Modifier.styleable {
                            height(ButtonDefaults.MediumContainerHeight)
                            fillWidth()
                        },
                        enabled = isContinueButtonEnabled,
                        shapes = ButtonDefaults.shapes()
                    ) {
                        Text(
                            text = stringResource(string.continue_button),
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
            items = emptyList(),
            isContinueButtonEnabled = true,
            isCloseButtonVisible = true,
            onContinue = { },
            onBack = { },
            canGoBack = true
        )
    }
}