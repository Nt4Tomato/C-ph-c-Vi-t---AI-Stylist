package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cophuc.ui.CostumeViewModel
import com.example.cophuc.ui.components.CulturalContextDialog
import com.example.cophuc.ui.screens.CulturalArchiveScreen
import com.example.cophuc.ui.screens.DiscoveryScreen
import com.example.cophuc.ui.screens.FittingRoomScreen
import com.example.cophuc.ui.screens.SavedOutfitsScreen
import com.example.ui.theme.ImperialCrimson
import com.example.ui.theme.ImperialGoldLight
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    viewModel: CostumeViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Listen to snackbar messages
    LaunchedEffect(state.snackbarMessage) {
        state.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    // Back button handling
    BackHandler(enabled = state.currentTab != 0) {
        viewModel.setTab(0)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("main_scaffold"),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = ImperialGoldLight,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = ImperialCrimson,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "CỔ PHỤC VIỆT",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Gìn giữ & Tỏa sáng tinh hoa",
                                fontSize = 10.sp,
                                color = ImperialGoldLight
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.showCulturalDialog(
                                title = "Về Ứng Dụng Cổ Phục Việt",
                                description = "Không gian tương tác và bảo tồn di sản trang phục truyền thống Việt Nam. Khám phá chuẩn mực lịch sử và tự do sáng tạo phối đồ.",
                                dynasty = "Di sản văn hóa"
                            )
                        },
                        modifier = Modifier.testTag("app_info_top_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Thông tin",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ImperialCrimson,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("main_navigation_bar"),
                containerColor = MaterialTheme.colorScheme.surface,
                windowInsets = WindowInsets.navigationBars
            ) {
                NavigationBarItem(
                    selected = state.currentTab == 0,
                    onClick = { viewModel.setTab(0) },
                    icon = { Icon(Icons.Default.Explore, contentDescription = "Khám phá") },
                    label = { Text("Khám phá", fontSize = 11.sp) },
                    modifier = Modifier.testTag("nav_tab_discovery")
                )
                NavigationBarItem(
                    selected = state.currentTab == 1,
                    onClick = { viewModel.setTab(1) },
                    icon = { Icon(Icons.Default.Checkroom, contentDescription = "Phòng thử đồ") },
                    label = { Text("Phòng thử", fontSize = 11.sp) },
                    modifier = Modifier.testTag("nav_tab_fitting_room")
                )
                NavigationBarItem(
                    selected = state.currentTab == 2,
                    onClick = { viewModel.setTab(2) },
                    icon = { Icon(Icons.Default.Bookmarks, contentDescription = "Bộ sưu tập") },
                    label = { Text("Bộ phối", fontSize = 11.sp) },
                    modifier = Modifier.testTag("nav_tab_saved")
                )
                NavigationBarItem(
                    selected = state.currentTab == 3,
                    onClick = { viewModel.setTab(3) },
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = "Điển tích") },
                    label = { Text("Điển tích", fontSize = 11.sp) },
                    modifier = Modifier.testTag("nav_tab_archive")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state.currentTab) {
                0 -> DiscoveryScreen(
                    state = state,
                    onSelectOccasion = { viewModel.selectOccasionFilter(it) },
                    onSelectRegion = { viewModel.selectRegionFilter(it) },
                    onSelectStyle = { viewModel.selectStyleFilter(it) },
                    onTryCostume = { costume ->
                        viewModel.selectCostume(costume)
                        viewModel.setTab(1)
                    },
                    onShowCulturalInfo = { title, desc, dynasty ->
                        viewModel.showCulturalDialog(title, desc, dynasty)
                    }
                )
                1 -> FittingRoomScreen(
                    state = state,
                    onSelectCostume = { viewModel.selectCostume(it) },
                    onSelectColor = { viewModel.selectColor(it) },
                    onSelectPattern = { viewModel.selectPattern(it) },
                    onToggleAccessory = { viewModel.toggleAccessory(it) },
                    onSaveOutfit = { viewModel.saveCurrentOutfit() },
                    onShowCulturalInfo = { title, desc, dynasty ->
                        viewModel.showCulturalDialog(title, desc, dynasty)
                    },
                    onSelectEvent = { viewModel.selectEvent(it) },
                    onSelectWeather = { viewModel.selectWeather(it) }
                )
                2 -> SavedOutfitsScreen(
                    state = state,
                    onLoadOutfit = {
                        viewModel.loadSavedOutfitToFittingRoom(it)
                        viewModel.setTab(1)
                    },
                    onDeleteOutfit = { viewModel.deleteSavedOutfit(it) },
                    onExportCard = { outfit ->
                        com.example.cophuc.ui.components.CostumeCardBitmapGenerator.shareCostumeCard(context, outfit)
                    },
                    onCreateLookbook = { title, theme, desc, coverStyle, outfitIds ->
                        viewModel.createLookbook(title, theme, desc, coverStyle, outfitIds)
                    },
                    onDeleteLookbook = { viewModel.deleteLookbook(it) }
                )
                3 -> CulturalArchiveScreen(
                    onShowDetail = { title, desc, dynasty ->
                        viewModel.showCulturalDialog(title, desc, dynasty)
                    }
                )
            }

            // Cultural Dialog Popup (< 50 words)
            CulturalContextDialog(
                dialogData = state.culturalDialog,
                onDismiss = { viewModel.dismissCulturalDialog() }
            )
        }
    }
}
