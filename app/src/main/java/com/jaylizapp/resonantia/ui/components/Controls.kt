package com.jaylizapp.resonantia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jaylizapp.resonantia.data.Zone
import kotlin.math.abs

@Composable
fun TargetControl(
    currentFrequency: Int,
    targetFrequency: Int,
    targetZone: Zone,
    onTargetChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "FRECUENCIA OBJETIVO",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "$targetFrequency Hz",
                color = Color(0xFF7C3AED),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Slider(
            value = targetFrequency.toFloat(),
            onValueChange = { onTargetChanged(it.toInt()) },
            valueRange = 100f..1000f,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF7C3AED),
                activeTrackColor = Color(0xFF7C3AED)
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            TargetBox("Objetivo", targetZone.name, Modifier.weight(1f))
            TargetBox("Distancia", "${abs(targetFrequency - currentFrequency)} Hz", Modifier.weight(1f))
            TargetBox("Dirección", if (targetFrequency > currentFrequency) "↑" else if (targetFrequency < currentFrequency) "↓" else "●", Modifier.weight(1f))
        }
    }
}

@Composable
fun TargetBox(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(9.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, fontSize = 7.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        Text(text = value, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Buscar: culpa, claridad, miedo...", fontSize = 9.sp) },
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(9.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )
        Button(
            onClick = { onSearch(text) },
            modifier = Modifier.height(48.dp),
            shape = RoundedCornerShape(9.dp)
        ) {
            Text("Buscar", fontSize = 9.sp)
        }
    }
}

@Composable
fun MainControls(
    currentFrequency: Int,
    onFrequencyChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        ControlItem(
            text = "← −10",
            onClick = { onFrequencyChanged(currentFrequency - 10) },
            modifier = Modifier.weight(1f)
        )
        ControlItem(
            text = "500 Hz",
            onClick = { onFrequencyChanged(500) },
            modifier = Modifier.weight(1f)
        )
        ControlItem(
            text = "+10 →",
            onClick = { onFrequencyChanged(currentFrequency + 10) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ControlItem(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(9.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = borderStroke()
    ) {
        Text(text = text, fontSize = 9.sp)
    }
}

@Composable
fun borderStroke() = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)

@Composable
fun HistoryList(
    history: List<Int>,
    onFrequencySelected: (Int) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "HISTORIAL",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "Limpiar",
                fontSize = 8.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.clickable { onClear() }
            )
        }
        Spacer(modifier = Modifier.height(9.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            items(history.reversed()) { freq ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(6.dp))
                        .clickable { onFrequencySelected(freq) }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Text(text = "$freq Hz", fontSize = 8.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            }
        }
    }
}
