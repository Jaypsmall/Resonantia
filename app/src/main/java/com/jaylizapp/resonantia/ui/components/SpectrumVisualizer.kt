package com.jaylizapp.resonantia.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jaylizapp.resonantia.data.Zone
import kotlin.math.*

@Composable
fun SpectrumVisualizer(
    frequency: Int,
    zone: Zone,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "spectrum")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    val intensity = (frequency - 100) / 900f
    val color = zone.color

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(390.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height / 2

            // Background Gradient
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(color.copy(alpha = 0.12f * intensity), Color.Transparent),
                    center = Offset(centerX, centerY),
                    radius = size.width * 0.7f
                )
            )

            // Rings
            val baseRadius = 35.dp.toPx() + intensity * 80.dp.toPx()
            for (i in 0 until 7) {
                val pulse = sin(time * 0.2f + i * 0.9f)
                val radius = baseRadius + i * 28.dp.toPx() + pulse * 7.dp.toPx()
                drawCircle(
                    color = color.copy(alpha = 0.07f + intensity * 0.06f),
                    radius = radius,
                    center = Offset(centerX, centerY),
                    style = Stroke(width = 1.dp.toPx() + intensity * 1.2f.dp.toPx())
                )
            }

            // Particles (Simplified for Compose)
            // In a real app, you might want to manage a list of particle states
            // For now, we'll draw a few stable ones
            val particleCount = 40
            val random = java.util.Random(42)
            for (i in 0 until particleCount) {
                val pAngle = random.nextFloat() * 2 * PI.toFloat()
                val pRadiusBase = 25.dp.toPx() + random.nextFloat() * 150.dp.toPx()
                val pSpeed = 0.1f + random.nextFloat() * 0.5f
                val pOrbit = if (random.nextBoolean()) 1 else -1
                
                val currentRadius = pRadiusBase * (0.65f + intensity * 0.75f)
                val angle = pAngle + time * 0.1f * pSpeed * (0.4f + intensity * 2f) * pOrbit
                
                val x = centerX + cos(angle) * currentRadius
                val y = centerY + sin(angle) * currentRadius
                val alpha = 0.15f + (sin(time * 0.2f + i) + 1f) * 0.25f
                
                drawCircle(
                    color = color.copy(alpha = alpha.coerceIn(0f, 1f)),
                    radius = (1.dp.toPx() + random.nextFloat() * 2.dp.toPx()) * (0.7f + intensity),
                    center = Offset(x, y)
                )
            }

            // Geometry
            val points = (6 + (intensity * 10).toInt())
            val geoRadius = 50.dp.toPx() + intensity * 75.dp.toPx()
            val rotation = time * 2.5f

            rotate(rotation, Offset(centerX, centerY)) {
                val path = Path()
                for (i in 0 until points) {
                    val angle = (2 * PI.toFloat() / points) * i
                    val wave = sin(time * 0.3f + i) * (5.dp.toPx() + intensity * 12.dp.toPx())
                    val r = geoRadius + wave
                    val x = centerX + cos(angle) * r
                    val y = centerY + sin(angle) * r
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                path.close()
                drawPath(
                    path = path,
                    color = color.copy(alpha = 0.5f),
                    style = Stroke(width = 1.2f.dp.toPx() + intensity * 1.5f.dp.toPx())
                )
            }
            
            // Inner Geometry
             rotate(-rotation, Offset(centerX, centerY)) {
                val path = Path()
                for (i in 0 until points) {
                    val angle = (2 * PI.toFloat() / points) * i
                    val r = geoRadius * 0.52f
                    val x = centerX + cos(angle) * r
                    val y = centerY + sin(angle) * r
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                path.close()
                drawPath(
                    path = path,
                    color = color.copy(alpha = 0.25f),
                    style = Stroke(width = 1.dp.toPx())
                )
            }

            // Core
            val corePulse = sin(time * 0.4f * (0.5f + intensity * 2f))
            val coreRadius = 16.dp.toPx() + intensity * 28.dp.toPx() + corePulse * 3.dp.toPx()
            
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(color.copy(alpha = 0.75f), color.copy(alpha = 0.22f), Color.Transparent),
                    center = Offset(centerX, centerY),
                    radius = coreRadius * 2.5f
                ),
                radius = coreRadius * 2.5f,
                center = Offset(centerX, centerY)
            )
            
            drawCircle(
                color = color.copy(alpha = 0.22f),
                radius = coreRadius,
                center = Offset(centerX, centerY)
            )
            
            drawCircle(
                color = color.copy(alpha = 0.8f),
                radius = coreRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 1.5.dp.toPx())
            )
            
            drawCircle(
                color = Color.White.copy(alpha = 0.9f),
                radius = 4.dp.toPx() + intensity * 3.dp.toPx(),
                center = Offset(centerX, centerY)
            )
        }

        // Overlays
        Column(
            modifier = Modifier
                .padding(14.dp)
                .align(Alignment.TopStart)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f),
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(horizontal = 9.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "LIVE FIELD",
                    fontSize = 8.sp,
                    letterSpacing = 1.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(14.dp)
                .align(Alignment.TopEnd),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "${(intensity * 100).toInt()}%",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "intensidad",
                fontSize = 7.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                letterSpacing = 0.5.sp
            )
        }
    }
}
