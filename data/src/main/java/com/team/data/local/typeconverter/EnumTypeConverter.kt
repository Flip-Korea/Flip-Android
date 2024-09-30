package com.team.data.local.typeconverter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.team.data.local.entity.post.BackgroundColorTypeEntity
import com.team.domain.type.BackgroundColorType

@ProvidedTypeConverter
class EnumTypeConverter {

    @TypeConverter
    fun toBackgroundColorTypeEntity(value: String) = enumValueOf<BackgroundColorTypeEntity>(value)

    @TypeConverter
    fun fromBackgroundColorTypeEntity(value: BackgroundColorTypeEntity) = value.name
}

//class Converters {
//
//    @TypeConverter
//    fun toHealth(value: Int) = enumValues<Health>()[value]
//
//    @TypeConverter
//    fun fromHealth(value: Health) = value.ordinal
//}