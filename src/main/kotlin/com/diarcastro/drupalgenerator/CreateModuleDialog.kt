package com.diarcastro.drupalgenerator

import com.intellij.openapi.ui.DialogWrapper
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
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class CreateModuleDialog : DialogWrapper(true) {
    private var mainPanel: JPanel = JPanel()
    private var name: JTextField = JTextField("my_module", 20)
    private var description: JTextField = JTextField("My module description", 30)
    private lateinit var tree: CheckboxTree

    init {
        init()
        title = "New Drupal Module"
    }

    override fun getOKAction(): Action {
        val okAction = super.getOKAction()
        okAction.putValue(Action.NAME, "Create Module")
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
        val nameLabel = JLabel("Module name:")
        val descriptionLabel = JLabel("Module description:")

        val panels = mutableListOf<JPanel>()

        val panelModule = createSection(
            "Module details",
            nameLabel,
            name,
        )

        val panelModuleDescription = createSection(
            "",
            descriptionLabel,
            description,
        )
        panels.add(panelModule)
        panels.add(panelModuleDescription)

        /* File tree */
        val rootNode = TreeNodeWithID("my_module", "root")
        val nodeInfo = TreeNodeWithID("my_module.info.yml", "info")
        val nodeInstall = TreeNodeWithID("my_module.install", "install")
        val nodeLibraries = TreeNodeWithID("my_module.libraries.yml", "libraries")
        val nodeLinks = TreeNodeWithID("my_module.links.menu", "links")
        val nodeModule = TreeNodeWithID("my_module.module", "module")
        val nodePermissions = TreeNodeWithID("my_module.permissions.yml", "permissions")
        val nodeRouting = TreeNodeWithID("my_module.routing.yml", "routing")
        val nodeServices = TreeNodeWithID("my_module.services.yml", "services")
        val nodeReadme = TreeNodeWithID("README.md", "readme")

        // Disable by default some files
        nodeInstall.isChecked = false;
        nodeLinks.isChecked = false;
        nodePermissions.isChecked = false;
        nodeRouting.isChecked = false;
        nodeServices.isChecked = false;

        rootNode.add(nodeInfo)
        rootNode.add(nodeInstall)
        rootNode.add(nodeLibraries)
        rootNode.add(nodeLinks)
        rootNode.add(nodeModule)
        rootNode.add(nodePermissions)
        rootNode.add(nodeRouting)
        rootNode.add(nodeServices)
        rootNode.add(nodeReadme)

        // Create the CheckboxTree
        tree = object : CheckboxTree(CheckboxTreeRenderer(), rootNode) {
            override fun getCellRenderer(): CheckboxTreeRenderer {
                return CheckboxTreeRenderer()
            }
        }
        tree.isRootVisible = true
        val scrollPanel = JBScrollPane(tree)
        val filesPanel = createSection("Files to generate")
        filesPanel.add(scrollPanel, BorderLayout.CENTER)
        panels.add(filesPanel)

        SwingUtilities.invokeLater {
            name.selectAll()
            name.requestFocusInWindow()
        }
        panels.forEach({ mainPanel.add(it, BorderLayout.NORTH) })

        name.document.addDocumentListener(object : DocumentListener {
            fun updateNodesLabels() {
                if (name.text.isEmpty()) {
                    return
                }
                val resourceName = toSnakeCase(name.text)
                rootNode.userObject = resourceName
                nodeInfo.userObject = "$resourceName.info.yml"
                nodeInstall.userObject = "$resourceName.install"
                nodeLibraries.userObject = "$resourceName.libraries.yml"
                nodeLinks.userObject = "$resourceName.links.menu"
                nodeModule.userObject = "$resourceName.module"
                nodePermissions.userObject = "$resourceName.permissions.yml"
                nodeRouting.userObject = "$resourceName.routing.yml"
                nodeServices.userObject = "$resourceName.services.yml"

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

    fun getData(): ModuleData {
        val data = ModuleData()
        data.name = name.text
        data.description = description.text
        data.filesToGenerate = getCheckedItems(tree)

        return data
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

