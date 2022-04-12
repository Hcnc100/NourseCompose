package com.nullpointer.noursecompose.models.alarm

import androidx.annotation.StringRes
import com.nullpointer.noursecompose.ui.screen.destinations.Destination

enum class AlarmTypes(
    val stringResource: String,
    val stringDescription: String,
) {
    ONE_SHOT("Una vez", "La alamra sonara una vez y sera desactivada"),
    INDEFINITELY("Indefinidamente", "La alarma sonara cada cierto tiempo, hasta que la desactives"),
    RANGE("Selecciona un rango",
        "La alarma sonara entre los dias que scojas, pasado el tiempo se desactivara")
}