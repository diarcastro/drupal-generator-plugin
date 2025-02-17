package com.diarcastro.drupalgenerator

import com.intellij.openapi.util.IconLoader
import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.SimpleTextAttributes
import javax.swing.Icon
import javax.swing.JTree

class CheckboxTreeRenderer : CheckboxTree.CheckboxTreeCellRenderer() {
    override fun customizeRenderer(
        tree: JTree,
        value: Any?,
        selected: Boolean,
        expanded: Boolean,
        leaf: Boolean,
        row: Int,
        hasFocus: Boolean
    ) {
        if (value is CheckedTreeNode) {
            val text = value.userObject.toString()
            val icon: Icon
            icon = if (leaf) {
                AppIcons.FILE_CODE
            } else {
                if (expanded) {
                    AppIcons.FOLDER_OPEN
                } else {
                    AppIcons.FOLDER
                }
            }

            textRenderer.icon = icon
            textRenderer.append(text, SimpleTextAttributes.REGULAR_ATTRIBUTES)
            textRenderer.append("  ")
        }
    }
}

object AppIcons {
    val FOLDER: Icon = IconLoader.getIcon("icons/folder.png", AppIcons::class.java)
    val FOLDER_OPEN: Icon = IconLoader.getIcon("icons/folder-open.png", AppIcons::class.java)
    val FILE_CODE: Icon = IconLoader.getIcon("icons/file-code.png", AppIcons::class.java)
}