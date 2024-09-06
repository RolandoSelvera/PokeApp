package com.rolandoselvera.pokeapp.common

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class Utils {
    /**
     * Obtain JsonString from object
     *
     * @param data Object to serialize
     * @return json string from Object
     */
    fun <T> serialize(data: T): String? {
        val mGson = Gson()

        val json: String? = try {
            mGson.toJson(data)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            null
        }

        return json
    }

    /**
     * Obtain Object from jsonString
     *
     * @param json String to convert in Object
     * @param typeClass Object class to convert
     *
     * @return Object from json string
     */
    fun <T> deserialize(json: String, typeClass: Class<T>): T? {
        val mGson = Gson()

        val data: T? = try {
            mGson.fromJson(json, typeClass)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            null
        }

        return data
    }
}