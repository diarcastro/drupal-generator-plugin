package com.diarcastro.drupalgenerator

class ComponentData {
    var name = ""
    var status = "stable"
    var schema = "\$schema: https://git.drupalcode.org/project/drupal/-/raw/10.3.x/core/assets/schemas/v1/metadata.schema.json"

    fun toTitle(): String {
        return toTitleCase(this.name)
    }

    fun fileName(): String {
        return toKebebCase(this.name)
    }

    fun isValid(): Boolean {
        return this.name.isNotBlank()
    }
}