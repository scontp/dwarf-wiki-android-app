package com.filistocl.dfwiki.data

import android.content.Context
import com.filistocl.dfwiki.model.Material
import kotlinx.serialization.json.Json
import java.io.InputStream

class WikiRepository(private val context: Context) {
    private val json = Json { 
        ignoreUnknownKeys = true 
        coerceInputValues = true
    }

    fun getMaterials(): List<Material> {
        return try {
            val inputStream: InputStream = context.assets.open("df_rocks_ores.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            json.decodeFromString<List<Material>>(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}