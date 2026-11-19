package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DemoShopTypePickerDialog
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassOutlinedButton
import com.example.ui.components.LanguageToggle
import com.example.ui.components.VividButton
import com.example.ui.theme.AlertRed
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VividGreen
import com.example.ui.viewmodel.AuthViewModel
import com.example.util.AppLanguage
import com.example.util.ShopLocale

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isBusy by authViewModel.isBusy.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()
    val successMessage by authViewModel.successMessage.collectAsState()

    val currentLanguage by authViewModel.currentLanguage.collectAsState()
    var showDemoShopTypePicker by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .statusBarsPadding()
            .displayCutoutPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        // Language switcher in top corner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.End
        ) {
            LanguageToggle(
                currentLanguage = currentLanguage,
                onToggle = { authViewModel.toggleLanguage() }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 40.dp)
                .widthIn(max = 520.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Branding Icon & Title
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color(0x265DD62C), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Storefront,
                    contentDescription = null,
                    tint = VividGreen,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = ShopLocale.get("app_title", currentLanguage),
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = ShopLocale.get("app_subtitle", currentLanguage),
                color = TextSecondary,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Main Auth Glass Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                testTag = "auth_card"
            ) {
                Column {
                    // Instant Demo Mode Button -> Opens Shop Type Picker
                    GlassOutlinedButton(
                        text = if (currentLanguage == AppLanguage.BN) "⚡ ডেমো স্টোর দেখুন" else "⚡ Explore Demo Store",
                        onClick = {
                            authViewModel.clearMessages()
                            showDemoShopTypePicker = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "explore_demo_store_btn"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Google Sign-In Primary Action
                    VividButton(
                        text = ShopLocale.get("sign_in_with_google", currentLanguage),
                        onClick = {
                            authViewModel.clearMessages()
                            authViewModel.signInWithGoogle(context)
                        },
                        isLoading = isBusy,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "google_sign_in_btn"
                    )

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = errorMessage ?: "",
                            color = AlertRed,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }

                    if (successMessage != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = successMessage ?: "",
                            color = VividGreen,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        if (showDemoShopTypePicker) {
            DemoShopTypePickerDialog(
                currentLanguage = currentLanguage,
                onSelectType = { chosenType ->
                    showDemoShopTypePicker = false
                    authViewModel.launchDemoMode(chosenType)
                },
                onDismiss = { showDemoShopTypePicker = false }
            )
        }
    }
}
