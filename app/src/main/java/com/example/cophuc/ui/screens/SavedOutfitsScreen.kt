package com.example.cophuc.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cophuc.data.model.CompatibilityGrade
import com.example.cophuc.data.model.Lookbook
import com.example.cophuc.data.model.SavedOutfit
import com.example.cophuc.ui.CostumeUiState
import com.example.cophuc.ui.components.CostumeExportCardDialog
import com.example.cophuc.ui.components.CreateLookbookDialog
import com.example.cophuc.ui.components.LookbookViewerDialog
import com.example.ui.theme.*

@Composable
fun SavedOutfitsScreen(
    state: CostumeUiState,
    onLoadOutfit: (SavedOutfit) -> Unit,
    onDeleteOutfit: (String) -> Unit,
    onExportCard: (SavedOutfit) -> Unit,
    onCreateLookbook: (title: String, theme: String, description: String, coverStyle: String, outfitIds: List<String>) -> Unit = { _, _, _, _, _ -> },
    onDeleteLookbook: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var outfitToExport by remember { mutableStateOf<SavedOutfit?>(null) }
    var lookbookToView by remember { mutableStateOf<Lookbook?>(null) }
    var showCreateLookbookDialog by remember { mutableStateOf(false) }
    var selectedSubTab by remember { mutableIntStateOf(0) } // 0: Tác phẩm, 1: Lookbook

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("saved_outfits_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ImperialGoldDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Bộ Sưu Tập & Lookbook",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Lưu giữ tác phẩm và xuất bản các tập san Lookbook nghệ thuật cổ phục.",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }

                            Button(
                                onClick = { showCreateLookbookDialog = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ImperialCrimson,
                                    contentColor = Color.White
                                ),
                                border = androidx.compose.foundation.BorderStroke(1.dp, ImperialGoldLight),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                                modifier = Modifier.testTag("btn_open_create_lookbook")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CollectionsBookmark,
                                    contentDescription = null,
                                    tint = ImperialGoldLight,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("+ Tạo Lookbook", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Sub-tabs Navigation
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = SilkCreamDark.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ImperialGold.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TabRow(
                        selectedTabIndex = selectedSubTab,
                        containerColor = Color.Transparent,
                        contentColor = ImperialCrimson,
                        indicator = { tabPositions ->
                            if (selectedSubTab < tabPositions.size) {
                                TabRowDefaults.SecondaryIndicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSubTab]),
                                    height = 3.dp,
                                    color = ImperialCrimson
                                )
                            }
                        },
                        divider = {}
                    ) {
                        Tab(
                            selected = selectedSubTab == 0,
                            onClick = { selectedSubTab = 0 },
                            text = {
                                Text(
                                    text = "Tác phẩm (${state.savedOutfits.size})",
                                    fontSize = 13.sp,
                                    fontWeight = if (selectedSubTab == 0) FontWeight.Bold else FontWeight.Medium,
                                    color = if (selectedSubTab == 0) ImperialCrimson else Color.Gray
                                )
                            },
                            modifier = Modifier.testTag("subtab_outfits")
                        )
                        Tab(
                            selected = selectedSubTab == 1,
                            onClick = { selectedSubTab = 1 },
                            text = {
                                Text(
                                    text = "Tập san Lookbook (${state.lookbooks.size})",
                                    fontSize = 13.sp,
                                    fontWeight = if (selectedSubTab == 1) FontWeight.Bold else FontWeight.Medium,
                                    color = if (selectedSubTab == 1) ImperialCrimson else Color.Gray
                                )
                            },
                            modifier = Modifier.testTag("subtab_lookbooks")
                        )
                    }
                }
            }

            // Tab 0: Single Outfits List
            if (selectedSubTab == 0) {
                if (state.savedOutfits.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Style,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Chưa có bộ phối nào được lưu",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Hãy vào 'Phòng thử đồ' để tự tay kết hợp và lưu lại trang phục của riêng bạn!",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                } else {
                    items(state.savedOutfits, key = { it.id }) { outfit ->
                        SavedOutfitCard(
                            outfit = outfit,
                            onLoad = { onLoadOutfit(outfit) },
                            onDelete = { onDeleteOutfit(outfit.id) },
                            onExport = {
                                outfitToExport = outfit
                            }
                        )
                    }
                }
            } else {
                // Tab 1: Lookbooks List
                if (state.lookbooks.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoStories,
                                    contentDescription = null,
                                    tint = ImperialGoldDark,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Chưa có tập san Lookbook nào",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Hãy tạo tập san Lookbook để tuyển chọn và xuất bản các bộ phối di sản của bạn!",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = { showCreateLookbookDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = ImperialCrimson),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("+ Tạo Lookbook đầu tiên", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                } else {
                    items(state.lookbooks, key = { it.id }) { lookbook ->
                        LookbookCard(
                            lookbook = lookbook,
                            savedOutfits = state.savedOutfits,
                            onViewLookbook = { lookbookToView = lookbook },
                            onDelete = { onDeleteLookbook(lookbook.id) }
                        )
                    }
                }
            }
        }

        // Export Card Dialog showing Front and Back visualizer images
        outfitToExport?.let { outfit ->
            CostumeExportCardDialog(
                outfit = outfit,
                onDismiss = { outfitToExport = null },
                onLoadInFittingRoom = { loadedOutfit ->
                    outfitToExport = null
                    onLoadOutfit(loadedOutfit)
                }
            )
        }

        // Create Lookbook Dialog
        if (showCreateLookbookDialog) {
            CreateLookbookDialog(
                savedOutfits = state.savedOutfits,
                onDismiss = { showCreateLookbookDialog = false },
                onCreate = { title, theme, description, coverStyle, outfitIds ->
                    onCreateLookbook(title, theme, description, coverStyle, outfitIds)
                    selectedSubTab = 1
                }
            )
        }

        // Lookbook Viewer Dialog
        lookbookToView?.let { lb ->
            LookbookViewerDialog(
                lookbook = lb,
                savedOutfits = state.savedOutfits,
                onDismiss = { lookbookToView = null },
                onLoadInFittingRoom = { loadedOutfit ->
                    lookbookToView = null
                    onLoadOutfit(loadedOutfit)
                }
            )
        }
    }
}

