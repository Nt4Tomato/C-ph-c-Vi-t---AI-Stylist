package com.example.cophuc.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cophuc.data.model.*
import com.example.cophuc.ui.CostumeUiState
import com.example.ui.theme.*

@Composable
fun DiscoveryScreen(
    state: CostumeUiState,
    onSelectOccasion: (Occasion?) -> Unit,
    onSelectRegion: (Region?) -> Unit,
    onSelectStyle: (CostumeStyle?) -> Unit,
    onTryCostume: (CostumeItem) -> Unit,
    onShowCulturalInfo: (title: String, description: String, dynasty: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("discovery_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header Banner
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ImperialCrimson),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = ImperialGold.copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = "BẢN SẮC NGÀN NĂM",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ImperialGoldLight,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Khám Phá Cổ Phục Việt",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Chiêm ngưỡng dòng chảy trang phục truyền thống qua các thời kỳ Lý, Trần, Lê, Nguyễn với nét đẹp thuần khiết phương Nam.",
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        // Filters Section
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Bộ lọc tiêu chí",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Filter by Occasion
                Text(
                    text = "Dịp & Sự kiện",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = state.selectedOccasion == null,
                        onClick = { onSelectOccasion(null) },
                        label = { Text("Tất cả dịp") },
                        modifier = Modifier.testTag("filter_occasion_all")
                    )
                    Occasion.entries.forEach { occ ->
                        FilterChip(
                            selected = state.selectedOccasion == occ,
                            onClick = { onSelectOccasion(if (state.selectedOccasion == occ) null else occ) },
                            label = { Text(occ.title) },
                            modifier = Modifier.testTag("filter_occasion_${occ.name}")
                        )
                    }
                }

                // Filter by Region
                Text(
                    text = "Vùng miền",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = state.selectedRegion == null,
                        onClick = { onSelectRegion(null) },
                        label = { Text("Tất cả vùng") },
                        modifier = Modifier.testTag("filter_region_all")
                    )
                    listOf(Region.BAC_BO, Region.TRUNG_BO, Region.NAM_BO).forEach { reg ->
                        FilterChip(
                            selected = state.selectedRegion == reg,
                            onClick = { onSelectRegion(if (state.selectedRegion == reg) null else reg) },
                            label = { Text(reg.title) },
                            modifier = Modifier.testTag("filter_region_${reg.name}")
                        )
                    }
                }

                // Filter by Style
                Text(
                    text = "Phong cách",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = state.selectedStyle == null,
                        onClick = { onSelectStyle(null) },
                        label = { Text("Tất cả phong cách") },
                        modifier = Modifier.testTag("filter_style_all")
                    )
                    CostumeStyle.entries.forEach { style ->
                        FilterChip(
                            selected = state.selectedStyle == style,
                            onClick = { onSelectStyle(if (state.selectedStyle == style) null else style) },
                            label = { Text(style.title) },
                            modifier = Modifier.testTag("filter_style_${style.name}")
                        )
                    }
                }
            }
        }

        // Costumes list count
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Danh mục trang phục (${state.filteredCostumes.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Costume Cards
        items(state.filteredCostumes, key = { it.id }) { costume ->
            CostumeCard(
                costume = costume,
                onTryClick = { onTryCostume(costume) },
                onInfoClick = {
                    onShowCulturalInfo(costume.name, costume.culturalDescription, costume.dynasty.title)
                }
            )
        }
    }
}

@Composable
fun CostumeCard(
    costume: CostumeItem,
    onTryClick: () -> Unit,
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("costume_card_${costume.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Category & Dynasty badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ImperialCrimson.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = costume.category,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ImperialCrimson,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SilkCreamDark
                ) {
                    Text(
                        text = "${costume.dynasty.title} • ${costume.region.title}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = DarkLacquer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Costume Name
            Text(
                text = costume.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Short Cultural Context (<50 words)
            Text(
                text = costume.culturalDescription,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Color swatches preview
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Bảng sắc:",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
                costume.availableColors.take(4).forEach { col ->
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(col.color)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onInfoClick,
                    modifier = Modifier.weight(1f).testTag("info_button_${costume.id}"),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = ImperialCrimson
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Điển tích", fontSize = 12.sp, color = ImperialCrimson)
                }

                Button(
                    onClick = onTryClick,
                    modifier = Modifier.weight(1.3f).testTag("try_button_${costume.id}"),
                    colors = ButtonDefaults.buttonColors(containerColor = ImperialCrimson),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Phối đồ ngay", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
