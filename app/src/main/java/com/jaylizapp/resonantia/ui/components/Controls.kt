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
fun SearchBar(
    searchResult: Zone?,
    lastQuery: String,
    onSearch: (String) -> Unit,
    onResultClick: (Int) -> Unit
) {
    var text by remember { mutableStateOf("") }
    val suggestions = listOf("Culpa", "Equilibrio", "Protección", "Expresión", "Claridad", "Conexión")
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Buscar estados: paz, amor, culpa...", fontSize = 9.sp) },
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
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            items(suggestions) { suggestion ->
                SuggestionChip(
                    onClick = { 
                        text = suggestion
                        onSearch(suggestion)
                    },
                    label = { Text(suggestion, fontSize = 8.sp) },
                    shape = RoundedCornerShape(8.dp),
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        labelColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }

        if (searchResult != null) {
            Spacer(modifier = Modifier.height(16.dp))
            SearchResultCard(searchResult, onResultClick)
        } else if (lastQuery.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No se encontró nada para '$lastQuery'",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun SearchResultCard(zone: Zone, onClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick((zone.min + zone.max) / 2) },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = zone.color.copy(alpha = 0.15f)
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, zone.color.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = zone.name.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    color = zone.secondaryColor.copy(alpha = 0.8f)
                )
                Surface(
                    color = zone.color,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "${(zone.min + zone.max) / 2} Hz",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = zone.secondaryColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = zone.description,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Correspondencia: ${zone.state}",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
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
