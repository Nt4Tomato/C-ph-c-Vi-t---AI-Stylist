package com.example.cophuc.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cophuc.data.model.*
import com.example.ui.theme.*
import kotlin.math.*

/**
 * 4 Directional Views for Vietnamese Traditional Costume Showcase:
 * - Trước (Front view - 0°)
 * - Sau (Back view - 180°)
 * - Trái (Left side view - 270°)
 * - Phải (Right side view - 90°)
 */
enum class CostumeView(val label: String, val angle: Float) {
    FRONT("Trước", 0f),
    BACK("Sau", 180f),
    LEFT("Trái", 270f),
    RIGHT("Phải", 90f)
}

/**
 * Volumetric Costume Visualizer for Vietnamese Traditional Garments.
 * - Zoom in / Zoom out support via pinch gestures and on-screen controls (+ / - / 100%).
 * - 4 Directional Perspective Buttons at top-right: Trước, Sau, Trái, Phải.
 * - Full 3D volumetric torso with genuine front-to-back depth (never collapses to 2D when viewed from sides).
 * - All decorative accessories are rigidly anchored directly onto the garment's 3D surface.
 */
@Composable
fun CostumeVisualizer(
    costume: CostumeItem,
    selectedColor: CostumeColor,
    selectedPattern: CostumePattern,
    selectedAccessories: List<AccessoryItem>,
    onItemClick: (title: String, description: String, dynasty: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedRobeColor by animateColorAsState(
        targetValue = selectedColor.color,
        animationSpec = tween(durationMillis = 300),
        label = "robeColor"
    )

    // View Perspective State (Instant switching without rotation animation)
    var currentView by remember { mutableStateOf(CostumeView.FRONT) }

    // Zoom and Pan State
    var zoomScale by remember { mutableFloatStateOf(1.0f) }
    var panOffset by remember { mutableStateOf(Offset.Zero) }

    fun selectView(view: CostumeView) {
        currentView = view
        panOffset = Offset.Zero
    }

    val animatedZoom by animateFloatAsState(
        targetValue = zoomScale,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "costumeZoom"
    )
    val animatedPanX by animateFloatAsState(
        targetValue = panOffset.x,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "costumePanX"
    )
    val animatedPanY by animateFloatAsState(
        targetValue = panOffset.y,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "costumePanY"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(24.dp))
            .testTag("costume_visualizer_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var showClassificationDialog by remember { mutableStateOf(false) }

            // Header Bar: Royal Heritage Showcase Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Costume Name (Pure White Text on rich Imperial Crimson badge)
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = ImperialCrimson,
                    border = androidx.compose.foundation.BorderStroke(1.dp, ImperialGold.copy(alpha = 0.5f)),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Checkroom,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${costume.dynasty.title} • ${costume.name}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Yellow Classification Box: Single line, if not enough space shows "...", clickable to open Classification Table
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = ImperialGold.copy(alpha = 0.22f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ImperialGoldDark),
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { showClassificationDialog = true }
                        .testTag("costume_classification_badge")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${costume.style.title} • ${costume.category}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ImperialGoldDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            softWrap = false,
                            modifier = Modifier.widthIn(max = 135.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Xem bảng phân loại",
                            tint = ImperialGoldDark,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }

            if (showClassificationDialog) {
                CostumeClassificationDialog(
                    costume = costume,
                    onDismiss = { showClassificationDialog = false }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Canvas Stage
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                SilkCream,
                                SilkCreamDark.copy(alpha = 0.85f),
                                Color(0xFFDDD2BE)
                            ),
                            center = Offset(300f, 250f),
                            radius = 650f
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Interactive Showcase Canvas with Zoom, Pan & Volumetric 3D Rendering
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                val newZoom = (zoomScale * zoom).coerceIn(0.75f, 3.0f)
                                zoomScale = newZoom
                                if (newZoom > 1.0f) {
                                    val maxPanX = 140f * (newZoom - 1f)
                                    val maxPanY = 160f * (newZoom - 1f)
                                    panOffset = Offset(
                                        x = (panOffset.x + pan.x).coerceIn(-maxPanX, maxPanX),
                                        y = (panOffset.y + pan.y).coerceIn(-maxPanY, maxPanY)
                                    )
                                } else {
                                    panOffset = Offset.Zero
                                }
                            }
                        }
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onDoubleTap = {
                                    if (zoomScale > 1.2f) {
                                        zoomScale = 1.0f
                                        panOffset = Offset.Zero
                                    } else {
                                        zoomScale = 1.8f
                                    }
                                }
                            )
                        }
                        .graphicsLayer {
                            scaleX = animatedZoom
                            scaleY = animatedZoom
                            translationX = animatedPanX
                            translationY = animatedPanY
                        }
                        .testTag("costume_3d_canvas")
                ) {
                    val cx = size.width / 2
                    val cy = size.height / 2

                    // 1. Perspective Circular Floor Pedestal
                    draw3DPedestal(cx, cy)

                    // 2. Render Full Volumetric Costume & Synchronously Locked Accessories
                    renderVolumetric3DCostume(
                        cx = cx,
                        cy = cy,
                        rotationDeg = currentView.angle,
                        costume = costume,
                        baseColor = animatedRobeColor,
                        pattern = selectedPattern,
                        accessories = selectedAccessories
                    )
                }

                // Top-Right: 4 Directional Buttons: Trước, Sau, Trái, Phải
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black.copy(alpha = 0.55f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ImperialGold.copy(alpha = 0.45f)),
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(3.dp),
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val views = listOf(
                            CostumeView.FRONT,
                            CostumeView.BACK,
                            CostumeView.LEFT,
                            CostumeView.RIGHT
                        )
                        views.forEach { view ->
                            val isSelected = currentView == view
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { selectView(view) }
                                    .testTag(
                                        when (view) {
                                            CostumeView.FRONT -> "btn_view_truoc"
                                            CostumeView.BACK -> "btn_view_sau"
                                            CostumeView.LEFT -> "btn_view_trai"
                                            CostumeView.RIGHT -> "btn_view_phai"
                                        }
                                    ),
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) ImperialCrimson else Color.Transparent,
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, ImperialGoldLight) else null
                            ) {
                                Text(
                                    text = view.label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else Color.White.copy(alpha = 0.85f),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }

                // Bottom-Left: Zoom In / Zoom Out / Reset Controls (Phóng to, thu nhỏ)
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black.copy(alpha = 0.55f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                zoomScale = (zoomScale - 0.25f).coerceAtLeast(0.75f)
                                if (zoomScale <= 1.0f) panOffset = Offset.Zero
                            },
                            modifier = Modifier
                                .size(28.dp)
                                .testTag("btn_zoom_out")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ZoomOut,
                                contentDescription = "Thu nhỏ",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable {
                                    zoomScale = 1.0f
                                    panOffset = Offset.Zero
                                }
                                .testTag("btn_zoom_reset"),
                            shape = RoundedCornerShape(6.dp),
                            color = if (zoomScale != 1.0f) ImperialGoldDark.copy(alpha = 0.7f) else Color.Transparent
                        ) {
                            Text(
                                text = "${(zoomScale * 100).roundToInt()}%",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                modifier = Modifier
                                    .padding(horizontal = 5.dp, vertical = 3.dp)
                                    .testTag("zoom_scale_indicator")
                            )
                        }

                        IconButton(
                            onClick = {
                                zoomScale = (zoomScale + 0.25f).coerceAtMost(3.0f)
                            },
                            modifier = Modifier
                                .size(28.dp)
                                .testTag("btn_zoom_in")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ZoomIn,
                                contentDescription = "Phóng to",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                // Bottom-Right: Cultural Info Button overlay
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp),
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.55f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ImperialGold.copy(alpha = 0.45f)),
                    shadowElevation = 4.dp
                ) {
                    IconButton(
                        onClick = { onItemClick(costume.name, costume.culturalDescription, costume.dynasty.title) },
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("btn_cultural_info")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Ý nghĩa",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Style & Palette Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(selectedColor.color)
                        .border(1.dp, Color.Black.copy(alpha = 0.2f), CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = selectedColor.name,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(" • ", color = Color.Gray, fontSize = 11.sp)
                Text(
                    text = "Hoa văn: ${selectedPattern.name}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(" • ", color = Color.Gray, fontSize = 11.sp)
                Text(
                    text = "${selectedAccessories.size} phụ kiện",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ImperialCrimson
                )
            }
        }
    }
}

// 3D Pedestal
internal fun DrawScope.draw3DPedestal(cx: Float, cy: Float) {
    val pedestalY = cy + 120f
    val baseWidth = 186f
    val baseHeight = 42f

    // Soft shadow
    drawOval(
        color = Color(0x33000000),
        topLeft = Offset(cx - baseWidth / 2, pedestalY + 8f),
        size = Size(baseWidth, baseHeight)
    )

    // Wood edge tier
    drawOval(
        color = Color(0xFFBFAF92),
        topLeft = Offset(cx - baseWidth / 2, pedestalY + 3f),
        size = Size(baseWidth, baseHeight)
    )

    // Top surface
    drawOval(
        brush = Brush.radialGradient(
            colors = listOf(Color(0xFFF3ECE0), Color(0xFFD6C8AC)),
            center = Offset(cx, pedestalY),
            radius = baseWidth / 2
        ),
        topLeft = Offset(cx - baseWidth / 2, pedestalY),
        size = Size(baseWidth, baseHeight)
    )

    // Gold inlaid concentric rim
    drawOval(
        color = ImperialGoldDark.copy(alpha = 0.45f),
        topLeft = Offset(cx - (baseWidth * 0.78f) / 2, pedestalY + baseHeight * 0.11f),
        size = Size(baseWidth * 0.78f, baseHeight * 0.78f),
        style = Stroke(width = 1.3f)
    )

    // Center imperial seal dot
    drawCircle(
        color = ImperialCrimson,
        radius = 3.6f,
        center = Offset(cx, pedestalY + baseHeight / 2)
    )
}

// ==============================================================================
// 3D FULL VOLUMETRIC COSTUME ENGINE (True 3D Cross-Sections in 360°)
// ==============================================================================

private data class Point3D(val x: Float, val y: Float, val z: Float)

private fun project3D(
    localX: Float,
    localY: Float,
    localZ: Float,
    cosA: Float,
    sinA: Float,
    cx: Float,
    scale: Float
): Point3D {
    val rotX = localX * cosA - localZ * sinA
    val rotZ = localX * sinA + localZ * cosA
    val screenX = cx + rotX * scale
    // 3D downward pitch factor (~4 degrees perspective)
    val screenY = localY + rotZ * 0.065f * scale
    return Point3D(screenX, screenY, rotZ)
}

/**
 * Calculates the horizontal 3D envelope half-width of an elliptical cross-section
 * with semi-axes (rx, rz) when rotated by angle A (cosA, sinA).
 * This ensures the costume retains its full 3D body depth from ANY angle (never flattens to 2D!).
 */
private fun getEnvelopeHalfWidth(rx: Float, rz: Float, cosA: Float, sinA: Float): Float {
    return sqrt((rx * cosA).pow(2) + (rz * sinA).pow(2))
}

