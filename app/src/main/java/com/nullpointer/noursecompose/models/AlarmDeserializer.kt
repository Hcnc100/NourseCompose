package com.nullpointer.noursecompose.models

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.models.alarm.AlarmTypes
import java.lang.reflect.Type

class AlarmDeserializer : JsonDeserializer<Alarm?> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): Alarm {
        val jsonObject = json.asJsonObject
        return Alarm(
            name = jsonObject["name"].asString,
            description = jsonObject["description"].asString,
            typeAlarm = AlarmTypes.valueOf(jsonObject["typeAlarm"].asString),
            nextAlarm = jsonObject["nextAlarm"]?.asLong,
            pathFile = jsonObject["pathFile"]?.asString,
            repeaterEvery = jsonObject["repeaterEvery"]?.asLong,
            rangeInitAlarm = jsonObject["rangeInitAlarm"]?.asLong,
            rangeFinishAlarm = jsonObject["rangeFinishAlarm"]?.asLong,
            createdAt = jsonObject["createdAt"].asLong,
            isActive= jsonObject["isActive"].asBoolean,
            id = jsonObject["id"].asLong
        )
    }
}