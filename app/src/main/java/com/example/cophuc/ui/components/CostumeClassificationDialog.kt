package com.example.cophuc.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cophuc.data.model.CostumeItem
import com.example.ui.theme.*

@Composable
fun CostumeClassificationDialog(
    costume: CostumeItem,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("costume_classification_dialog"),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Category,
                    contentDescription = null,
                    tint = ImperialGoldDark
                )
                Text(
                    text = "Bảng Phân Loại Cổ Phục",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = ImperialCrimsonDark
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Main Header Banner
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = ImperialCrimson,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Checkroom,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = costume.name,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "${costume.dynasty.title} • ${costume.dynasty.period}",
                                color = ImperialGoldLight,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                ClassificationRowItem(
                    label = "Dáng trang phục (Style)",
                    value = costume.style.title,
                    icon = Icons.Default.Style
                )
                ClassificationRowItem(
                    label = "Thể loại phẩm phục",
                    value = costume.category,
                    icon = Icons.Default.AccountBalance
                )
                ClassificationRowItem(
                    label = "Triều đại & Niên đại",
                    value = "${costume.dynasty.title} (${costume.dynasty.period})",
                    icon = Icons.Default.HistoryEdu
                )
                ClassificationRowItem(
                    label = "Vùng miền văn hóa",
                    value = costume.region.title,
                    icon = Icons.Default.LocationOn
                )
                ClassificationRowItem(
                    label = "Dịp phục sức nghi lễ",
                    value = costume.occasions.joinToString(", ") { it.title },
                    icon = Icons.Default.Celebration
                )
                ClassificationRowItem(
                    label = "Quy chế & Chuẩn mực mặc",
                    value = costume.wearingNorms,
                    icon = Icons.Default.Verified
                )
                ClassificationRowItem(
                    label = "Ý nghĩa văn hóa phục sức",
                    value = costume.culturalDescription,
                    icon = Icons.Default.MenuBook
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = ImperialCrimson),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Đóng", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = SilkCream,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun ClassificationRowItem(
    label: String,
    value: String,
    icon: ImageVector
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE2D9C8)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ImperialGoldDark,
                modifier = Modifier
                    .size(20.dp)
                    .padding(top = 2.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = value,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkLacquer,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
