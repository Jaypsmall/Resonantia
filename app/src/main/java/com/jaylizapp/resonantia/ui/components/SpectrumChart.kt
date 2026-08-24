package com.jaylizapp.resonantia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jaylizapp.resonantia.data.Zone
import com.jaylizapp.resonantia.data.zones

@Composable
fun SpectrumChart(
    currentFrequency: Int,
    targetFrequency: Int,
    onFrequencySelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val ticks = listOf(100, 200, 350, 500, 650, 750, 850, 1000)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(18.dp)
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth().height(320.dp)) {
            val chartWidth = maxWidth
            
            // Interaction layer
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val percentage = offset.x / size.width
                            val freq = 100 + (percentage * 900).toInt()
                            onFrequencySelected(freq.coerceIn(100, 1000))
                        }
                    }
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragStart = { offset ->
                                val percentage = offset.x / size.width
                                val freq = 100 + (percentage * 900).toInt()
                                onFrequencySelected(freq.coerceIn(100, 1000))
                            },
                            onHorizontalDrag = { change, _ ->
                                change.consume()
                                val percentage = change.position.x / size.width
                                val freq = 100 + (percentage * 900).toInt()
                                onFrequencySelected(freq.coerceIn(100, 1000))
                            }
                        )
                    }
            )

            // Grid lines
            ticks.forEach { tick ->
                val percentage = (tick - 100) / 900f
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .offset(x = chartWidth * percentage)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.1f),
                                    MaterialTheme.colorScheme.outlineVariant,
                                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.1f)
                                )
                            )
                        )
                )
            }

            Column(modifier = Modifier.fillMaxSize()) {
                zones.forEach { zone ->
                    val isActive = currentFrequency in zone.min..zone.max
                    ZoneRow(
                        zone = zone,
                        isActive = isActive,
                        chartWidth = chartWidth,
                        onZoneClick = { onFrequencySelected((zone.min + zone.max) / 2) }
                    )
                }
            }

            // Target Frequency Marker (Purple Glow)
            val targetPercentage = (targetFrequency - 100) / 900f
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(3.dp)
                    .offset(x = (chartWidth * targetPercentage) - 1.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color.Transparent, Color(0xFF7C3AED).copy(alpha = 0.5f), Color.Transparent)
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.5.dp)
                    .offset(x = chartWidth * targetPercentage)
                    .background(Color(0xFF7C3AED))
            ) {
                Text(
                    text = "OBJETIVO",
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7C3AED),
                    modifier = Modifier
                        .padding(start = 5.dp, top = 12.dp)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f), RoundedCornerShape(2.dp))
                        .padding(horizontal = 2.dp)
                )
            }

            // Current Frequency Marker (Dynamic Glow)
            val currentPercentage = (currentFrequency - 100) / 900f
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .offset(x = (chartWidth * currentPercentage) - 1.5.dp)
                    .width(3.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .offset(y = (-6).dp)
                        .background(MaterialTheme.colorScheme.primary, androidx.compose.foundation.shape.CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.surface, androidx.compose.foundation.shape.CircleShape)
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(3.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color.Transparent, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), Color.Transparent)
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.5.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
            
            // Current Frequency Label (Floating Badge)
            Surface(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp),
                tonalElevation = 4.dp,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .offset(x = (chartWidth * currentPercentage) - 30.dp, y = (-35).dp)
            ) {
                Text(
                    text = "$currentFrequency Hz",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        // Axis
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .height(43.dp)
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                ticks.forEach { tick ->
                    val percentage = (tick - 100) / 900f
                    Column(
                        modifier = Modifier.offset(x = maxWidth * percentage - 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(modifier = Modifier.size(1.dp, 4.dp).background(MaterialTheme.colorScheme.outlineVariant))
                        Text(
                            text = tick.toString(),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
            Text(
                text = "FRECUENCIA SIMBÓLICA (Hz) →",
                fontSize = 7.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f),
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
fun ZoneRow(
    zone: Zone,
    isActive: Boolean,
    chartWidth: androidx.compose.ui.unit.Dp,
    onZoneClick: () -> Unit
) {
    val start = (zone.min - 100) / 900f
    val end = (zone.max - 100) / 900f
    val widthPercent = end - start
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .width(chartWidth * widthPercent)
                .offset(x = chartWidth * start)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(zone.color.copy(alpha = 0.7f), zone.color)
                    )
                )
                .then(if (isActive) Modifier.border(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.6f), RoundedCornerShape(10.dp)) else Modifier)
                .clickable { onZoneClick() },
            contentAlignment = Alignment.Center
        ) {
            if (isActive) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(alpha = 0.15f))
                )
            }
            Text(
                text = zone.name,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                color = zone.secondaryColor,
                maxLines = 1
            )
        }
    }
}
