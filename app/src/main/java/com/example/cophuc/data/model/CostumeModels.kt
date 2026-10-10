package com.example.cophuc.data.model

import androidx.compose.ui.graphics.Color

enum class Dynasty(val title: String, val period: String) {
    LY_TRAN("Nhà Lý - Trần", "Thế kỷ XI - XIV"),
    LE("Nhà Hậu Lê", "Thế kỷ XV - XVIII"),
    NGUYEN("Triều Nguyễn", "1802 - 1945"),
    CONTEMPORARY("Đương đại / Cách tân", "Thế kỷ XX - XXI")
}

enum class Region(val title: String) {
    BAC_BO("Bắc Bộ"),
    TRUNG_BO("Trung Bộ"),
    NAM_BO("Nam Bộ"),
    ALL("Toàn quốc")
}

enum class Occasion(val title: String) {
    WEDDING("Cưới hỏi truyền thống"),
    TET_HOLIDAY("Lễ Tết & Du xuân"),
    ART_PHOTOSHOOT("Chụp ảnh nghệ thuật"),
    FESTIVAL("Lễ hội văn hóa")
}

enum class CostumeStyle(val title: String) {
    ROYAL("Cung đình quý tộc"),
    FOLK("Dân gian truyền thống"),
    MODERN_INNOVATION("Cách tân hiện đại")
}

data class HeritageEvent(
    val id: String,
    val title: String,
    val subtitle: String,
    val suitableCostumeIds: List<String>,
    val tips: String,
    val culturalEtiquette: String
)

data class HeritageWeather(
    val id: String,
    val title: String,
    val tempRange: String,
    val suitableCostumeIds: List<String>,
    val advice: String
)

data class CostumeColor(
    val id: String,
    val name: String,
    val hexValue: Long,
    val color: Color,
    val culturalMeaning: String
)

data class CostumePattern(
    val id: String,
    val name: String,
    val dynasty: Dynasty,
    val description: String,
    val symbolism: String
)

data class AccessoryItem(
    val id: String,
    val name: String,
    val category: String, // "Đầu", "Cổ/Trang sức", "Cầm tay", "Chân"
    val suitableDynasties: List<Dynasty>,
    val suitableStyles: List<CostumeStyle>,
    val culturalDescription: String, // Dưới 50 từ
    val historicalSignificance: String
)

data class CostumeItem(
    val id: String,
    val name: String,
    val category: String,
    val dynasty: Dynasty,
    val region: Region,
    val style: CostumeStyle,
    val occasions: List<Occasion>,
    val availableColors: List<CostumeColor>,
    val availablePatterns: List<CostumePattern>,
    val compatibleAccessoryIds: List<String>,
    val culturalDescription: String, // Dưới 50 từ
    val detailedHistory: String,
    val wearingNorms: String
)

enum class CompatibilityGrade {
    PERFECT_HARMONY,   // Chuẩn mực lịch sử
    CREATIVE_FUSION,   // Giao thoa phá cách (cần lưu ý)
    HISTORICAL_MISMATCH // Lệch triều đại / quy chuẩn phẩm phục
}

data class CompatibilityCheckResult(
    val grade: CompatibilityGrade,
    val title: String,
    val message: String,
    val recommendations: String
)

data class SavedOutfit(
    val id: String,
    val name: String,
    val costume: CostumeItem,
    val selectedColor: CostumeColor,
    val selectedPattern: CostumePattern,
    val selectedAccessories: List<AccessoryItem>,
    val compatibility: CompatibilityCheckResult,
    val createdAt: Long = System.currentTimeMillis()
)

data class Lookbook(
    val id: String,
    val title: String,
    val theme: String,
    val description: String,
    val coverStyle: String, // "imperial_crimson", "royal_gold", "jade_green", "classic_silk"
    val outfitIds: List<String>,
    val createdAt: Long = System.currentTimeMillis()
)

