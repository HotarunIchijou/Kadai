package org.kaorun.kadai.ui.navigation

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import org.kaorun.kadai.ui.screens.main.MainScreen
import org.kaorun.kadai.ui.screens.permission.PermissionScreen
import org.kaorun.kadai.ui.screens.task.TaskScreen
import org.kaorun.kadai.ui.screens.task.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    startRoute: NavRoute,
    onCompleteOnboarding: () -> Unit,
    initialDeepLink: NavRoute? = null
) {
    val initialStack: Array<NavKey> = remember(initialDeepLink, startRoute) {
        when {
            initialDeepLink != null -> DeepLinkParser.buildSyntheticBackStack(initialDeepLink)
            else -> arrayOf(startRoute)
        }
    }

    val backStack = rememberNavBackStack(*initialStack)
    val spatialSpec: FiniteAnimationSpec<IntOffset> = remember { tween(300) }
    val effectsSpec: FiniteAnimationSpec<Float> = remember { tween(300) }
    val fastFadeSpec: FiniteAnimationSpec<Float> = remember { tween(100) }

    val enterTransition = scaleIn(animationSpec = effectsSpec, initialScale = 0.9f) +
            slideInHorizontally(animationSpec = spatialSpec, initialOffsetX = { it }) +
            fadeIn(animationSpec = effectsSpec)

    val exitTransition = scaleOut(animationSpec = effectsSpec, targetScale = 0.9f) +
            slideOutHorizontally(animationSpec = spatialSpec, targetOffsetX = { -(it * 0.1f).toInt() }) +
            fadeOut(animationSpec = effectsSpec)

    val popEnterTransition = scaleIn(animationSpec = effectsSpec, initialScale = 0.9f) +
            slideInHorizontally(animationSpec = spatialSpec, initialOffsetX = { -(it * 0.1f).toInt() }) +
            fadeIn(animationSpec = effectsSpec)

    val popExitTransition = scaleOut(animationSpec = effectsSpec, targetScale = 0.9f) +
            slideOutHorizontally(animationSpec = spatialSpec, targetOffsetX = { it }) +
            fadeOut(animationSpec = fastFadeSpec)

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = { enterTransition togetherWith exitTransition },
        popTransitionSpec = { popEnterTransition togetherWith popExitTransition },
        predictivePopTransitionSpec = { popEnterTransition togetherWith popExitTransition },
        entryProvider = entryProvider {
            entry<PermissionRoute> {
                PermissionScreen(
                    onContinue = {
                        onCompleteOnboarding()
                        backStack.clear()
                        backStack.add(MainRoute)
                    }
                )
            }

            entry<MainRoute> {
                MainScreen(
                    onNavigateToTask = { taskId ->
                        if (taskId != null) {
                            backStack.add(TaskRoute(taskId = taskId))
                        } else {
                            backStack.add(AddTaskRoute())
                        }
                    }
                )
            }

            entry<AddTaskRoute> {
                val viewModel = hiltViewModel<TaskViewModel>()
                val focusRequester = remember { FocusRequester() }

                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }

                TaskScreen(
                    viewModel = viewModel,
                    modifier = Modifier.focusRequester(focusRequester),
                    onBack = {
                        if (backStack.size > 1) {
                            backStack.removeLastOrNull()
                        }
                    }
                )
            }

            entry<TaskRoute> { key ->
                val viewModel = hiltViewModel<TaskViewModel>()

                LaunchedEffect(key.taskId) {
                    viewModel.load(key.taskId)
                }

                TaskScreen(
                    viewModel = viewModel,
                    onBack = {
                        if (backStack.size > 1) {
                            backStack.removeLastOrNull()
                        }
                    }
                )
            }
        }
    )
}