package com.example.cophuc.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cophuc.data.CostumeDataRepository
import com.example.cophuc.data.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID

data class CulturalDialogData(
    val title: String = "",
    val description: String = "",
    val dynasty: String = "",
    val isVisible: Boolean = false
)

data class CostumeUiState(
    // Navigation
    val currentTab: Int = 0, // 0: Khám phá, 1: Phòng thử đồ, 2: Tác phẩm, 3: Văn hóa

    // Discovery filters
    val selectedOccasion: Occasion? = null,
    val selectedRegion: Region? = null,
    val selectedStyle: CostumeStyle? = null,
    val filteredCostumes: List<CostumeItem> = CostumeDataRepository.costumes,

    // Fitting Room (Mix & Match)
    val activeCostume: CostumeItem = CostumeDataRepository.costumes.first(),
    val activeColor: CostumeColor = CostumeDataRepository.colors.first(),
    val activePattern: CostumePattern = CostumeDataRepository.patterns.first(),
    val selectedAccessories: List<AccessoryItem> = listOf(
        CostumeDataRepository.accessories.first { it.id == "acc_khan_vanh" },
        CostumeDataRepository.accessories.first { it.id == "acc_kieng_vang" }
    ),
    val compatibility: CompatibilityCheckResult = CostumeDataRepository.checkCompatibility(
        CostumeDataRepository.costumes.first(),
        listOf(
            CostumeDataRepository.accessories.first { it.id == "acc_khan_vanh" },
            CostumeDataRepository.accessories.first { it.id == "acc_kieng_vang" }
        )
    ),

    // Cultural context dialog (<50 words popup)
    val culturalDialog: CulturalDialogData = CulturalDialogData(),

    // Event & Weather selection for recommendation
    val selectedEvent: HeritageEvent = CostumeDataRepository.events.first(),
    val selectedWeather: HeritageWeather = CostumeDataRepository.weathers.first(),

    // Saved Outfits & Lookbooks
    val savedOutfits: List<SavedOutfit> = emptyList(),
    val lookbooks: List<Lookbook> = emptyList(),
    val snackbarMessage: String? = null
)

class CostumeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CostumeUiState())
    val uiState: StateFlow<CostumeUiState> = _uiState.asStateFlow()

    init {
        // Initialize with default presets
        val nhatBinh = CostumeDataRepository.costumes.first { it.id == "nhat_binh" }
        val tuThan = CostumeDataRepository.costumes.first { it.id == "tu_than" }

        val preset1 = SavedOutfit(
            id = "preset_1",
            name = "Hoàng Triều Nhật Bình Cung Đình",
            costume = nhatBinh,
            selectedColor = CostumeDataRepository.colors.first { it.id == "c_red" },
            selectedPattern = CostumeDataRepository.patterns.first { it.id == "p_phoenix" },
            selectedAccessories = listOf(
                CostumeDataRepository.accessories.first { it.id == "acc_khan_vanh" },
                CostumeDataRepository.accessories.first { it.id == "acc_kieng_vang" },
                CostumeDataRepository.accessories.first { it.id == "acc_quat_tram" }
            ),
            compatibility = CostumeDataRepository.checkCompatibility(
                nhatBinh,
                listOf(
                    CostumeDataRepository.accessories.first { it.id == "acc_khan_vanh" },
                    CostumeDataRepository.accessories.first { it.id == "acc_kieng_vang" },
                    CostumeDataRepository.accessories.first { it.id == "acc_quat_tram" }
                )
            )
        )

        val preset2 = SavedOutfit(
            id = "preset_2",
            name = "Kinh Bắc Tứ Thân Quai Thao",
            costume = tuThan,
            selectedColor = CostumeDataRepository.colors.first { it.id == "c_indigo" },
            selectedPattern = CostumeDataRepository.patterns.first { it.id == "p_lotus" },
            selectedAccessories = listOf(
                CostumeDataRepository.accessories.first { it.id == "acc_non_ba_tam" },
                CostumeDataRepository.accessories.first { it.id == "acc_khan_mo_qua" }
            ),
            compatibility = CostumeDataRepository.checkCompatibility(
                tuThan,
                listOf(
                    CostumeDataRepository.accessories.first { it.id == "acc_non_ba_tam" },
                    CostumeDataRepository.accessories.first { it.id == "acc_khan_mo_qua" }
                )
            )
        )

        val aoBaBa = CostumeDataRepository.costumes.first { it.id == "ao_ba_ba" }
        val preset3 = SavedOutfit(
            id = "preset_3",
            name = "Nam Bộ Áo Bà Ba Miền Tây",
            costume = aoBaBa,
            selectedColor = CostumeDataRepository.colors.first { it.id == "c_jade" },
            selectedPattern = CostumeDataRepository.patterns.first { it.id == "p_lotus" },
            selectedAccessories = listOf(
                CostumeDataRepository.accessories.first { it.id == "acc_khan_ran" },
                CostumeDataRepository.accessories.first { it.id == "acc_non_la" }
            ),
            compatibility = CostumeDataRepository.checkCompatibility(
                aoBaBa,
                listOf(
                    CostumeDataRepository.accessories.first { it.id == "acc_khan_ran" },
                    CostumeDataRepository.accessories.first { it.id == "acc_non_la" }
                )
            )
        )

        val defaultLookbook = Lookbook(
            id = "lb_hoang_trieu",
            title = "Tuyển Tập Phục Sức Cổ Phong Việt",
            theme = "Hoàng Gia & Dân Gian",
            description = "Tập san Lookbook nghệ thuật tuyển chọn các phom dáng trang trọng quý phái của cung đình và dân gian Việt Nam qua các triều đại.",
            coverStyle = "imperial_crimson",
            outfitIds = listOf("preset_1", "preset_2", "preset_3")
        )

        _uiState.update {
            it.copy(
                savedOutfits = listOf(preset1, preset2, preset3),
                lookbooks = listOf(defaultLookbook)
            )
        }
    }

    fun setTab(index: Int) {
        _uiState.update { it.copy(currentTab = index) }
    }

    // Filter handlers
    fun selectOccasionFilter(occasion: Occasion?) {
        _uiState.update { current ->
            val updated = current.copy(selectedOccasion = occasion)
            updated.copy(filteredCostumes = computeFilteredCostumes(updated))
        }
    }

    fun selectRegionFilter(region: Region?) {
        _uiState.update { current ->
            val updated = current.copy(selectedRegion = region)
            updated.copy(filteredCostumes = computeFilteredCostumes(updated))
        }
    }

    fun selectStyleFilter(style: CostumeStyle?) {
        _uiState.update { current ->
            val updated = current.copy(selectedStyle = style)
            updated.copy(filteredCostumes = computeFilteredCostumes(updated))
        }
    }

    private fun computeFilteredCostumes(state: CostumeUiState): List<CostumeItem> {
        return CostumeDataRepository.costumes.filter { costume ->
            val matchOccasion = state.selectedOccasion == null || costume.occasions.contains(state.selectedOccasion)
            val matchRegion = state.selectedRegion == null || costume.region == state.selectedRegion || costume.region == Region.ALL
            val matchStyle = state.selectedStyle == null || costume.style == state.selectedStyle
            matchOccasion && matchRegion && matchStyle
        }
    }

    // Mix & Match handlers
    fun selectCostume(costume: CostumeItem) {
        _uiState.update { current ->
            val newColor = if (costume.availableColors.contains(current.activeColor)) current.activeColor else costume.availableColors.firstOrNull() ?: CostumeDataRepository.colors.first()
            val newPattern = if (costume.availablePatterns.contains(current.activePattern)) current.activePattern else costume.availablePatterns.firstOrNull() ?: CostumeDataRepository.patterns.first()
            val compatibility = CostumeDataRepository.checkCompatibility(costume, current.selectedAccessories)
            current.copy(
                activeCostume = costume,
                activeColor = newColor,
                activePattern = newPattern,
                compatibility = compatibility
            )
        }
    }

    fun selectColor(color: CostumeColor) {
        _uiState.update { it.copy(activeColor = color) }
    }

    fun selectPattern(pattern: CostumePattern) {
        _uiState.update { it.copy(activePattern = pattern) }
    }

    fun selectEvent(event: HeritageEvent) {
        _uiState.update { it.copy(selectedEvent = event) }
    }

    fun selectWeather(weather: HeritageWeather) {
        _uiState.update { it.copy(selectedWeather = weather) }
    }

    fun toggleAccessory(accessory: AccessoryItem) {
        _uiState.update { current ->
            val currentList = current.selectedAccessories.toMutableList()
            if (currentList.any { it.id == accessory.id }) {
                currentList.removeAll { it.id == accessory.id }
            } else {
                if (accessory.category == "Trang sức đầu") {
                    currentList.removeAll { it.category == "Trang sức đầu" }
                }
                currentList.add(accessory)
            }
            val compatibility = CostumeDataRepository.checkCompatibility(current.activeCostume, currentList)
            current.copy(selectedAccessories = currentList, compatibility = compatibility)
        }
    }

    // Cultural Context dialog (<50 words)
    fun showCulturalDialog(title: String, description: String, dynasty: String) {
        _uiState.update {
            it.copy(
                culturalDialog = CulturalDialogData(
                    title = title,
                    description = description,
                    dynasty = dynasty,
                    isVisible = true
                )
            )
        }
    }

    fun dismissCulturalDialog() {
        _uiState.update { it.copy(culturalDialog = it.culturalDialog.copy(isVisible = false)) }
    }

    // Save current mix & match outfit
    fun saveCurrentOutfit(customTitle: String? = null) {
        val state = _uiState.value
        val title = customTitle?.ifBlank { null }
            ?: "${state.activeCostume.name} - Sắc ${state.activeColor.name}"

        val newOutfit = SavedOutfit(
            id = UUID.randomUUID().toString(),
            name = title,
            costume = state.activeCostume,
            selectedColor = state.activeColor,
            selectedPattern = state.activePattern,
            selectedAccessories = state.selectedAccessories,
            compatibility = state.compatibility
        )

        _uiState.update {
            it.copy(
                savedOutfits = listOf(newOutfit) + it.savedOutfits,
                snackbarMessage = "Đã lưu bộ phối '$title' vào bộ sưu tập!"
            )
        }
    }

    fun deleteSavedOutfit(id: String) {
        _uiState.update { current ->
            current.copy(
                savedOutfits = current.savedOutfits.filterNot { it.id == id },
                snackbarMessage = "Đã xóa bộ phối thành công."
            )
        }
    }

    fun loadSavedOutfitToFittingRoom(outfit: SavedOutfit) {
        _uiState.update {
            it.copy(
                currentTab = 1,
                activeCostume = outfit.costume,
                activeColor = outfit.selectedColor,
                activePattern = outfit.selectedPattern,
                selectedAccessories = outfit.selectedAccessories,
                compatibility = outfit.compatibility,
                snackbarMessage = "Đã mở bộ phối '${outfit.name}' trong phòng thử đồ!"
            )
        }
    }

    // Lookbook creation and management
    fun createLookbook(
        title: String,
        theme: String,
        description: String,
        coverStyle: String,
        outfitIds: List<String>
    ) {
        val newLookbook = Lookbook(
            id = UUID.randomUUID().toString(),
            title = title.ifBlank { "Lookbook Cổ Phục Di Sản" },
            theme = theme,
            description = description.ifBlank { "Tuyển tập những tác phẩm cổ phục truyền thống Việt Nam đặc sắc." },
            coverStyle = coverStyle,
            outfitIds = outfitIds
        )
        _uiState.update {
            it.copy(
                lookbooks = listOf(newLookbook) + it.lookbooks,
                snackbarMessage = "Đã xuất bản tập san Lookbook '${newLookbook.title}' thành công!"
            )
        }
    }

    fun deleteLookbook(lookbookId: String) {
        _uiState.update { current ->
            current.copy(
                lookbooks = current.lookbooks.filterNot { it.id == lookbookId },
                snackbarMessage = "Đã xóa Lookbook khỏi bộ sưu tập."
            )
        }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}
