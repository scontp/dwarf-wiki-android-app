package com.filistocl.dfwiki.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.filistocl.dfwiki.data.WikiRepository
import com.filistocl.dfwiki.model.Material

private const val TAG = "WikiViewModel"

class WikiViewModel(private val repository: WikiRepository) : ViewModel() {

    private val _materials = mutableStateOf<List<Material>>(emptyList())
    val materials: State<List<Material>> = _materials

    // State for filters
    var selectedCategory: MutableState<String?> = mutableStateOf(null)
    var selectedHardness: MutableState<String?> = mutableStateOf(null)
    var selectedValue: MutableState<String?> = mutableStateOf(null)

    init {
        loadMaterials()
    }

    fun loadMaterials() {
        try {
            _materials.value = repository.getMaterials()
        } catch (e: Exception) {
            Log.e(TAG, "Error loading materials", e)
            _materials.value = emptyList() // Provide empty list on error
        }
    }
}
