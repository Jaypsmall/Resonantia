package com.jaylizapp.resonantia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.BrightnessHigh
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.launch
import com.jaylizapp.resonantia.ui.components.*
import com.jaylizapp.resonantia.ui.theme.ResonantiaTheme
import com.jaylizapp.resonantia.viewmodel.SpectrumViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: SpectrumViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val systemInDark = isSystemInDarkTheme()
            var isDarkMode by rememberSaveable { mutableStateOf(systemInDark) }
            
            ResonantiaTheme(darkTheme = isDarkMode) {
                val view = LocalView.current
                if (!view.isInEditMode) {
                    SideEffect {
                        val window = (view.context as android.app.Activity).window
                        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkMode
                    }
                }

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet(
                            drawerContainerColor = MaterialTheme.colorScheme.surface,
                            drawerTonalElevation = 2.dp
                        ) {
                            DrawerContent(
                                isDarkMode = isDarkMode,
                                onThemeToggle = { isDarkMode = !isDarkMode },
                                onClose = { scope.launch { drawerState.close() } }
                            )
                        }
                    }
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = { AppFooter() }
                    ) { innerPadding ->
                        VibrationalSpectrumApp(
                            viewModel = viewModel,
                            isDarkMode = isDarkMode,
                            onThemeToggle = { isDarkMode = !isDarkMode },
                            onMenuClick = { scope.launch { drawerState.open() } },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerContent(isDarkMode: Boolean, onThemeToggle: () -> Unit, onClose: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.icono_resonantia),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "RESONANTIA",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        DrawerItem(Icons.Default.Star, "Explorador de Frecuencias", true, onClose)
        DrawerItem(Icons.Default.History, "Historial de Sesiones", false, onClose)
        DrawerItem(Icons.Default.Settings, "Configuración Avanzada", false, onClose)
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        
        DrawerItem(
            icon = if (isDarkMode) Icons.Default.BrightnessHigh else Icons.Default.Brightness4,
            label = if (isDarkMode) "Modo Claro" else "Modo Oscuro",
            selected = false,
            onClick = {
                onThemeToggle()
                onClose()
            }
        )
        
        DrawerItem(Icons.Default.Info, "Sobre la Resonancia", false, onClose)
        
                                    Spacer(modifier = Modifier.weight(1f))

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Resonantia v1.0.4 PRO",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                        Text(
                                            text = "Created by JAYLIZ with ❤️",
                                            fontSize = 8.sp,
                                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
                                        )
                                    }
    }
}

@Composable
fun DrawerItem(icon: ImageVector, label: String, selected: Boolean, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(text = label, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal) },
        selected = selected,
        onClick = onClick,
        icon = { Icon(icon, contentDescription = null) },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(vertical = 4.dp),
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = Color.Transparent,
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun VibrationalSpectrumApp(
    viewModel: SpectrumViewModel,
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val currentZone = viewModel.getZoneForFrequency(state.currentFrequency)
    val targetZone = viewModel.getZoneForFrequency(state.targetFrequency)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            Header(
                isDarkMode = isDarkMode,
                onMenuClick = onMenuClick,
                onThemeToggle = onThemeToggle
            )
        }
        
        item {
            FrequencyVisualSection(
                frequency = state.currentFrequency,
                zoneName = currentZone.name,
                currentZone = currentZone
            )
        }
        
        item {
            SpectrumChart(
                currentFrequency = state.currentFrequency,
                targetFrequency = state.targetFrequency,
                onFrequencySelected = { viewModel.updateFrequency(it) }
            )
        }
        
        item {
            ZoneInfo(
                frequency = state.currentFrequency,
                zone = currentZone
            )
        }
        
        item {
            TargetControl(
                currentFrequency = state.currentFrequency,
                targetFrequency = state.targetFrequency,
                targetZone = targetZone,
                onTargetChanged = { viewModel.updateTargetFrequency(it) }
            )
        }
        
        item {
            SearchBar(onSearch = { viewModel.searchState(it) })
        }
        
        item {
            MainControls(
                currentFrequency = state.currentFrequency,
                onFrequencyChanged = { viewModel.updateFrequency(it) }
            )
        }
        
        item {
            HistoryList(
                history = state.history,
                onFrequencySelected = { viewModel.updateFrequency(it) },
                onClear = { viewModel.clearHistory() }
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
fun Header(isDarkMode: Boolean, onMenuClick: () -> Unit, onThemeToggle: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "INTERACTIVE SYMBOLIC SPECTRUM",
                fontSize = 8.sp,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.outline
            )
            IconButton(onClick = onThemeToggle) {
                Icon(
                    imageVector = if (isDarkMode) Icons.Default.BrightnessHigh else Icons.Default.Brightness4,
                    contentDescription = "Cambiar Tema",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        Text(
            text = "Vibrational Spectrum PRO",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-1.3).sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Text(
            text = "Sistema visual interactivo para explorar frecuencia, geometría, movimiento y estados simbólicos.",
            fontSize = 11.sp,
            lineHeight = 17.sp,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(top = 7.dp)
        )
        
        Row(
            modifier = Modifier.padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .background(Color(0xFF20B779), androidx.compose.foundation.shape.CircleShape)
            )
            Text(
                text = "Visual Resonance Engine · ONLINE",
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun FrequencyVisualSection(
    frequency: Int,
    zoneName: String,
    currentZone: com.jaylizapp.resonantia.data.Zone
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = frequency.toString(),
                        fontSize = 39.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-2).sp
                    )
                    Text(
                        text = " Hz",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = zoneName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "ZONA ACTUAL",
                        fontSize = 8.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
            
            SpectrumVisualizer(
                frequency = frequency,
                zone = currentZone,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun AppFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Resonantia v1.0.4 PRO",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.outline,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Text(
            text = "Created by JAYLIZ with ❤️",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(top = 2.dp)
        )
        Text(
            text = "Interactive Symbolic Spectrum · Visual Resonance Engine",
            fontSize = 8.sp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
