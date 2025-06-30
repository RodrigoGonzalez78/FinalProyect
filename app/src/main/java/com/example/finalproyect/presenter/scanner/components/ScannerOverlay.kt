package com.example.finalproyect.presenter.scanner.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun ScannerOverlay(
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val overlayColor = Color.Black.copy(alpha = 0.5f)
    val borderColor = MaterialTheme.colorScheme.primary
    val cornerRadius = with(density) { 16.dp.toPx() }
    val borderWidth = with(density) { 3.dp.toPx() }

    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        // Tamaño del área de escaneo (cuadrado)
        val scanAreaSize = minOf(canvasWidth, canvasHeight) * 0.7f
        val scanAreaLeft = (canvasWidth - scanAreaSize) / 2
        val scanAreaTop = (canvasHeight - scanAreaSize) / 2

        // Dibujar overlay oscuro
        drawRect(
            color = overlayColor,
            size = Size(canvasWidth, canvasHeight)
        )

        // Crear área transparente para el escáner
        drawRoundRect(
            color = Color.Transparent,
            topLeft = Offset(scanAreaLeft, scanAreaTop),
            size = Size(scanAreaSize, scanAreaSize),
            cornerRadius = CornerRadius(cornerRadius),
            blendMode = BlendMode.Clear
        )

        // Dibujar bordes del área de escaneo
        drawScannerBorder(
            scanAreaLeft = scanAreaLeft,
            scanAreaTop = scanAreaTop,
            scanAreaSize = scanAreaSize,
            borderColor = borderColor,
            borderWidth = borderWidth,
            cornerRadius = cornerRadius
        )
    }
}

private fun DrawScope.drawScannerBorder(
    scanAreaLeft: Float,
    scanAreaTop: Float,
    scanAreaSize: Float,
    borderColor: Color,
    borderWidth: Float,
    cornerRadius: Float
) {
    val cornerLength = scanAreaSize * 0.1f

    // Esquina superior izquierda
    drawLine(
        color = borderColor,
        start = Offset(scanAreaLeft, scanAreaTop + cornerRadius),
        end = Offset(scanAreaLeft, scanAreaTop + cornerLength),
        strokeWidth = borderWidth
    )
    drawLine(
        color = borderColor,
        start = Offset(scanAreaLeft + cornerRadius, scanAreaTop),
        end = Offset(scanAreaLeft + cornerLength, scanAreaTop),
        strokeWidth = borderWidth
    )

    // Esquina superior derecha
    drawLine(
        color = borderColor,
        start = Offset(scanAreaLeft + scanAreaSize, scanAreaTop + cornerRadius),
        end = Offset(scanAreaLeft + scanAreaSize, scanAreaTop + cornerLength),
        strokeWidth = borderWidth
    )
    drawLine(
        color = borderColor,
        start = Offset(scanAreaLeft + scanAreaSize - cornerRadius, scanAreaTop),
        end = Offset(scanAreaLeft + scanAreaSize - cornerLength, scanAreaTop),
        strokeWidth = borderWidth
    )

    // Esquina inferior izquierda
    drawLine(
        color = borderColor,
        start = Offset(scanAreaLeft, scanAreaTop + scanAreaSize - cornerRadius),
        end = Offset(scanAreaLeft, scanAreaTop + scanAreaSize - cornerLength),
        strokeWidth = borderWidth
    )
    drawLine(
        color = borderColor,
        start = Offset(scanAreaLeft + cornerRadius, scanAreaTop + scanAreaSize),
        end = Offset(scanAreaLeft + cornerLength, scanAreaTop + scanAreaSize),
        strokeWidth = borderWidth
    )

    // Esquina inferior derecha
    drawLine(
        color = borderColor,
        start = Offset(scanAreaLeft + scanAreaSize, scanAreaTop + scanAreaSize - cornerRadius),
        end = Offset(scanAreaLeft + scanAreaSize, scanAreaTop + scanAreaSize - cornerLength),
        strokeWidth = borderWidth
    )
    drawLine(
        color = borderColor,
        start = Offset(scanAreaLeft + scanAreaSize - cornerRadius, scanAreaTop + scanAreaSize),
        end = Offset(scanAreaLeft + scanAreaSize - cornerLength, scanAreaTop + scanAreaSize),
        strokeWidth = borderWidth
    )
}