internal fun DrawScope.renderVolumetric3DCostume(
    cx: Float,
    cy: Float,
    rotationDeg: Float,
    costume: CostumeItem,
    baseColor: Color,
    pattern: CostumePattern,
    accessories: List<AccessoryItem>
) {
    val rad = Math.toRadians(rotationDeg.toDouble())
    val cosA = cos(rad).toFloat() // +1 (Front) -> 0 (Side) -> -1 (Back)
    val sinA = sin(rad).toFloat() // 0 (Front) -> +1 (Right) -> -1 (Left)

    val scale = min(size.width / 360f, size.height / 320f) * 0.90f

    // Garment vertical levels (Pure garment display without head/neck)
    val collarY = cy - 65f * scale
    val shoulderY = collarY + 12f * scale
    val chestY = shoulderY + 18f * scale
    val waistY = cy + 18f * scale
    val hipY = cy + 55f * scale
    val hemY = cy + 115f * scale

    val accessoryIds = accessories.map { it.id }.toSet()

    // -------------------------------------------------------------------------
    // 3D CROSS-SECTIONAL RADII (Semi-axes: rx = width, rz = depth)
    // -------------------------------------------------------------------------
    val halfWShoulder = getEnvelopeHalfWidth(26f, 12f, cosA, sinA) * scale
    val halfWChest = getEnvelopeHalfWidth(22f, 14f, cosA, sinA) * scale
    val halfWWaist = getEnvelopeHalfWidth(20f, 13f, cosA, sinA) * scale
    val halfWHip = getEnvelopeHalfWidth(28f, 18f, cosA, sinA) * scale
    val halfWHem = getEnvelopeHalfWidth(44f, 24f, cosA, sinA) * scale

    // 3D Anchor Keypoints on the garment surface:
    val collarCenter = project3D(0f, collarY + 2f * scale, 0f, cosA, sinA, cx, scale)
    val headAnchor = project3D(0f, collarY - 14f * scale, 0f, cosA, sinA, cx, scale)

    // Collar surface points (front, back, left, right)
    val collarFront = project3D(0f, collarY + 4f * scale, 10f, cosA, sinA, cx, scale)
    val collarBack = project3D(0f, collarY - 1f * scale, -10f, cosA, sinA, cx, scale)
    val collarLeft = project3D(-14f, collarY + 2f * scale, 0f, cosA, sinA, cx, scale)
    val collarRight = project3D(14f, collarY + 2f * scale, 0f, cosA, sinA, cx, scale)

    // Shoulder points
    val leftShoulder = project3D(-26f, shoulderY, 0f, cosA, sinA, cx, scale)
    val rightShoulder = project3D(26f, shoulderY, 0f, cosA, sinA, cx, scale)

    // Armpit / Underarm points (connecting seamless sleeve drape)
    val leftUnderarm = project3D(-19f, chestY + 10f * scale, 0f, cosA, sinA, cx, scale)
    val rightUnderarm = project3D(19f, chestY + 10f * scale, 0f, cosA, sinA, cx, scale)

    // Waist side points (vị trí xẻ tà bên hông)
    val leftWaistSide = project3D(-20f, waistY, 0f, cosA, sinA, cx, scale)
    val rightWaistSide = project3D(20f, waistY, 0f, cosA, sinA, cx, scale)

    // Hem side points
    val leftHemSide = project3D(-44f, hemY, 0f, cosA, sinA, cx, scale)
    val rightHemSide = project3D(44f, hemY, 0f, cosA, sinA, cx, scale)

    // Front flap & back flap center hem
    val frontHemCenter = project3D(0f, hemY + 4f * scale, 24f, cosA, sinA, cx, scale)
    val backHemCenter = project3D(0f, hemY - 2f * scale, -24f, cosA, sinA, cx, scale)

    val isAoTac = costume.id == "ao_tac"
    val isTayChen = costume.id == "ngu_than_tay_chen"
    val isNhatBinh = costume.id == "nhat_binh"
    val isLongBao = costume.id == "long_bao"
    val isVienLinh = costume.id == "vien_linh"
    val isAoBaBa = costume.id == "ao_ba_ba"
    val isCachTan = costume.id == "ao_dai_cach_tan"

    val cuffDist = when {
        isLongBao -> 58f
        isAoTac -> 56f
        isVienLinh -> 54f
        isNhatBinh -> 50f
        isAoBaBa -> 35f
        isTayChen -> 36f
        isCachTan -> 38f
        else -> 46f
    }
    val cuffDrop = when {
        isLongBao -> 28f
        isAoTac -> 26f
        isVienLinh -> 22f
        isNhatBinh -> 16f
        isAoBaBa -> 2f
        isTayChen -> 4f
        isCachTan -> 6f
        else -> 12f
    }
    val cuffW = when {
        isLongBao -> 8.0f
        isAoTac -> 7.5f
        isVienLinh -> 7.0f
        isNhatBinh -> 5.6f
        isAoBaBa -> 2.5f
        isTayChen -> 2.6f
        isCachTan -> 3.0f
        else -> 4.6f
    }

    // Sleeve Cuffs (Tay áo thụng/tay chẽn)
    val leftCuff = project3D(-cuffDist, waistY + cuffDrop * scale, 4f, cosA, sinA, cx, scale)
    val rightCuff = project3D(cuffDist, waistY + cuffDrop * scale, 4f, cosA, sinA, cx, scale)

    // Trigonometric visibility: fade out front/back surface decorations at side views
    // so no flat collar stripes, yem, or emblems float in the air from the side!
    val frontAlpha = (cosA * 1.6f).coerceIn(0f, 1f)
    val backAlpha = (-cosA * 1.6f).coerceIn(0f, 1f)
    val isSideView = abs(sinA) > 0.65f // True when viewed from side (Trái 270° hoặc Phải 90°)

    // Dynamic 3D lighting shader
    val lightAngle = cos(rad - 0.4).toFloat() * 0.18f
    val litColor = baseColor.lighten(0.22f + lightAngle)
    val midColor = baseColor.lighten(lightAngle)
    val shadowColor = baseColor.darken(0.24f - lightAngle)

    val silkShader = Brush.horizontalGradient(
        colors = listOf(shadowColor, midColor, litColor, midColor, shadowColor),
        startX = cx - halfWHem,
        endX = cx + halfWHem
    )

    // =========================================================================
    // PASS 1: REAR COLLAR INTERIOR DEPTH
    // =========================================================================
    // Interior neck cavity of the tailored robe (no wooden neck stick!)
    val innerCavity = Path().apply {
        moveTo(collarLeft.x, collarLeft.y)
        cubicTo(
            collarBack.x, collarBack.y + 1f * scale,
            collarBack.x, collarBack.y + 1f * scale,
            collarRight.x, collarRight.y
        )
        cubicTo(
            collarCenter.x, collarCenter.y + 6f * scale,
            collarCenter.x, collarCenter.y + 6f * scale,
            collarLeft.x, collarLeft.y
        )
        close()
    }
    drawPath(innerCavity, color = baseColor.darken(0.40f))

    // =========================================================================
    // PASS 2: SEAMLESS ROBE & VOLUMETRIC TORSO
    // =========================================================================
    if (!isSideView) {
        // 0. Traditional inner skirt/trousers peeking out beneath the hem
        drawInnerTrouserSkirt(cx, hemY, scale, costume.id)

        // UNIFIED SEAMLESS ROBE & SLEEVES IN ONE SINGLE CONTINUOUS PATH
        // Both arms flow directly into the body without seams or gaps
        val unifiedRobePath = Path().apply {
            // 1. Top left collar opening
            moveTo(collarLeft.x, collarLeft.y)

            // 2. Continuous shoulder flowing directly into outer left sleeve
            cubicTo(
                (collarLeft.x + leftShoulder.x) / 2f, collarLeft.y - 1f * scale,
                leftShoulder.x, leftShoulder.y,
                (leftShoulder.x + leftCuff.x) / 2f, (leftShoulder.y + leftCuff.y) / 2f - 4f * scale
            )
            // Outer left cuff
            cubicTo(
                leftCuff.x - 2f * scale, leftCuff.y - 6f * scale,
                leftCuff.x - cuffW * scale, leftCuff.y - 2f * scale,
                leftCuff.x - cuffW * scale, leftCuff.y
            )
            // Left cuff tubular opening
            cubicTo(
                leftCuff.x - 2f * scale, leftCuff.y + 6f * scale,
                leftCuff.x + 2f * scale, leftCuff.y + 6f * scale,
                leftCuff.x + cuffW * scale, leftCuff.y
            )
            // Flowing underarm wide sleeve drape seamlessly into torso waist
            cubicTo(
                leftCuff.x + 8f * scale, leftCuff.y - 8f * scale,
                (leftCuff.x + leftUnderarm.x) / 2f + 4f * scale, (leftCuff.y + leftUnderarm.y) / 2f + 6f * scale,
                leftUnderarm.x, leftUnderarm.y
            )
            // Torso side down into left hem
            cubicTo(
                cx - halfWWaist, waistY,
                cx - halfWHip, hipY,
                cx - halfWHem, hemY
            )
            // Bottom hem curve connecting across front/back
            cubicTo(
                cx - halfWHem * 0.4f, hemY + 4f * scale,
                cx + halfWHem * 0.4f, hemY + 4f * scale,
                cx + halfWHem, hemY
            )
            // Torso side up from right hem to right underarm
            cubicTo(
                cx + halfWHip, hipY,
                cx + halfWWaist, waistY,
                rightUnderarm.x, rightUnderarm.y
            )
            // Flowing underarm drape out to right cuff
            cubicTo(
                (rightCuff.x + rightUnderarm.x) / 2f - 4f * scale, (rightCuff.y + rightUnderarm.y) / 2f + 6f * scale,
                rightCuff.x - 8f * scale, rightCuff.y - 8f * scale,
                rightCuff.x - cuffW * scale, rightCuff.y
            )
            // Right cuff tubular opening
            cubicTo(
                rightCuff.x - 2f * scale, rightCuff.y + 6f * scale,
                rightCuff.x + 2f * scale, rightCuff.y + 6f * scale,
                rightCuff.x + cuffW * scale, rightCuff.y
            )
            // Upper right sleeve up to right shoulder
            cubicTo(
                rightCuff.x + cuffW * scale, rightCuff.y - 2f * scale,
                rightCuff.x + 2f * scale, rightCuff.y - 6f * scale,
                (rightShoulder.x + rightCuff.x) / 2f, (rightShoulder.y + rightCuff.y) / 2f - 4f * scale
            )
            // Shoulder to right collar
            cubicTo(
                rightShoulder.x, rightShoulder.y,
                (collarRight.x + rightShoulder.x) / 2f, collarRight.y - 1f * scale,
                collarRight.x, collarRight.y
            )
            // Front neckline opening back to collarLeft
            cubicTo(
                collarCenter.x, collarCenter.y + 3f * scale,
                collarCenter.x, collarCenter.y + 3f * scale,
                collarLeft.x, collarLeft.y
            )
            close()
        }
        drawPath(unifiedRobePath, brush = silkShader)
        drawPath(unifiedRobePath, color = Color.Black.copy(alpha = 0.15f), style = Stroke(width = 1.3f * scale))

        // Tubular sleeve cuffs inner shadow rim
        drawOval(
            color = baseColor.darken(0.22f),
            topLeft = Offset(leftCuff.x - cuffW * scale, leftCuff.y - 1f * scale),
            size = Size(cuffW * 2f * scale, 5f * scale),
            style = Stroke(width = 1.2f * scale)
        )
        drawOval(
            color = baseColor.darken(0.22f),
            topLeft = Offset(rightCuff.x - cuffW * scale, rightCuff.y - 1f * scale),
            size = Size(cuffW * 2f * scale, 5f * scale),
            style = Stroke(width = 1.2f * scale)
        )

        // Woven brocade damask texture & cloth drapery folds
        drawSilkDamaskWeave(cx, collarY, hemY, halfWHem, scale, baseColor, cosA, sinA, false)
        drawFabricDrapingFolds(cx, chestY, waistY, hemY, scale, baseColor, cosA, sinA, false)

        // Authentic royal hem & sleeve trims
        if (costume.id == "nhat_binh") {
            drawNhatBinhSleeveBands(leftCuff, scale, 1.0f, false)
            drawNhatBinhSleeveBands(rightCuff, scale, 1.0f, false)
            drawThuyBaTamSon(cx, hemY, halfWHem, scale, 1.0f, sinA)
        } else if (costume.id == "long_bao") {
            drawThuyBaTamSon(cx, hemY, halfWHem, scale, 1.2f, sinA)
        } else if (costume.id == "ao_tac") {
            drawThuyBaTamSon(cx, hemY, halfWHem, scale, 0.75f, sinA)
        }
    } else {
        // SIDE VIEW (Góc nhìn Trái 270° hoặc Phải 90°)
        // 0. Traditional inner skirt/trousers peeking out beneath the hem
        drawInnerTrouserSkirt(cx, hemY, scale, costume.id)

        // 1. Full 3D Volumetric Body Silhouette (Front & Back flap profile)
        val sideBodyPath = Path().apply {
            moveTo(collarBack.x, collarBack.y)
            // Back contour from collar to hem
            cubicTo(
                collarBack.x + (backHemCenter.x - collarBack.x) * 0.3f, chestY,
                collarBack.x + (backHemCenter.x - collarBack.x) * 0.7f, waistY + 10f * scale,
                backHemCenter.x, hemY
            )
            // Bottom hem connecting back to front
            cubicTo(
                (backHemCenter.x + frontHemCenter.x) / 2f, hemY + 4f * scale,
                (backHemCenter.x + frontHemCenter.x) / 2f, hemY + 4f * scale,
                frontHemCenter.x, hemY + 2f * scale
            )
            // Front contour from hem up to collar front
            cubicTo(
                collarFront.x + (frontHemCenter.x - collarFront.x) * 0.7f, waistY + 10f * scale,
                collarFront.x + (frontHemCenter.x - collarFront.x) * 0.3f, chestY,
                collarFront.x, collarFront.y
            )
            // Top collar neckline connecting front to back
            cubicTo(
                (collarFront.x + collarBack.x) / 2f, collarY + 2f * scale,
                (collarFront.x + collarBack.x) / 2f, collarY + 2f * scale,
                collarBack.x, collarBack.y
            )
            close()
        }
        drawPath(sideBodyPath, brush = silkShader)
        drawPath(sideBodyPath, color = Color.Black.copy(alpha = 0.16f), style = Stroke(width = 1.3f * scale))

        // 2. Authentic Side Slit (Xẻ tà bên hông) with inner white silk trousers
        val slitX = cx
        val innerTrouserPath = Path().apply {
            moveTo(slitX - 3f * scale, waistY + 6f * scale)
            lineTo(slitX - 5f * scale, hemY)
            lineTo(slitX + 5f * scale, hemY)
            lineTo(slitX + 3f * scale, waistY + 6f * scale)
            close()
        }
        drawPath(innerTrouserPath, color = Color(0xFFF7F5EE))

        // Crisp side slit crease
        val slitSeam = Path().apply {
            moveTo(slitX, waistY)
            lineTo(slitX, hemY)
        }
        drawPath(slitSeam, color = Color.Black.copy(alpha = 0.25f), style = Stroke(width = 1.8f * scale, cap = StrokeCap.Round))

        // 3. NEAR SLEEVE ONLY (Connected seamlessly to the near shoulder!)
        // Far sleeve is completely occluded and NOT DRAWN (không có phần thừa)!
        val isRightNear = sinA > 0 // sinA > 0: Right side is near; sinA < 0: Left side is near
        val nearShoulder = if (isRightNear) rightShoulder else leftShoulder
        val nearCuff = if (isRightNear) rightCuff else leftCuff

        val nearSleevePath = Path().apply {
            moveTo(nearShoulder.x - 8f * scale, nearShoulder.y + 2f * scale)
            cubicTo(
                nearShoulder.x - 10f * scale, nearShoulder.y + 15f * scale,
                nearCuff.x - 6f * scale, nearCuff.y - 10f * scale,
                nearCuff.x - 5f * scale, nearCuff.y
            )
            cubicTo(
                nearCuff.x, nearCuff.y + 5f * scale,
                nearCuff.x, nearCuff.y + 5f * scale,
                nearCuff.x + 5f * scale, nearCuff.y
            )
            cubicTo(
                nearCuff.x + 6f * scale, nearCuff.y - 10f * scale,
                nearShoulder.x + 10f * scale, nearShoulder.y + 15f * scale,
                nearShoulder.x + 8f * scale, nearShoulder.y + 2f * scale
            )
            close()
        }
        drawPath(nearSleevePath, brush = silkShader)
        drawPath(nearSleevePath, color = Color.Black.copy(alpha = 0.16f), style = Stroke(width = 1.2f * scale))

        // Tubular sleeve cuff rim
        drawOval(
            color = baseColor.darken(0.22f),
            topLeft = Offset(nearCuff.x - 5f * scale, nearCuff.y - 1f * scale),
            size = Size(10f * scale, 5f * scale),
            style = Stroke(width = 1.2f * scale)
        )

        // Side view fabric texture & drapery folds
        drawSilkDamaskWeave(cx, collarY, hemY, halfWHem, scale, baseColor, cosA, sinA, true)
        drawFabricDrapingFolds(cx, chestY, waistY, hemY, scale, baseColor, cosA, sinA, true)

        if (costume.id == "nhat_binh") {
            drawNhatBinhSleeveBands(nearCuff, scale, 1.0f, true)
            drawThuyBaTamSon(cx, hemY, halfWHem, scale, 1.0f, sinA)
        } else if (costume.id == "long_bao") {
            drawThuyBaTamSon(cx, hemY, halfWHem, scale, 1.2f, sinA)
        }
    }

    // =========================================================================
    // PASS 3: FRONT DETAILS & EMBLEMS (Only visible when facing front)
    // =========================================================================
    if (frontAlpha > 0.05f) {
        // Inner white silk collar
        val innerCollar = Path().apply {
            moveTo(collarLeft.x + 2f * scale, collarLeft.y)
            cubicTo(
                collarFront.x, collarFront.y + 2f * scale,
                collarFront.x, collarFront.y + 2f * scale,
                collarRight.x - 2f * scale, collarRight.y
            )
            close()
        }
        drawPath(innerCollar, color = Color(0xFFFAF7F0).copy(alpha = frontAlpha))

        when (costume.id) {
            "nhat_binh" -> {
                drawNhatBinhCollar(cx, collarY, chestY, scale, frontAlpha, sinA)
            }
            "tu_than" -> {
                drawTuThanAuthenticDetails(cx, collarFront, collarY, chestY, waistY, hemY, scale, frontAlpha, cosA, sinA)
            }
            "giao_linh" -> {
                drawGiaoLinhAuthenticDetails(cx, collarLeft, collarRight, chestY, waistY, scale, frontAlpha, baseColor)
            }
            "doi_kham" -> {
                drawDoiKhamAuthenticDetails(cx, collarLeft, collarRight, chestY, waistY, hemY, scale, frontAlpha, baseColor)
            }
            "ao_ba_ba" -> {
                drawAoBaBaAuthenticDetails(cx, collarFront, collarY, chestY, waistY, hemY, scale, frontAlpha, baseColor)
            }
            "ao_dai_cach_tan" -> {
                drawAoDaiCachTanAuthenticDetails(cx, collarFront, collarY, chestY, waistY, hemY, scale, frontAlpha, baseColor)
            }
            "vien_linh" -> {
                drawVienLinhAuthenticDetails(cx, collarFront, collarY, chestY, waistY, scale, frontAlpha, baseColor)
            }
            "long_bao" -> {
                drawLongBaoAuthenticDetails(cx, collarFront, collarY, chestY, waistY, hemY, scale, frontAlpha, baseColor)
            }
            "ao_tac" -> {
                drawNguThanFrontDetails(collarFront, chestY, waistY, hemY, scale, frontAlpha, true)
            }
            "ngu_than_tay_chen" -> {
                drawNguThanFrontDetails(collarFront, chestY, waistY, hemY, scale, frontAlpha, false)
            }
            else -> {
                drawNguThanFrontDetails(collarFront, chestY, waistY, hemY, scale, frontAlpha, false)
            }
        }

        // Front Center Cultural Motif (Curved & compressed with perspective)
        val frontEmblemX = collarFront.x
        val frontEmblemY = chestY + (if (costume.id == "tu_than") 24f else 16f) * scale
        renderCurvedFrontMotif(frontEmblemX, frontEmblemY, scale, pattern, frontAlpha, cosA)
    }

    // =========================================================================
    // PASS 4: BACK DETAILS (Crossfaded with backAlpha)
    // =========================================================================
    if (backAlpha > 0.05f) {
        val spineX = collarBack.x

        val spineSeam = Path().apply {
            moveTo(spineX, collarY + 4f * scale)
            lineTo(spineX, hemY + 3f * scale)
        }
        drawPath(spineSeam, color = Color.Black.copy(alpha = 0.22f * backAlpha), style = Stroke(width = 2f * scale, cap = StrokeCap.Round))
        drawPath(spineSeam, color = ImperialGoldLight.copy(alpha = 0.15f * backAlpha), style = Stroke(width = 0.8f * scale, cap = StrokeCap.Round))

        val backNeckTrim = Path().apply {
            moveTo(collarLeft.x, collarLeft.y)
            cubicTo(
                collarBack.x, collarBack.y - 1f * scale,
                collarBack.x, collarBack.y - 1f * scale,
                collarRight.x, collarRight.y
            )
        }
        drawPath(backNeckTrim, color = ImperialGold.copy(alpha = backAlpha), style = Stroke(width = 2.4f * scale, cap = StrokeCap.Round))

        if (costume.id == "nhat_binh") {
            drawNhatBinhBackYoke(cx, collarY, chestY, scale, backAlpha, sinA)
            drawThuyBaTamSon(cx, hemY, halfWHem, scale, backAlpha, sinA)
        } else if (costume.id == "long_bao") {
            drawLongBaoBackDetails(cx, collarY, chestY, waistY, scale, backAlpha, sinA)
            drawThuyBaTamSon(cx, hemY, halfWHem, scale, backAlpha, sinA)
        } else if (costume.id == "vien_linh") {
            drawVienLinhBackDetails(cx, collarY, chestY, waistY, scale, backAlpha)
        } else if (costume.id == "ao_ba_ba") {
            drawAoBaBaBackDetails(cx, collarY, chestY, waistY, hemY, scale, backAlpha)
        } else if (costume.id == "tu_than") {
            drawTuThanBackSash(cx, waistY, scale, backAlpha)
        }

        val backMedallionY = waistY - 18f * scale
        renderBackRoyalMedallion(spineX, backMedallionY, scale, pattern, backAlpha)
    }

    // =========================================================================
    // PASS 5: ACCESSORIES (Rigidly anchored to 3D garment; occluded parts hidden)
    // =========================================================================

    // 1. Kiềng Vàng Chạm Sen (Clings directly to the collar ring)
    if ("acc_kieng_vang" in accessoryIds) {
        render3DKiengVang(collarCenter, scale, cosA, sinA)
    }

    // 2. Bội Ngọc Phong Thủy (Anchored directly to the left waist side seam)
    // When viewed from Right (sinA > 0.35), left side is occluded behind the torso -> omitted!
    if ("acc_ngoc_boi" in accessoryIds && leftWaistSide.z >= -2f) {
        render3DNgocBoi(leftWaistSide, scale, cosA, sinA)
    }

    // 3. Quạt Trầm Cung Đình (Held at right cuff)
    // When viewed from Left (sinA < -0.35), right cuff is occluded behind the torso -> omitted!
    if ("acc_quat_tram" in accessoryIds && rightCuff.z >= -2f) {
        render3DQuatTram(rightCuff, scale, cosA, sinA)
    }

    // 4. Hài Thêu Mũi Cong (Directly beneath the 3D hem on pedestal)
    if ("acc_hai_theu" in accessoryIds) {
        render3DHaiTheu(cx, hemY, scale, cosA, sinA, frontHemCenter)
    }

    // 5. Headwear Accessories:
    if ("acc_khan_vanh" in accessoryIds) {
        render3DKhanVanh(headAnchor, scale, cosA, sinA)
    } else if ("acc_non_ba_tam" in accessoryIds) {
        render3DNonBaTam(headAnchor, leftShoulder, rightShoulder, scale, cosA, sinA)
    } else if ("acc_khan_mo_qua" in accessoryIds) {
        render3DKhanMoQua(headAnchor, collarCenter, scale, cosA, sinA)
    } else if ("acc_non_la" in accessoryIds) {
        render3DNonLa(headAnchor, collarCenter, scale, cosA, sinA)
    } else if ("acc_khan_ran" in accessoryIds) {
        render3DKhanRan(headAnchor, collarCenter, chestY, scale, cosA, sinA)
    } else if ("acc_tram_cai" in accessoryIds) {
        render3DTramCai(headAnchor, scale, cosA, sinA)
    }

    // 6. Waist Belt Accessories:
    if ("acc_dai_ngoc" in accessoryIds) {
        render3DDaiNgoc(cx, waistY, halfWWaist, scale, cosA, sinA)
    }
}

