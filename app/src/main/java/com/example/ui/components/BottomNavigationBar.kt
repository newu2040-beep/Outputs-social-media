package com.example.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.ui.Screen

@Composable
fun OutputsBottomNavigationBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.navigationBarsPadding(),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        val isHome = currentScreen is Screen.Home
        NavigationBarItem(
            selected = isHome,
            onClick = { onNavigate(Screen.Home) },
            icon = {
                Icon(
                    imageVector = if (isHome) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") },
            modifier = Modifier.testTag("nav_home"),
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        val isExplore = currentScreen is Screen.Explore
        NavigationBarItem(
            selected = isExplore,
            onClick = { onNavigate(Screen.Explore) },
            icon = {
                Icon(
                    imageVector = if (isExplore) Icons.Filled.Explore else Icons.Outlined.Explore,
                    contentDescription = "Explore"
                )
            },
            label = { Text("Explore") },
            modifier = Modifier.testTag("nav_explore")
        )

        val isCreate = currentScreen is Screen.CreateOutput
        NavigationBarItem(
            selected = isCreate,
            onClick = { onNavigate(Screen.CreateOutput) },
            icon = {
                Icon(
                    imageVector = if (isCreate) Icons.Filled.AddCircle else Icons.Outlined.AddCircleOutline,
                    contentDescription = "Create Output"
                )
            },
            label = { Text("Output") },
            modifier = Modifier.testTag("nav_create")
        )

        val isGroups = currentScreen is Screen.Groups || currentScreen is Screen.GroupDetail
        NavigationBarItem(
            selected = isGroups,
            onClick = { onNavigate(Screen.Groups) },
            icon = {
                Icon(
                    imageVector = if (isGroups) Icons.Filled.Group else Icons.Outlined.Group,
                    contentDescription = "Groups"
                )
            },
            label = { Text("Groups") },
            modifier = Modifier.testTag("nav_groups")
        )

        val isInbox = currentScreen is Screen.Inbox || currentScreen is Screen.ChatDetail
        NavigationBarItem(
            selected = isInbox,
            onClick = { onNavigate(Screen.Inbox) },
            icon = {
                Icon(
                    imageVector = if (isInbox) Icons.Filled.Inbox else Icons.Outlined.Inbox,
                    contentDescription = "Inbox"
                )
            },
            label = { Text("Inbox") },
            modifier = Modifier.testTag("nav_inbox")
        )

        val isProfile = currentScreen is Screen.Profile
        NavigationBarItem(
            selected = isProfile,
            onClick = { onNavigate(Screen.Profile) },
            icon = {
                Icon(
                    imageVector = if (isProfile) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "Profile"
                )
            },
            label = { Text("Profile") },
            modifier = Modifier.testTag("nav_profile")
        )
    }
}
