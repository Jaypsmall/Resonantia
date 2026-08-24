package com.jaylizapp.resonantia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jaylizapp.resonantia.data.Zone
import kotlin.math.abs

@Composable
fun ZoneInfo(
    frequency: Int,
    zone: Zone,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = zone.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${zone.min}–${zone.max} Hz",
                fontSize = 8.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        Spacer(modifier = Modifier.height(17.dp))
        
        Text(
            text = zone.description,
            fontSize = 11.sp,
            lineHeight = 17.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Data Grid
        DataGrid(zone)

        Spacer(modifier = Modifier.height(16.dp))

        // Dynamic Profile
        DynamicProfile(frequency)

        Spacer(modifier = Modifier.height(14.dp))

        // Route
        TransitionRoute(zone)
    }
}

@Composable
fun DataGrid(zone: Zone) {
    val items = listOf(
        "Estado" to zone.state,
        "Cualidad" to zone.quality,
        "Bloqueo" to zone.block,
        "Práctica" to zone.practice
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        for (i in 0 until items.size step 2) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DataBox(items[i].first, items[i].second, Modifier.weight(1f))
                if (i + 1 < items.size) {
                    DataBox(items[i+1].first, items[i+1].second, Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun DataBox(title: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(11.dp)
    ) {
        Text(
            text = title.uppercase(),
            fontSize = 7.sp,
            letterSpacing = 1.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            fontSize = 10.sp,
            lineHeight = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun DynamicProfile(f: Int) {
    Column {
        Text(
            text = "PERFIL DINÁMICO",
            fontSize = 8.sp,
            letterSpacing = 1.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(9.dp))

        val clarity = ((f - 100) / 900f).coerceIn(0f, 1f)
        val expression = (1f - abs(f - 575) / 475f).coerceIn(0f, 1f)
        val protection = (1f - abs(f - 425) / 425f).coerceIn(0f, 1f)
        val integration = ((f - 700) / 300f).coerceIn(0f, 1f)

        ProfileBar("Claridad", clarity)
        ProfileBar("Expresión", expression)
        ProfileBar("Protección", protection)
        ProfileBar("Integración", integration)
    }
}

@Composable
fun ProfileBar(label: String, progress: Float) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 8.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Text(text = "${(progress * 100).toInt()}%", fontSize = 8.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.onSurface)
            )
        }
    }
}

@Composable
fun TransitionRoute(zone: Zone) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(11.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                )
            )
            .padding(14.dp)
    ) {
        Text(
            text = "RUTA DE TRANSICIÓN",
            color = Color(0xFF7C3AED),
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(7.dp))
        Text(
            text = zone.exit,
            fontSize = 10.sp,
            lineHeight = 15.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 8.dp, vertical = 5.dp)
        ) {
            Text(
                text = zone.direction,
                fontSize = 8.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
