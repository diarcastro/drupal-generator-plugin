package com.diarcastro.drupalgenerator

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.ui.Messages
import java.io.File


class CreateModuleAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val folder = e.getData(CommonDataKeys.VIRTUAL_FILE)
        if (folder == null || !folder.isDirectory) {
            Messages.showErrorDialog(project, "Please select a folder.", "Invalid Selection")
            return
        }

        val dialog = CreateModuleDialog()
        if (dialog.showAndGet()) {
            val moduleData = dialog.getData()
            if (!moduleData.isValid()) {
                return
            }

            val fileName = moduleData.fileName()
            val possiblePath = "${folder.path}${File.separator}$fileName"
            if (File(possiblePath).exists()) {
                Messages.showErrorDialog(project, "The component $fileName exist already!", "Error")
                return
            }

            // Perform file creation inside a write-action
            WriteCommandAction.runWriteCommandAction(project) {
                try {
                    val newResourceFolder = folder.createChildDirectory(this, fileName)
                    val filesToGenerate = mapOf(
                        "info" to "$fileName.info.yml",
                        "install" to "$fileName.install",
                        "libraries" to "$fileName.libraries.yml",
                        "links" to "$fileName.links.menu.yml",
                        "module" to "$fileName.module",
                        "permissions" to "$fileName.permissions.yml",
                        "routing" to "$fileName.routing.yml",
                        "services" to "$fileName.services.yml",
                        "readme" to "README.md",
                    )
                    moduleFiles.forEach { (key, templateFilePath) ->
                        val filenameToGenerate = filesToGenerate[key] ?: ""
                        if (moduleData.filesToGenerate.contains(key)) {
                            val fileContent = getTemplateFile(templateFilePath, moduleData.getDataObject())

                            val file = newResourceFolder.createChildData(this, filenameToGenerate)
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