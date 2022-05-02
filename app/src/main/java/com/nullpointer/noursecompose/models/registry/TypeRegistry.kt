package com.nullpointer.noursecompose.models.registry

import androidx.annotation.StringRes
import com.nullpointer.noursecompose.R


enum class TypeRegistry(
    @StringRes
    val title: Int,
    @StringRes
    val description:Int
) {
    CREATE(R.string.title_create,R.string.description_create),
    UPDATE(R.string.title_update,R.string.description_update),
    DELETER(R.string.title_deleter,R.string.description_deleter),
    REGISTRY(R.string.title_registry,R.string.description_registry),
    UNREGISTER(R.string.title_unregistry_alarm,R.string.description_unregistry),
    RESTORE(R.string.title_restore,R.string.description_restore),
    ERROR_LAUNCH(R.string.title_error_lauch,R.string.description_error_launch),
    LAUNCH(R.string.title_launch,R.string.description_launch)
}