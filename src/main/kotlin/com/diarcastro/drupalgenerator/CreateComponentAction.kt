package com.diarcastro.drupalgenerator

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.ui.Messages
import java.io.File


class CreateComponentAction : AnAction() {
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

            val fileName = componentData.fileName()
            val possiblePath = "${folder.path}${File.separator}$fileName"
            if (File(possiblePath).exists()) {
                Messages.showErrorDialog(project, "The component $fileName exist already!", "Error")
                return
            }

            // Perform file creation inside a write-action
            WriteCommandAction.runWriteCommandAction(project) {
                try {
                    val sdcFolder = folder.createChildDirectory(this, fileName)
                    val srcFolder = sdcFolder.createChildDirectory(this, "src")
                    val componentFile = sdcFolder.createChildData(this, "$fileName.component.yml")
                    val componentTwigFile = sdcFolder.createChildData(this, "$fileName.twig")
                    val componentScssFile = srcFolder.createChildData(this, "$fileName.scss")
                    val componentCssFile = sdcFolder.createChildData(this, "$fileName.css")
                    val componentJsFile = sdcFolder.createChildData(this, "$fileName.js")
                    val componentStoriesFile = sdcFolder.createChildData(this, "$fileName.stories.twig")
                    componentFile.setBinaryContent(templateComponent(componentData))
                    componentTwigFile.setBinaryContent(templateTwig(componentData))
                    componentScssFile.setBinaryContent(templateScss(componentData))
                    componentCssFile.setBinaryContent(templateScss(componentData))
                    componentJsFile.setBinaryContent(templateJs(componentData))
                    componentStoriesFile.setBinaryContent(templateStories(componentData, "parent"))
                } catch (ex: Exception) {
                    Messages.showErrorDialog(project, "Failed to create file: ${ex.message}", "Error")
                }
            }
        }
    }
}