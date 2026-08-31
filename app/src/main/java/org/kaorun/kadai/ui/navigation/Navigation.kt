package org.kaorun.kadai.ui.navigation

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.coroutines.android.awaitFrame
import org.kaorun.kadai.ui.screens.main.MainScreen
import org.kaorun.kadai.ui.screens.permission.PermissionScreen
import org.kaorun.kadai.ui.screens.settings.about.SettingsAboutScreen
import org.kaorun.kadai.ui.screens.settings.appearance.SettingsAppearanceScreen
import org.kaorun.kadai.ui.screens.settings.main.SettingsScreen
import org.kaorun.kadai.ui.screens.task.TaskScreen
import org.kaorun.kadai.ui.screens.task.TaskUiState
import org.kaorun.kadai.ui.screens.task.TaskViewModel
import org.kaorun.kadai.ui.screens.task.components.TaskDetailPlaceholder
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    startRoute: NavRoute,
    initialDeepLink: NavRoute? = null,
    onCompleteOnboarding: () -> Unit
) {
    val initialStack: Array<NavKey> = remember(initialDeepLink, startRoute) {
        when {
            initialDeepLink != null -> DeepLinkParser.buildSyntheticBackStack(initialDeepLink)
            else -> arrayOf(startRoute)
        }
    }
    val backStack = rememberNavBackStack(*initialStack)
    var addingTaskId by remember { mutableStateOf<Long?>(null) }
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()

    val spatialSpec: FiniteAnimationSpec<IntOffset> = remember { tween(300) }
    val effectsSpec: FiniteAnimationSpec<Float> = remember { tween(300) }
    val fastFadeSpec: FiniteAnimationSpec<Float> = remember { tween(100) }

    val enterTransition = scaleIn(animationSpec = effectsSpec, initialScale = 0.9f) +
        slideInHorizontally(animationSpec = spatialSpec, initialOffsetX = { it }) +
        fadeIn(animationSpec = effectsSpec)

    val exitTransition = scaleOut(animationSpec = effectsSpec, targetScale = 0.9f) +
        slideOutHorizontally(
            animationSpec = spatialSpec,
            targetOffsetX = { -(it * 0.1f).toInt() }) +
        fadeOut(animationSpec = effectsSpec)

    val popEnterTransition = scaleIn(animationSpec = effectsSpec, initialScale = 0.9f) +
        slideInHorizontally(
            animationSpec = spatialSpec,
            initialOffsetX = { -(it * 0.1f).toInt() }) +
        fadeIn(animationSpec = effectsSpec)

    val popExitTransition = scaleOut(animationSpec = effectsSpec, targetScale = 0.9f) +
        slideOutHorizontally(animationSpec = spatialSpec, targetOffsetX = { it }) +
        fadeOut(animationSpec = fastFadeSpec)

    val onBack = {
        if (backStack.size > 1) backStack.removeLastOrNull()
    }

    PredictiveBackHandler(enabled = backStack.size > 1) { progressFlow ->
        try {
            progressFlow.collect { _ ->
            }
            onBack()
        } catch (_: CancellationException) {
        }
    }

    NavDisplay(
        backStack = backStack,
        modifier = modifier.fillMaxSize(),
        onBack = onBack,
        sceneStrategies = listOf(listDetailStrategy),
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = { enterTransition togetherWith exitTransition },
        popTransitionSpec = { popEnterTransition togetherWith popExitTransition },
        predictivePopTransitionSpec = { popEnterTransition togetherWith popExitTransition },
        entryProvider = entryProvider {
            entry<NotificationPermissionRoute> {
                PermissionScreen(
                    onContinue = {
                        onCompleteOnboarding()
                        backStack.clear()
                        backStack.add(MainRoute)
                    },
                    onBack = onBack,
                    canGoBack = backStack.size > 1
                )
            }

            entry<MainRoute>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        TaskDetailPlaceholder()
                    }
                )
            ) {
                val selectedTaskId = when (val currentRoute = backStack.lastOrNull()) {
                    is TaskRoute -> currentRoute.taskId
                    is AddTaskRoute -> addingTaskId
                    else -> null
                }

                MainScreen(
                    selectedTaskId = selectedTaskId,
                    onNavigateToTask = { taskId ->
                        addingTaskId = null
                        if (taskId != null) {
                            backStack.add(TaskRoute(taskId = taskId))
                        } else {
                            backStack.add(AddTaskRoute())
                        }
                    },
                    onNavigateToSettings = {
                        backStack.add(SettingsRoute)
                    },
                    onPermissionCardClick = {
                        backStack.add(NotificationPermissionRoute)
                    }
                )
            }

            entry<AddTaskRoute>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) {
                val viewModel = hiltViewModel<TaskViewModel>()
                val focusRequester = remember { FocusRequester() }
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(uiState) {
                    addingTaskId = (uiState as? TaskUiState.Success)?.id
                }

                DisposableEffect(Unit) {
                    onDispose {
                        addingTaskId = null
                    }
                }

                LaunchedEffect(Unit) {
                    viewModel.load(null)
                    awaitFrame()
                    focusRequester.requestFocus()
                }

                TaskScreen(
                    viewModel = viewModel,
                    modifier = Modifier.focusRequester(focusRequester),
                    onBack = onBack
                )
            }

            entry<TaskRoute>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) { key ->
                val viewModel = hiltViewModel<TaskViewModel>()

                LaunchedEffect(key.taskId) {
                    viewModel.load(key.taskId)
                }

                TaskScreen(
                    viewModel = viewModel,
                    onBack = onBack
                )
            }

            entry<SettingsRoute> {
                SettingsScreen(
                    onNavigateToAppearance = { backStack.add(SettingsAppearanceRoute) },
                    onNavigateToNotifications = { backStack.add(NotificationPermissionRoute) },
                    onNavigateToAbout = { backStack.add(SettingsAboutRoute) },
                    onBack = onBack
                )
            }

            entry<SettingsAppearanceRoute> {
                SettingsAppearanceScreen(
                    onBack = onBack
                )
            }

            entry<SettingsAboutRoute> {
                SettingsAboutScreen(
                    onBack = onBack
                )
            }
        }
    )
}