// ==============================================================================
// DETAILED 3D ACCESSORIES (Rigidly attached to Garment Surface)
// ==============================================================================

/**
 * 1. Kiềng Vàng Chạm Sen:
 * Wraps flush around the collar ring in 3D.
 * When viewed from the side, it slopes down and clings directly to the front chest fabric.
 */
private fun DrawScope.render3DKiengVang(
    collarCenter: Point3D,
    scale: Float,
    cosA: Float,
    sinA: Float
) {
    val rx = 15f * scale
    val rz = 12f * scale
    val centerY = collarCenter.y + 4f * scale
    val centerX = collarCenter.x

    val numPoints = 28
    val frontPoints = mutableListOf<Offset>()

    for (i in 0..numPoints) {
        val angle = (i.toFloat() / numPoints) * 2f * Math.PI
        val localX = (cos(angle) * rx).toFloat()
        // Forward slope: front sits slightly lower than back
        val localYOffset = (sin(angle) * 3f * scale).toFloat()
        val localZ = (sin(angle) * rz).toFloat()

        val rotX = localX * cosA - localZ * sinA
        val rotZ = localX * sinA + localZ * cosA
        val pt = Offset(centerX + rotX, centerY + localYOffset + rotZ * 0.065f)

        if (rotZ >= -2f) {
            frontPoints.add(pt)
        }
    }

    if (frontPoints.size >= 2) {
        val frontPath = Path().apply {
            moveTo(frontPoints.first().x, frontPoints.first().y)
            for (p in frontPoints) {
                lineTo(p.x, p.y)
            }
        }
        drawPath(
            frontPath,
            brush = Brush.linearGradient(
                colors = listOf(ImperialGoldLight, ImperialGold, ImperialGoldDark),
                start = Offset(centerX - rx, centerY),
                end = Offset(centerX + rx, centerY)
            ),
            style = Stroke(width = 3.4f * scale, cap = StrokeCap.Round)
        )

        // Golden lotus pendant at front center
        val frontZ = rz * cosA
        if (frontZ > -1f) {
            val pendantX = centerX + rz * sinA
            val pendantY = centerY + 5f * scale
            drawCircle(color = ImperialGoldLight, radius = 2.6f * scale, center = Offset(pendantX, pendantY))
            drawCircle(color = ImperialCrimson, radius = 1.3f * scale, center = Offset(pendantX, pendantY))
        }
    }

    // Rear clasp when facing back
    if (cosA < 0) {
        val backClasp = Offset(centerX - rz * sinA, centerY - 2f * scale)
        drawCircle(color = ImperialGoldDark, radius = 2f * scale, center = backClasp)
    }
}

/**
 * 2. Bội Ngọc Phong Thủy:
 * Anchored directly to the left waist side seam of the robe; hangs flush against the silk fabric.
 */
private fun DrawScope.render3DNgocBoi(
    waistSide: Point3D,
    scale: Float,
    cosA: Float,
    sinA: Float
) {
    // Only render if not fully occluded by the far side of the body
    if (waistSide.z < -6f) return

    val alpha = if (waistSide.z < 0) 0.5f else 1.0f
    val startX = waistSide.x
    val startY = waistSide.y

    val cordLength = 12f * scale
    val cordEnd = Offset(startX, startY + cordLength)

    // Suspension cord hanging flush against fabric
    drawLine(
        color = ImperialGold.copy(alpha = alpha),
        start = Offset(startX, startY),
        end = cordEnd,
        strokeWidth = 1.4f * scale,
        cap = StrokeCap.Round
    )

    // Jade Bi Disc (đồng điếu ngọc bích)
    val jadeCenter = Offset(cordEnd.x, cordEnd.y + 6f * scale)
    val jadeRadius = 5.2f * scale
    drawCircle(color = JadeGreen.copy(alpha = alpha), radius = jadeRadius, center = jadeCenter)
    drawCircle(color = JadeGreenDark.copy(alpha = alpha), radius = jadeRadius, center = jadeCenter, style = Stroke(width = 1.2f * scale))
    drawCircle(color = SilkCreamDark.copy(alpha = alpha), radius = 2.0f * scale, center = jadeCenter)

    // Red silk tassels
    val tasselPath = Path().apply {
        moveTo(jadeCenter.x, jadeCenter.y + jadeRadius)
        cubicTo(
            jadeCenter.x - 2f * scale, jadeCenter.y + jadeRadius + 8f * scale,
            jadeCenter.x + 2f * scale, jadeCenter.y + jadeRadius + 14f * scale,
            jadeCenter.x, jadeCenter.y + jadeRadius + 22f * scale
        )
    }
    drawPath(tasselPath, color = ImperialCrimson.copy(alpha = alpha), style = Stroke(width = 2.4f * scale, cap = StrokeCap.Round))
}

/**
 * 3. Quạt Trầm Cung Đình:
 * Held directly inside the right sleeve cuff opening.
 */
private fun DrawScope.render3DQuatTram(
    cuff: Point3D,
    scale: Float,
    cosA: Float,
    sinA: Float,
    isRear: Boolean = false
) {
    val gx = cuff.x
    val gy = cuff.y

    val fanLength = 24f * scale
    val baseAngle = -1.25f + sinA * 0.35f
    val tipX = gx + cos(baseAngle) * fanLength * (if (cosA >= 0) 1f else -0.85f)
    val tipY = gy + sin(baseAngle) * fanLength

    val alpha = if (isRear) 0.85f else 1.0f

    val fanLeaf = Path().apply {
        moveTo(gx, gy)
        cubicTo(
            gx + (tipX - gx) * 0.4f - 6f * scale, gy + (tipY - gy) * 0.4f,
            tipX - 10f * scale, tipY,
            tipX, tipY
        )
        cubicTo(
            tipX + 12f * scale, tipY + 4f * scale,
            tipX + 14f * scale, tipY + 12f * scale,
            gx + 8f * scale, gy - 4f * scale
        )
        close()
    }

    drawPath(
        fanLeaf,
        brush = Brush.linearGradient(
            colors = listOf(Color(0xFF8D6E63).copy(alpha = alpha), Color(0xFF5D4037).copy(alpha = alpha)),
            start = Offset(gx, gy),
            end = Offset(tipX, tipY)
        )
    )

    // Ribs & gold trim
    val ribEnd1 = Offset(tipX - 4f * scale, tipY + 2f * scale)
    val ribEnd2 = Offset(tipX + 6f * scale, tipY + 6f * scale)
    drawLine(color = ImperialGold.copy(alpha = alpha), start = Offset(gx, gy), end = ribEnd1, strokeWidth = 1.3f * scale)
    drawLine(color = ImperialGold.copy(alpha = alpha), start = Offset(gx, gy), end = ribEnd2, strokeWidth = 1.3f * scale)
    drawPath(fanLeaf, color = ImperialGold.copy(alpha = 0.7f * alpha), style = Stroke(width = 1.2f * scale))

    // Silk tassel from fan pivot
    val tasselTip = Offset(gx - 2f * scale, gy + 14f * scale)
    drawLine(color = ImperialGold.copy(alpha = alpha), start = Offset(gx, gy), end = tasselTip, strokeWidth = 1.6f * scale, cap = StrokeCap.Round)
    drawCircle(color = ImperialCrimson.copy(alpha = alpha), radius = 2.4f * scale, center = tasselTip)
}

/**
 * 4. Hài Thêu Mũi Cong:
 * Placed directly beneath the 3D hem of the robe, resting on the wooden pedestal.
 * When viewed from the side, the shoes align naturally with the side profile of the hem.
 */
private fun DrawScope.render3DHaiTheu(
    cx: Float,
    hemY: Float,
    scale: Float,
    cosA: Float,
    sinA: Float,
    frontHemCenter: Point3D
) {
    val shoeW = 13f * scale
    val shoeH = 6.5f * scale
    val shoeFacingY = hemY + 3f * scale

    val shoeAlpha = (cosA + 0.4f).coerceIn(0.25f, 1f)

    // Occlusion handling from side: omit far foot when viewed from side
    val drawLeft = sinA <= 0.65f
    val drawRight = sinA >= -0.65f

    val leftFoot = project3D(-12f, hemY, 6f, cosA, sinA, cx, scale)
    val rightFoot = project3D(12f, hemY, 6f, cosA, sinA, cx, scale)

    if (drawLeft) {
        drawRoundRect(
            brush = Brush.verticalGradient(
                colors = listOf(ImperialCrimsonLight.copy(alpha = shoeAlpha), ImperialCrimsonDark.copy(alpha = shoeAlpha))
            ),
            topLeft = Offset(leftFoot.x - shoeW / 2, shoeFacingY),
            size = Size(shoeW, shoeH),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(shoeH / 2, shoeH / 2)
        )
        if (cosA >= -0.2f) {
            drawCircle(
                color = ImperialGoldLight.copy(alpha = shoeAlpha),
                radius = 2.0f * scale,
                center = Offset(leftFoot.x + sinA * 2f * scale, shoeFacingY + 1f * scale)
            )
        }
    }

    if (drawRight) {
        drawRoundRect(
            brush = Brush.verticalGradient(
                colors = listOf(ImperialCrimsonLight.copy(alpha = shoeAlpha), ImperialCrimsonDark.copy(alpha = shoeAlpha))
            ),
            topLeft = Offset(rightFoot.x - shoeW / 2, shoeFacingY),
            size = Size(shoeW, shoeH),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(shoeH / 2, shoeH / 2)
        )
        if (cosA >= -0.2f) {
            drawCircle(
                color = ImperialGoldLight.copy(alpha = shoeAlpha),
                radius = 2.0f * scale,
                center = Offset(rightFoot.x + sinA * 2f * scale, shoeFacingY + 1f * scale)
            )
        }
    }
}

/**
 * 5. Khăn Vành Dây:
 * Royal wrapped turban resting on top of the head/collar.
 */
private fun DrawScope.render3DKhanVanh(
    headAnchor: Point3D,
    scale: Float,
    cosA: Float,
    sinA: Float
) {
    val khanW = 24f * scale
    val khanTopY = headAnchor.y - 18f * scale

    for (i in 0..4) {
        val y = khanTopY + (i * 3.3f * scale)
        val arc = Path().apply {
            moveTo(headAnchor.x - khanW, y)
            cubicTo(
                headAnchor.x - khanW * 0.5f, y - 2.8f * scale + sinA * 1.5f * scale,
                headAnchor.x + khanW * 0.5f, y - 2.8f * scale - sinA * 1.5f * scale,
                headAnchor.x + khanW, y
            )
        }
        val col = if (i % 2 == 0) ImperialGold else ImperialGoldLight
        drawPath(arc, color = col, style = Stroke(width = 2.8f * scale, cap = StrokeCap.Round))
    }

    if (cosA >= -0.1f) {
        val broochX = headAnchor.x + sinA * 8f * scale
        val broochY = headAnchor.y - 12f * scale
        drawCircle(color = ImperialGoldDark, radius = 3.6f * scale, center = Offset(broochX, broochY))
        drawCircle(color = JadeGreen, radius = 2.2f * scale, center = Offset(broochX, broochY))
    }

    if (cosA < 0) {
        val knotX = headAnchor.x - sinA * 6f * scale
        drawCircle(color = ImperialCrimson, radius = 3.2f * scale, center = Offset(knotX, headAnchor.y - 14f * scale))
    }
}

/**
 * 6. Nón Ba Tầm Quai Thao:
 * Circular flat hat with Quai Thao ribbons connecting directly to the robe's shoulders.
 */
