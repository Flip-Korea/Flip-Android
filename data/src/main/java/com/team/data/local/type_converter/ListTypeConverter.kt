package com.team.data.local.type_converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@ProvidedTypeConverter
class ListTypeConverter(private val moshi: Moshi) {

    // List<Int>
    @TypeConverter
    fun intListToJson(list: List<Int>?): String? {
        return moshi.adapter(List::class.java).toJson(list)
    }

    @TypeConverter
    fun jsonToIntList(json: String): List<Int>? {
        val intListType = Types.newParameterizedType(List::class.java, Int::class.javaObjectType)
        val adapter: JsonAdapter<List<Int>> = moshi.adapter(intListType)
        return adapter.fromJson(json)
    }

    // List<String>
    @TypeConverter
    fun stringListToJson(list: List<String>?): String? {
        val stringListType = Types.newParameterizedType(List::class.java, String::class.java)
        val adapter: JsonAdapter<List<String>> = moshi.adapter(stringListType)
        return adapter.toJson(list)
    }

    @TypeConverter
    fun jsonToStringList(json: String): List<String>? {
        val stringListType = Types.newParameterizedType(List::class.java, String::class.java)
        val adapter: JsonAdapter<List<String>> = moshi.adapter(stringListType)
        return adapter.fromJson(json)
    }

    // List<String>
    @TypeConverter
    fun longListToJson(list: List<Long>?): String? {
        val stringListType = Types.newParameterizedType(List::class.java, Long::class.java)
        val adapter: JsonAdapter<List<Long>> = moshi.adapter(stringListType)
        return adapter.toJson(list)
    }

    @TypeConverter
    fun jsonToLongList(json: String): List<Long>? {
        val stringListType = Types.newParameterizedType(List::class.java, Long::class.java)
        val adapter: JsonAdapter<List<Long>> = moshi.adapter(stringListType)
        return adapter.fromJson(json)
    }
}