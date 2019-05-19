package com.dllewellyn.CryptoApi

import com.google.gson.Gson
import org.springframework.core.io.ClassPathResource
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

data class Resp(val code : String, val value : String)

@Suppress("UNCHECKED_CAST")
@RestController
class ApiController {

    private val gson: Gson by lazy {
        Gson()
    }

    private val dataFromFile: String by lazy {
        ClassPathResource("static/all_currencies.json")
                .inputStream.bufferedReader().use { it.readText() }
    }


    private val mp = mutableMapOf<String, String>().apply {
        putAll(gson.fromJson(dataFromFile, Map::class.java) as Map<String, String>)
    }

    @GetMapping("/all")
    fun pairs() = mp.map {
        Resp(it.key, it.value)
    }

    @GetMapping("/name/{id}")
    fun getBuyName(@PathVariable("id") name : String) =
            if (mp.containsKey(name)) {
                mapOf("value" to mp[name])
            } else {
                throw NotFoundException()
            }
}