private fun DrawScope.render3DNonBaTam(
    headAnchor: Point3D,
    leftShoulder: Point3D,
    rightShoulder: Point3D,
    scale: Float,
    cosA: Float,
    sinA: Float
) {
    val brimW = 84f * scale
    val brimH = 21f * scale
    val brimY = headAnchor.y - 15f * scale + sinA * 2.5f * scale

    // Hat brim disc
    drawOval(color = Color(0xFFE8C88B), topLeft = Offset(headAnchor.x - brimW / 2, brimY), size = Size(brimW, brimH))
    drawOval(color = Color(0xFFC7A567), topLeft = Offset(headAnchor.x - brimW / 2, brimY), size = Size(brimW, brimH), style = Stroke(width = 1.4f * scale))

    // Shallow crown
    drawOval(
        color = Color(0xFFD8B777),
        topLeft = Offset(headAnchor.x - brimW * 0.25f, brimY + brimH * 0.15f),
        size = Size(brimW * 0.5f, brimH * 0.65f)
    )

    // Ribbons connecting directly to the garment shoulders
    val hatLeftEdge = Offset(headAnchor.x - brimW * 0.38f, brimY + brimH / 2f)
    val hatRightEdge = Offset(headAnchor.x + brimW * 0.38f, brimY + brimH / 2f)

    // Only draw near/visible ribbons:
    if (sinA <= 0.65f) {
        val ribbonL = Path().apply {
            moveTo(hatLeftEdge.x, hatLeftEdge.y)
            cubicTo(
                (hatLeftEdge.x + leftShoulder.x) / 2f, hatLeftEdge.y + 12f * scale,
                leftShoulder.x - 2f * scale, leftShoulder.y + 10f * scale,
                leftShoulder.x, leftShoulder.y + 24f * scale
            )
        }
        drawPath(ribbonL, color = Color(0xFF261D1A), style = Stroke(width = 2.2f * scale, cap = StrokeCap.Round))
        drawCircle(color = ImperialGold, radius = 2.4f * scale, center = Offset(leftShoulder.x, leftShoulder.y + 24f * scale))
    }

    if (sinA >= -0.65f) {
        val ribbonR = Path().apply {
            moveTo(hatRightEdge.x, hatRightEdge.y)
            cubicTo(
                (hatRightEdge.x + rightShoulder.x) / 2f, hatRightEdge.y + 12f * scale,
                rightShoulder.x + 2f * scale, rightShoulder.y + 10f * scale,
                rightShoulder.x, rightShoulder.y + 24f * scale
            )
        }
        drawPath(ribbonR, color = Color(0xFF261D1A), style = Stroke(width = 2.2f * scale, cap = StrokeCap.Round))
        drawCircle(color = ImperialGold, radius = 2.4f * scale, center = Offset(rightShoulder.x, rightShoulder.y + 24f * scale))
    }
}

/**
 * 7. Khăn Mỏ Quạ:
 * Crepe scarf hugs head shape; tied at nape with tails falling on the back collar.
 */
private fun DrawScope.render3DKhanMoQua(
    headAnchor: Point3D,
    collarCenter: Point3D,
    scale: Float,
    cosA: Float,
    sinA: Float
) {
    val hx = headAnchor.x
    val hy = headAnchor.y

    val scarfPath = Path().apply {
        moveTo(hx - 15f * scale, hy - 4f * scale)
        cubicTo(
            hx - 12f * scale, hy - 18f * scale,
            hx + 12f * scale, hy - 18f * scale,
            hx + 15f * scale, hy - 4f * scale
        )
        cubicTo(
            hx + 12f * scale, hy + 8f * scale,
            hx - 12f * scale, hy + 8f * scale,
            hx - 15f * scale, hy - 4f * scale
        )
        close()
    }
    drawPath(scarfPath, color = Color(0xFF212121))
    drawPath(scarfPath, color = Color(0xFF424242), style = Stroke(width = 1.2f * scale))

    // Sharp triangular beak fold in front
    if (cosA > -0.1f) {
        val peakX = hx + sinA * 4f * scale
        val peakY = hy - 1f * scale
        val beak = Path().apply {
            moveTo(peakX - 8f * scale, hy - 8f * scale)
            lineTo(peakX, peakY)
            lineTo(peakX + 8f * scale, hy - 8f * scale)
            close()
        }
        drawPath(beak, color = Color(0xFF111111))
        drawPath(beak, color = Color(0xFF555555), style = Stroke(width = 1.0f * scale))
    }

    // Tied knot at nape falling onto collar
    if (cosA < 0.2f) {
        val knotX = hx - sinA * 4f * scale
        val knotY = hy + 8f * scale
        drawCircle(color = Color(0xFF111111), radius = 3f * scale, center = Offset(knotX, knotY))

        val tail1 = Path().apply {
            moveTo(knotX - 1f * scale, knotY)
            cubicTo(knotX - 6f * scale, knotY + 12f * scale, knotX - 2f * scale, knotY + 24f * scale, knotX - 5f * scale, knotY + 32f * scale)
        }
        drawPath(tail1, color = Color(0xFF212121), style = Stroke(width = 2.4f * scale, cap = StrokeCap.Round))

        val tail2 = Path().apply {
            moveTo(knotX + 1f * scale, knotY)
            cubicTo(knotX + 6f * scale, knotY + 10f * scale, knotX + 2f * scale, knotY + 22f * scale, knotX + 4f * scale, knotY + 30f * scale)
        }
        drawPath(tail2, color = Color(0xFF212121), style = Stroke(width = 2.4f * scale, cap = StrokeCap.Round))
    }
}

/**
 * 8. Nón Lá Xứ Huế:
 * 3D conical hat; silk chinstrap hugs chin directly down to collar.
 */
private fun DrawScope.render3DNonLa(
    headAnchor: Point3D,
    collarCenter: Point3D,
    scale: Float,
    cosA: Float,
    sinA: Float
) {
    val hx = headAnchor.x
    val apexY = headAnchor.y - 32f * scale
    val baseY = headAnchor.y + 2f * scale

    val rimW = 74f * scale
    val rimH = 18f * scale

    val conePath = Path().apply {
        moveTo(hx, apexY)
        lineTo(hx - rimW / 2, baseY)
        cubicTo(
            hx, baseY + rimH * 0.7f,
            hx, baseY + rimH * 0.7f,
            hx + rimW / 2, baseY
        )
        lineTo(hx, apexY)
        close()
    }

    drawPath(
        conePath,
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFFF9F3DF), Color(0xFFE4D5B2), Color(0xFFC7B17B)),
            startY = apexY,
            endY = baseY + rimH
        )
    )
    drawPath(conePath, color = Color(0xFFBFAF8A), style = Stroke(width = 1.3f * scale))

    // Bamboo concentric rings
    for (i in 1..4) {
        val y = apexY + (baseY - apexY) * (i / 4.5f)
        val w = (rimW / 2) * (i / 4.5f)
        val arc = Path().apply {
            moveTo(hx - w, y)
            cubicTo(hx, y + (rimH * 0.5f) * (i / 4.5f), hx, y + (rimH * 0.5f) * (i / 4.5f), hx + w, y)
        }
        drawPath(arc, color = Color(0xFFBFAF8A).copy(alpha = 0.6f), style = Stroke(width = 1.1f * scale))
    }

    // Silk chinstrap hugging chin to collar
    val strapLeft = Offset(hx - rimW * 0.35f, baseY + 1f * scale)
    val strapRight = Offset(hx + rimW * 0.35f, baseY + 1f * scale)
    val chinPt = Offset(hx, collarCenter.y + 4f * scale)

    val strapPath = Path().apply {
        moveTo(strapLeft.x, strapLeft.y)
        cubicTo(
            chinPt.x - 6f * scale, chinPt.y + 2f * scale,
            chinPt.x + 6f * scale, chinPt.y + 2f * scale,
            strapRight.x, strapRight.y
        )
    }
    drawPath(strapPath, color = Color(0xFF8E24AA).copy(alpha = 0.85f), style = Stroke(width = 2.4f * scale, cap = StrokeCap.Round))
}

/**
 * Cultural motif on chest (Curved & compressed with 3D perspective)
 */
private fun DrawScope.renderCurvedFrontMotif(
    x: Float,
    y: Float,
    scale: Float,
    pattern: CostumePattern,
    alpha: Float,
    cosA: Float
) {
    if (alpha < 0.05f) return
    val compress = cosA.coerceIn(0.1f, 1f)
    val motifScale = scale * 1.05f

    when (pattern.id) {
        "p_phoenix" -> {
            // Hoàng Phượng Cung Đình (Phượng hoàng vỗ cánh)
            drawCircle(color = ImperialGoldLight.copy(alpha = alpha), radius = 2.4f * motifScale * compress, center = Offset(x, y - 10f * motifScale))
            val crest = Path().apply {
                moveTo(x, y - 12f * motifScale)
                cubicTo(x + 3f * motifScale * compress, y - 16f * motifScale, x + 5f * motifScale * compress, y - 14f * motifScale, x + 3f * motifScale * compress, y - 11f * motifScale)
            }
            drawPath(crest, color = ImperialCrimson.copy(alpha = alpha), style = Stroke(width = 1.3f * motifScale, cap = StrokeCap.Round))

            val body = Path().apply {
                moveTo(x, y - 10f * motifScale)
                cubicTo(x - 3f * motifScale * compress, y - 6f * motifScale, x - 2f * motifScale * compress, y - 2f * motifScale, x, y)
                cubicTo(x + 3f * motifScale * compress, y + 2f * motifScale, x + 1f * motifScale * compress, y + 6f * motifScale, x, y + 8f * motifScale)
            }
            drawPath(body, color = ImperialGold.copy(alpha = alpha), style = Stroke(width = 2.4f * motifScale, cap = StrokeCap.Round))

            val wingL = Path().apply {
                moveTo(x, y - 5f * motifScale)
                cubicTo(x - 9f * motifScale * compress, y - 12f * motifScale, x - 16f * motifScale * compress, y - 8f * motifScale, x - 20f * motifScale * compress, y - 3f * motifScale)
                cubicTo(x - 14f * motifScale * compress, y - 1f * motifScale, x - 8f * motifScale * compress, y + 1f * motifScale, x, y - 1f * motifScale)
            }
            drawPath(wingL, color = ImperialGoldLight.copy(alpha = alpha * 0.9f))
            drawPath(wingL, color = ImperialGoldDark.copy(alpha = alpha), style = Stroke(width = 1.2f * motifScale))

            val wingR = Path().apply {
                moveTo(x, y - 5f * motifScale)
                cubicTo(x + 9f * motifScale * compress, y - 12f * motifScale, x + 16f * motifScale * compress, y - 8f * motifScale, x + 20f * motifScale * compress, y - 3f * motifScale)
                cubicTo(x + 14f * motifScale * compress, y - 1f * motifScale, x + 8f * motifScale * compress, y + 1f * motifScale, x, y - 1f * motifScale)
            }
            drawPath(wingR, color = ImperialGoldLight.copy(alpha = alpha * 0.9f))
            drawPath(wingR, color = ImperialGoldDark.copy(alpha = alpha), style = Stroke(width = 1.2f * motifScale))

            val tailCenter = Path().apply {
                moveTo(x, y + 8f * motifScale)
                cubicTo(x - 4f * motifScale * compress, y + 15f * motifScale, x + 4f * motifScale * compress, y + 22f * motifScale, x, y + 28f * motifScale)
            }
            drawPath(tailCenter, color = ImperialGold.copy(alpha = alpha), style = Stroke(width = 2.0f * motifScale, cap = StrokeCap.Round))
            drawCircle(color = ImperialCrimson.copy(alpha = alpha), radius = 1.8f * motifScale * compress, center = Offset(x, y + 28f * motifScale))

            val tailL = Path().apply {
                moveTo(x - 2f * motifScale * compress, y + 7f * motifScale)
                cubicTo(x - 10f * motifScale * compress, y + 13f * motifScale, x - 14f * motifScale * compress, y + 20f * motifScale, x - 8f * motifScale * compress, y + 25f * motifScale)
            }
            drawPath(tailL, color = ImperialGoldLight.copy(alpha = alpha), style = Stroke(width = 1.5f * motifScale, cap = StrokeCap.Round))
            drawCircle(color = ImperialGoldDark.copy(alpha = alpha), radius = 1.5f * motifScale * compress, center = Offset(x - 8f * motifScale * compress, y + 25f * motifScale))

            val tailR = Path().apply {
                moveTo(x + 2f * motifScale * compress, y + 7f * motifScale)
                cubicTo(x + 10f * motifScale * compress, y + 13f * motifScale, x + 14f * motifScale * compress, y + 20f * motifScale, x + 8f * motifScale * compress, y + 25f * motifScale)
            }
            drawPath(tailR, color = ImperialGoldLight.copy(alpha = alpha), style = Stroke(width = 1.5f * motifScale, cap = StrokeCap.Round))
            drawCircle(color = ImperialGoldDark.copy(alpha = alpha), radius = 1.5f * motifScale * compress, center = Offset(x + 8f * motifScale * compress, y + 25f * motifScale))
        }
        "p_crane" -> {
            drawOval(
                color = ImperialGold.copy(alpha = alpha * 0.45f),
                topLeft = Offset(x - 14f * motifScale * compress, y - 14f * motifScale),
                size = Size(28f * motifScale * compress, 28f * motifScale),
                style = Stroke(width = 1.4f * motifScale)
            )
            drawLine(color = ImperialGoldLight.copy(alpha = alpha * 0.7f), start = Offset(x - 7f * motifScale * compress, y), end = Offset(x + 7f * motifScale * compress, y), strokeWidth = 1.4f * motifScale, cap = StrokeCap.Round)
            drawLine(color = ImperialGoldLight.copy(alpha = alpha * 0.7f), start = Offset(x, y - 7f * motifScale), end = Offset(x, y + 7f * motifScale), strokeWidth = 1.4f * motifScale, cap = StrokeCap.Round)

            val craneBody = Path().apply {
                moveTo(x - 3f * motifScale * compress, y - 1f * motifScale)
                cubicTo(x, y - 4f * motifScale, x + 4f * motifScale * compress, y - 8f * motifScale, x + 7f * motifScale * compress, y - 12f * motifScale)
            }
            drawPath(craneBody, color = Color.White.copy(alpha = alpha), style = Stroke(width = 2.0f * motifScale, cap = StrokeCap.Round))
            drawCircle(color = ImperialCrimson.copy(alpha = alpha), radius = 1.6f * motifScale * compress, center = Offset(x + 8f * motifScale * compress, y - 13f * motifScale))

            val cWing1 = Path().apply {
                moveTo(x, y - 2f * motifScale)
                cubicTo(x - 6f * motifScale * compress, y - 10f * motifScale, x - 14f * motifScale * compress, y - 13f * motifScale, x - 18f * motifScale * compress, y - 9f * motifScale)
                cubicTo(x - 12f * motifScale * compress, y - 5f * motifScale, x - 6f * motifScale * compress, y - 3f * motifScale, x, y)
            }
            drawPath(cWing1, color = Color.White.copy(alpha = alpha * 0.95f))
            drawPath(cWing1, color = ImperialGoldLight.copy(alpha = alpha), style = Stroke(width = 1.1f * motifScale))

            val legs = Path().apply {
                moveTo(x - 2f * motifScale * compress, y + 2f * motifScale)
                lineTo(x - 12f * motifScale * compress, y + 14f * motifScale)
            }
            drawPath(legs, color = ImperialGoldDark.copy(alpha = alpha), style = Stroke(width = 1.4f * motifScale, cap = StrokeCap.Round))
        }
        "p_lotus" -> {
            val ripple = Path().apply {
                moveTo(x - 14f * motifScale * compress, y + 8f * motifScale)
                cubicTo(x - 7f * motifScale * compress, y + 6f * motifScale, x - 3f * motifScale * compress, y + 10f * motifScale, x, y + 8f * motifScale)
                cubicTo(x + 3f * motifScale * compress, y + 6f * motifScale, x + 7f * motifScale * compress, y + 10f * motifScale, x + 14f * motifScale * compress, y + 8f * motifScale)
            }
            drawPath(ripple, color = JadeGreen.copy(alpha = alpha * 0.8f), style = Stroke(width = 1.6f * motifScale, cap = StrokeCap.Round))

            drawOval(
                color = JadeGreenDark.copy(alpha = alpha * 0.8f),
                topLeft = Offset(x - 12f * motifScale * compress, y + 3f * motifScale),
                size = Size(24f * motifScale * compress, 6f * motifScale)
            )

            val pL = Path().apply {
                moveTo(x, y + 5f * motifScale)
                cubicTo(x - 12f * motifScale * compress, y + 3f * motifScale, x - 14f * motifScale * compress, y - 4f * motifScale, x - 7f * motifScale * compress, y - 8f * motifScale)
                cubicTo(x - 3f * motifScale * compress, y - 4f * motifScale, x, y + 1f * motifScale, x, y + 5f * motifScale)
            }
            drawPath(pL, color = LotusPink.copy(alpha = alpha * 0.9f))
            drawPath(pL, color = ImperialGoldLight.copy(alpha = alpha), style = Stroke(width = 1.1f * motifScale))

            val pR = Path().apply {
                moveTo(x, y + 5f * motifScale)
                cubicTo(x + 12f * motifScale * compress, y + 3f * motifScale, x + 14f * motifScale * compress, y - 4f * motifScale, x + 7f * motifScale * compress, y - 8f * motifScale)
                cubicTo(x + 3f * motifScale * compress, y - 4f * motifScale, x, y + 1f * motifScale, x, y + 5f * motifScale)
            }
            drawPath(pR, color = LotusPink.copy(alpha = alpha * 0.9f))
            drawPath(pR, color = ImperialGoldLight.copy(alpha = alpha), style = Stroke(width = 1.1f * motifScale))

            val pCenter = Path().apply {
                moveTo(x - 4f * motifScale * compress, y + 4f * motifScale)
                cubicTo(x - 5f * motifScale * compress, y - 6f * motifScale, x, y - 12f * motifScale, x, y - 12f * motifScale)
                cubicTo(x, y - 12f * motifScale, x + 5f * motifScale * compress, y - 6f * motifScale, x + 4f * motifScale * compress, y + 4f * motifScale)
                close()
            }
            drawPath(pCenter, color = LotusPink.copy(alpha = alpha))
            drawPath(pCenter, color = ImperialGoldLight.copy(alpha = alpha), style = Stroke(width = 1.3f * motifScale))
            drawCircle(color = ImperialGoldLight.copy(alpha = alpha), radius = 2.0f * motifScale * compress, center = Offset(x, y - 1f * motifScale))
        }
        "p_dongson" -> {
            val rOuter = 16f * motifScale * compress
            val rBirds = 12f * motifScale * compress
            val rTooth = 8f * motifScale * compress
            val rSun = 4f * motifScale * compress

            drawOval(color = BronzeDrum.copy(alpha = alpha), topLeft = Offset(x - rOuter, y - 16f * motifScale), size = Size(rOuter * 2f, 32f * motifScale), style = Stroke(width = 2.0f * motifScale))
            drawOval(color = ImperialGold.copy(alpha = alpha * 0.7f), topLeft = Offset(x - rBirds, y - 12f * motifScale), size = Size(rBirds * 2f, 24f * motifScale), style = Stroke(width = 1.2f * motifScale))
            drawOval(color = BronzeDrum.copy(alpha = alpha * 0.8f), topLeft = Offset(x - rTooth, y - 8f * motifScale), size = Size(rTooth * 2f, 16f * motifScale), style = Stroke(width = 1.2f * motifScale))

            for (i in 0 until 8) {
                val ang = (i.toFloat() / 8f) * 2f * Math.PI
                val sx = x + (cos(ang) * rSun).toFloat()
                val sy = y + (sin(ang) * 4f * motifScale).toFloat()
                drawLine(color = ImperialGoldLight.copy(alpha = alpha), start = Offset(x, y), end = Offset(sx, sy), strokeWidth = 1.4f * motifScale, cap = StrokeCap.Round)
            }
            drawCircle(color = ImperialGoldLight.copy(alpha = alpha), radius = 1.6f * motifScale * compress, center = Offset(x, y))

            for (i in 0 until 4) {
                val ang = (i.toFloat() / 4f) * 2f * Math.PI + 0.3
                val bx = x + (cos(ang) * (rBirds + rTooth) / 2f).toFloat()
                val by = y + (sin(ang) * 10f * motifScale).toFloat()
                drawCircle(color = ImperialGoldLight.copy(alpha = alpha * 0.9f), radius = 1.3f * motifScale * compress, center = Offset(bx, by))
            }
        }
        else -> {
            val cloud = Path().apply {
                moveTo(x - 12f * motifScale * compress, y + 2f * motifScale)
                cubicTo(x - 16f * motifScale * compress, y - 4f * motifScale, x - 8f * motifScale * compress, y - 10f * motifScale, x - 3f * motifScale * compress, y - 6f * motifScale)
                cubicTo(x - 2f * motifScale * compress, y - 13f * motifScale, x + 6f * motifScale * compress, y - 13f * motifScale, x + 8f * motifScale * compress, y - 6f * motifScale)
                cubicTo(x + 14f * motifScale * compress, y - 9f * motifScale, x + 18f * motifScale * compress, y - 1f * motifScale, x + 12f * motifScale * compress, y + 4f * motifScale)
                cubicTo(x + 7f * motifScale * compress, y + 8f * motifScale, x - 2f * motifScale * compress, y + 7f * motifScale, x - 12f * motifScale * compress, y + 2f * motifScale)
                close()
            }
            drawPath(cloud, brush = Brush.radialGradient(colors = listOf(ImperialGoldLight.copy(alpha = alpha * 0.9f), ImperialGold.copy(alpha = alpha * 0.6f)), center = Offset(x, y), radius = 16f * motifScale))
            drawPath(cloud, color = ImperialGoldLight.copy(alpha = alpha), style = Stroke(width = 1.6f * motifScale))
            drawCircle(color = ImperialCrimson.copy(alpha = alpha * 0.8f), radius = 2.2f * motifScale * compress, center = Offset(x - 4f * motifScale * compress, y - 3f * motifScale))
            drawCircle(color = ImperialGoldLight.copy(alpha = alpha * 0.8f), radius = 2.2f * motifScale * compress, center = Offset(x + 4f * motifScale * compress, y - 2f * motifScale))
        }
    }
}

