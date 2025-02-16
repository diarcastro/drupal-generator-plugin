package com.diarcastro.drupalgenerator

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ComboBox
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Font
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JTextField
import javax.swing.JPanel
import javax.swing.SwingUtilities


class CreateFileDialog : DialogWrapper(true) {
    private var mainPanel: JPanel = JPanel()
    private var sdcName: JTextField = JTextField("my-sdc", 20)
    private var sdcLabel: JLabel = JLabel("SDC Name:")
    private val statusDropdown = ComboBox(arrayOf("experimental", "stable", "deprecated", "obsolete"))

    init {
        init()
        title = "New Drupal SDC"
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
        mainPanel.minimumSize = Dimension(450, 300)

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


        SwingUtilities.invokeLater {
            sdcName.selectAll()
            sdcName.requestFocusInWindow()
        }
        panels.forEach({ mainPanel.add(it, BorderLayout.NORTH) })

        return mainPanel
    }

    fun getComponentData(): ComponentData {
        val componentData = ComponentData()
        componentData.name = sdcName.text
        componentData.status = statusDropdown.selectedItem as String

        return componentData
    }
}