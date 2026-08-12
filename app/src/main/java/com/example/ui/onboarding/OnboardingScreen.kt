package com.example.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserProfileEntity
import com.example.data.model.PrivacyMode
import com.example.ui.MainViewModel
import com.example.ui.components.FirebaseAuthCard
import com.example.ui.components.RealtimePermissionsCard
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    currentProfile: UserProfileEntity?,
    viewModel: MainViewModel? = null,
    onCompleteOnboarding: (UserProfileEntity) -> Unit
) {
    var step by remember { mutableIntStateOf(1) } // 1: Welcome & 18+, 2: DOB & Citizenship, 3: Privacy Mode, 4: Interests

    var day by remember { mutableStateOf((currentProfile?.dobDay ?: 14).toString()) }
    var month by remember { mutableStateOf((currentProfile?.dobMonth ?: 5).toString()) }
    var year by remember { mutableStateOf((currentProfile?.dobYear ?: 1998).toString()) }

    var selectedCountry by remember { mutableStateOf(currentProfile?.citizenshipCountry ?: "United States") }
    var countryExpanded by remember { mutableStateOf(false) }

    var selectedPrivacyMode by remember { mutableStateOf(currentProfile?.privacyMode ?: PrivacyMode.ANONYMOUS_NUMBER) }
    var publicUsername by remember { mutableStateOf(currentProfile?.publicUsername ?: "ShadowWriter") }

    var ageErrorMessage by remember { mutableStateOf<String?>(null) }

    val countries = listOf("United States", "Nepal", "India", "United Kingdom", "Canada", "Australia", "Germany", "Japan", "Brazil", "France")

    val interests = listOf("Mystery", "Real Incident", "Horror", "Stories", "Voice Stories", "Unexplained", "Cryptids", "History", "Civic Privacy", "Technology", "Opinions")
    var selectedInterests by remember { mutableStateOf(setOf("Mystery", "Real Incident", "Stories")) }

    fun calculateAge(d: Int, m: Int, y: Int): Int {
        val today = Calendar.getInstance()
        var age = today.get(Calendar.YEAR) - y
        if (today.get(Calendar.MONTH) + 1 < m || (today.get(Calendar.MONTH) + 1 == m && today.get(Calendar.DAY_OF_MONTH) < d)) {
            age--
        }
        return age
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header Logo
            Surface(
                modifier = Modifier.size(72.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.Shield,
                        contentDescription = "Outputs Logo",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "OUTPUTS",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = 2.sp
            )

            Text(
                text = "Say it. Share it. Solve it.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(24.dp))

            when (step) {
                1 -> {
                    // Step 1: 18+ Notice & Welcome
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Security,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Mandatory 18+ Gate",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Outputs is an adults-only privacy social platform for sharing real stories, mystery solving, and civic discussions. All members must verify 18+ eligibility.",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            if (viewModel != null) {
                                FirebaseAuthCard(viewModel = viewModel)
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            Button(
                                onClick = { step = 2 },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("onboarding_continue_18_btn"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("I am 18+ — Proceed to Verification")
                            }
                        }
                    }
                }

                2 -> {
                    // Step 2: Date of Birth & Citizenship
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text(
                                text = "Date of Birth Verification",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = "Your age is calculated automatically. DOB remains private and encrypted.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = day,
                                    onValueChange = { day = it },
                                    label = { Text("Day") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("dob_day_input")
                                )
                                OutlinedTextField(
                                    value = month,
                                    onValueChange = { month = it },
                                    label = { Text("Month") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("dob_month_input")
                                )
                                OutlinedTextField(
                                    value = year,
                                    onValueChange = { year = it },
                                    label = { Text("Year") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier
                                        .weight(1.2f)
                                        .testTag("dob_year_input")
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Citizenship Country",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            ExposedDropdownMenuBox(
                                expanded = countryExpanded,
                                onExpandedChange = { countryExpanded = !countryExpanded }
                            ) {
                                OutlinedTextField(
                                    value = selectedCountry,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = countryExpanded) },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                )
                                ExposedDropdownMenu(
                                    expanded = countryExpanded,
                                    onDismissRequest = { countryExpanded = false }
                                ) {
                                    countries.forEach { c ->
                                        DropdownMenuItem(
                                            text = { Text(c) },
                                            onClick = {
                                                selectedCountry = c
                                                countryExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            if (ageErrorMessage != null) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = ageErrorMessage!!,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            RealtimePermissionsCard()

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    val d = day.toIntOrNull() ?: 1
                                    val m = month.toIntOrNull() ?: 1
                                    val y = year.toIntOrNull() ?: 2000
                                    val computedAge = calculateAge(d, m, y)

                                    if (computedAge < 18) {
                                        ageErrorMessage = "Outputs is available only to users aged 18 and above."
                                    } else {
                                        ageErrorMessage = null
                                        step = 3
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("verify_age_btn"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Verify 18+ Eligibility")
                            }
                        }
                    }
                }

                3 -> {
                    // Step 3: Privacy Mode Setup
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text(
                                text = "Choose Public Identity Mode",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = "How should your contributions appear to the community?",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            PrivacyMode.values().forEach { mode ->
                                val isSelected = selectedPrivacyMode == mode
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable { selectedPrivacyMode = mode },
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = if (isSelected) Icons.Filled.Check else Icons.Filled.Lock,
                                            contentDescription = null,
                                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = mode.displayName,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = mode.description,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }

                            if (selectedPrivacyMode == PrivacyMode.REAL_PROFILE || selectedPrivacyMode == PrivacyMode.PSEUDONYM) {
                                Spacer(modifier = Modifier.height(12.dp))
                                OutlinedTextField(
                                    value = publicUsername,
                                    onValueChange = { publicUsername = it },
                                    label = { Text("Display Name") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = { step = 4 },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("privacy_next_btn"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Continue")
                            }
                        }
                    }
                }

                4 -> {
                    // Step 4: Interests & Finish
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text(
                                text = "Select Interests",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                interests.forEach { topic ->
                                    val selected = selectedInterests.contains(topic)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(
                                                if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                            )
                                            .clickable {
                                                selectedInterests = if (selected) {
                                                    selectedInterests - topic
                                                } else {
                                                    selectedInterests + topic
                                                }
                                            }
                                            .padding(horizontal = 14.dp, vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = topic,
                                            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    val d = day.toIntOrNull() ?: 14
                                    val m = month.toIntOrNull() ?: 5
                                    val y = year.toIntOrNull() ?: 1999

                                    val newProfile = UserProfileEntity(
                                        id = 1,
                                        dobDay = d,
                                        dobMonth = m,
                                        dobYear = y,
                                        citizenshipCountry = selectedCountry,
                                        isAgeVerified = true,
                                        isIdentityVerified = true,
                                        publicUsername = publicUsername,
                                        privacyMode = selectedPrivacyMode,
                                        anonymousNumberCode = (1000..9999).random()
                                    )
                                    onCompleteOnboarding(newProfile)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("enter_outputs_btn"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Enter Outputs")
                            }
                        }
                    }
                }
            }
        }
    }
}