/**
 * Royal Back Medallion (Bổ tử tròn / Hồi văn lưng áo triều đình)
 */
private fun DrawScope.renderBackRoyalMedallion(
    cx: Float,
    cy: Float,
    scale: Float,
    pattern: CostumePattern,
    alpha: Float
) {
    if (alpha < 0.05f) return
    val rOuter = 20f * scale
    val rInner = 16f * scale

    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(ImperialGoldLight.copy(alpha = alpha * 0.85f), ImperialGoldDark.copy(alpha = alpha * 0.95f)),
            center = Offset(cx, cy),
            radius = rOuter
        ),
        radius = rOuter,
        center = Offset(cx, cy)
    )
    drawCircle(color = ImperialGoldLight.copy(alpha = alpha), radius = rOuter, center = Offset(cx, cy), style = Stroke(width = 1.5f * scale))
    drawCircle(color = ImperialCrimson.copy(alpha = alpha * 0.8f), radius = rInner, center = Offset(cx, cy), style = Stroke(width = 1.0f * scale))

    when (pattern.id) {
        "p_phoenix" -> {
            drawCircle(color = ImperialCrimsonLight.copy(alpha = alpha), radius = 3f * scale, center = Offset(cx, cy - 4f * scale))
            val tail = Path().apply {
                moveTo(cx, cy - 2f * scale)
                cubicTo(cx - 6f * scale, cy + 4f * scale, cx + 6f * scale, cy + 8f * scale, cx, cy + 12f * scale)
            }
            drawPath(tail, color = ImperialGoldLight.copy(alpha = alpha), style = Stroke(width = 2.0f * scale, cap = StrokeCap.Round))
        }
        "p_crane" -> {
            drawLine(color = ImperialGoldLight.copy(alpha = alpha), start = Offset(cx - 8f * scale, cy), end = Offset(cx + 8f * scale, cy), strokeWidth = 1.6f * scale, cap = StrokeCap.Round)
            drawLine(color = ImperialGoldLight.copy(alpha = alpha), start = Offset(cx, cy - 8f * scale), end = Offset(cx, cy + 8f * scale), strokeWidth = 1.6f * scale, cap = StrokeCap.Round)
            drawCircle(color = ImperialGoldLight.copy(alpha = alpha), radius = 4f * scale, center = Offset(cx, cy), style = Stroke(width = 1.2f * scale))
        }
        "p_lotus" -> {
            drawCircle(color = LotusPink.copy(alpha = alpha), radius = 5f * scale, center = Offset(cx, cy))
            drawCircle(color = ImperialGoldLight.copy(alpha = alpha), radius = 2.5f * scale, center = Offset(cx, cy))
        }
        "p_dongson" -> {
            for (i in 0 until 8) {
                val ang = (i.toFloat() / 8f) * 2f * Math.PI
                val sx = cx + (cos(ang) * 9f * scale).toFloat()
                val sy = cy + (sin(ang) * 9f * scale).toFloat()
                drawLine(color = ImperialGoldLight.copy(alpha = alpha), start = Offset(cx, cy), end = Offset(sx, sy), strokeWidth = 1.2f * scale, cap = StrokeCap.Round)
            }
            drawCircle(color = ImperialGoldLight.copy(alpha = alpha), radius = 3f * scale, center = Offset(cx, cy))
        }
        else -> {
            val c = Path().apply {
                moveTo(cx - 7f * scale, cy)
                cubicTo(cx - 5f * scale, cy - 5f * scale, cx + 5f * scale, cy - 5f * scale, cx + 7f * scale, cy)
                cubicTo(cx + 4f * scale, cy + 5f * scale, cx - 4f * scale, cy + 5f * scale, cx - 7f * scale, cy)
            }
            drawPath(c, color = ImperialGoldLight.copy(alpha = alpha), style = Stroke(width = 1.8f * scale))
        }
    }
}

/**
 * Woven jacquard brocade micro-texture (Hoa văn gấm vóc dệt chìm)
 */
private fun DrawScope.drawSilkDamaskWeave(
    cx: Float,
    collarY: Float,
    hemY: Float,
    halfWHem: Float,
    scale: Float,
    baseColor: Color,
    cosA: Float,
    sinA: Float,
    isSideView: Boolean
) {
    val weaveColor = baseColor.lighten(0.18f).copy(alpha = 0.20f)
    val goldThread = ImperialGoldLight.copy(alpha = 0.12f)
    val stepY = 28f * scale
    val stepX = 22f * scale
    val startY = collarY + 16f * scale
    val endY = hemY - 14f * scale

    var y = startY
    var row = 0
    while (y < endY) {
        val xSpan = if (isSideView) halfWHem * 0.7f else (halfWHem * (0.4f + (y - startY) / (endY - startY) * 0.55f))
        val shiftX = if (row % 2 == 1) stepX / 2f else 0f
        var x = cx - xSpan + shiftX
        while (x <= cx + xSpan) {
            drawCircle(color = weaveColor, radius = 1.4f * scale, center = Offset(x, y))
            drawLine(color = goldThread, start = Offset(x - 2.2f * scale, y), end = Offset(x + 2.2f * scale, y), strokeWidth = 0.8f * scale)
            drawLine(color = goldThread, start = Offset(x, y - 2.2f * scale), end = Offset(x, y + 2.2f * scale), strokeWidth = 0.8f * scale)
            x += stepX
        }
        y += stepY
        row++
    }
}

/**
 * Natural cloth draping creases & highlights (Nếp lụa buông rủ tự nhiên)
 */
private fun DrawScope.drawFabricDrapingFolds(
    cx: Float,
    chestY: Float,
    waistY: Float,
    hemY: Float,
    scale: Float,
    baseColor: Color,
    cosA: Float,
    sinA: Float,
    isSideView: Boolean
) {
    val shadow = Color.Black.copy(alpha = 0.12f)
    val highlight = Color.White.copy(alpha = 0.10f)

    if (!isSideView) {
        val foldOffsets = listOf(-16f, 0f, 16f)
        for (fo in foldOffsets) {
            val fx = cx + fo * scale + sinA * 5f * scale
            val foldPath = Path().apply {
                moveTo(fx * 0.95f, waistY + 4f * scale)
                cubicTo(
                    fx + fo * 0.2f * scale, (waistY + hemY) / 2f,
                    fx + fo * 0.4f * scale, hemY - 12f * scale,
                    fx + fo * 0.6f * scale, hemY - 2f * scale
                )
            }
            drawPath(foldPath, color = shadow, style = Stroke(width = 2.2f * scale, cap = StrokeCap.Round))
            drawPath(foldPath, color = highlight, style = Stroke(width = 1.2f * scale, cap = StrokeCap.Round))
        }

        val underarmL = Path().apply {
            moveTo(cx - 20f * scale, chestY + 12f * scale)
            cubicTo(cx - 16f * scale, waistY - 2f * scale, cx - 12f * scale, waistY + 6f * scale, cx - 10f * scale, waistY + 14f * scale)
        }
        drawPath(underarmL, color = shadow, style = Stroke(width = 1.8f * scale, cap = StrokeCap.Round))

        val underarmR = Path().apply {
            moveTo(cx + 20f * scale, chestY + 12f * scale)
            cubicTo(cx + 16f * scale, waistY - 2f * scale, cx + 12f * scale, waistY + 6f * scale, cx + 10f * scale, waistY + 14f * scale)
        }
        drawPath(underarmR, color = shadow, style = Stroke(width = 1.8f * scale, cap = StrokeCap.Round))
    } else {
        val sideFold = Path().apply {
            moveTo(cx, waistY)
            cubicTo(cx - 4f * scale, waistY + 30f * scale, cx + 4f * scale, hemY - 30f * scale, cx, hemY - 4f * scale)
        }
        drawPath(sideFold, color = shadow, style = Stroke(width = 2.0f * scale, cap = StrokeCap.Round))
        drawPath(sideFold, color = highlight, style = Stroke(width = 1.0f * scale, cap = StrokeCap.Round))
    }
}

/**
 * Thủy Ba Tam Sơn (Sóng nước cung đình & Ba ngọn núi vàng thêu gấu áo)
 */
private fun DrawScope.drawThuyBaTamSon(
    cx: Float,
    hemY: Float,
    halfWHem: Float,
    scale: Float,
    alpha: Float,
    sinA: Float
) {
    if (alpha < 0.05f) return
    val waveH = 15f * scale
    val waveY = hemY - waveH
    val shiftX = sinA * 6f * scale

    drawLine(
        color = ImperialGold.copy(alpha = alpha),
        start = Offset(cx - halfWHem + shiftX, waveY),
        end = Offset(cx + halfWHem + shiftX, waveY),
        strokeWidth = 2.2f * scale,
        cap = StrokeCap.Round
    )

    val waveColors = listOf(Color(0xFFD32F2F), Color(0xFFFBC02D), Color(0xFF1976D2), Color(0xFF388E3C), Color(0xFFFAF7F0))
    val numStripes = 12
    val step = (halfWHem * 1.8f) / numStripes
    for (i in 0 until numStripes) {
        val sx = cx - halfWHem * 0.9f + shiftX + (i * step)
        val col = waveColors[i % waveColors.size].copy(alpha = alpha * 0.85f)
        val waveStripe = Path().apply {
            moveTo(sx, waveY + 2f * scale)
            lineTo(sx + 5f * scale, hemY)
        }
        drawPath(waveStripe, color = col, style = Stroke(width = 2.4f * scale, cap = StrokeCap.Round))
    }

    val mountainPeakY = waveY - 5f * scale
    val mountain = Path().apply {
        moveTo(cx - 14f * scale + shiftX, waveY)
        lineTo(cx - 8f * scale + shiftX, mountainPeakY + 3f * scale)
        lineTo(cx - 4f * scale + shiftX, waveY)
        lineTo(cx + shiftX, mountainPeakY)
        lineTo(cx + 4f * scale + shiftX, waveY)
        lineTo(cx + 8f * scale + shiftX, mountainPeakY + 3f * scale)
        lineTo(cx + 14f * scale + shiftX, waveY)
        close()
    }
    drawPath(mountain, color = ImperialGoldLight.copy(alpha = alpha))
    drawPath(mountain, color = ImperialGoldDark.copy(alpha = alpha), style = Stroke(width = 1.2f * scale))
    drawCircle(color = ImperialCrimson.copy(alpha = alpha), radius = 1.6f * scale, center = Offset(cx + shiftX, mountainPeakY + 3f * scale))
}

/**
 * Dải Ngũ Sắc cửa tay áo & Dải Thùy Lưu Áo Nhật Bình (Đỏ, Vàng, Lam, Lục, Tím)
 */
