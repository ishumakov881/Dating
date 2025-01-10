package com.lds.quickdeal.megaplan.entity

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object TaskStatusSerializer : KSerializer<TaskStatus> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("TaskStatus", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: TaskStatus) {
        encoder.encodeString(value.name.lowercase())
    }

    override fun deserialize(decoder: Decoder): TaskStatus {
        val status = decoder.decodeString()
        return TaskStatus.entries.find { it.name.equals(status, ignoreCase = true) }
            ?: TaskStatus.NONE
    }
}
