package com.filistocl.dfwiki.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.filistocl.dfwiki.data.WikiRepository
import com.filistocl.dfwiki.model.Material

class WikiViewModel(private val repository: WikiRepository) : ViewModel() {
    private val _materials = mutableStateOf<List<Material>>(emptyList())
    val materials: State<List<Material>> = _materials

    init {
        loadMaterials()
    }

    fun loadMaterials() {
        _materials.value = repository.getMaterials()
    }
}
