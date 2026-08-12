package com.example.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

data class PermissionItem(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val permissions: List<String>
)

@Composable
fun RealtimePermissionsCard(
    modifier: Modifier = Modifier,
    onAllPermissionsGranted: () -> Unit = {}
) {
    val context = LocalContext.current

    val permissionItems = remember {
        val items = mutableListOf<PermissionItem>()

        // 1. Notification Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            items.add(
                PermissionItem(
                    id = "notifications",
                    title = "Push Notifications",
                    description = "Receive instant alerts for case file updates, encrypted DM messages, and mystery responses.",
                    icon = Icons.Filled.Notifications,
                    permissions = listOf(Manifest.permission.POST_NOTIFICATIONS)
                )
            )
        }

        // 2. Gallery / Photos Permission
        val galleryPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        items.add(
            PermissionItem(
                id = "gallery",
                title = "Gallery & Media Access",
                description = "Upload evidence photos, mystery case files, and citizenship verification documents.",
                icon = Icons.Filled.PhotoLibrary,
                permissions = galleryPermissions
            )
        )

        // 3. Camera Permission
        items.add(
            PermissionItem(
                id = "camera",
                title = "Camera Capture",
                description = "Take live photos of incident locations and crime scene evidence board items.",
                icon = Icons.Filled.CameraAlt,
                permissions = listOf(Manifest.permission.CAMERA)
            )
        )

        // 4. Microphone Permission
        items.add(
            PermissionItem(
                id = "microphone",
                title = "Microphone & Voice Posts",
                description = "Record spoken audio posts, confidential voice notes, and audio stories.",
                icon = Icons.Filled.Mic,
                permissions = listOf(Manifest.permission.RECORD_AUDIO)
            )
        )

        // 5. Location / Citizenship Verification Permission
        items.add(
            PermissionItem(
                id = "location",
                title = "Real-time Location & Citizenship",
                description = "Verify local civic context, region-based anonymity rules, and authentic incident mapping.",
                icon = Icons.Filled.LocationOn,
                permissions = listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        )

        items
    }

    // State map for granted status of each item
    val permissionStates = remember {
        mutableStateMapOf<String, Boolean>().apply {
            permissionItems.forEach { item ->
                this[item.id] = checkPermissionsGranted(context, item.permissions)
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        // Update states after permission prompt
        permissionItems.forEach { item ->
            permissionStates[item.id] = checkPermissionsGranted(context, item.permissions)
        }
    }

    fun requestAllPending() {
        val pendingList = permissionItems
            .filter { permissionStates[it.id] != true }
            .flatMap { it.permissions }
            .distinct()
        if (pendingList.isNotEmpty()) {
            launcher.launch(pendingList.toTypedArray())
        }
    }

    val allGranted = permissionItems.all { permissionStates[it.id] == true }

    LaunchedEffect(allGranted) {
        if (allGranted) {
            onAllPermissionsGranted()
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Shield,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Essential Permissions & Verification",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Grant permissions to enable notifications, gallery, voice notes & real-time location context.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            permissionItems.forEach { item ->
                val isGranted = permissionStates[item.id] == true
                PermissionRow(
                    item = item,
                    isGranted = isGranted,
                    onRequestPermission = {
                        launcher.launch(item.permissions.toTypedArray())
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (!allGranted) {
                Button(
                    onClick = { requestAllPending() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("request_all_permissions_btn"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Grant All Required Permissions in Real-time")
                }
            } else {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "All realtime permissions & verifications granted!",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PermissionRow(
    item: PermissionItem,
    isGranted: Boolean,
    onRequestPermission: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (isGranted) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = if (isGranted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    if (isGranted) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Granted",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (!isGranted) {
                OutlinedButton(
                    onClick = onRequestPermission,
                    modifier = Modifier.testTag("perm_btn_${item.id}"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Grant", style = MaterialTheme.typography.labelMedium)
                }
            } else {
                Text(
                    text = "Active",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

private fun checkPermissionsGranted(context: Context, permissions: List<String>): Boolean {
    return permissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}
