package com.example.cophuc.ui.components

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.cophuc.data.model.CompatibilityGrade
import com.example.cophuc.data.model.Lookbook
import com.example.cophuc.data.model.SavedOutfit
import com.example.ui.theme.*

/**
 * Hộp thoại trình chiếu Tập San Lookbook Di Sản Cổ Phục tương tác dạng lật trang tạp chí.
 * Hiển thị cả Mặt Trước và Mặt Sau của từng bộ phối cùng lời bình nghệ thuật.
 */
@Composable
fun LookbookViewerDialog(
    lookbook: Lookbook,
    savedOutfits: List<SavedOutfit>,
    onDismiss: () -> Unit,
    onLoadInFittingRoom: (SavedOutfit) -> Unit
) {
    val context = LocalContext.current
    val outfitsInLookbook = remember(lookbook, savedOutfits) {
        lookbook.outfitIds.mapNotNull { id -> savedOutfits.find { it.id == id } }
    }

    // Trang 0: Bìa; Trang 1..N: Từng bộ phối
    val totalPages = 1 + outfitsInLookbook.size
    var currentPage by remember { mutableIntStateOf(0) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.95f)
                .testTag("lookbook_viewer_dialog"),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(2.dp, ImperialGold)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            ) {
                // Top Header bar
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
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AutoStories,
                                    contentDescription = null,
                                    tint = ImperialGoldLight,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = lookbook.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ImperialCrimson,
                                maxLines = 1
                            )
                            Text(
                                text = if (currentPage == 0) "Trang Bìa • ${lookbook.theme}" else "Trang $currentPage / ${outfitsInLookbook.size} • Tác phẩm di sản",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("btn_close_lookbook_viewer")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Đóng", tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Page Content
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if (currentPage == 0) {
                        // ==========================================
                        // TRANG BÌA TẬP SAN LOOKBOOK (MAGAZINE COVER)
                        // ==========================================
                        val coverBrush = when (lookbook.coverStyle) {
                            "royal_gold" -> Brush.verticalGradient(listOf(Color(0xFF5A4100), Color(0xFF8A6500), Color(0xFFD4A017)))
                            "jade_green" -> Brush.verticalGradient(listOf(Color(0xFF0F3E2C), Color(0xFF1E5D44), Color(0xFF2E8563)))
                            "classic_silk" -> Brush.verticalGradient(listOf(Color(0xFF1A264F), Color(0xFF283B76), Color(0xFF3B5299)))
                            else -> Brush.verticalGradient(listOf(Color(0xFF5E0B12), Color(0xFF8A141D), Color(0xFFB5232F)))
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.fillMaxSize(),
                            border = BorderStroke(2.dp, ImperialGoldLight)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(coverBrush)
                                    .padding(20.dp)
                            ) {
                                // Viền hoàng kim hoa văn
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .border(1.5.dp, ImperialGoldLight.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                                        .padding(16.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .verticalScroll(rememberScrollState()),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "TẬP SAN DI SẢN THỜI TRANG",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ImperialGoldLight,
                                            letterSpacing = 2.sp
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "CỔ PHỤC VIỆT",
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White,
                                            letterSpacing = 3.sp
                                        )

                                        Spacer(modifier = Modifier.height(18.dp))

                                        // Huy hiệu chủ đề
                                        Surface(
                                            shape = RoundedCornerShape(20.dp),
                                            color = Color.Black.copy(alpha = 0.35f),
                                            border = BorderStroke(1.dp, ImperialGoldLight)
                                        ) {
                                            Text(
                                                text = "✦  ${lookbook.theme.uppercase()}  ✦",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = ImperialGoldLight,
                                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(20.dp))

                                        Text(
                                            text = lookbook.title,
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            textAlign = TextAlign.Center,
                                            lineHeight = 28.sp
                                        )

                                        Spacer(modifier = Modifier.height(14.dp))

                                        Text(
                                            text = lookbook.description,
                                            fontSize = 13.sp,
                                            color = Color.White.copy(alpha = 0.9f),
                                            textAlign = TextAlign.Center,
                                            lineHeight = 18.sp
                                        )

                                        Spacer(modifier = Modifier.height(24.dp))

                                        // Thống kê số lượng tác phẩm
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color.White.copy(alpha = 0.15f),
                                            border = BorderStroke(0.8.dp, Color.White.copy(alpha = 0.4f))
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.CollectionsBookmark,
                                                    contentDescription = null,
                                                    tint = ImperialGoldLight,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Text(
                                                    text = "Tuyển chọn ${outfitsInLookbook.size} tác phẩm phối đồ di sản",
                                                    fontSize = 12.sp,
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(24.dp))

                                        Button(
                                            onClick = {
                                                if (outfitsInLookbook.isNotEmpty()) currentPage = 1
                                            },
                                            enabled = outfitsInLookbook.isNotEmpty(),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = ImperialGoldLight,
                                                contentColor = ImperialCrimsonDark
                                            ),
                                            shape = RoundedCornerShape(14.dp),
                                            modifier = Modifier.testTag("btn_start_reading_lookbook")
                                        ) {
                                            Text(
                                                text = "Mở xem tập san  →",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = ImperialCrimsonDark
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        // ==========================================
                        // TỪNG TRANG TÁC PHẨM PHỐI ĐỒ (LOOKBOOK LOOK)
                        // ==========================================
                        val currentOutfit = outfitsInLookbook[currentPage - 1]
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Header tác phẩm trang
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = ImperialCrimsonDark,
                                border = BorderStroke(1.dp, ImperialGoldLight),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = currentOutfit.name,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "${currentOutfit.costume.dynasty.title} • ${currentOutfit.costume.name}",
                                            fontSize = 11.sp,
                                            color = ImperialGoldLight
                                        )
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = ImperialGoldLight.copy(alpha = 0.25f)
                                    ) {
                                        Text(
                                            text = "LOOK #$currentPage",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ImperialGoldLight,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            // =========================================================================
                            // HAI HÌNH ẢNH TRƯỚC VÀ SAU CỦA TRANG PHỤC TỪ PHÒNG THỬ
                            // =========================================================================
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(230.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Khung Mặt Trước
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(),
                                    shape = RoundedCornerShape(14.dp),
                                    color = SilkCream,
                                    border = BorderStroke(1.2.dp, ImperialGold.copy(alpha = 0.7f))
                                ) {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        CostumeAngleCanvas(
                                            costume = currentOutfit.costume,
                                            color = currentOutfit.selectedColor.color,
                                            pattern = currentOutfit.selectedPattern,
                                            accessories = currentOutfit.selectedAccessories,
                                            viewAngle = CostumeView.FRONT,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(bottomEnd = 8.dp, topStart = 8.dp),
                                            color = ImperialCrimson,
                                            modifier = Modifier
                                                .align(Alignment.TopStart)
                                                .padding(4.dp)
                                        ) {
                                            Text(
                                                text = "Mặt Trước",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }

                                // Khung Mặt Sau
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(),
                                    shape = RoundedCornerShape(14.dp),
                                    color = SilkCream,
                                    border = BorderStroke(1.2.dp, ImperialGold.copy(alpha = 0.7f))
                                ) {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        CostumeAngleCanvas(
                                            costume = currentOutfit.costume,
                                            color = currentOutfit.selectedColor.color,
                                            pattern = currentOutfit.selectedPattern,
                                            accessories = currentOutfit.selectedAccessories,
                                            viewAngle = CostumeView.BACK,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(bottomEnd = 8.dp, topStart = 8.dp),
                                            color = ImperialGoldDark,
                                            modifier = Modifier
                                                .align(Alignment.TopStart)
                                                .padding(4.dp)
                                        ) {
                                            Text(
                                                text = "Mặt Sau",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            // Thông số di sản & Lời bình
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(12.dp)
                                                    .clip(CircleShape)
                                                    .background(currentOutfit.selectedColor.color)
                                                    .border(1.dp, Color.Black.copy(alpha = 0.2f), CircleShape)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Sắc: ${currentOutfit.selectedColor.name}",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                        Text(
                                            text = "Hoa văn: ${currentOutfit.selectedPattern.name}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }

                                    if (currentOutfit.selectedAccessories.isNotEmpty()) {
                                        Text(
                                            text = "Phụ kiện: ${currentOutfit.selectedAccessories.joinToString { it.name }}",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    // Chuẩn mực lịch sử
                                    val statusColor = when (currentOutfit.compatibility.grade) {
                                        CompatibilityGrade.PERFECT_HARMONY -> HeritageHarmonyGreen
                                        CompatibilityGrade.CREATIVE_FUSION -> HeritageWarningAmber
                                        CompatibilityGrade.HISTORICAL_MISMATCH -> ImperialCrimson
                                    }
                                    Text(
                                        text = "★ Đánh giá: ${currentOutfit.compatibility.title}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = statusColor
                                    )
                                    Text(
                                        text = currentOutfit.compatibility.message,
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp,
                                        color = Color.DarkGray
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Navigation & Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Nút Trang trước
                    OutlinedButton(
                        onClick = { if (currentPage > 0) currentPage-- },
                        enabled = currentPage > 0,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("btn_lookbook_prev_page")
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Trước", fontSize = 12.sp)
                    }

                    // Indicator
                    Text(
                        text = if (currentPage == 0) "Bìa" else "$currentPage / ${outfitsInLookbook.size}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Nút Trang sau hoặc Chia sẻ
                    if (currentPage < outfitsInLookbook.size) {
                        Button(
                            onClick = { currentPage++ },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ImperialCrimson),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("btn_lookbook_next_page")
                        ) {
                            Text("Sau", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    } else {
                        // Trang cuối: Chia sẻ Lookbook hoặc thử đồ
                        Button(
                            onClick = {
                                if (currentPage > 0) {
                                    val currentOutfit = outfitsInLookbook[currentPage - 1]
                                    CostumeCardBitmapGenerator.shareCostumeCard(context, currentOutfit)
                                } else {
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "🏮 TẬP SAN LOOKBOOK CỔ PHỤC VIỆT NAM: ${lookbook.title}\n" +
                                                    "• Chủ đề: ${lookbook.theme}\n" +
                                                    "• ${lookbook.description}\n" +
                                                    "• Tuyển tập ${outfitsInLookbook.size} tác phẩm di sản đặc sắc.\n" +
                                                    "#CoPhucViet #LookbookCoPhong"
                                        )
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Chia sẻ Lookbook"))
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ImperialCrimson),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("btn_share_lookbook_page")
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Chia sẻ ảnh", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
