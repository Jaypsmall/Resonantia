package com.jaylizapp.resonantia.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import java.util.Random

data class VisualParticle(
    val angle: Float,
    val radiusBase: Float,
    val speed: Float,
    val orbit: Int,
    val size: Float
)

@Composable
fun SpectrumVisualizer(
    frequency: Int,
    zone: Zone,
    modifier: Modifier = Modifier
) {
    // Usamos el transition de forma que solo invalide el Canvas, no todo el Composable
    val infiniteTransition = rememberInfiniteTransition(label = "spectrum")
    val timeState = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    val intensity = (frequency - 100) / 900f
    val color = zone.color
    
    val particles = remember {
        val random = Random(42)
        List(50) {
            VisualParticle(
                angle = random.nextFloat() * 2 * PI.toFloat(),
                radiusBase = 20f + random.nextFloat() * 160f,
                speed = 0.05f + random.nextFloat() * 0.4f,
                orbit = if (random.nextBoolean()) 1 else -1,
                size = 1f + random.nextFloat() * 2.5f
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(390.dp)
            .background(if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surface else Color(0xFF1A1A1A))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Leemos el valor aquí para que solo se redibuje el Canvas (Optimization)
            val time = timeState.value
            val centerX = size.width / 2
            val centerY = size.height / 2
            val baseDensity = 1.dp.toPx()

            // 1. Atmosphere
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(color.copy(alpha = 0.15f * intensity), Color.Transparent),
                    center = Offset(centerX, centerY),
                    radius = size.width * 0.8f
                )
            )

            // 2. Dynamic Rings
            val baseRadius = 40.dp.toPx() + intensity * 90.dp.toPx()
            for (i in 0 until 8) {
                val pulse = sin(time * 0.15f + i * 0.8f)
                val radius = baseRadius + i * 25.dp.toPx() + pulse * (5.dp.toPx() + intensity * 10.dp.toPx())
                drawCircle(
                    color = color.copy(alpha = (0.05f + intensity * 0.08f) / (i * 0.5f + 1f)),
                    radius = radius,
                    center = Offset(centerX, centerY),
                    style = Stroke(width = baseDensity * (0.8f + intensity))
                )
            }

            // 3. Particles
            particles.forEachIndexed { i, p ->
                val currentRadius = p.radiusBase.dp.toPx() * (0.7f + intensity * 0.8f)
                val angle = p.angle + time * 0.08f * p.speed * (0.5f + intensity * 2.5f) * p.orbit
                
                val x = centerX + cos(angle) * currentRadius
                val y = centerY + sin(angle) * currentRadius
                val alpha = 0.1f + (sin(time * 0.25f + i) + 1f) * 0.3f * (0.5f + intensity * 0.5f)
                
                drawCircle(
                    color = color.copy(alpha = alpha.coerceIn(0f, 1f)),
                    radius = p.size.dp.toPx() * (0.8f + intensity * 0.6f),
                    center = Offset(x, y)
                )
            }

            // 4. Sacred Geometry
            val points = (6 + (intensity * 12).toInt())
            val geoRadius = 60.dp.toPx() + intensity * 80.dp.toPx()
            val rotation = time * 2.0f

            rotate(rotation, Offset(centerX, centerY)) {
                val path = Path()
                for (i in 0 until points) {
                    val angle = (2 * PI.toFloat() / points) * i
                    val wave = sin(time * 0.4f + i) * (4.dp.toPx() + intensity * 15.dp.toPx())
                    val r = geoRadius + wave
                    val x = centerX + cos(angle) * r
                    val y = centerY + sin(angle) * r
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                path.close()
                drawPath(
                    path = path,
                    color = color.copy(alpha = 0.4f + intensity * 0.2f),
                    style = Stroke(width = baseDensity * (1f + intensity * 2f))
                )
            }
            
            // 5. Secondary Geometry
             rotate(-rotation * 0.7f, Offset(centerX, centerY)) {
                val path = Path()
                val innerPoints = 6
                for (i in 0 until innerPoints) {
                    val angle = (2 * PI.toFloat() / innerPoints) * i
                    val r = geoRadius * 0.5f * (0.9f + intensity * 0.2f)
                    val x = centerX + cos(angle) * r
                    val y = centerY + sin(angle) * r
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                path.close()
                drawPath(
                    path = path,
                    color = color.copy(alpha = 0.2f),
                    style = Stroke(width = baseDensity)
                )
            }

            // 6. Glowing Core
            val corePulse = sin(time * 0.5f * (0.6f + intensity * 2.5f))
            val coreRadius = 18.dp.toPx() + intensity * 30.dp.toPx() + corePulse * 4.dp.toPx()
            
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(color.copy(alpha = 0.6f + intensity * 0.3f), Color.Transparent),
                    center = Offset(centerX, centerY),
                    radius = coreRadius * 2.5f
                ),
                radius = coreRadius * 2.5f,
                center = Offset(centerX, centerY)
            )
            
            drawCircle(
                color = color.copy(alpha = 0.3f),
                radius = coreRadius,
                center = Offset(centerX, centerY)
            )
            
            drawCircle(
                color = color.copy(alpha = 0.9f),
                radius = coreRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = baseDensity * 2f)
            )
            
            drawCircle(
                color = Color.White.copy(alpha = 0.95f),
                radius = 5.dp.toPx() + intensity * 4.dp.toPx(),
                center = Offset(centerX, centerY)
            )
        }

        // Labels Overlay
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(24.dp)
            ) {
                Text(
                    text = "LIVE FIELD",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${(intensity * 100).toInt()}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "RESONANCIA",
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}