@Composable
fun SavedOutfitCard(
    outfit: SavedOutfit,
    onLoad: () -> Unit,
    onDelete: () -> Unit,
    onExport: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("saved_outfit_card_${outfit.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Name and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = outfit.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${outfit.costume.dynasty.title} • ${outfit.costume.region.title}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp).testTag("delete_outfit_${outfit.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Xóa",
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Configuration Details Pill
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SilkCreamDark.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(outfit.selectedColor.color)
                                .border(1.dp, Color.Black.copy(alpha = 0.2f), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Sắc vải: ${outfit.selectedColor.name}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("•", color = Color.Gray)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Hoa văn: ${outfit.selectedPattern.name}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (outfit.selectedAccessories.isNotEmpty()) {
                        Text(
                            text = "Phụ kiện: ${outfit.selectedAccessories.joinToString(", ") { it.name }}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Historical Compatibility Badge
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val statusColor: Color = when (outfit.compatibility.grade) {
                            CompatibilityGrade.PERFECT_HARMONY -> HeritageHarmonyGreen
                            CompatibilityGrade.CREATIVE_FUSION -> HeritageWarningAmber
                            CompatibilityGrade.HISTORICAL_MISMATCH -> ImperialCrimson
                        }
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = statusColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = outfit.compatibility.title,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onExport,
                    modifier = Modifier.weight(1f).testTag("export_card_${outfit.id}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = SilkCream,
                        contentColor = ImperialCrimson
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, ImperialGoldDark),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = null,
                        tint = ImperialCrimson,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Xuất thẻ ảnh",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ImperialCrimson
                    )
                }

                Button(
                    onClick = onLoad,
                    modifier = Modifier.weight(1.2f).testTag("load_outfit_${outfit.id}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ImperialCrimson,
                        contentColor = Color.White
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ImperialGoldLight),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Checkroom,
                        contentDescription = null,
                        tint = ImperialGoldLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Mở phòng thử",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun LookbookCard(
    lookbook: Lookbook,
    savedOutfits: List<SavedOutfit>,
    onViewLookbook: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val outfitsInLookbook = remember(lookbook, savedOutfits) {
        lookbook.outfitIds.mapNotNull { id -> savedOutfits.find { it.id == id } }
    }

    val (coverColor, coverAccent) = when (lookbook.coverStyle) {
        "royal_gold" -> Pair(Color(0xFF8B6508), ImperialGoldLight)
        "jade_green" -> Pair(Color(0xFF1B4D3E), Color(0xFF66BB6A))
        "classic_silk" -> Pair(Color(0xFF1A365D), Color(0xFF64B5F6))
        else -> Pair(ImperialCrimson, ImperialGoldLight)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("lookbook_card_${lookbook.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with cover style accent bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .width(6.dp)
                            .height(36.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(coverColor)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = lookbook.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = coverColor.copy(alpha = 0.15f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, coverColor.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = lookbook.theme,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = coverColor,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${outfitsInLookbook.size} tác phẩm",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp).testTag("delete_lookbook_${lookbook.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Xóa",
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Description
            Text(
                text = lookbook.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Mini thumbnails / list of included outfits
            if (outfitsInLookbook.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SilkCreamDark.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "DANH SÁCH BỘ PHỐI TRONG TẬP SAN:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = ImperialCrimson
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            outfitsInLookbook.take(3).forEach { outfit ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, ImperialGold.copy(alpha = 0.4f)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(6.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(10.dp)
                                                    .clip(CircleShape)
                                                    .background(outfit.selectedColor.color)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = outfit.name,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                maxLines = 1
                                            )
                                        }
                                        Text(
                                            text = outfit.costume.name,
                                            fontSize = 9.sp,
                                            color = Color.Gray,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                            if (outfitsInLookbook.size > 3) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, ImperialGold.copy(alpha = 0.4f)),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "+${outfitsInLookbook.size - 3}",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = coverColor
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Action: Mở tập san Lookbook
            Button(
                onClick = onViewLookbook,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_view_lookbook_${lookbook.id}"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = coverColor,
                    contentColor = Color.White
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, coverAccent),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoStories,
                    contentDescription = null,
                    tint = coverAccent,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Mở Tập San Lookbook Di Sản",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

