package com.diarcastro.drupalgenerator

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.CheckboxTree
import com.intellij.ui.components.JBScrollPane
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.Font
import javax.swing.Action
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JTextField
import javax.swing.JPanel
import javax.swing.SwingUtilities
import javax.swing.tree.TreePath
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class CreateFileDialog : DialogWrapper(true) {
    private var mainPanel: JPanel = JPanel()
    private var sdcName: JTextField = JTextField("my-sdc", 20)
    private var sdcLabel: JLabel = JLabel("SDC Name:")
    private val statusDropdown = ComboBox(arrayOf("experimental", "stable", "deprecated", "obsolete"))
    private lateinit var tree: CheckboxTree

    init {
        init()
        title = "New Drupal SDC"
    }

    override fun getOKAction(): Action {
        val okAction = super.getOKAction()
        okAction.putValue(Action.NAME, "Create Component")
        return okAction
    }

    private fun createSection(title: String = "", vararg components: JComponent): JPanel {
        val panel = JPanel(BorderLayout())
        if (title.isNotEmpty()) {
            val titleLabel = JLabel(title)
            titleLabel.font = titleLabel.font.deriveFont(Font.BOLD, 14f)
            panel.add(titleLabel, BorderLayout.NORTH)
        }

        val contentPanel = JPanel(FlowLayout(FlowLayout.LEFT, 15, 10))
        components.forEach { contentPanel.add(it, BorderLayout.NORTH) }
        panel.add(contentPanel, BorderLayout.CENTER)

        return panel
    }

    override fun createCenterPanel(): JComponent {
        mainPanel.layout = BoxLayout(mainPanel, BoxLayout.Y_AXIS)

        val panels = mutableListOf<JPanel>()

        val statusLabel = JLabel("Status:")
        statusDropdown.selectedItem = "stable"
        val panelStatus = createSection(
            "Component Details",
            statusLabel,
            statusDropdown,
        )

        panels.add(panelStatus)
        val panelComponent = createSection(
            "",
            sdcLabel,
            sdcName,
        )
        panels.add(panelComponent)

        /* File tree */
        val rootNode = TreeNodeWithID("my-sdc", "root")
        val srcFolder = TreeNodeWithID("src", "src")
        val nodeSass = TreeNodeWithID("my-sdc.scss", "scss")

        srcFolder.add(nodeSass)
        val nodeComponent = TreeNodeWithID("my-sdc.component.yml", "yml")
        val nodeCss = TreeNodeWithID("my-sdc.css", "css")
        val nodeJs = TreeNodeWithID("my-sdc.js", "js")
        val nodeStories = TreeNodeWithID("my-sdc.stories.twig", "stories")
        val nodeTwig = TreeNodeWithID("my-sdc.twig", "twig")
        rootNode.add(srcFolder)
        rootNode.add(nodeComponent)
        rootNode.add(nodeCss)
        rootNode.add(nodeJs)
        rootNode.add(nodeStories)
        rootNode.add(nodeTwig)

        // Create the CheckboxTree
        tree = object : CheckboxTree(CheckboxTreeRenderer(), rootNode) {
            override fun getCellRenderer(): CheckboxTreeRenderer {
                return CheckboxTreeRenderer()
            }
        }
        tree.expandPath(TreePath(srcFolder.path))
        tree.isRootVisible = true
        val scrollPanel = JBScrollPane(tree)
        val filesPanel = createSection("Files to generate")
        filesPanel.add(scrollPanel, BorderLayout.CENTER)
        panels.add(filesPanel)

        SwingUtilities.invokeLater {
            sdcName.selectAll()
            sdcName.requestFocusInWindow()
        }
        panels.forEach({ mainPanel.add(it, BorderLayout.NORTH) })

        sdcName.document.addDocumentListener(object : DocumentListener {
            fun updateNodesLabels() {
                if (sdcName.text.isEmpty()) {
                    return
                }
                val componentName = toKebabCase(sdcName.text)
                rootNode.userObject = componentName
                nodeSass.userObject = "$componentName.scss"
                nodeComponent.userObject = "$componentName.component.yml"
                nodeCss .userObject = "$componentName.css"
                nodeJs.userObject = "$componentName.js"
                nodeStories.userObject = "$componentName.stories.twig"
                nodeTwig.userObject = "$componentName.twig"

                tree.updateUI()
            }

            override fun insertUpdate(e: DocumentEvent?) {
                updateNodesLabels()
            }

            override fun removeUpdate(e: DocumentEvent?) {
                updateNodesLabels()
            }

            override fun changedUpdate(e: DocumentEvent?) {}
        })

        return mainPanel
    }

    fun getComponentData(): ComponentData {
        val componentData = ComponentData()
        componentData.name = sdcName.text
        componentData.status = statusDropdown.selectedItem as String
        componentData.filesToGenerated = getCheckedItems(tree)

        return componentData
    }

    private fun getCheckedItems(tree: CheckboxTree): List<String> {
        val checkedNodes = mutableListOf<String>()
        val root = tree.model.root as TreeNodeWithID

        fun collectChecked(node: TreeNodeWithID) {
            if (node.isChecked) {
                checkedNodes.add(node.id)
            }

            for (i in 0 until node.childCount) {
                collectChecked(node.getChildAt(i) as TreeNodeWithID)
            }
        }

        collectChecked(root)

        return checkedNodes
    }
}