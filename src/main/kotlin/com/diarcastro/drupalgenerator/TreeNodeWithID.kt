package com.diarcastro.drupalgenerator

import com.intellij.ui.CheckedTreeNode
class TreeNodeWithID(var label: String, val id: String) : CheckedTreeNode(label)  {
    override fun toString(): String = label
}