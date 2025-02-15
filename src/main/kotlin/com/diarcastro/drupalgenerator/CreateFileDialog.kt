package com.diarcastro.drupalgenerator

import com.intellij.openapi.ui.DialogWrapper
import java.awt.FlowLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JTextField
import javax.swing.JPanel
import javax.swing.SwingUtilities


class CreateFileDialog : DialogWrapper(true) {
    private var sdcName: JTextField = JTextField("my-sdc", 20)
    private var sdcLabel: JLabel = JLabel("SDC Name:")
    private var contentPane: JPanel = JPanel(FlowLayout(FlowLayout.LEFT))

    init {
        init()
        title = "New Drupal SDC"

    }

    override fun createCenterPanel(): JComponent? {
        contentPane.add(sdcLabel)
        contentPane.add(sdcName)
        SwingUtilities.invokeLater {
            sdcName.selectAll()
            sdcName.requestFocusInWindow()
        }
        return contentPane
    }

    fun getComponentData(): ComponentData {
        val componentData = ComponentData();
        componentData.name = sdcName.text

        return componentData
    }
}