package com.omran.quran.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omran.quran.R
import com.omran.quran.ui.components.DarkGlassBackground
import com.omran.quran.ui.components.ErrorBanner
import com.omran.quran.ui.components.OmranCard
import com.omran.quran.ui.components.OmranField
import com.omran.quran.ui.components.PrimaryButton
import com.omran.quran.ui.theme.GlassMuted
import com.omran.quran.ui.theme.GlassSilver
import com.omran.quran.ui.theme.GlassWhite

@Composable
fun LoginScreen(
    onRegister: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AuthScaffold(title = "معهد المقرئ", subtitle = "منظومة الإدارة القرآنية والتحفيظ") {
        ErrorBanner(state.error) { viewModel.update { it.copy(error = null) } }
        OmranField(state.email, { v -> viewModel.update { it.copy(email = v) } }, "البريد الإلكتروني", keyboardType = KeyboardType.Email)
        Spacer(Modifier.height(12.dp))
        OmranField(state.password, { v -> viewModel.update { it.copy(password = v) } }, "كلمة المرور", keyboardType = KeyboardType.Password, isPassword = true)
        Spacer(Modifier.height(20.dp))
        PrimaryButton("تسجيل الدخول", onClick = viewModel::signIn, loading = state.loading)
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onRegister, modifier = Modifier.fillMaxWidth()) {
            Text("إنشاء حساب مدير المعهد", color = GlassSilver, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AuthScaffold(title = "تسجيل مدير المعهد", subtitle = "إنشاء حساب جديد للمدير وإدارة المعهد") {
        ErrorBanner(state.error) { viewModel.update { it.copy(error = null) } }
        OmranField(state.name, { v -> viewModel.update { it.copy(name = v) } }, "الاسم الكامل للمدير")
        Spacer(Modifier.height(12.dp))
        OmranField(state.email, { v -> viewModel.update { it.copy(email = v) } }, "البريد الإلكتروني", keyboardType = KeyboardType.Email)
        Spacer(Modifier.height(12.dp))
        OmranField(state.password, { v -> viewModel.update { it.copy(password = v) } }, "كلمة المرور", keyboardType = KeyboardType.Password, isPassword = true)
        Spacer(Modifier.height(12.dp))
        OmranField(state.instituteName, { v -> viewModel.update { it.copy(instituteName = v) } }, "اسم المعهد")
        Spacer(Modifier.height(20.dp))
        PrimaryButton("إنشاء حساب المدير", onClick = viewModel::register, loading = state.loading)
        TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("عودة لتسجيل الدخول", color = GlassMuted) }
    }
}

@Composable
private fun AuthScaffold(title: String, subtitle: String, content: @Composable () -> Unit) {
    DarkGlassBackground {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.08f))
                    .border(1.dp, Color.White.copy(alpha = 0.22f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().padding(6.dp),
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(title, style = MaterialTheme.typography.displaySmall, color = GlassWhite, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = GlassMuted)
            Spacer(Modifier.height(24.dp))
            OmranCard(Modifier.fillMaxWidth()) { content() }
        }
    }
}
