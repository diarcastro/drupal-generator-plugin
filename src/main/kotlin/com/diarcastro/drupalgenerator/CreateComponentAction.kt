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
                    if (componentData.filesToGenerated.scss) {
                        val srcFolder = sdcFolder.createChildDirectory(this, "src")
                        val componentScssFile = srcFolder.createChildData(this, "$fileName.scss")
                        componentScssFile.setBinaryContent(templateScss(componentData))
                    }

                    if (componentData.filesToGenerated.twig) {
                        val componentTwigFile = sdcFolder.createChildData(this, "$fileName.twig")
                        componentTwigFile.setBinaryContent(templateTwig(componentData))
                    }

                    if (componentData.filesToGenerated.yml) {
                        val componentFile = sdcFolder.createChildData(this, "$fileName.component.yml")
                        componentFile.setBinaryContent(templateComponent(componentData))
                    }

                    if (componentData.filesToGenerated.css) {
                        val componentCssFile = sdcFolder.createChildData(this, "$fileName.css")
                        componentCssFile.setBinaryContent(templateScss(componentData))
                    }

                    if (componentData.filesToGenerated.story) {
                        val componentStoriesFile = sdcFolder.createChildData(this, "$fileName.stories.twig")
                        componentStoriesFile.setBinaryContent(templateStories(componentData, folder.parent.name))
                    }

                    if (componentData.filesToGenerated.js) {
                        val componentJsFile = sdcFolder.createChildData(this, "$fileName.js")
                        componentJsFile.setBinaryContent(templateJs(componentData))
                    }
                } catch (ex: Exception) {
                    Messages.showErrorDialog(project, "Failed to create file: ${ex.message}", "Error")
                }
            }
        }
    }
}