private fun DrawScope.drawNhatBinhSleeveBands(
    cuffCenter: Point3D,
    scale: Float,
    alpha: Float,
    isSideView: Boolean
) {
    val cuffX = cuffCenter.x
    val cuffY = cuffCenter.y
    val bandColors = listOf(Color(0xFFD32F2F), Color(0xFFFBC02D), Color(0xFF1976D2), Color(0xFF388E3C), Color(0xFF7B1FA2))
    val bandCount = bandColors.size
    val stripeWidth = 1.8f * scale

    for (i in 0 until bandCount) {
        val yOffset = (i - bandCount / 2f) * stripeWidth
        val bandOval = Path().apply {
            addOval(
                androidx.compose.ui.geometry.Rect(
                    center = Offset(cuffX, cuffY + yOffset),
                    radius = 5.5f * scale
                )
            )
        }
        drawPath(bandOval, color = bandColors[i].copy(alpha = alpha), style = Stroke(width = stripeWidth))
    }
    drawCircle(color = ImperialGoldLight.copy(alpha = alpha), radius = 6.2f * scale, center = Offset(cuffX, cuffY), style = Stroke(width = 1.2f * scale))

    // Dải Thùy Lưu: hai dải lụa mềm mại buông rủ từ cửa tay áo theo chuẩn cung đình triều Nguyễn
    val thuyLuuCrimson = Path().apply {
        moveTo(cuffX - 2f * scale, cuffY + 5f * scale)
        cubicTo(
            cuffX - 5f * scale, cuffY + 20f * scale,
            cuffX - 2f * scale, cuffY + 36f * scale,
            cuffX - 4f * scale, cuffY + 48f * scale
        )
    }
    drawPath(thuyLuuCrimson, color = ImperialCrimson.copy(alpha = alpha * 0.9f), style = Stroke(width = 2.0f * scale, cap = StrokeCap.Round))
    drawCircle(color = ImperialGoldLight.copy(alpha = alpha), radius = 1.6f * scale, center = Offset(cuffX - 4f * scale, cuffY + 48f * scale))

    val thuyLuuGold = Path().apply {
        moveTo(cuffX + 2f * scale, cuffY + 5f * scale)
        cubicTo(
            cuffX + 5f * scale, cuffY + 18f * scale,
            cuffX + 3f * scale, cuffY + 34f * scale,
            cuffX + 5f * scale, cuffY + 44f * scale
        )
    }
    drawPath(thuyLuuGold, color = ImperialGold.copy(alpha = alpha * 0.9f), style = Stroke(width = 1.8f * scale, cap = StrokeCap.Round))
    drawCircle(color = JadeGreen.copy(alpha = alpha), radius = 1.5f * scale, center = Offset(cuffX + 5f * scale, cuffY + 44f * scale))
}

/**
 * Lớp lót dưới gấu áo: Thường tơ Bát ti trắng (Nhật Bình), Váy đụp đen (Tứ Thân), Quần thụng lụa trắng (Áo Tấc & Ngũ Thân)
 */
private fun DrawScope.drawInnerTrouserSkirt(
    cx: Float,
    hemY: Float,
    scale: Float,
    costumeId: String
) {
    when (costumeId) {
        "tu_than" -> {
            // Váy đụp đen xòe rộng của phụ nữ Bắc Bộ
            val skirtTop = hemY - 6f * scale
            val skirtBottom = hemY + 16f * scale
            val skirt = Path().apply {
                moveTo(cx - 36f * scale, skirtTop)
                lineTo(cx - 42f * scale, skirtBottom)
                cubicTo(cx, skirtBottom + 3f * scale, cx, skirtBottom + 3f * scale, cx + 42f * scale, skirtBottom)
                lineTo(cx + 36f * scale, skirtTop)
                close()
            }
            drawPath(skirt, color = Color(0xFF1C1A1A))
            drawPath(skirt, color = Color(0xFF333333), style = Stroke(width = 1.0f * scale))
        }
        "nhat_binh" -> {
            // Thường (váy quấn) tơ Bát ti trắng xếp nếp cung đình
            val skirtTop = hemY - 4f * scale
            val skirtBottom = hemY + 15f * scale
            val skirt = Path().apply {
                moveTo(cx - 38f * scale, skirtTop)
                lineTo(cx - 43f * scale, skirtBottom)
                cubicTo(cx, skirtBottom + 3f * scale, cx, skirtBottom + 3f * scale, cx + 43f * scale, skirtBottom)
                lineTo(cx + 38f * scale, skirtTop)
                close()
            }
            drawPath(skirt, color = Color(0xFFF7F5EE))
            // Nếp gấp tơ lụa
            for (i in -4..4) {
                val px = cx + i * 8f * scale
                drawLine(
                    color = Color(0xFFE2DDD2),
                    start = Offset(px * 0.98f, skirtTop + 2f * scale),
                    end = Offset(px, skirtBottom),
                    strokeWidth = 1.2f * scale
                )
            }
            drawPath(skirt, color = Color(0xFFDCD6C8), style = Stroke(width = 1.0f * scale))
        }
        "ao_ba_ba" -> {
            // Quần lụa đen bóng truyền thống Nam Bộ
            val trouserTop = hemY - 6f * scale
            val trouserBottom = hemY + 16f * scale
            val leftLeg = Path().apply {
                moveTo(cx - 34f * scale, trouserTop)
                lineTo(cx - 38f * scale, trouserBottom)
                lineTo(cx - 3f * scale, trouserBottom)
                lineTo(cx - 1f * scale, trouserTop)
                close()
            }
            drawPath(leftLeg, color = Color(0xFF1C1A1A))
            drawPath(leftLeg, color = Color(0xFF383838), style = Stroke(width = 1.0f * scale))

            val rightLeg = Path().apply {
                moveTo(cx + 1f * scale, trouserTop)
                lineTo(cx + 3f * scale, trouserBottom)
                lineTo(cx + 38f * scale, trouserBottom)
                lineTo(cx + 34f * scale, trouserTop)
                close()
            }
            drawPath(rightLeg, color = Color(0xFF1C1A1A))
            drawPath(rightLeg, color = Color(0xFF383838), style = Stroke(width = 1.0f * scale))

            drawLine(
                color = Color.Black.copy(alpha = 0.35f),
                start = Offset(cx, trouserTop),
                end = Offset(cx, trouserBottom - 3f * scale),
                strokeWidth = 1.6f * scale
            )
        }
        "long_bao" -> {
            // Quần thụng lụa hoàng y cung đình thêu chỉ vàng
            val trouserTop = hemY - 4f * scale
            val trouserBottom = hemY + 15f * scale
            val leftLeg = Path().apply {
                moveTo(cx - 34f * scale, trouserTop)
                lineTo(cx - 38f * scale, trouserBottom)
                lineTo(cx - 4f * scale, trouserBottom)
                lineTo(cx - 2f * scale, trouserTop)
                close()
            }
            drawPath(leftLeg, color = Color(0xFFF0C55A))
            drawPath(leftLeg, color = ImperialGoldDark, style = Stroke(width = 0.9f * scale))

            val rightLeg = Path().apply {
                moveTo(cx + 2f * scale, trouserTop)
                lineTo(cx + 4f * scale, trouserBottom)
                lineTo(cx + 38f * scale, trouserBottom)
                lineTo(cx + 34f * scale, trouserTop)
                close()
            }
            drawPath(rightLeg, color = Color(0xFFF0C55A))
            drawPath(rightLeg, color = ImperialGoldDark, style = Stroke(width = 0.9f * scale))

            drawLine(
                color = ImperialGoldDark.copy(alpha = 0.35f),
                start = Offset(cx, trouserTop),
                end = Offset(cx, trouserBottom - 3f * scale),
                strokeWidth = 1.4f * scale
            )
        }
        else -> {
            // Quần thụng lụa trắng ống rộng truyền thống
            val trouserTop = hemY - 4f * scale
            val trouserBottom = hemY + 15f * scale
            val leftLeg = Path().apply {
                moveTo(cx - 34f * scale, trouserTop)
                lineTo(cx - 38f * scale, trouserBottom)
                lineTo(cx - 4f * scale, trouserBottom)
                lineTo(cx - 2f * scale, trouserTop)
                close()
            }
            drawPath(leftLeg, color = Color(0xFFFAF8F2))
            drawPath(leftLeg, color = Color(0xFFE5DFD4), style = Stroke(width = 0.9f * scale))

            val rightLeg = Path().apply {
                moveTo(cx + 2f * scale, trouserTop)
                lineTo(cx + 4f * scale, trouserBottom)
                lineTo(cx + 38f * scale, trouserBottom)
                lineTo(cx + 34f * scale, trouserTop)
                close()
            }
            drawPath(rightLeg, color = Color(0xFFFAF8F2))
            drawPath(rightLeg, color = Color(0xFFE5DFD4), style = Stroke(width = 0.9f * scale))

            drawLine(
                color = Color.Black.copy(alpha = 0.12f),
                start = Offset(cx, trouserTop),
                end = Offset(cx, trouserBottom - 3f * scale),
                strokeWidth = 1.4f * scale
            )
        }
    }
}

/**
 * Cổ Lập Lĩnh, Nẹp Vạt Chéo và 5 Cúc Vàng Ngũ Thường (Áo Tấc & Áo Ngũ Thân)
 */
private fun DrawScope.drawNguThanFrontDetails(
    collarFront: Point3D,
    chestY: Float,
    waistY: Float,
    hemY: Float,
    scale: Float,
    alpha: Float,
    isAoTac: Boolean
) {
    if (alpha < 0.05f) return

    val lapel = Path().apply {
        moveTo(collarFront.x, collarFront.y)
        cubicTo(
            collarFront.x + 8f * scale, chestY - 2f * scale,
            collarFront.x + 12f * scale, chestY + 12f * scale,
            collarFront.x + 14f * scale, chestY + 22f * scale
        )
        cubicTo(
            collarFront.x + 10f * scale, waistY,
            collarFront.x + 6f * scale, (waistY + hemY) / 2f,
            collarFront.x + 2f * scale, hemY
        )
    }
    drawPath(lapel, color = ImperialGold.copy(alpha = alpha), style = Stroke(width = 2.4f * scale, cap = StrokeCap.Round))
    drawPath(lapel, color = ImperialGoldLight.copy(alpha = alpha * 0.7f), style = Stroke(width = 1.0f * scale, cap = StrokeCap.Round))

    // 5 Cúc Vàng Ngũ Thường theo hình chữ Quảng (广)
    val buttonPoints = listOf(
        Offset(collarFront.x, collarFront.y + 2f * scale), // 1. Cổ đứng (Nhân)
        Offset(collarFront.x + 5f * scale, collarFront.y + 11f * scale), // 2. Xương quai xanh (Lễ)
        Offset(collarFront.x + 11f * scale, chestY + 6f * scale), // 3. Giữa ngực phải (Nghĩa)
        Offset(collarFront.x + 15f * scale, chestY + 19f * scale), // 4. Hõm nách phải (Trí)
        Offset(collarFront.x + 13f * scale, waistY - 2f * scale) // 5. Mạn sườn eo (Tín)
    )

    for (pt in buttonPoints) {
        drawLine(color = ImperialGoldDark.copy(alpha = alpha), start = Offset(pt.x - 2.5f * scale, pt.y), end = pt, strokeWidth = 1.2f * scale, cap = StrokeCap.Round)
        drawCircle(color = ImperialGoldDark.copy(alpha = alpha), radius = 2.4f * scale, center = pt)
        drawCircle(color = ImperialGoldLight.copy(alpha = alpha), radius = 1.6f * scale, center = Offset(pt.x - 0.5f * scale, pt.y - 0.5f * scale))
        drawCircle(color = Color.White.copy(alpha = alpha * 0.9f), radius = 0.7f * scale, center = Offset(pt.x - 0.8f * scale, pt.y - 0.8f * scale))
    }

    if (isAoTac) {
        // Viền tà áo rộng đúng một tấc (~4cm)
        val tacHemBand = Path().apply {
            moveTo(collarFront.x - 38f * scale, hemY - 4f * scale)
            lineTo(collarFront.x + 38f * scale, hemY - 4f * scale)
        }
        drawPath(tacHemBand, color = ImperialGoldLight.copy(alpha = alpha * 0.85f), style = Stroke(width = 2.4f * scale, cap = StrokeCap.Round))
    }

    val innerHemTrim = Path().apply {
        moveTo(collarFront.x - 30f * scale, hemY - 2f * scale)
        lineTo(collarFront.x + 30f * scale, hemY - 2f * scale)
    }
    drawPath(innerHemTrim, color = Color(0xFFFAF7F0).copy(alpha = alpha * 0.8f), style = Stroke(width = 2.0f * scale, cap = StrokeCap.Round))
}

/**
 * Cổ Áo Nhật Bình (Chữ nhật, Ngũ Sắc, Kim Khánh vàng cài ngực)
 */
private fun DrawScope.drawNhatBinhCollar(
    cx: Float,
    collarY: Float,
    chestY: Float,
    scale: Float,
    alpha: Float,
    sinA: Float
) {
    if (alpha < 0.05f) return
    val collarWidth = 20f * scale
    val collarShiftX = sinA * 6f * scale

    val collarYoke = Path().apply {
        moveTo(cx - collarWidth + collarShiftX, collarY + 2f * scale)
        cubicTo(
            cx - collarWidth * 0.95f + collarShiftX, chestY + 12f * scale,
            cx - collarWidth * 0.85f + collarShiftX, chestY + 24f * scale,
            cx - collarWidth * 0.75f + collarShiftX, chestY + 32f * scale
        )
        cubicTo(
            cx + collarShiftX, chestY + 34f * scale,
            cx + collarShiftX, chestY + 34f * scale,
            cx + collarWidth * 0.75f + collarShiftX, chestY + 32f * scale
        )
        cubicTo(
            cx + collarWidth * 0.85f + collarShiftX, chestY + 24f * scale,
            cx + collarWidth * 0.95f + collarShiftX, chestY + 12f * scale,
            cx + collarWidth + collarShiftX, collarY + 2f * scale
        )
        close()
    }
    drawPath(collarYoke, color = ImperialGold.copy(alpha = alpha))
    drawPath(collarYoke, color = ImperialGoldLight.copy(alpha = alpha), style = Stroke(width = 1.4f * scale))

    val stripes = listOf(Color(0xFFD32F2F), Color(0xFF1976D2), Color(0xFF388E3C), Color(0xFFFBC02D), Color(0xFF7B1FA2))
    stripes.forEachIndexed { idx, col ->
        val offset = idx * 1.8f * scale
        val sL = Path().apply {
            moveTo(cx - collarWidth + collarShiftX + 2f * scale + offset, collarY + 3f * scale)
            lineTo(cx - collarWidth * 0.72f + collarShiftX + offset, chestY + 30f * scale)
        }
        drawPath(sL, color = col.copy(alpha = alpha), style = Stroke(width = 1.7f * scale, cap = StrokeCap.Round))

        val sR = Path().apply {
            moveTo(cx + collarWidth + collarShiftX - 2f * scale - offset, collarY + 3f * scale)
            lineTo(cx + collarWidth * 0.72f + collarShiftX - offset, chestY + 30f * scale)
        }
        drawPath(sR, color = col.copy(alpha = alpha), style = Stroke(width = 1.7f * scale, cap = StrokeCap.Round))
    }

    val claspCenter = Offset(cx + collarShiftX, chestY + 33f * scale)
    drawRoundRect(
        color = ImperialGoldLight.copy(alpha = alpha),
        topLeft = Offset(claspCenter.x - 7f * scale, claspCenter.y - 3f * scale),
        size = Size(14f * scale, 6f * scale),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(2f * scale, 2f * scale)
    )
    drawCircle(color = JadeGreen.copy(alpha = alpha), radius = 2.0f * scale, center = claspCenter)
}

private fun DrawScope.drawNhatBinhBackYoke(
    cx: Float,
    collarY: Float,
    chestY: Float,
    scale: Float,
    alpha: Float,
    sinA: Float
) {
    if (alpha < 0.05f) return
    val yokeW = 22f * scale
    val shiftX = sinA * 5f * scale
    val backYoke = Path().apply {
        moveTo(cx - yokeW + shiftX, collarY + 3f * scale)
        cubicTo(cx + shiftX, chestY + 6f * scale, cx + shiftX, chestY + 6f * scale, cx + yokeW + shiftX, collarY + 3f * scale)
    }
    drawPath(backYoke, color = ImperialGold.copy(alpha = alpha), style = Stroke(width = 3.6f * scale, cap = StrokeCap.Round))
}

/**
 * Chi tiết Áo Tứ Thân (Yếm đào thêu sen, hai vạt buộc mối, thắt lưng đa sắc)
 */
