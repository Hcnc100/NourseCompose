package com.nullpointer.noursecompose.models.notify

enum class TypeNotify(val nameType: String,val  description: String) {
    NOTIFY("Notificacion", "Lanza solo una notificacions"),
    ALARM("Alarma", "Lanza una alarma de pantalla completa, que vibra y suena");
}