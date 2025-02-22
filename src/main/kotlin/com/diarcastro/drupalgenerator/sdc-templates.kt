package com.diarcastro.drupalgenerator

import java.nio.charset.StandardCharsets

fun readResourceFile(fileName: String): String? {
    val inputStream = object {}.javaClass.classLoader.getResourceAsStream(fileName)
    return inputStream?.bufferedReader(StandardCharsets.UTF_8)?.use { it.readText() }
}

fun getTemplateFile(templatePath: String, data: Map<String, String>): ByteArray {
    val templateContent = readResourceFile(templatePath)
    if (templateContent == null) {
        return "".toByteArray()
    }

    val updatedContent = data.entries.fold(templateContent) { acc, (key, value) ->
        acc.replace("{{$key}}", value)
    }

    return updatedContent.toByteArray()
}