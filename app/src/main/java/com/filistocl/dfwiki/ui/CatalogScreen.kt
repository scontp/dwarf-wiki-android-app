package com.filistocl.dfwiki.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.filistocl.dfwiki.data.WikiRepository
import com.filistocl.dfwiki.model.Material

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    repository: WikiRepository,
    onMaterialClick: (Material) -> Unit,
    onBack: () -> Unit
) {
    val allMaterials = remember { repository.getMaterials() }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var selectedHardness by remember { mutableStateOf<String?>(null) }
    var selectedValue by remember { mutableStateOf<String?>(null) }

    val categories = remember {
        allMaterials.map { it.category }.distinct().sorted()
    }

    val hardnessLevels = listOf("Baja", "Media", "Alta")
    val valueLevels = listOf("Bajo", "Medio", "Alto")

    val filteredMaterials = remember(allMaterials, searchQuery, selectedCategory, selectedHardness, selectedValue) {
        allMaterials.filter {
            material ->
            val matchesSearch = searchQuery.isBlank() ||
                    material.name.contains(searchQuery, ignoreCase = true) ||
                    material.type.contains(searchQuery, ignoreCase = true) ||
                    material.description.contains(searchQuery, ignoreCase = true)

            val matchesCategory = selectedCategory == null || material.category == selectedCategory

            val matchesHardness = selectedHardness == null ||
                    when (selectedHardness) {
                        "Baja" -> material.properties.hardness.equals("Soft", ignoreCase = true)
                        "Media" -> material.properties.hardness.equals("Medium", ignoreCase = true) || material.properties.hardness.equals("Hard", ignoreCase = true)
                        "Alta" -> material.properties.hardness.equals("Very Hard", ignoreCase = true)
                        else -> false
                    }

            val matchesValue = selectedValue == null ||
                    when (selectedValue) {
                        "Bajo" -> material.properties.value_multiplier < 50
                        "Medio" -> material.properties.value_multiplier >= 50 && material.properties.value_multiplier < 200
                        "Alto" -> material.properties.value_multiplier >= 200
                        else -> false
                    }

            matchesSearch && matchesCategory && matchesHardness && matchesValue
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C2C2C))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) {
                Text(
                    text = "← Volver",
                    color = Color(0xFFD4AF37),
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Catálogo de Materiales",
                color = Color(0xFFD4AF37),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(72.dp))
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar material...", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFD4AF37),
                unfocusedBorderColor = Color(0xFF555555),
                cursorColor = Color(0xFFD4AF37)
            ),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedCategory == null && selectedHardness == null && selectedValue == null,
                    onClick = {
                        selectedCategory = null
                        selectedHardness = null
                        selectedValue = null
                    },
                    label = { Text("Todos", color = Color.White, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFD4AF37).copy(alpha = 0.3f),
                        containerColor = Color(0xFF3D3D3D)
                    )
                )
            }
            items(categories) {
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = {
                        selectedCategory = if (selectedCategory == category) null else category
                        selectedHardness = null
                        selectedValue = null
                    },
                    label = { Text(category, color = Color.White, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFD4AF37).copy(alpha = 0.3f),
                        containerColor = Color(0xFF3D3D3D)
                    )
                )
            }
            items(hardnessLevels) {
                FilterChip(
                    selected = selectedHardness == hardness,
                    onClick = {
                        selectedHardness = if (selectedHardness == hardness) null else hardness
                        selectedCategory = null
                        selectedValue = null
                    },
                    label = { Text(hardness, color = Color.White, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFD4AF37).copy(alpha = 0.3f),
                        containerColor = Color(0xFF3D3D3D)
                    )
                )
            }
            items(valueLevels) {
                FilterChip(
                    selected = selectedValue == value,
                    onClick = {
                        selectedValue = if (selectedValue == value) null else value
                        selectedCategory = null
                        selectedHardness = null
                    },
                    label = { Text(value, color = Color.White, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFD4AF37).copy(alpha = 0.3f),
                        containerColor = Color(0xFF3D3D3D)
                    )
                )
            }
        }

        Text(
            text = "${filteredMaterials.size} materiales encontrados",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        if (filteredMaterials.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sin resultados 😔\nIntenta con otro término de búsqueda.",
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredMaterials, key = { it.id }) {
                    MaterialCard(material = it, onClick = { onMaterialClick(it) })
                }
            }
        }
    }
}

@Composable
private fun MaterialCard(material: Material, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF3D3D3D)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF555555)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = material.name.take(2).uppercase(),
                        color = Color(0xFFD4AF37),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = material.name,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    text = "${material.type} — ${material.category}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Text(
                    text = "Valor: ${material.properties.value_multiplier}x · Dureza: ${material.properties.hardness}",
                    color = Color(0xFFD4AF37).copy(alpha = 0.7f),
                    fontSize = 11.sp
                )
            }

            Text(
                text = "→",
                color = Color(0xFFD4AF37),
                fontSize = 20.sp
            )
        }
    }
}