private fun DrawScope.drawTuThanAuthenticDetails(
    cx: Float,
    collarFront: Point3D,
    collarY: Float,
    chestY: Float,
    waistY: Float,
    hemY: Float,
    scale: Float,
    alpha: Float,
    cosA: Float,
    sinA: Float
) {
    if (alpha < 0.05f) return
    val shiftX = sinA * 4f * scale

    val yem = Path().apply {
        moveTo(cx - 13f * scale + shiftX, collarY + 4f * scale)
        cubicTo(cx - 10f * scale + shiftX, chestY + 12f * scale, cx + 10f * scale + shiftX, chestY + 12f * scale, cx + 13f * scale + shiftX, collarY + 4f * scale)
        cubicTo(cx + 9f * scale + shiftX, chestY + 20f * scale, cx - 9f * scale + shiftX, chestY + 20f * scale, cx - 13f * scale + shiftX, collarY + 4f * scale)
        close()
    }
    drawPath(yem, color = Color(0xFFE91E63).copy(alpha = alpha))
    drawPath(yem, color = ImperialGoldLight.copy(alpha = alpha * 0.7f), style = Stroke(width = 1.2f * scale))
    drawCircle(color = ImperialGoldLight.copy(alpha = alpha), radius = 2.4f * scale, center = Offset(cx + shiftX, chestY + 10f * scale))

    val knotCenter = Offset(cx + shiftX, waistY - 3f * scale)
    drawOval(
        color = JadeGreen.copy(alpha = alpha),
        topLeft = Offset(knotCenter.x - 12f * scale, knotCenter.y - 3.5f * scale),
        size = Size(24f * scale, 7f * scale)
    )

    val ribbonGreen = Path().apply {
        moveTo(knotCenter.x - 4f * scale, knotCenter.y + 3f * scale)
        cubicTo(knotCenter.x - 12f * scale, knotCenter.y + 20f * scale, knotCenter.x - 3f * scale, knotCenter.y + 45f * scale, knotCenter.x - 10f * scale, hemY - 12f * scale)
    }
    drawPath(ribbonGreen, color = JadeGreen.copy(alpha = alpha), style = Stroke(width = 3.6f * scale, cap = StrokeCap.Round))

    val ribbonPink = Path().apply {
        moveTo(knotCenter.x + 4f * scale, knotCenter.y + 3f * scale)
        cubicTo(knotCenter.x + 10f * scale, knotCenter.y + 18f * scale, knotCenter.x + 3f * scale, knotCenter.y + 40f * scale, knotCenter.x + 9f * scale, hemY - 10f * scale)
    }
    drawPath(ribbonPink, color = LotusPink.copy(alpha = alpha), style = Stroke(width = 3.6f * scale, cap = StrokeCap.Round))
    drawCircle(color = ImperialGold.copy(alpha = alpha), radius = 3.2f * scale, center = knotCenter)
}

private fun DrawScope.drawTuThanBackSash(cx: Float, waistY: Float, scale: Float, alpha: Float) {
    if (alpha < 0.05f) return
    drawOval(
        color = JadeGreen.copy(alpha = alpha),
        topLeft = Offset(cx - 14f * scale, waistY - 3f * scale),
        size = Size(28f * scale, 6f * scale)
    )
}

/**
 * Áo Giao Lĩnh (Cổ chéo V sâu, đai lưng to bản thêu hoa văn)
 */
private fun DrawScope.drawGiaoLinhAuthenticDetails(
    cx: Float,
    collarLeft: Point3D,
    collarRight: Point3D,
    chestY: Float,
    waistY: Float,
    scale: Float,
    alpha: Float,
    baseColor: Color
) {
    if (alpha < 0.05f) return

    val crossCollarLeft = Path().apply {
        moveTo(collarLeft.x, collarLeft.y)
        lineTo(cx + 12f * scale, waistY - 6f * scale)
    }
    drawPath(crossCollarLeft, color = ImperialGold.copy(alpha = alpha), style = Stroke(width = 3.4f * scale, cap = StrokeCap.Round))

    val crossCollarRight = Path().apply {
        moveTo(collarRight.x, collarRight.y)
        lineTo(cx - 10f * scale, chestY + 16f * scale)
    }
    drawPath(crossCollarRight, color = ImperialGoldLight.copy(alpha = alpha * 0.9f), style = Stroke(width = 2.8f * scale, cap = StrokeCap.Round))

    val sashY = waistY - 6f * scale
    val sashHeight = 12f * scale
    drawRoundRect(
        brush = Brush.verticalGradient(listOf(ImperialCrimson, ImperialCrimsonDark)),
        topLeft = Offset(cx - 18f * scale, sashY),
        size = Size(36f * scale, sashHeight),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(2f * scale, 2f * scale)
    )
    drawRoundRect(
        color = ImperialGoldLight.copy(alpha = alpha),
        topLeft = Offset(cx - 18f * scale, sashY),
        size = Size(36f * scale, sashHeight),
        style = Stroke(width = 1.3f * scale)
    )
    drawCircle(color = JadeGreen.copy(alpha = alpha), radius = 3.2f * scale, center = Offset(cx, sashY + sashHeight / 2))
    drawCircle(color = ImperialGold.copy(alpha = alpha), radius = 3.2f * scale, center = Offset(cx, sashY + sashHeight / 2), style = Stroke(width = 1.2f * scale))
}

/**
 * Áo Đối Khâm (Hai vạt thẳng song song, viền nẹp thêu hoa văn, dây buộc ngực)
 */
private fun DrawScope.drawDoiKhamAuthenticDetails(
    cx: Float,
    collarLeft: Point3D,
    collarRight: Point3D,
    chestY: Float,
    waistY: Float,
    hemY: Float,
    scale: Float,
    alpha: Float,
    baseColor: Color
) {
    if (alpha < 0.05f) return
    val lapelGap = 7f * scale

    drawRect(
        color = Color(0xFFFAF7F0).copy(alpha = alpha),
        topLeft = Offset(cx - lapelGap, chestY - 4f * scale),
        size = Size(lapelGap * 2f, waistY - chestY + 4f * scale)
    )

    val bandL = Path().apply {
        moveTo(cx - lapelGap, collarLeft.y)
        lineTo(cx - lapelGap, hemY)
    }
    drawPath(bandL, color = ImperialGold.copy(alpha = alpha), style = Stroke(width = 3.2f * scale, cap = StrokeCap.Square))
    drawPath(bandL, color = ImperialCrimson.copy(alpha = alpha * 0.8f), style = Stroke(width = 1.4f * scale, cap = StrokeCap.Square))

    val bandR = Path().apply {
        moveTo(cx + lapelGap, collarRight.y)
        lineTo(cx + lapelGap, hemY)
    }
    drawPath(bandR, color = ImperialGold.copy(alpha = alpha), style = Stroke(width = 3.2f * scale, cap = StrokeCap.Square))
    drawPath(bandR, color = ImperialCrimson.copy(alpha = alpha * 0.8f), style = Stroke(width = 1.4f * scale, cap = StrokeCap.Square))

    val tieY = chestY + 8f * scale
    drawLine(color = ImperialGoldLight.copy(alpha = alpha), start = Offset(cx - lapelGap, tieY), end = Offset(cx + lapelGap, tieY), strokeWidth = 1.8f * scale)
    drawCircle(color = ImperialCrimson.copy(alpha = alpha), radius = 2.4f * scale, center = Offset(cx, tieY))
    drawLine(color = ImperialGold.copy(alpha = alpha), start = Offset(cx, tieY), end = Offset(cx, tieY + 12f * scale), strokeWidth = 1.4f * scale, cap = StrokeCap.Round)
}

/**
 * Chi tiết thân trước Áo Bà Ba Nam Bộ:
 * - Cổ tròn xẻ nhẹ trước ngực viền mép thanh mảnh
 * - Đường nẹp xẻ giữa cài hàng khuy tròn (5 cúc tròn xà cừ/vàng)
 * - Hai túi áo xẻ vuông ở vạt dưới bên trái và bên phải
 */
private fun DrawScope.drawAoBaBaAuthenticDetails(
    cx: Float,
    collarFront: Point3D,
    collarY: Float,
    chestY: Float,
    waistY: Float,
    hemY: Float,
    scale: Float,
    alpha: Float,
    baseColor: Color
) {
    if (alpha < 0.05f) return

    val placketX = collarFront.x

    // 1. Central front button placket
    val placket = Path().apply {
        moveTo(placketX, collarFront.y + 4f * scale)
        lineTo(placketX, hemY)
    }
    drawPath(placket, color = baseColor.darken(0.35f).copy(alpha = alpha), style = Stroke(width = 2.2f * scale, cap = StrokeCap.Round))
    drawPath(placket, color = Color.White.copy(alpha = alpha * 0.4f), style = Stroke(width = 0.8f * scale, cap = StrokeCap.Round))

    // 2. Row of 5 front round pearl buttons
    val buttonCount = 5
    val startY = collarFront.y + 12f * scale
    val endY = waistY + 14f * scale
    val stepY = (endY - startY) / (buttonCount - 1)

    for (i in 0 until buttonCount) {
        val by = startY + i * stepY
        drawCircle(color = Color(0xFFFAF7EE).copy(alpha = alpha), radius = 2.2f * scale, center = Offset(placketX, by))
        drawCircle(color = baseColor.darken(0.4f).copy(alpha = alpha), radius = 2.2f * scale, center = Offset(placketX, by), style = Stroke(width = 0.8f * scale))
        drawCircle(color = ImperialGold.copy(alpha = alpha), radius = 0.8f * scale, center = Offset(placketX, by))
    }

    // 3. Two lower patch pockets (Túi áo bà ba truyền thống)
    val pocketW = 14f * scale
    val pocketH = 16f * scale
    val pocketY = waistY + 12f * scale

    // Left pocket
    val leftPocketX = cx - 22f * scale
    val leftPocket = Path().apply {
        moveTo(leftPocketX - pocketW / 2f, pocketY)
        lineTo(leftPocketX + pocketW / 2f, pocketY)
        lineTo(leftPocketX + pocketW / 2f, pocketY + pocketH - 3f * scale)
        cubicTo(
            leftPocketX + pocketW / 2f, pocketY + pocketH,
            leftPocketX - pocketW / 2f, pocketY + pocketH,
            leftPocketX - pocketW / 2f, pocketY + pocketH - 3f * scale
        )
        close()
    }
    drawPath(leftPocket, color = baseColor.darken(0.12f).copy(alpha = alpha))
    drawPath(leftPocket, color = baseColor.darken(0.35f).copy(alpha = alpha), style = Stroke(width = 1.2f * scale))
    drawLine(
        color = ImperialGoldLight.copy(alpha = alpha * 0.6f),
        start = Offset(leftPocketX - pocketW / 2f, pocketY + 3f * scale),
        end = Offset(leftPocketX + pocketW / 2f, pocketY + 3f * scale),
        strokeWidth = 1.0f * scale
    )

    // Right pocket
    val rightPocketX = cx + 22f * scale
    val rightPocket = Path().apply {
        moveTo(rightPocketX - pocketW / 2f, pocketY)
        lineTo(rightPocketX + pocketW / 2f, pocketY)
        lineTo(rightPocketX + pocketW / 2f, pocketY + pocketH - 3f * scale)
        cubicTo(
            rightPocketX + pocketW / 2f, pocketY + pocketH,
            rightPocketX - pocketW / 2f, pocketY + pocketH,
            rightPocketX - pocketW / 2f, pocketY + pocketH - 3f * scale
        )
        close()
    }
    drawPath(rightPocket, color = baseColor.darken(0.12f).copy(alpha = alpha))
    drawPath(rightPocket, color = baseColor.darken(0.35f).copy(alpha = alpha), style = Stroke(width = 1.2f * scale))
    drawLine(
        color = ImperialGoldLight.copy(alpha = alpha * 0.6f),
        start = Offset(rightPocketX - pocketW / 2f, pocketY + 3f * scale),
        end = Offset(rightPocketX + pocketW / 2f, pocketY + 3f * scale),
        strokeWidth = 1.0f * scale
    )
}

private fun DrawScope.drawAoBaBaBackDetails(
    cx: Float,
    collarY: Float,
    chestY: Float,
    waistY: Float,
    hemY: Float,
    scale: Float,
    alpha: Float
) {
    if (alpha < 0.05f) return
    val leftSeam = Path().apply {
        moveTo(cx - 16f * scale, chestY + 6f * scale)
        cubicTo(cx - 12f * scale, waistY, cx - 14f * scale, waistY + 10f * scale, cx - 18f * scale, hemY - 4f * scale)
    }
    drawPath(leftSeam, color = Color.Black.copy(alpha = 0.15f * alpha), style = Stroke(width = 1.2f * scale))

    val rightSeam = Path().apply {
        moveTo(cx + 16f * scale, chestY + 6f * scale)
        cubicTo(cx + 12f * scale, waistY, cx + 14f * scale, waistY + 10f * scale, cx + 18f * scale, hemY - 4f * scale)
    }
    drawPath(rightSeam, color = Color.Black.copy(alpha = 0.15f * alpha), style = Stroke(width = 1.2f * scale))
}

/**
 * Chi tiết Áo Dài Cách Tân Lemur:
 * - Cổ áo cách tân viền chỉ kim tuyến
 * - Đường chiết eo thon thả
 * - Cài trâm/hoa cài tinh tế
 */
private fun DrawScope.drawAoDaiCachTanAuthenticDetails(
    cx: Float,
    collarFront: Point3D,
    collarY: Float,
    chestY: Float,
    waistY: Float,
    hemY: Float,
    scale: Float,
    alpha: Float,
    baseColor: Color
) {
    if (alpha < 0.05f) return

    val fx = collarFront.x
    val collarPiping = Path().apply {
        moveTo(fx - 14f * scale, collarFront.y)
        cubicTo(fx, collarFront.y + 4f * scale, fx, collarFront.y + 4f * scale, fx + 14f * scale, collarFront.y)
    }
    drawPath(collarPiping, color = ImperialGoldLight.copy(alpha = alpha), style = Stroke(width = 2.0f * scale, cap = StrokeCap.Round))

    val leftDart = Path().apply {
        moveTo(cx - 12f * scale, chestY + 8f * scale)
        cubicTo(cx - 10f * scale, waistY, cx - 10f * scale, waistY, cx - 16f * scale, hemY - 12f * scale)
    }
    drawPath(leftDart, color = baseColor.darken(0.3f).copy(alpha = alpha * 0.7f), style = Stroke(width = 1.4f * scale))

    val rightDart = Path().apply {
        moveTo(cx + 12f * scale, chestY + 8f * scale)
        cubicTo(cx + 10f * scale, waistY, cx + 10f * scale, waistY, cx + 16f * scale, hemY - 12f * scale)
    }
    drawPath(rightDart, color = baseColor.darken(0.3f).copy(alpha = alpha * 0.7f), style = Stroke(width = 1.4f * scale))

    val broochX = fx + 10f * scale
    val broochY = collarFront.y + 3f * scale
    drawCircle(color = ImperialGoldLight.copy(alpha = alpha), radius = 2.6f * scale, center = Offset(broochX, broochY))
    drawCircle(color = ImperialCrimson.copy(alpha = alpha), radius = 1.3f * scale, center = Offset(broochX, broochY))
}

/**
 * Áo Viên Lĩnh (Đoàn Lĩnh): Cổ tròn khép kín, cài khuy lệch vai phải, Bổ Tử thêu vuông trước ngực
 */
