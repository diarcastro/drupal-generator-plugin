package com.diarcastro.drupalgenerator

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.ui.Messages
import javax.swing.JOptionPane


class CreateFilesAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val folder = e.getData(CommonDataKeys.VIRTUAL_FILE)
        if (folder == null || !folder.isDirectory) {
            Messages.showErrorDialog(project, "Please select a folder.", "Invalid Selection")
            return
        }

        val dialog = CreateFileDialog()
        if (dialog.showAndGet()) {
            val componentData = dialog.getComponentData()
            if (!componentData.isValid()) {
                return
            }

            // Perform file creation inside a write-action
            WriteCommandAction.runWriteCommandAction(project) {
                try {
                    val fileName = componentData.fileName()
                    val sdcFolder = folder.createChildDirectory(this, fileName)
                    val componentFile = sdcFolder.createChildData(this, "$fileName.component.yml")
                    val componentTwigFile = sdcFolder.createChildData(this, "$fileName.twig")
                    componentFile.setBinaryContent(templateComponent(componentData))
                    componentTwigFile.setBinaryContent(templateTwig(componentData))

                    val message = "<html>The component <b>$fileName</b> was created!</html>"
                    JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE)
                } catch (ex: Exception) {
                    Messages.showErrorDialog(project, "Failed to create file: ${ex.message}", "Error")
                }
            }
        }
    }
}