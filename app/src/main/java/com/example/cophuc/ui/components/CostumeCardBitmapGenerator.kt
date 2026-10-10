package com.example.cophuc.ui.components

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.example.cophuc.data.model.CompatibilityGrade
import com.example.cophuc.data.model.SavedOutfit
import com.example.ui.theme.*
import java.io.File
import java.io.FileOutputStream

object CostumeCardBitmapGenerator {

    /**
     * Kết xuất thẻ ảnh hoàn chỉnh gồm cả MẶT TRƯỚC và MẶT SAU của trang phục từ phòng thử
     * sang định dạng hình ảnh Bitmap độ nét cao (1080x1480 px).
     */
    fun generateCardBitmap(context: Context, outfit: SavedOutfit): Bitmap {
        val width = 1080
        val height = 1480
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val androidCanvas = android.graphics.Canvas(bitmap)
        val composeCanvas = Canvas(androidCanvas)
        val drawScope = CanvasDrawScope()

        drawScope.draw(
            density = Density(context),
            layoutDirection = LayoutDirection.Ltr,
            canvas = composeCanvas,
            size = Size(width.toFloat(), height.toFloat())
        ) {
            // 1. Nền hoàng triều giấy lụa cao cấp
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFAF7F0),
                        Color(0xFFF3ECE0),
                        Color(0xFFEBE1CE)
                    )
                )
            )

            // Viền hoàng kim kép
            drawRect(
                color = ImperialGoldDark,
                topLeft = Offset(24f, 24f),
                size = Size(width - 48f, height - 48f),
                style = Stroke(width = 5f)
            )
            drawRect(
                color = ImperialGold.copy(alpha = 0.5f),
                topLeft = Offset(36f, 36f),
                size = Size(width - 72f, height - 72f),
                style = Stroke(width = 2f)
            )

            // 2. Banner tiêu đề Cổ Phục Hoàng Triều
            drawRoundRect(
                color = ImperialCrimsonDark,
                topLeft = Offset(50f, 50f),
                size = Size(width - 100f, 150f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(24f, 24f)
            )
            drawRoundRect(
                color = ImperialGoldLight,
                topLeft = Offset(52f, 52f),
                size = Size(width - 104f, 146f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(24f, 24f),
                style = Stroke(width = 3f)
            )

            // 3. Khung 1: MẶT TRƯỚC (Bên trái)
            val leftBoxX = 60f
            val boxY = 225f
            val boxWidth = 460f
            val boxHeight = 720f

            drawRoundRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFFDF9),
                        Color(0xFFF6F0E2),
                        Color(0xFFE5DAC4)
                    ),
                    center = Offset(leftBoxX + boxWidth / 2, boxY + boxHeight / 2),
                    radius = boxWidth * 0.9f
                ),
                topLeft = Offset(leftBoxX, boxY),
                size = Size(boxWidth, boxHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(28f, 28f)
            )
            drawRoundRect(
                color = ImperialGold,
                topLeft = Offset(leftBoxX, boxY),
                size = Size(boxWidth, boxHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(28f, 28f),
                style = Stroke(width = 3f)
            )

            // Render 3D Pedestal & Volumetric Costume Mặt Trước
            val frontCx = leftBoxX + boxWidth / 2
            val frontCy = boxY + boxHeight / 2 + 10f
            draw3DPedestal(frontCx, frontCy)
            renderVolumetric3DCostume(
                cx = frontCx,
                cy = frontCy,
                rotationDeg = 0f, // MẶT TRƯỚC (Perspective 0°)
                costume = outfit.costume,
                baseColor = outfit.selectedColor.color,
                pattern = outfit.selectedPattern,
                accessories = outfit.selectedAccessories
            )

            // Huy hiệu MẶT TRƯỚC
            drawRoundRect(
                color = ImperialCrimson,
                topLeft = Offset(leftBoxX + 20f, boxY + 20f),
                size = Size(170f, 48f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f, 14f)
            )
            drawRoundRect(
                color = ImperialGoldLight,
                topLeft = Offset(leftBoxX + 20f, boxY + 20f),
                size = Size(170f, 48f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f, 14f),
                style = Stroke(width = 2f)
            )

            // 4. Khung 2: MẶT SAU (Bên phải)
            val rightBoxX = 560f

            drawRoundRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFFDF9),
                        Color(0xFFF6F0E2),
                        Color(0xFFE5DAC4)
                    ),
                    center = Offset(rightBoxX + boxWidth / 2, boxY + boxHeight / 2),
                    radius = boxWidth * 0.9f
                ),
                topLeft = Offset(rightBoxX, boxY),
                size = Size(boxWidth, boxHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(28f, 28f)
            )
            drawRoundRect(
                color = ImperialGold,
                topLeft = Offset(rightBoxX, boxY),
                size = Size(boxWidth, boxHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(28f, 28f),
                style = Stroke(width = 3f)
            )

            // Render 3D Pedestal & Volumetric Costume Mặt Sau
            val backCx = rightBoxX + boxWidth / 2
            val backCy = boxY + boxHeight / 2 + 10f
            draw3DPedestal(backCx, backCy)
            renderVolumetric3DCostume(
                cx = backCx,
                cy = backCy,
                rotationDeg = 180f, // MẶT SAU (Perspective 180°)
                costume = outfit.costume,
                baseColor = outfit.selectedColor.color,
                pattern = outfit.selectedPattern,
                accessories = outfit.selectedAccessories
            )

            // Huy hiệu MẶT SAU
            drawRoundRect(
                color = ImperialGoldDark,
                topLeft = Offset(rightBoxX + 20f, boxY + 20f),
                size = Size(170f, 48f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f, 14f)
            )
            drawRoundRect(
                color = ImperialGoldLight,
                topLeft = Offset(rightBoxX + 20f, boxY + 20f),
                size = Size(170f, 48f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f, 14f),
                style = Stroke(width = 2f)
            )

            // 5. Khung thông tin di sản chi tiết ở dưới
            val infoY = 970f
            val infoHeight = 440f
            drawRoundRect(
                color = Color.White.copy(alpha = 0.95f),
                topLeft = Offset(50f, infoY),
                size = Size(width - 100f, infoHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(24f, 24f)
            )
            drawRoundRect(
                color = ImperialGold.copy(alpha = 0.6f),
                topLeft = Offset(50f, infoY),
                size = Size(width - 100f, infoHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(24f, 24f),
                style = Stroke(width = 2.5f)
            )

            // Dấu ấn triều đình son đỏ
            drawCircle(
                color = ImperialCrimson.copy(alpha = 0.12f),
                center = Offset(width - 140f, height - 160f),
                radius = 65f
            )
            drawCircle(
                color = ImperialCrimson.copy(alpha = 0.45f),
                center = Offset(width - 140f, height - 160f),
                radius = 65f,
                style = Stroke(width = 3f)
            )
        }

        // Vẽ chữ sắc nét bằng Android Paint
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isSubpixelText = true
        }

        // Chữ trong huy hiệu MẶT TRƯỚC
        paint.color = android.graphics.Color.WHITE
        paint.textSize = 28f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textAlign = Paint.Align.CENTER
        androidCanvas.drawText("MẶT TRƯỚC", 60f + 20f + 85f, 225f + 20f + 33f, paint)

        // Chữ trong huy hiệu MẶT SAU
        androidCanvas.drawText("MẶT SAU", 560f + 20f + 85f, 225f + 20f + 33f, paint)

        // Tiêu đề Banner trên
        paint.color = android.graphics.Color.WHITE
        paint.textSize = 44f
        paint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        androidCanvas.drawText(outfit.name, width / 2f, 115f, paint)

        paint.color = android.graphics.Color.parseColor("#F5E6C8") // ImperialGoldLight
        paint.textSize = 28f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        androidCanvas.drawText(
            "${outfit.costume.dynasty.title} • ${outfit.costume.name}",
            width / 2f,
            160f,
            paint
        )

        // Thông tin di sản bên dưới
        paint.textAlign = Paint.Align.LEFT
        val textLeft = 90f
        var currentY = 1030f

        paint.color = android.graphics.Color.parseColor("#8A141D") // ImperialCrimson
        paint.textSize = 34f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        androidCanvas.drawText("THÔNG SỐ PHỐI ĐỒ DI SẢN", textLeft, currentY, paint)

        currentY += 50f
        paint.color = android.graphics.Color.parseColor("#222222")
        paint.textSize = 30f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        androidCanvas.drawText("• Sắc vải ngũ hành: ${outfit.selectedColor.name}", textLeft, currentY, paint)

        currentY += 46f
        androidCanvas.drawText("• Hoa văn truyền thống: ${outfit.selectedPattern.name}", textLeft, currentY, paint)

        currentY += 46f
        val accText = if (outfit.selectedAccessories.isNotEmpty()) {
            outfit.selectedAccessories.joinToString(", ") { it.name }
        } else {
            "Thuần trang phục nguyên bản"
        }
        val safeAccText = if (accText.length > 45) accText.take(43) + "..." else accText
        androidCanvas.drawText("• Phụ kiện: $safeAccText", textLeft, currentY, paint)

        currentY += 56f
        // Đường phân cách
        paint.color = android.graphics.Color.parseColor("#E0D6C2")
        paint.strokeWidth = 2f
        androidCanvas.drawLine(textLeft, currentY, width - 90f, currentY, paint)

        currentY += 46f
        // Đánh giá chuẩn mực
        val gradeColorHex = when (outfit.compatibility.grade) {
            CompatibilityGrade.PERFECT_HARMONY -> "#2E7D32" // HeritageHarmonyGreen
            CompatibilityGrade.CREATIVE_FUSION -> "#D97706"  // HeritageWarningAmber
            CompatibilityGrade.HISTORICAL_MISMATCH -> "#8A141D"
        }
        paint.color = android.graphics.Color.parseColor(gradeColorHex)
        paint.textSize = 30f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        androidCanvas.drawText("★ Đánh giá: ${outfit.compatibility.title}", textLeft, currentY, paint)

        currentY += 44f
        paint.color = android.graphics.Color.parseColor("#555555")
        paint.textSize = 25f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val safeMsg = if (outfit.compatibility.message.length > 55) {
            outfit.compatibility.message.take(53) + "..."
        } else {
            outfit.compatibility.message
        }
        androidCanvas.drawText(safeMsg, textLeft, currentY, paint)

        // Đóng dấu son góc dưới
        paint.color = android.graphics.Color.parseColor("#8A141D")
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 24f
        paint.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        androidCanvas.drawText("CỔ PHỤC", width - 140f, height - 170f, paint)
        androidCanvas.drawText("VIỆT NAM", width - 140f, height - 140f, paint)

        return bitmap
    }

    /**
     * Lưu ảnh thẻ di sản vào thư mục cache và trả về Uri thông qua FileProvider
     */
    fun saveCardBitmapToCache(context: Context, outfit: SavedOutfit): Uri {
        val bitmap = generateCardBitmap(context, outfit)
        val imagesFolder = File(context.cacheDir, "images")
        if (!imagesFolder.exists()) {
            imagesFolder.mkdirs()
        }
        val file = File(imagesFolder, "the_anh_co_phuc_${outfit.id}.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
        }
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    /**
     * Chia sẻ thẻ ảnh (bao gồm cả ảnh Trước và Sau) tới các ứng dụng khác
     */
    fun shareCostumeCard(context: Context, outfit: SavedOutfit) {
        val uri = saveCardBitmapToCache(context, outfit)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(
                Intent.EXTRA_TEXT,
                "🏮 THẺ ẢNH DI SẢN CỔ PHỤC VIỆT NAM\n" +
                        "Tác phẩm: ${outfit.name}\n" +
                        "• Dáng áo: ${outfit.costume.name} (${outfit.costume.dynasty.title})\n" +
                        "• Sắc màu: ${outfit.selectedColor.name}\n" +
                        "• Hoa văn: ${outfit.selectedPattern.name}\n" +
                        "• Phụ kiện: ${outfit.selectedAccessories.joinToString { it.name }}\n" +
                        "• Đánh giá: ${outfit.compatibility.title}\n" +
                        "• Đã đính kèm ảnh kết xuất 2 mặt Trước & Sau trực quan từ phòng thử.\n" +
                        "#CoPhucViet #TheAnhDiSan #VietnameseCostume"
            )
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Chia sẻ thẻ ảnh cổ phục (Mặt Trước & Sau)"))
    }
}
