package com.nullpointer.noursecompose.models.registry


enum class TypeRegistry(
    val stringValue: String,
) {
    CREATE("Create"),
    UPDATE("Update"),
    DELETER("Deleter"),
    REGISTRY("Registry Alarm"),
    UNREGISTER("Unregister Alarm"),
    RESTORE("Restore"),
    ERROR_LAUNCH("Error launch"),
    LAUNCH("Launch");

    companion object {
        fun myValueOf(value: String) = when (value) {
            UPDATE.stringValue -> UPDATE
            DELETER.stringValue -> DELETER
            REGISTRY.stringValue -> REGISTRY
            LAUNCH.stringValue -> LAUNCH
            ERROR_LAUNCH.stringValue -> ERROR_LAUNCH
            UNREGISTER.stringValue->UNREGISTER
            RESTORE.stringValue->RESTORE
            else -> CREATE
        }
    }
}