package com.example.cophuc.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
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
import com.example.cophuc.data.CostumeDataRepository
import com.example.cophuc.data.model.Dynasty
import com.example.ui.theme.*

@Composable
fun CulturalArchiveScreen(
    onShowDetail: (title: String, description: String, dynasty: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("cultural_archive_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = IndigoSilk),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "ĐIỂN TÍCH VĂN HÓA",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Kho Tàng Lịch Sử Phục Sức Việt",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Nghiên cứu nguồn gốc triều đại, tư tưởng thẩm mỹ phương Đông và ý nghĩa biểu tượng trong trang phục dân tộc.",
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        // Section 1: Các Thời kỳ Lịch Sử Tiêu Biểu
        item {
            Text(
                text = "1. Các thời kỳ lịch sử tiêu biểu",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        items(Dynasty.entries) { dynasty ->
            val description = when (dynasty) {
                Dynasty.LY_TRAN -> "Thời kỳ Phật giáo hưng thịnh, trang phục mang phong khí hào sảng, phóng khoáng với áo Giao Lĩnh, Viên Lĩnh cổ tròn và họa tiết hoa sen, mây lửa."
                Dynasty.LE -> "Quy chế phẩm phục Nho giáo được củng cố nghiêm cẩn. Áo Đối Khâm và Giao Lĩnh phát triển rực rỡ, khởi đầu nét duyên của áo Tứ Thân dân gian."
                Dynasty.NGUYEN -> "Đỉnh cao của sự thống nhất y phục Đại Nam. Định hình Áo Nhật Bình cung đình, Long Bào hoàng đế, Áo Tấc lễ nghi và Áo Ngũ Thân - tiền thân áo dài Việt Nam."
                Dynasty.CONTEMPORARY -> "Giai đoạn chuyển giao thế kỷ XX - XXI. Đánh dấu sự ra đời của Áo Dài tân thời Lemur, Áo Bà Ba Nam Bộ và các sáng tạo cách tân tôn vinh bản sắc dân tộc."
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onShowDetail(dynasty.title, description, dynasty.period) }
                    .testTag("dynasty_card_${dynasty.name}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = dynasty.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = ImperialCrimson
                        )
                        Text(
                            text = dynasty.period,
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = description,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Section 2: Biểu Tượng & Hoa Văn Cổ
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "2. Ý nghĩa hoa văn biểu tượng",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        items(CostumeDataRepository.patterns, key = { it.id }) { pattern ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onShowDetail(pattern.name, pattern.description, pattern.dynasty.title) }
                    .testTag("archive_pattern_${pattern.id}"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(ImperialGold.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = ImperialGoldDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = pattern.name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = pattern.dynasty.title,
                                fontSize = 11.sp,
                                color = ImperialCrimson
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = pattern.symbolism,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Section 3: Quy tắc phối đồ cổ phục
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "3. Quy tắc cốt lõi khi phối cổ phục",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SilkCreamDark.copy(alpha = 0.7f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "• Tính đồng đại (Chronological Harmony): Phụ kiện đội đầu và hài dép nên đi cùng niên đại với dáng áo (ví dụ: Áo cung đình thời Nguyễn đi kèm Khăn vành dây; Áo Tứ thân Bắc Bộ đi cùng Nón quai thao / Khăn mỏ quạ).",
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "• Ngũ Hành sắc phục (Five Elements Color Theory): Sắc màu cung đình xưa được quy định nghiêm ngặt: Vàng chính hoàng dành riêng cho bậc Đế vương; Đỏ son cho hoàng thân lễ nhạc; Xanh ngọc và tím cho bậc quý nữ khuê các.",
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "• Phom dáng trang nghiêm: Dáng áo cổ phục đề cao sự kín đáo, buông rủ thanh thoát, tôn vinh khí chất khoan thai, nhu mì của người phụ nữ Việt Nam.",
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
