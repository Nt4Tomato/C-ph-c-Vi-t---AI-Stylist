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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.example.cophuc.data.model.SavedOutfit
import com.example.ui.theme.*

/**
 * Thẻ Ảnh Di Sản Cổ Phục Việt Nam:
 * Hiển thị 2 hình ảnh trước và sau của trang phục trong phòng thử,
 * kèm thông tin văn hóa, sắc vải ngũ hành, hoa văn và đánh giá quy chuẩn lịch sử.
 */
@Composable
fun CostumeExportCardDialog(
    outfit: SavedOutfit,
    onDismiss: () -> Unit,
    onLoadInFittingRoom: (SavedOutfit) -> Unit
) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .testTag("costume_export_card_dialog"),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            border = BorderStroke(2.dp, ImperialGold)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Top Action Bar
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
                                    imageVector = Icons.Default.PhotoLibrary,
                                    contentDescription = null,
                                    tint = ImperialGoldLight,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "THẺ ẢNH DI SẢN CỔ PHỤC",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ImperialCrimson
                            )
                            Text(
                                text = "Lưu giữ góc nhìn Trước & Sau từ phòng thử",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("btn_close_export_card")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Đóng",
                            tint = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Export Card Container
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(6.dp, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = SilkCream),
                        border = BorderStroke(1.5.dp, ImperialGold.copy(alpha = 0.6f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Royal Header Banner
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = ImperialCrimsonDark,
                                border = BorderStroke(1.dp, ImperialGoldLight),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp, horizontal = 12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = outfit.name,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${outfit.costume.dynasty.title} • ${outfit.costume.name}",
                                        fontSize = 12.sp,
                                        color = ImperialGoldLight,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // =========================================================================
                            // HAI HÌNH ẢNH TRƯỚC VÀ SAU CỦA TRANG PHỤC TRONG PHÒNG THỬ
                            // =========================================================================
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(260.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // HÌNH ẢNH 1: MẶT TRƯỚC
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(),
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFFF7F3E9),
                                    border = BorderStroke(1.2.dp, ImperialGoldLight.copy(alpha = 0.7f)),
                                    shadowElevation = 3.dp
                                ) {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        // Background radial glow
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    Brush.radialGradient(
                                                        colors = listOf(
                                                            SilkCream,
                                                            SilkCreamDark.copy(alpha = 0.7f),
                                                            Color(0xFFE4DAC7)
                                                        )
                                                    )
                                                )
                                        )

                                        // Front Canvas
                                        CostumeAngleCanvas(
                                            costume = outfit.costume,
                                            color = outfit.selectedColor.color,
                                            pattern = outfit.selectedPattern,
                                            accessories = outfit.selectedAccessories,
                                            viewAngle = CostumeView.FRONT,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .testTag("export_canvas_front")
                                        )

                                        // Badge: Mặt Trước
                                        Surface(
                                            shape = RoundedCornerShape(bottomEnd = 10.dp, topStart = 10.dp),
                                            color = ImperialCrimson.copy(alpha = 0.92f),
                                            border = BorderStroke(1.dp, ImperialGoldLight),
                                            modifier = Modifier
                                                .align(Alignment.TopStart)
                                                .padding(6.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Visibility,
                                                    contentDescription = null,
                                                    tint = ImperialGoldLight,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Text(
                                                    text = "Mặt Trước",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            }
                                        }
                                    }
                                }

                                // HÌNH ẢNH 2: MẶT SAU
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(),
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFFF7F3E9),
                                    border = BorderStroke(1.2.dp, ImperialGoldLight.copy(alpha = 0.7f)),
                                    shadowElevation = 3.dp
                                ) {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        // Background radial glow
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    Brush.radialGradient(
                                                        colors = listOf(
                                                            SilkCream,
                                                            SilkCreamDark.copy(alpha = 0.7f),
                                                            Color(0xFFE4DAC7)
                                                        )
                                                    )
                                                )
                                        )

                                        // Back Canvas
                                        CostumeAngleCanvas(
                                            costume = outfit.costume,
                                            color = outfit.selectedColor.color,
                                            pattern = outfit.selectedPattern,
                                            accessories = outfit.selectedAccessories,
                                            viewAngle = CostumeView.BACK,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .testTag("export_canvas_back")
                                        )

                                        // Badge: Mặt Sau
                                        Surface(
                                            shape = RoundedCornerShape(bottomEnd = 10.dp, topStart = 10.dp),
                                            color = ImperialGoldDark.copy(alpha = 0.92f),
                                            border = BorderStroke(1.dp, ImperialGoldLight),
                                            modifier = Modifier
                                                .align(Alignment.TopStart)
                                                .padding(6.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.FlipCameraAndroid,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Text(
                                                    text = "Mặt Sau",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Outfit Heritage Specs
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color.White.copy(alpha = 0.9f),
                                border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Sắc vải & Hoa văn
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
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
                                                text = "Sắc màu: ${outfit.selectedColor.name}",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }

                                        Text(
                                            text = "Hoa văn: ${outfit.selectedPattern.name}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    // Phụ kiện
                                    if (outfit.selectedAccessories.isNotEmpty()) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Diamond,
                                                contentDescription = null,
                                                tint = ImperialGoldDark,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Phụ kiện: ${outfit.selectedAccessories.joinToString(", ") { it.name }}",
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    Divider(color = Color.LightGray.copy(alpha = 0.4f), thickness = 0.8.dp)

                                    // Đánh giá chuẩn mực lịch sử
                                    val statusColor: Color = when (outfit.compatibility.grade) {
                                        CompatibilityGrade.PERFECT_HARMONY -> HeritageHarmonyGreen
                                        CompatibilityGrade.CREATIVE_FUSION -> HeritageWarningAmber
                                        CompatibilityGrade.HISTORICAL_MISMATCH -> ImperialCrimson
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Verified,
                                            contentDescription = null,
                                            tint = statusColor,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "Chuẩn mực: ${outfit.compatibility.title}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = statusColor
                                        )
                                    }

                                    Text(
                                        text = outfit.compatibility.message,
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

                // Action Buttons at bottom of dialog
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Button Share
                    OutlinedButton(
                        onClick = {
                            CostumeCardBitmapGenerator.shareCostumeCard(context, outfit)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_share_export_card"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = SilkCream,
                            contentColor = ImperialCrimson
                        ),
                        border = BorderStroke(1.2.dp, ImperialGold)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = ImperialCrimson,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Chia sẻ ảnh",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ImperialCrimson
                        )
                    }

                    // Button Mở phòng thử
                    Button(
                        onClick = {
                            onLoadInFittingRoom(outfit)
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("btn_open_fitting_room_from_card"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ImperialCrimson,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, ImperialGoldLight)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Checkroom,
                            contentDescription = null,
                            tint = ImperialGoldLight,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Mở phòng thử",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