private fun DrawScope.drawVienLinhAuthenticDetails(
    cx: Float,
    collarFront: Point3D,
    collarY: Float,
    chestY: Float,
    waistY: Float,
    scale: Float,
    alpha: Float,
    baseColor: Color
) {
    if (alpha < 0.05f) return

    val fx = collarFront.x
    val roundCollar = Path().apply {
        moveTo(fx - 14f * scale, collarFront.y - 2f * scale)
        cubicTo(fx - 10f * scale, collarFront.y + 5f * scale, fx + 10f * scale, collarFront.y + 5f * scale, fx + 14f * scale, collarFront.y - 2f * scale)
    }
    drawPath(roundCollar, color = ImperialGold.copy(alpha = alpha), style = Stroke(width = 3.6f * scale, cap = StrokeCap.Round))
    drawPath(roundCollar, color = ImperialCrimson.copy(alpha = alpha * 0.8f), style = Stroke(width = 1.4f * scale, cap = StrokeCap.Round))

    val lapelSeam = Path().apply {
        moveTo(fx + 10f * scale, collarFront.y + 4f * scale)
        cubicTo(fx + 16f * scale, chestY - 2f * scale, fx + 20f * scale, chestY + 6f * scale, fx + 22f * scale, chestY + 12f * scale)
    }
    drawPath(lapelSeam, color = ImperialGoldLight.copy(alpha = alpha), style = Stroke(width = 2.0f * scale, cap = StrokeCap.Round))

    for (i in 0..2) {
        val bx = fx + (12f + i * 4.5f) * scale
        val by = collarFront.y + (7f + i * 6f) * scale
        drawCircle(color = ImperialGoldLight.copy(alpha = alpha), radius = 2.0f * scale, center = Offset(bx, by))
        drawCircle(color = ImperialGoldDark.copy(alpha = alpha), radius = 2.0f * scale, center = Offset(bx, by), style = Stroke(width = 0.8f * scale))
    }

    val badgeSize = 28f * scale
    val badgeLeft = cx - badgeSize / 2f
    val badgeTop = chestY + 2f * scale
    drawRect(color = ImperialGoldDark.copy(alpha = alpha * 0.85f), topLeft = Offset(badgeLeft, badgeTop), size = Size(badgeSize, badgeSize))
    drawRect(color = ImperialCrimson.copy(alpha = alpha * 0.9f), topLeft = Offset(badgeLeft + 2f * scale, badgeTop + 2f * scale), size = Size(badgeSize - 4f * scale, badgeSize - 4f * scale))
    drawRect(color = ImperialGold.copy(alpha = alpha), topLeft = Offset(badgeLeft, badgeTop), size = Size(badgeSize, badgeSize), style = Stroke(width = 1.6f * scale))
    drawRect(color = ImperialGoldLight.copy(alpha = alpha * 0.7f), topLeft = Offset(badgeLeft + 3f * scale, badgeTop + 3f * scale), size = Size(badgeSize - 6f * scale, badgeSize - 6f * scale), style = Stroke(width = 0.9f * scale))
}

private fun DrawScope.drawVienLinhBackDetails(
    cx: Float,
    collarY: Float,
    chestY: Float,
    waistY: Float,
    scale: Float,
    alpha: Float
) {
    if (alpha < 0.05f) return
    val badgeSize = 28f * scale
    val badgeLeft = cx - badgeSize / 2f
    val badgeTop = chestY + 2f * scale
    drawRect(color = ImperialGoldDark.copy(alpha = alpha * 0.85f), topLeft = Offset(badgeLeft, badgeTop), size = Size(badgeSize, badgeSize))
    drawRect(color = ImperialCrimson.copy(alpha = alpha * 0.9f), topLeft = Offset(badgeLeft + 2f * scale, badgeTop + 2f * scale), size = Size(badgeSize - 4f * scale, badgeSize - 4f * scale))
    drawRect(color = ImperialGold.copy(alpha = alpha), topLeft = Offset(badgeLeft, badgeTop), size = Size(badgeSize, badgeSize), style = Stroke(width = 1.6f * scale))
}

/**
 * Long Bào Hoàng Đế Triều Nguyễn:
 * - Cổ đứng nẹp vàng gấm mây
 * - Đại Long Ngũ Trảo Thần Uy (Rồng năm móng thêu chỉ vàng cuộn mây lửa, châu ngọc)
 */
private fun DrawScope.drawLongBaoAuthenticDetails(
    cx: Float,
    collarFront: Point3D,
    collarY: Float,
    chestY: Float,
    waistY: Float,
    hemY: Float,
    scale: Float,
    alpha: Float,
    baseColor: Color
) {
    if (alpha < 0.05f) return

    val fx = collarFront.x
    val standCollar = Path().apply {
        moveTo(fx - 15f * scale, collarFront.y - 2f * scale)
        cubicTo(fx, collarFront.y + 4f * scale, fx, collarFront.y + 4f * scale, fx + 15f * scale, collarFront.y - 2f * scale)
    }
    drawPath(standCollar, color = ImperialGoldLight.copy(alpha = alpha), style = Stroke(width = 4.0f * scale, cap = StrokeCap.Round))
    drawPath(standCollar, color = ImperialCrimson.copy(alpha = alpha * 0.9f), style = Stroke(width = 1.6f * scale, cap = StrokeCap.Round))

    val dragonCenterX = cx
    val dragonCenterY = chestY + 12f * scale
    val dragonR = 24f * scale

    drawCircle(color = ImperialGold.copy(alpha = alpha * 0.25f), radius = dragonR + 4f * scale, center = Offset(dragonCenterX, dragonCenterY))
    drawCircle(color = ImperialGoldLight.copy(alpha = alpha * 0.9f), radius = dragonR, center = Offset(dragonCenterX, dragonCenterY), style = Stroke(width = 2.2f * scale))
    drawCircle(color = ImperialGoldDark.copy(alpha = alpha * 0.7f), radius = dragonR - 3f * scale, center = Offset(dragonCenterX, dragonCenterY), style = Stroke(width = 1.0f * scale))

    val dragonBody = Path().apply {
        moveTo(dragonCenterX - 14f * scale, dragonCenterY + 10f * scale)
        cubicTo(
            dragonCenterX - 18f * scale, dragonCenterY - 12f * scale,
            dragonCenterX + 6f * scale, dragonCenterY - 18f * scale,
            dragonCenterX, dragonCenterY - 4f * scale
        )
        cubicTo(
            dragonCenterX - 6f * scale, dragonCenterY + 6f * scale,
            dragonCenterX + 16f * scale, dragonCenterY + 12f * scale,
            dragonCenterX + 12f * scale, dragonCenterY - 10f * scale
        )
    }
    drawPath(dragonBody, color = ImperialGoldLight.copy(alpha = alpha), style = Stroke(width = 4.2f * scale, cap = StrokeCap.Round))
    drawPath(dragonBody, color = ImperialCrimson.copy(alpha = alpha * 0.7f), style = Stroke(width = 1.4f * scale, cap = StrokeCap.Round))

    drawCircle(color = ImperialGoldLight.copy(alpha = alpha), radius = 4.5f * scale, center = Offset(dragonCenterX, dragonCenterY - 6f * scale))
    drawCircle(color = Color.White.copy(alpha = alpha), radius = 1.4f * scale, center = Offset(dragonCenterX - 2f * scale, dragonCenterY - 7f * scale))
    drawCircle(color = Color.White.copy(alpha = alpha), radius = 1.4f * scale, center = Offset(dragonCenterX + 2f * scale, dragonCenterY - 7f * scale))
    drawLine(color = ImperialGoldLight.copy(alpha = alpha), start = Offset(dragonCenterX - 3f * scale, dragonCenterY - 5f * scale), end = Offset(dragonCenterX - 9f * scale, dragonCenterY - 2f * scale), strokeWidth = 1.2f * scale)
    drawLine(color = ImperialGoldLight.copy(alpha = alpha), start = Offset(dragonCenterX + 3f * scale, dragonCenterY - 5f * scale), end = Offset(dragonCenterX + 9f * scale, dragonCenterY - 2f * scale), strokeWidth = 1.2f * scale)

    drawCircle(color = ImperialCrimson.copy(alpha = alpha), radius = 3.2f * scale, center = Offset(dragonCenterX, dragonCenterY + 4f * scale))
    drawCircle(color = ImperialGoldLight.copy(alpha = alpha), radius = 1.6f * scale, center = Offset(dragonCenterX, dragonCenterY + 4f * scale))
}

private fun DrawScope.drawLongBaoBackDetails(
    cx: Float,
    collarY: Float,
    chestY: Float,
    waistY: Float,
    scale: Float,
    alpha: Float,
    sinA: Float
) {
    if (alpha < 0.05f) return
    val dragonCenterX = cx
    val dragonCenterY = chestY + 12f * scale
    val dragonR = 24f * scale
    drawCircle(color = ImperialGold.copy(alpha = alpha * 0.25f), radius = dragonR + 4f * scale, center = Offset(dragonCenterX, dragonCenterY))
    drawCircle(color = ImperialGoldLight.copy(alpha = alpha * 0.9f), radius = dragonR, center = Offset(dragonCenterX, dragonCenterY), style = Stroke(width = 2.2f * scale))
    val dragonBody = Path().apply {
        moveTo(dragonCenterX - 14f * scale, dragonCenterY + 10f * scale)
        cubicTo(
            dragonCenterX - 18f * scale, dragonCenterY - 12f * scale,
            dragonCenterX + 6f * scale, dragonCenterY - 18f * scale,
            dragonCenterX, dragonCenterY - 4f * scale
        )
        cubicTo(
            dragonCenterX - 6f * scale, dragonCenterY + 6f * scale,
            dragonCenterX + 16f * scale, dragonCenterY + 12f * scale,
            dragonCenterX + 12f * scale, dragonCenterY - 10f * scale
        )
    }
    drawPath(dragonBody, color = ImperialGoldLight.copy(alpha = alpha), style = Stroke(width = 4.2f * scale, cap = StrokeCap.Round))
}

/**
 * Khăn Rằn Nam Bộ dệt kẻ ô caro vắt qua cổ rủ hai vạt xuống trước ngực
 */
private fun DrawScope.render3DKhanRan(
    headAnchor: Point3D,
    collarCenter: Point3D,
    chestY: Float,
    scale: Float,
    cosA: Float,
    sinA: Float
) {
    val alpha = (cosA * 1.5f).coerceIn(0.2f, 1.0f)
    val neckX = collarCenter.x
    val neckY = collarCenter.y + 4f * scale

    val scarfW = 7.5f * scale
    val scarfLen = 38f * scale

    val leftDrape = Path().apply {
        moveTo(neckX - 8f * scale, neckY)
        lineTo(neckX - 8f * scale - scarfW, neckY)
        lineTo(neckX - 10f * scale - scarfW, neckY + scarfLen)
        lineTo(neckX - 10f * scale, neckY + scarfLen)
        close()
    }
    drawPath(leftDrape, color = Color.White.copy(alpha = alpha))
    drawPath(leftDrape, color = Color(0xFF2C3E50).copy(alpha = alpha), style = Stroke(width = 1.0f * scale))

    for (i in 0..7) {
        val y = neckY + i * 4.6f * scale
        drawLine(
            color = Color(0xFF1E2B37).copy(alpha = alpha * 0.75f),
            start = Offset(neckX - 8f * scale - scarfW, y),
            end = Offset(neckX - 8f * scale, y),
            strokeWidth = 2.0f * scale
        )
    }

    val rightDrape = Path().apply {
        moveTo(neckX + 8f * scale, neckY)
        lineTo(neckX + 8f * scale + scarfW, neckY)
        lineTo(neckX + 10f * scale + scarfW, neckY + scarfLen - 4f * scale)
        lineTo(neckX + 10f * scale, neckY + scarfLen - 4f * scale)
        close()
    }
    drawPath(rightDrape, color = Color.White.copy(alpha = alpha))
    drawPath(rightDrape, color = Color(0xFF2C3E50).copy(alpha = alpha), style = Stroke(width = 1.0f * scale))

    for (i in 0..7) {
        val y = neckY + i * 4.4f * scale
        drawLine(
            color = Color(0xFF1E2B37).copy(alpha = alpha * 0.75f),
            start = Offset(neckX + 8f * scale, y),
            end = Offset(neckX + 8f * scale + scarfW, y),
            strokeWidth = 2.0f * scale
        )
    }

    for (f in 0..3) {
        val fx = neckX - 8f * scale - f * 2f * scale
        drawLine(color = Color.LightGray.copy(alpha = alpha), start = Offset(fx, neckY + scarfLen), end = Offset(fx, neckY + scarfLen + 4f * scale), strokeWidth = 1.0f * scale)
    }
}

/**
 * Đai Ngọc Hoàng Gia quấn quanh eo với các bản ngọc thạch vuông viền vàng
 */
private fun DrawScope.render3DDaiNgoc(
    cx: Float,
    waistY: Float,
    halfWWaist: Float,
    scale: Float,
    cosA: Float,
    sinA: Float
) {
    val beltW = halfWWaist * 0.95f
    val beltH = 5.5f * scale
    val beltY = waistY + 2f * scale

    drawRect(color = ImperialGoldDark, topLeft = Offset(cx - beltW, beltY - beltH / 2f), size = Size(beltW * 2f, beltH))
    drawRect(color = ImperialGoldLight, topLeft = Offset(cx - beltW, beltY - beltH / 2f), size = Size(beltW * 2f, beltH), style = Stroke(width = 1.2f * scale))

    val plateCount = 5
    val step = (beltW * 1.6f) / (plateCount - 1)
    val startX = cx - beltW * 0.8f

    for (i in 0 until plateCount) {
        val px = startX + i * step
        val plateSize = 5f * scale
        drawRect(color = JadeGreen, topLeft = Offset(px - plateSize / 2f, beltY - plateSize / 2f), size = Size(plateSize, plateSize))
        drawRect(color = ImperialGoldLight, topLeft = Offset(px - plateSize / 2f, beltY - plateSize / 2f), size = Size(plateSize, plateSize), style = Stroke(width = 0.9f * scale))
        drawCircle(color = Color.White.copy(alpha = 0.6f), radius = 1.0f * scale, center = Offset(px, beltY))
    }
}

/**
 * Trâm Cài Phượng Kim Tuyến cắm nghiêng trên đỉnh đầu/búi tóc
 */
private fun DrawScope.render3DTramCai(
    headAnchor: Point3D,
    scale: Float,
    cosA: Float,
    sinA: Float
) {
    val hx = headAnchor.x
    val hy = headAnchor.y - 12f * scale

    val pinPath = Path().apply {
        moveTo(hx - 8f * scale, hy + 4f * scale)
        lineTo(hx + 12f * scale, hy - 6f * scale)
    }
    drawPath(pinPath, color = ImperialGoldLight, style = Stroke(width = 2.0f * scale, cap = StrokeCap.Round))

    val phoenixCenter = Offset(hx + 12f * scale, hy - 6f * scale)
    drawCircle(color = ImperialGoldLight, radius = 4f * scale, center = phoenixCenter)
    drawCircle(color = ImperialCrimson, radius = 1.6f * scale, center = phoenixCenter)

    val drop1 = Offset(phoenixCenter.x + 2f * scale, phoenixCenter.y + 6f * scale)
    val drop2 = Offset(phoenixCenter.x + 4f * scale, phoenixCenter.y + 12f * scale)
    drawLine(color = ImperialGold, start = phoenixCenter, end = drop1, strokeWidth = 1.0f * scale)
    drawLine(color = ImperialGold, start = drop1, end = drop2, strokeWidth = 1.0f * scale)
    drawCircle(color = Color.White, radius = 1.8f * scale, center = drop1)
    drawCircle(color = JadeGreen, radius = 2.0f * scale, center = drop2)
}

private fun Color.lighten(factor: Float): Color {
    val f = factor.coerceIn(-0.5f, 0.5f)
    return Color(
        red = (red + f).coerceIn(0f, 1f),
        green = (green + f).coerceIn(0f, 1f),
        blue = (blue + f).coerceIn(0f, 1f),
        alpha = alpha
    )
}

private fun Color.darken(factor: Float): Color {
    val f = factor.coerceIn(0f, 0.7f)
    return Color(
        red = (red * (1f - f)).coerceIn(0f, 1f),
        green = (green * (1f - f)).coerceIn(0f, 1f),
        blue = (blue * (1f - f)).coerceIn(0f, 1f),
        alpha = alpha
    )
}

/**
 * Renders the costume at a specific perspective (e.g. FRONT or BACK) on a canvas.
 * Used for dual-view showcase in Export Cards (Thẻ ảnh) and comparative previews.
 */
@Composable
fun CostumeAngleCanvas(
    costume: CostumeItem,
    color: Color,
    pattern: CostumePattern,
    accessories: List<AccessoryItem>,
    viewAngle: CostumeView,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2
        val cy = size.height / 2
        draw3DPedestal(cx, cy)
        renderVolumetric3DCostume(
            cx = cx,
            cy = cy,
            rotationDeg = viewAngle.angle,
            costume = costume,
            baseColor = color,
            pattern = pattern,
            accessories = accessories
        )
    }
}
