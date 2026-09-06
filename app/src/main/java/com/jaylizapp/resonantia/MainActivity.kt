package com.jaylizapp.resonantia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.BrightnessHigh
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.jaylizapp.resonantia.ui.components.HistoryList
import com.jaylizapp.resonantia.ui.components.MainControls
import com.jaylizapp.resonantia.ui.components.SearchBar
import com.jaylizapp.resonantia.ui.components.SpectrumChart
import com.jaylizapp.resonantia.ui.components.SpectrumVisualizer
import com.jaylizapp.resonantia.ui.components.TargetControl
import com.jaylizapp.resonantia.ui.components.ZoneInfo
import com.jaylizapp.resonantia.ui.theme.ResonantiaTheme
import com.jaylizapp.resonantia.viewmodel.SpectrumViewModel
import kotlinx.coroutines.launch

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
                val pagerState = rememberPagerState(pageCount = { 3 })

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet(
                            drawerContainerColor = MaterialTheme.colorScheme.surface,
                            drawerTonalElevation = 2.dp
                        ) {
                            DrawerContent(
                                isDarkMode = isDarkMode,
                                currentPage = pagerState.currentPage,
                                onThemeToggle = { isDarkMode = !isDarkMode },
                                onNavigate = { page ->
                                    scope.launch { 
                                        pagerState.animateScrollToPage(page)
                                        drawerState.close()
                                    }
                                },
                                onClose = { scope.launch { drawerState.close() } }
                            )
                        }
                    }
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        VibrationalSpectrumApp(
                            viewModel = viewModel,
                            isDarkMode = isDarkMode,
                            pagerState = pagerState,
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
fun DrawerContent(
    isDarkMode: Boolean, 
    currentPage: Int,
    onThemeToggle: () -> Unit, 
    onNavigate: (Int) -> Unit,
    onClose: () -> Unit
) {
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
        
        DrawerItem(Icons.Default.AutoAwesome, "Panel Visual", currentPage == 0) { onNavigate(0) }
        DrawerItem(Icons.Default.GraphicEq, "Explorador Gráfico", currentPage == 1) { onNavigate(1) }
        DrawerItem(Icons.Default.Explore, "Análisis y Herramientas", currentPage == 2) { onNavigate(2) }
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        
        DrawerItem(
            icon = if (isDarkMode) Icons.Default.BrightnessHigh else Icons.Default.Brightness4,
            label = if (isDarkMode) "Modo Claro" else "Modo Oscuro",
            selected = false,
            onClick = {
                onThemeToggle()
                onClose()
            }
        )
        
        DrawerItem(Icons.Default.Settings, "Configuración", false, onClose)
        DrawerItem(Icons.Default.Info, "Sobre la Resonancia", false, onClose)
        
        Spacer(modifier = Modifier.weight(1f))

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VibrationalSpectrumApp(
    viewModel: SpectrumViewModel,
    isDarkMode: Boolean,
    pagerState: androidx.compose.foundation.pager.PagerState,
    onThemeToggle: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val currentZone = viewModel.getZoneForFrequency(state.currentFrequency)
    val targetZone = viewModel.getZoneForFrequency(state.targetFrequency)
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Header(
            isDarkMode = isDarkMode,
            onMenuClick = onMenuClick,
            onThemeToggle = onThemeToggle
        )

        val titles = listOf("VISUAL", "GRÁFICA", "ANALISIS")
        
        SecondaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.Transparent,
            divider = {},
            modifier = Modifier.height(48.dp)
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(text = title, fontSize = 10.sp, fontWeight = FontWeight.Black) },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.outline
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.Top
        ) { pageIndex ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                when (pageIndex) {
                    0 -> {
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
                                onFrequencySelected = { viewModel.updateFrequency(it) },
                                height = 180.dp
                            )
                        }
                        item {
                            MainControls(
                                currentFrequency = state.currentFrequency,
                                onFrequencyChanged = { viewModel.updateFrequency(it) }
                            )
                        }
                    }
                    1 -> {
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
                                targetFrequency = state.targetFrequency,
                                zone = currentZone
                            )
                        }
                    }
                    2 -> {
                        item {
                            TargetControl(
                                currentFrequency = state.currentFrequency,
                                targetFrequency = state.targetFrequency,
                                targetZone = targetZone,
                                onTargetChanged = { viewModel.updateTargetFrequency(it) }
                            )
                        }
                        item {
                            SearchBar(
                                searchResult = state.searchResult,
                                lastQuery = state.lastSearchQuery,
                                onSearch = { viewModel.searchState(it) },
                                onResultClick = { viewModel.updateFrequency(it) }
                            )
                        }
                        item {
                            HistoryList(
                                history = state.history,
                                onFrequencySelected = { viewModel.updateFrequency(it) },
                                onClear = { viewModel.clearHistory() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Header(isDarkMode: Boolean, onMenuClick: () -> Unit, onThemeToggle: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = "RESONANTIA",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Vibrational Spectrum PRO",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 0.5.sp
                    )
                }
            }
            
            IconButton(onClick = onThemeToggle) {
                Icon(
                    imageVector = if (isDarkMode) Icons.Default.BrightnessHigh else Icons.Default.Brightness4,
                    contentDescription = "Cambiar Tema",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
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
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = frequency.toString(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-1.5).sp
                    )
                    Text(
                        text = " Hz",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = zoneName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "ZONA ACTUAL",
                        fontSize = 7.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
            
            SpectrumVisualizer(
                frequency = frequency,
                zone = currentZone,
                modifier = Modifier.fillMaxWidth().height(320.dp)
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
