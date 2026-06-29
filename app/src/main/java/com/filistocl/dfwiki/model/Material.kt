package com.filistocl.dfwiki.model

import kotlinx.serialization.Serializable

@Serializable
data class Material(
    val id: String,
    val name: String,
    val type: String,
    val category: String,
    val properties: MaterialProperties,
    val decomposition: Decomposition,
    val description: String
)

@Serializable
data class MaterialProperties(
    val value_multiplier: Int,
    val hardness: String,
    val brittle: Boolean,
    val strength: String
)

@Serializable
data class Decomposition(
    val produces: List<String>
)