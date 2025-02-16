package com.diarcastro.drupalgenerator

class ComponentData {
    var name = ""
    var status = "stable"
    var schema = "\$schema: https://git.drupalcode.org/project/drupal/-/raw/10.3.x/core/assets/schemas/v1/metadata.schema.json"
    var filesToGenerated = ComponentDataFiles()

    fun toTitle(): String {
        return toTitleCase(this.name)
    }

    fun fileName(): String {
        return toKebebCase(this.name)
    }

    fun variableName(): String {
        return toCamelCase(this.name)
    }

    fun isValid(): Boolean {
        return this.name.isNotBlank()
    }
}

class ComponentDataFiles {
    var yml = true;
    var scss = true;
    var css = true;
    var js = true;
    var story = true;
    var twig = true;
}