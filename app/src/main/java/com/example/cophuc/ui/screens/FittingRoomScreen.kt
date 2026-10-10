package com.example.cophuc.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cophuc.data.CostumeDataRepository
import com.example.cophuc.data.model.*
import com.example.cophuc.ui.CostumeUiState
import com.example.cophuc.ui.components.CostumeVisualizer
import com.example.ui.theme.*

@Composable
fun FittingRoomScreen(
    state: CostumeUiState,
    onSelectCostume: (CostumeItem) -> Unit,
    onSelectColor: (CostumeColor) -> Unit,
    onSelectPattern: (CostumePattern) -> Unit,
    onToggleAccessory: (AccessoryItem) -> Unit,
    onSaveOutfit: () -> Unit,
    onShowCulturalInfo: (title: String, description: String, dynasty: String) -> Unit,
    onSelectEvent: (HeritageEvent) -> Unit = {},
    onSelectWeather: (HeritageWeather) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedCustomizerTab by remember { mutableIntStateOf(0) } // 0: Sự kiện, 1: Áo, 2: Màu, 3: Hoa văn, 4: Phụ kiện

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("fitting_room_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Showcase Visualizer Canvas
        CostumeVisualizer(
            costume = state.activeCostume,
            selectedColor = state.activeColor,
            selectedPattern = state.activePattern,
            selectedAccessories = state.selectedAccessories,
            onItemClick = onShowCulturalInfo
        )

        // Real-Time Compatibility Engine Banner
        CompatibilityBanner(
            result = state.compatibility,
            onLearnMore = {
                onShowCulturalInfo(
                    state.compatibility.title,
                    state.compatibility.message,
                    state.activeCostume.dynasty.title
                )
            }
        )

        // Customization Category Tabs: 5 Tabs with Pure White Text & Smooth Scrolling
        val customizerTabs = listOf("1. Sự kiện", "2. Trang phục", "3. Bảng màu", "4. Hoa văn", "5. Phụ kiện")
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = ImperialCrimsonDark,
            border = androidx.compose.foundation.BorderStroke(1.dp, ImperialGold.copy(alpha = 0.45f)),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(16.dp))
        ) {
            ScrollableTabRow(
                selectedTabIndex = selectedCustomizerTab,
                containerColor = Color.Transparent,
                contentColor = Color.White,
                edgePadding = 8.dp,
                indicator = { tabPositions ->
                    if (selectedCustomizerTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedCustomizerTab]),
                            height = 3.dp,
                            color = ImperialGoldLight
                        )
                    }
                },
                divider = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("customizer_tab_row")
            ) {
                customizerTabs.forEachIndexed { index, title ->
                    val isSelected = selectedCustomizerTab == index
                    Tab(
                        selected = isSelected,
                        onClick = { selectedCustomizerTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.78f)
                            )
                        },
                        modifier = Modifier.testTag("customizer_tab_$index")
                    )
                }
            }
        }

        // Customization Panels based on tab
        when (selectedCustomizerTab) {
            0 -> {
                // 1. Sự kiện & Thời tiết
                EventSelectorPanel(
                    selectedEvent = state.selectedEvent,
                    selectedWeather = state.selectedWeather,
                    onSelectEvent = onSelectEvent,
                    onSelectWeather = onSelectWeather,
                    onSelectCostumeAndSwitchTab = { costume ->
                        onSelectCostume(costume)
                        selectedCustomizerTab = 1
                    }
                )
            }
            1 -> {
                // 2. Trang phục & Gợi ý
                CostumeSelectorPanel(
                    activeCostume = state.activeCostume,
                    selectedEvent = state.selectedEvent,
                    selectedWeather = state.selectedWeather,
                    onSelectCostume = onSelectCostume,
                    onShowInfo = onShowCulturalInfo,
                    onChangeEventClick = { selectedCustomizerTab = 0 }
                )
            }
            2 -> {
                // 3. Bảng màu
                ColorPalettePanel(
                    costume = state.activeCostume,
                    activeColor = state.activeColor,
                    onSelectColor = onSelectColor
                )
            }
            3 -> {
                // 4. Hoa văn
                PatternSelectorPanel(
                    costume = state.activeCostume,
                    activePattern = state.activePattern,
                    onSelectPattern = onSelectPattern,
                    onShowInfo = onShowCulturalInfo
                )
            }
            4 -> {
                // 5. Phụ kiện
                AccessoriesSelectorPanel(
                    costume = state.activeCostume,
                    selectedAccessories = state.selectedAccessories,
                    onToggleAccessory = onToggleAccessory,
                    onShowInfo = onShowCulturalInfo
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Save & Export Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onSaveOutfit,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("save_outfit_button"),
                colors = ButtonDefaults.buttonColors(containerColor = ImperialCrimson),
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Icon(imageVector = Icons.Default.BookmarkAdded, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Lưu bộ phối vào Bộ Sưu Tập",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

private data class CompatibilityVisual(
    val bgColor: Color,
    val borderColor: Color,
    val iconColor: Color,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun CompatibilityBanner(
    result: CompatibilityCheckResult,
    onLearnMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val visual = when (result.grade) {
        CompatibilityGrade.PERFECT_HARMONY -> CompatibilityVisual(
            HeritageHarmonyGreen.copy(alpha = 0.1f),
            HeritageHarmonyGreen.copy(alpha = 0.4f),
            HeritageHarmonyGreen,
            Icons.Default.Verified
        )
        CompatibilityGrade.CREATIVE_FUSION -> CompatibilityVisual(
            HeritageWarningAmber.copy(alpha = 0.12f),
            HeritageWarningAmber.copy(alpha = 0.4f),
            HeritageWarningAmber,
            Icons.Default.Lightbulb
        )
        CompatibilityGrade.HISTORICAL_MISMATCH -> CompatibilityVisual(
            ImperialCrimson.copy(alpha = 0.12f),
            ImperialCrimson.copy(alpha = 0.4f),
            ImperialCrimson,
            Icons.Default.Warning
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("compatibility_banner"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = visual.bgColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, visual.borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = visual.icon,
                contentDescription = null,
                tint = visual.iconColor,
                modifier = Modifier
                    .size(24.dp)
                    .padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = result.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = visual.iconColor
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = result.message,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = result.recommendations,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

// 1. Event & Weather Selector
@Composable
fun EventSelectorPanel(
    selectedEvent: HeritageEvent,
    selectedWeather: HeritageWeather,
    onSelectEvent: (HeritageEvent) -> Unit,
    onSelectWeather: (HeritageWeather) -> Unit,
    onSelectCostumeAndSwitchTab: (CostumeItem) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Section: 1. Sự kiện sẽ tham gia
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Celebration,
                contentDescription = null,
                tint = ImperialCrimson,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "1. Sự kiện bạn sẽ tham gia:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = "Lựa chọn bối cảnh để hệ thống gợi ý quy chế phẩm phục và phom dáng chuẩn chỉ nhất.",
            fontSize = 11.sp,
            color = Color.Gray
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(CostumeDataRepository.events, key = { it.id }) { event ->
                val isSelected = event.id == selectedEvent.id
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) ImperialCrimson else MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) ImperialGold else Color.LightGray.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier
                        .width(220.dp)
                        .clickable { onSelectEvent(event) }
                        .testTag("event_card_${event.id}")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SỰ KIỆN",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) ImperialGoldLight else ImperialCrimson
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = ImperialGoldLight,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = event.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = event.subtitle,
                            fontSize = 11.sp,
                            color = if (isSelected) Color.White.copy(alpha = 0.85f) else Color.Gray,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }

        // Section: Thời tiết & Tiết khí
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.WbSunny,
                contentDescription = null,
                tint = ImperialGoldDark,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Thời tiết & Khí hậu:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(CostumeDataRepository.weathers, key = { it.id }) { weather ->
                val isSelected = weather.id == selectedWeather.id
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectWeather(weather) },
                    label = {
                        Text(
                            text = "${weather.title} (${weather.tempRange})",
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ImperialGoldDark,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("weather_chip_${weather.id}")
                )
            }
        }

        // Section: Gợi ý trang phục phù hợp nhất
        val recommendedCostumes = CostumeDataRepository.costumes.filter { costume ->
            selectedEvent.suitableCostumeIds.contains(costume.id) || selectedWeather.suitableCostumeIds.contains(costume.id)
        }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SilkCreamDark.copy(alpha = 0.45f),
            border = androidx.compose.foundation.BorderStroke(1.dp, ImperialGold.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = ImperialCrimson,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Gợi ý trang phục cho ${selectedEvent.title}:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = ImperialCrimson
                    )
                }

                Text(
                    text = "${selectedEvent.tips}\n• Lời khuyên thời tiết: ${selectedWeather.advice}",
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Các mẫu cổ phục thích hợp nhất (Bấm để thử ngay):",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    recommendedCostumes.forEach { costume ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(1.2.dp, ImperialGold.copy(alpha = 0.7f)),
                            shadowElevation = 2.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectCostumeAndSwitchTab(costume) }
                                .testTag("recommendation_item_${costume.id}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = ImperialCrimson,
                                        border = androidx.compose.foundation.BorderStroke(0.8.dp, ImperialGoldLight)
                                    ) {
                                        Text(
                                            text = "★ Khuyên dùng cho sự kiện",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = costume.name,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ImperialCrimson
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${costume.dynasty.title} • ${costume.category}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Button(
                                    onClick = { onSelectCostumeAndSwitchTab(costume) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = ImperialCrimson,
                                        contentColor = Color.White
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, ImperialGoldLight),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Checkroom,
                                        contentDescription = null,
                                        tint = ImperialGoldLight,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Thử ngay", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// 2. Costume Selector
@Composable
fun CostumeSelectorPanel(
    activeCostume: CostumeItem,
    selectedEvent: HeritageEvent,
    selectedWeather: HeritageWeather,
    onSelectCostume: (CostumeItem) -> Unit,
    onShowInfo: (String, String, String) -> Unit,
    onChangeEventClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Event & Weather Recommendation Header Banner
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = SilkCreamDark.copy(alpha = 0.55f),
            border = androidx.compose.foundation.BorderStroke(1.dp, ImperialGold.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Recommend,
                            contentDescription = null,
                            tint = ImperialCrimson,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Gợi ý: ${selectedEvent.title}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ImperialCrimson
                        )
                    }

                    TextButton(
                        onClick = onChangeEventClick,
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp),
                        modifier = Modifier.height(28.dp).testTag("btn_change_event")
                    ) {
                        Text("Đổi sự kiện", fontSize = 11.sp, color = ImperialCrimson, fontWeight = FontWeight.Bold)
                    }
                }

                Text(
                    text = "Tiết trời: ${selectedWeather.title} • ${selectedEvent.tips}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 14.sp
                )

                // Quick recommendation chips
                val recommended = CostumeDataRepository.costumes.filter {
                    selectedEvent.suitableCostumeIds.contains(it.id) || selectedWeather.suitableCostumeIds.contains(it.id)
                }
                if (recommended.isNotEmpty()) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(recommended, key = { "chip_${it.id}" }) { costume ->
                            val isSelected = costume.id == activeCostume.id
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) ImperialCrimson else SilkCream,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.2.dp,
                                    if (isSelected) ImperialGoldLight else ImperialGoldDark
                                ),
                                modifier = Modifier.clickable { onSelectCostume(costume) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "★",
                                        color = if (isSelected) ImperialGoldLight else ImperialCrimson,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = costume.name,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else ImperialCrimson
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Text(
            text = "Chọn dáng áo chính:",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(CostumeDataRepository.costumes, key = { it.id }) { costume ->
                val isSelected = costume.id == activeCostume.id
                val isRecommended = selectedEvent.suitableCostumeIds.contains(costume.id) ||
                        selectedWeather.suitableCostumeIds.contains(costume.id)

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) ImperialCrimson else MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) ImperialGold else if (isRecommended) ImperialGoldDark.copy(alpha = 0.5f) else Color.LightGray.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier
                        .width(168.dp)
                        .clickable { onSelectCostume(costume) }
                        .testTag("costume_select_${costume.id}")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = costume.dynasty.title,
                                fontSize = 10.sp,
                                color = if (isSelected) ImperialGoldLight else Color.Gray,
                                fontWeight = FontWeight.SemiBold
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = ImperialGoldLight,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        if (isRecommended) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isSelected) ImperialGold else ImperialCrimson,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) ImperialGoldLight else ImperialGold
                                ),
                                shadowElevation = 2.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Text(
                                        text = "★",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) ImperialCrimsonDark else ImperialGoldLight
                                    )
                                    Text(
                                        text = "Khuyên dùng",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) ImperialCrimsonDark else Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = costume.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = costume.category,
                            fontSize = 11.sp,
                            color = if (isSelected) Color.White.copy(alpha = 0.85f) else Color.Gray,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

// 2. Color Palette Panel
@Composable
fun ColorPalettePanel(
    costume: CostumeItem,
    activeColor: CostumeColor,
    onSelectColor: (CostumeColor) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Bảng sắc truyền thống (Ngũ Hành):",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            costume.availableColors.forEach { colorItem ->
                val isSelected = colorItem.id == activeColor.id
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onSelectColor(colorItem) }
                        .testTag("color_select_${colorItem.id}")
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(colorItem.color)
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) ImperialGold else Color.Gray.copy(alpha = 0.3f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = if (colorItem.id == "c_ivory") Color.Black else Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = colorItem.name.split(" ").take(2).joinToString(" "),
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(10.dp),
            color = SilkCreamDark.copy(alpha = 0.6f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Ý nghĩa sắc màu: ${activeColor.culturalMeaning}",
                fontSize = 12.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

// 3. Pattern Selector Panel
@Composable
fun PatternSelectorPanel(
    costume: CostumeItem,
    activePattern: CostumePattern,
    onSelectPattern: (CostumePattern) -> Unit,
    onShowInfo: (String, String, String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Hoa văn cổ truyền:",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(CostumeDataRepository.patterns, key = { it.id }) { pattern ->
                val isSelected = pattern.id == activePattern.id
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) ImperialGold.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) ImperialGoldDark else Color.LightGray.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier
                        .width(170.dp)
                        .clickable { onSelectPattern(pattern) }
                        .testTag("pattern_select_${pattern.id}")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = pattern.dynasty.title,
                                fontSize = 10.sp,
                                color = ImperialGoldDark,
                                fontWeight = FontWeight.SemiBold
                            )
                            IconButton(
                                onClick = { onShowInfo(pattern.name, pattern.description, pattern.dynasty.title) },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = pattern.name,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = pattern.symbolism,
                            fontSize = 11.sp,
                            color = Color.Gray,
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}

// 4. Accessories Selector Panel
@Composable
fun AccessoriesSelectorPanel(
    costume: CostumeItem,
    selectedAccessories: List<AccessoryItem>,
    onToggleAccessory: (AccessoryItem) -> Unit,
    onShowInfo: (String, String, String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Phụ kiện đi kèm (${selectedAccessories.size} đã chọn):",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            CostumeDataRepository.accessories.forEach { acc ->
                val isSelected = selectedAccessories.any { it.id == acc.id }
                val isCompatible = costume.compatibleAccessoryIds.contains(acc.id)

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) ImperialCrimson.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = if (isSelected) ImperialCrimson else Color.LightGray.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleAccessory(acc) }
                        .testTag("acc_toggle_${acc.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = { onToggleAccessory(acc) },
                            colors = CheckboxDefaults.colors(checkedColor = ImperialCrimson)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = acc.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (isCompatible) HeritageHarmonyGreen.copy(alpha = 0.15f) else Color.LightGray.copy(alpha = 0.5f)
                                ) {
                                    Text(
                                        text = if (isCompatible) "Chuẩn thời kỳ" else "Khác triều đại",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isCompatible) HeritageHarmonyGreen else Color.DarkGray,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "${acc.category} • ${acc.culturalDescription}",
                                fontSize = 11.sp,
                                color = Color.Gray,
                                maxLines = 1
                            )
                        }

                        IconButton(
                            onClick = { onShowInfo(acc.name, acc.culturalDescription, acc.category) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Chi tiết",
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
