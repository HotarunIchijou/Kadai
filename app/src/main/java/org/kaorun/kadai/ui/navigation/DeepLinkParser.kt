package org.kaorun.kadai.ui.navigation

import android.content.Intent
import android.net.Uri
import androidx.navigation3.runtime.NavKey

object DeepLinkParser {
    fun parseKey(intent: Intent?): NavRoute? {
        val uri: Uri = intent?.data ?: return null

        if (uri.scheme == "kadai" && uri.host == "task") {
            val taskId = uri.lastPathSegment?.toLongOrNull()
            if (taskId != null) {
                return TaskRoute(taskId = taskId)
            }
        }
        return null
    }

    fun buildSyntheticBackStack(key: NavRoute?): Array<NavKey> {
        if (key == null) return arrayOf(MainRoute)

        val stack = mutableListOf<NavKey>()
        var current: NavRoute? = key

        while (current != null) {
            stack.add(0, current)
            current = (current as? DeepLinkKey)?.parent
        }

        return stack.toTypedArray()
    }
}