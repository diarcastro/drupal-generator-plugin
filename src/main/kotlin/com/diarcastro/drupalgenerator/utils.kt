package com.diarcastro.drupalgenerator

/**
 * Convert a string to kebab-case.
 */
fun toKebebCase(input: String): String {
    return input
        .replace(" ", "-") // Remove spaces
        .replace(Regex("([a-z])([A-Z])"), "$1-$2") // Convert camelCase to kebab-case
        .replace("_", "-") // Convert snake_case to kebab-case
        .lowercase()
}

fun toTitleCase(input: String): String {
    return input
        .replace(Regex("([a-z])([A-Z])"), "$1 $2") // Convert camelCase to Title Case
        .replace("_", " ") // Convert snake_case to Title Case
        .replace("-", " ") // Convert kebab-case to Title Case
        .replaceFirstChar { it.uppercase() } // Capitalize first letter
}