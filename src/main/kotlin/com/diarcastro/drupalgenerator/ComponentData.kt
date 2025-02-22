package com.diarcastro.drupalgenerator

const val TEMPLATE_COMPONENT_PATH = "templates/component"
val componentFiles = mapOf(
    "yml" to "$TEMPLATE_COMPONENT_PATH/component.component.yml",
    "scss" to "$TEMPLATE_COMPONENT_PATH/src/component.scss",
    "twig" to "$TEMPLATE_COMPONENT_PATH/component.twig",
    "css" to "$TEMPLATE_COMPONENT_PATH/component.css",
    "js" to "$TEMPLATE_COMPONENT_PATH/component.js",
    "stories" to "$TEMPLATE_COMPONENT_PATH/component.stories.twig"
)

class ComponentData {
    var name = ""
    var resource = ""
    var status = "stable"
    var filesToGenerated = listOf<String>()

    fun toTitle(): String {
        return toTitleCase(this.name)
    }

    fun fileName(): String {
        return toKebabCase(this.name)
    }

    fun variableName(): String {
        return toCamelCase(this.name)
    }

    fun isValid(): Boolean {
        return this.name.isNotBlank()
    }

    fun getDataObject(): Map<String, String> {
        return mapOf(
            "title" to this.toTitle(),
            "status" to this.status,
            "titleKebabCase" to this.fileName(),
            "titleCamelCase" to this.variableName(),
            "resource" to this.resource,
        )
    }
}