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
                    componentData.resource = folder.parent.name
                    val sdcFolder = folder.createChildDirectory(this, fileName)
                    val componentFilesNames = mapOf(
                        "yml" to "$fileName.component.yml",
                        "scss" to "$fileName.scss",
                        "twig" to "$fileName.twig",
                        "css" to "$fileName.css",
                        "js" to "$fileName.js",
                        "stories" to "$fileName.stories.twig"
                    )
                    componentFiles.forEach{ (key, templateFilePath) ->
                        val filenameToGenerate = componentFilesNames[key] ?: ""
                        if (componentData.filesToGenerated.contains(key)) {
                            val fileContent = getTemplateFile(templateFilePath, componentData.getDataObject())
                            val subFolder = if (key === "scss") {
                                sdcFolder.createChildDirectory(this, "src")
                            } else {
                                null
                            }

                            val distFolder = subFolder ?: sdcFolder;
                            val file = distFolder.createChildData(this, filenameToGenerate)
                            file.setBinaryContent(fileContent)
                        }
                    }
                } catch (ex: Exception) {
                    Messages.showErrorDialog(project, "Failed to create file: ${ex.message}", "Error")
                }
            }
        }
    }
}