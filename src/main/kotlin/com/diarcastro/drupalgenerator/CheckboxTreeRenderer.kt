package com.diarcastro.drupalgenerator

import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.SimpleTextAttributes
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
            textRenderer.append(text, SimpleTextAttributes.REGULAR_ATTRIBUTES)
            textRenderer.append("  ")
        }
    }
}