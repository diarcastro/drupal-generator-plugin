package com.diarcastro.drupalgenerator

const val TEMPLATE_MODULE_PATH = "templates/module"
val moduleFiles = mapOf(
    "info" to "$TEMPLATE_MODULE_PATH/module.info.yml",
    "install" to "$TEMPLATE_MODULE_PATH/module.install",
    "links" to "$TEMPLATE_MODULE_PATH/module.links.menu.yml",
    "module" to "$TEMPLATE_MODULE_PATH/module.module",
    "permissions" to "$TEMPLATE_MODULE_PATH/module.permissions.yml",
    "routing" to "$TEMPLATE_MODULE_PATH/module.routing.yml",
    "services" to "$TEMPLATE_MODULE_PATH/module.services.yml",
    "readme" to "$TEMPLATE_MODULE_PATH/README.md",
)

class ModuleData {
    var name = ""
    var packageName = "Development"
    var versions = "^8 || ^9 || ^10"
    var filesToGenerate = listOf<String>()

    fun toTitle(): String {
        return toTitleCase(this.name)
    }

    fun fileName(): String {
        return toSnakeCase(this.name)
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
            "package" to this.packageName,
            "versions" to this.versions,
            "titleKebabCase" to this.fileName(),
            "titleCamelCase" to this.variableName(),
            "titleSnakeCase" to toSnakeCase(this.name),
            "titlePascalCase" to toPascalCase(this.name),
        )
    }
}