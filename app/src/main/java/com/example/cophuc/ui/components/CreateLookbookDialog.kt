package com.example.cophuc.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.cophuc.data.model.SavedOutfit
import com.example.ui.theme.*

/**
 * Hộp thoại tạo tập san Lookbook nghệ thuật từ các bộ phối đã lưu.
 */
@Composable
fun CreateLookbookDialog(
    savedOutfits: List<SavedOutfit>,
    onDismiss: () -> Unit,
    onCreate: (title: String, theme: String, description: String, coverStyle: String, outfitIds: List<String>) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedTheme by remember { mutableStateOf("Hoàng Gia Cung Đình") }
    var selectedCoverStyle by remember { mutableStateOf("imperial_crimson") }
    var description by remember {
        mutableStateOf("Tập san Lookbook nghệ thuật tuyển chọn những tinh hoa phục sức truyền thống qua các thời kỳ.")
    }
    var selectedOutfitIds by remember {
        mutableStateOf(savedOutfits.map { it.id }.toSet())
    }

    val themes = listOf(
        "Hoàng Gia Cung Đình",
        "Hôn Lễ Truyền Thống",
        "Hội Xuân Dân Gian",
        "Kỷ Yếu Cổ Phong",
        "Đời Thường Thanh Nhã"
    )

    val coverStyles = listOf(
        Pair("imperial_crimson", "Đỏ Hoàng Triều"),
        Pair("royal_gold", "Vàng Hoàng Kim"),
        Pair("jade_green", "Xanh Ngọc Bích"),
        Pair("classic_silk", "Lam Chàm Cổ Điển")
    )

    val quickTitles = listOf(
        "Dạ Yến Hoàng Cung",
        "Hôn Lễ Cổ Phong",
        "Sắc Xuân Kinh Bắc",
        "Dáng Xưa Phố Thị"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .testTag("create_lookbook_dialog"),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(2.dp, ImperialGold)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = ImperialCrimson,
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AutoStories,
                                    contentDescription = null,
                                    tint = ImperialGoldLight,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "TẠO TẬP SAN LOOKBOOK",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = ImperialCrimson
                            )
                            Text(
                                text = "Tập hợp các bộ phối thành cuốn tạp chí thời trang di sản",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("btn_close_create_lookbook")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Đóng", tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Form Scrollable
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // 1. Tên Lookbook
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Tên tập san Lookbook:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            placeholder = { Text("Ví dụ: Dạ Yến Hoàng Cung Triều Nguyễn", fontSize = 13.sp) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_lookbook_title"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Gợi ý tên nhanh
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(quickTitles) { suggestion ->
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = SilkCream,
                                    border = BorderStroke(1.dp, ImperialGold.copy(alpha = 0.5f)),
                                    modifier = Modifier.clickable { title = suggestion }
                                ) {
                                    Text(
                                        text = "+ $suggestion",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = ImperialCrimson,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    // 2. Chủ đề Lookbook
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Chủ đề tập san:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(themes) { theme ->
                                val isSelected = theme == selectedTheme
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedTheme = theme },
                                    label = { Text(theme, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = ImperialCrimson,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }

                    // 3. Phong cách bìa
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Phong cách trang bìa:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            coverStyles.forEach { (styleKey, styleName) ->
                                val isSelected = selectedCoverStyle == styleKey
                                val swatchColor = when (styleKey) {
                                    "imperial_crimson" -> ImperialCrimson
                                    "royal_gold" -> ImperialGoldDark
                                    "jade_green" -> JadeGreen
                                    else -> Color(0xFF1E3A8A)
                                }
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) swatchColor.copy(alpha = 0.15f) else Color.Transparent,
                                    border = BorderStroke(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) swatchColor else Color.LightGray
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedCoverStyle = styleKey }
                                ) {
                                    Column(
                                        modifier = Modifier.padding(8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(swatchColor)
                                                .border(1.dp, Color.White, CircleShape)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = styleName,
                                            fontSize = 10.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) swatchColor else Color.Gray,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 4. Lời tựa biên tập
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Lời tựa / Lời bình biên tập:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            placeholder = { Text("Viết lời giới thiệu về tập san...", fontSize = 12.sp) },
                            maxLines = 3,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_lookbook_desc"),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // 5. Chọn bộ phối đưa vào Lookbook
                    val isDark = isSystemInDarkTheme()
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Chọn tác phẩm đưa vào (${selectedOutfitIds.size}/${savedOutfits.size}):",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color.White else DarkLacquer
                            )

                            TextButton(
                                onClick = {
                                    selectedOutfitIds = if (selectedOutfitIds.size == savedOutfits.size) {
                                        emptySet()
                                    } else {
                                        savedOutfits.map { it.id }.toSet()
                                    }
                                },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = if (selectedOutfitIds.size == savedOutfits.size) "Bỏ chọn hết" else "Chọn tất cả",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) ImperialGoldLight else ImperialCrimson
                                )
                            }
                        }

                        if (savedOutfits.isEmpty()) {
                            Text(
                                text = "Chưa có bộ phối nào được lưu. Hãy vào phòng thử để lưu ít nhất 1 bộ phối trước!",
                                fontSize = 12.sp,
                                color = if (isDark) Color.White.copy(alpha = 0.8f) else Color.DarkGray
                            )
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                savedOutfits.forEach { outfit ->
                                    val isChecked = selectedOutfitIds.contains(outfit.id)

                                    // Màu sắc tương phản cao đảm bảo dễ nhìn trong cả trạng thái chọn và không chọn
                                    val itemBgColor = if (isChecked) {
                                        ImperialCrimson
                                    } else {
                                        if (isDark) DarkSurfaceVariant else SilkCreamDark.copy(alpha = 0.55f)
                                    }

                                    val itemBorderColor = if (isChecked) {
                                        ImperialGoldLight
                                    } else {
                                        ImperialGold.copy(alpha = 0.45f)
                                    }

                                    val outfitNameColor = if (isChecked) {
                                        Color.White
                                    } else {
                                        if (isDark) Color.White else DarkLacquer
                                    }

                                    val dynastyColor = if (isChecked) {
                                        ImperialGoldLight
                                    } else {
                                        if (isDark) ImperialGoldLight else ImperialGoldDark
                                    }

                                    val costumeNameColor = if (isChecked) {
                                        Color.White
                                    } else {
                                        if (isDark) ImperialGoldLight else ImperialCrimson
                                    }

                                    val detailColor = if (isChecked) {
                                        Color.White.copy(alpha = 0.9f)
                                    } else {
                                        if (isDark) Color.White.copy(alpha = 0.75f) else Color.DarkGray
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = itemBgColor,
                                        border = BorderStroke(
                                            width = if (isChecked) 1.5.dp else 1.dp,
                                            color = itemBorderColor
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                selectedOutfitIds = if (isChecked) {
                                                    selectedOutfitIds - outfit.id
                                                } else {
                                                    selectedOutfitIds + outfit.id
                                                }
                                            }
                                            .testTag("lookbook_outfit_item_${outfit.id}")
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 12.dp, vertical = 10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(18.dp)
                                                        .clip(CircleShape)
                                                        .background(outfit.selectedColor.color)
                                                        .border(
                                                            1.5.dp,
                                                            if (isChecked) ImperialGoldLight else Color.Black.copy(alpha = 0.3f),
                                                            CircleShape
                                                        )
                                                )
                                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                                    // 1. Tên tác phẩm / bộ phối
                                                    Text(
                                                        text = outfit.name,
                                                        fontSize = 13.5.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = outfitNameColor
                                                    )

                                                    // 2. Triều đại và tên trang phục chính
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                    ) {
                                                        Text(
                                                            text = outfit.costume.dynasty.title,
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.SemiBold,
                                                            color = dynastyColor
                                                        )
                                                        Text(
                                                            text = "•",
                                                            fontSize = 11.sp,
                                                            color = if (isChecked) Color.White.copy(alpha = 0.7f) else Color.Gray
                                                        )
                                                        Text(
                                                            text = outfit.costume.name,
                                                            fontSize = 11.5.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = costumeNameColor
                                                        )
                                                    }

                                                    // 3. Sắc vải và hoa văn
                                                    Text(
                                                        text = "Sắc ${outfit.selectedColor.name} • ${outfit.selectedPattern.name}",
                                                        fontSize = 10.sp,
                                                        color = detailColor
                                                    )
                                                }
                                            }

                                            Checkbox(
                                                checked = isChecked,
                                                onCheckedChange = { checked ->
                                                    selectedOutfitIds = if (checked) {
                                                        selectedOutfitIds + outfit.id
                                                    } else {
                                                        selectedOutfitIds - outfit.id
                                                    }
                                                },
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = ImperialGoldLight,
                                                    checkmarkColor = ImperialCrimsonDark,
                                                    uncheckedColor = if (isChecked) Color.White else (if (isDark) ImperialGoldLight else ImperialGoldDark)
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Hủy", fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            val finalTitle = title.ifBlank { "Lookbook $selectedTheme" }
                            onCreate(
                                finalTitle,
                                selectedTheme,
                                description,
                                selectedCoverStyle,
                                selectedOutfitIds.toList()
                            )
                            onDismiss()
                        },
                        enabled = selectedOutfitIds.isNotEmpty(),
                        modifier = Modifier
                            .weight(1.5f)
                            .testTag("btn_submit_create_lookbook"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ImperialCrimson,
                            contentColor = Color.White
                        ),
                        border = BorderStroke(1.dp, ImperialGoldLight),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CollectionsBookmark,
                            contentDescription = null,
                            tint = ImperialGoldLight,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Tạo Lookbook", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
