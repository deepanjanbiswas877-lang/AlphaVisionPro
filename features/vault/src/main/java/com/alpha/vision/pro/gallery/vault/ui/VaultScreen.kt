package com.alpha.vision.pro.gallery.vault.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.alpha.vision.pro.gallery.designsystem.components.AlphaTopBar
import com.alpha.vision.pro.gallery.vault.biometric.BiometricHelper
import com.alpha.vision.pro.gallery.vault.viewmodel.VaultEvent
import com.alpha.vision.pro.gallery.vault.viewmodel.VaultViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultScreen(
    onBack   : () -> Unit,
    viewModel: VaultViewModel = hiltViewModel()
) {
    val state   by viewModel.state.collectAsState()
    val context  = LocalContext.current
    val snackbar = remember { SnackbarHostState() }

    // Trigger biometric on enter
    LaunchedEffect(Unit) {
        if (!state.isAuthenticated) {
            viewModel.onEvent(VaultEvent.Authenticate)
            BiometricHelper.authenticate(
                activity   = context as FragmentActivity,
                onSuccess  = { viewModel.onEvent(VaultEvent.AuthSuccess) },
                onFailure  = { msg -> viewModel.onEvent(VaultEvent.AuthFailure(msg)) }
            )
        }
    }

    LaunchedEffect(state.snackMessage) {
        state.snackMessage?.let {
            snackbar.showSnackbar(it)
            viewModel.onEvent(VaultEvent.DismissSnack)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            AlphaTopBar(
                title          = "Vault",
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier         = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                !state.isAuthenticated -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.Lock, contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint     = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(16.dp))
                        Text("Biometric authentication required",
                            style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = {
                            viewModel.onEvent(VaultEvent.Authenticate)
                            BiometricHelper.authenticate(
                                activity  = context as FragmentActivity,
                                onSuccess = { viewModel.onEvent(VaultEvent.AuthSuccess) },
                                onFailure = { msg -> viewModel.onEvent(VaultEvent.AuthFailure(msg)) }
                            )
                        }) { Text("Authenticate") }
                    }
                }
                state.mediaItems.isEmpty() -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.LockOpen, contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint     = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(12.dp))
                        Text("Vault is empty", style = MaterialTheme.typography.bodyLarge)
                    }
                }
                else -> {
                    LazyVerticalGrid(
                        columns             = GridCells.Fixed(3),
                        contentPadding      = PaddingValues(2.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalArrangement   = Arrangement.spacedBy(2.dp),
                        modifier            = Modifier.fillMaxSize()
                    ) {
                        items(state.mediaItems, key = { it.id }) { item ->
                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(MaterialTheme.shapes.extraSmall)
                            ) {
                                AsyncImage(
                                    model              = item.uri,
                                    contentDescription = item.displayName,
                                    contentScale       = ContentScale.Crop,
                                    modifier           = Modifier.fillMaxSize()
                                )
                                IconButton(
                                    onClick  = { viewModel.onEvent(VaultEvent.RemoveItem(item.id)) },
                                    modifier = Modifier.align(Alignment.TopEnd)
                                ) {
                                    Icon(Icons.Filled.LockOpen, "Remove from vault",
                                        tint = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
