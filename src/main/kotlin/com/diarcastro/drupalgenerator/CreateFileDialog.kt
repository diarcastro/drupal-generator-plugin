package com.diarcastro.drupalgenerator

import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent
import javax.swing.JTextField
import javax.swing.JPanel

class CreateFileDialog : DialogWrapper(true) {
    private var fileNameField: JTextField = JTextField(20)
    private var contentPane: JPanel = JPanel()

    init {
        init()
        title = "New Drupal SDC"
        // Manually initialize the contentPane if not using GUI Designer
//        contentPane = JPanel()
//        fileNameField = JTextField(20)

    }

    override fun createCenterPanel(): JComponent? {
        contentPane.add(fileNameField)
        return contentPane
    }

    fun getFileName(): String {
        return fileNameField.text
    }
}