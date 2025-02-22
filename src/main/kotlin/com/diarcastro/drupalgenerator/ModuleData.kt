package com.diarcastro.drupalgenerator

const val TEMPLATE_MODULE_PATH = "templates/module"
val moduleFiles = mapOf(
    "info" to "$TEMPLATE_MODULE_PATH/module.info.yml",
    "install" to "$TEMPLATE_MODULE_PATH/module.install",
    "libraries" to "$TEMPLATE_MODULE_PATH/module.libraries.yml",
    "links" to "$TEMPLATE_MODULE_PATH/module.links.menu.yml",
    "module" to "$TEMPLATE_MODULE_PATH/module.module",
    "permissions" to "$TEMPLATE_MODULE_PATH/module.permissions.yml",
    "routing" to "$TEMPLATE_MODULE_PATH/module.routing.yml",
    "services" to "$TEMPLATE_MODULE_PATH/module.services.yml",
    "readme" to "$TEMPLATE_MODULE_PATH/README.md",
)

class ModuleData {
    var name = ""
    var packageName = "Custom"
    var versions = "^8 || ^9 || ^10"
    var description = "My module description"
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
            "title" to toTitle(),
            "description" to description,
            "package" to packageName,
            "versions" to versions,
            "titleKebabCase" to fileName(),
            "titleCamelCase" to variableName(),
            "titleSnakeCase" to toSnakeCase(name),
            "titlePascalCase" to toPascalCase(name),
        )
    }
}