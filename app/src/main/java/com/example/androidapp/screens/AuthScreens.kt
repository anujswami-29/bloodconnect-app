package com.example.androidapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.example.androidapp.state.AuthViewModel
import com.example.androidapp.state.UserRole
import com.example.androidapp.ui.components.RakhtSetuBackground
import com.example.androidapp.ui.components.RakhtSetuWordmark

// ---------------- LOGIN SCREEN ---------------- //

@Composable
fun LoginScreen(
    auth: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    RakhtSetuBackground {
        AuthCardLayout(title = "Login") {

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            // Password Field
            PasswordField(
                value = password,
                onValueChange = { password = it },
                showPassword = showPassword,
                onToggle = { showPassword = !showPassword }
            )

            ErrorText(auth.errorMessage)

            // Login Button
            Button(
                onClick = {
                    auth.login(email, password)
                    if (auth.isLoggedIn) onLoginSuccess()
                },
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("Log in")
            }

            SwitchAuthText(
                text = "New here?",
                buttonText = "Create an account",
                onClick = onGoToRegister
            )
        }
    }
}

// ---------------- REGISTER SCREEN ---------------- //

@Composable
fun RegisterScreen(
    auth: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onGoToLogin: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var role by remember { mutableStateOf(UserRole.DONOR) }
    var showPass by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }

    RakhtSetuBackground {
        AuthCardLayout(title = "Create account") {

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            PasswordField(password, { password = it }, showPass) { showPass = !showPass }
            PasswordField(confirmPassword, { confirmPassword = it }, showConfirm) { showConfirm = !showConfirm }

            Text("Select Role", style = MaterialTheme.typography.labelLarge)
            RoleChips(role) { role = it }

            ErrorText(auth.errorMessage)

            Button(
                onClick = {
                    auth.register(email, password, confirmPassword, role)
                    if (auth.isLoggedIn) onRegisterSuccess()
                },
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("Sign up")
            }

            SwitchAuthText(
                text = "Already have an account?",
                buttonText = "Log in",
                onClick = onGoToLogin
            )
        }
    }
}

// ---------------- REUSABLE UI COMPONENTS ---------------- //

@Composable
fun AuthCardLayout(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        RakhtSetuWordmark()
        Spacer(Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                content = {
                    Text(title, style = MaterialTheme.typography.titleLarge)
                    content()
                }
            )
        }
    }
}

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    showPassword: Boolean,
    onToggle: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Password") },
        leadingIcon = { Icon(Icons.Default.Lock, null) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onToggle) {
                Icon(
                    imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = null
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}

@Composable
fun ErrorText(error: String?) {
    if (error != null) {
        Text(text = error, color = MaterialTheme.colorScheme.error)
    }
}

@Composable
fun SwitchAuthText(text: String, buttonText: String, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text)
        Spacer(Modifier.width(8.dp))
        TextButton(onClick = onClick) { Text(buttonText) }
    }
}

// ---------------- ROLE CHIPS ---------------- //

@Composable
fun RoleChips(selected: UserRole, onSelect: (UserRole) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        RoleChip("Donor", UserRole.DONOR, selected, onSelect)
        RoleChip("Volunteer", UserRole.VOLUNTEER, selected, onSelect)
        RoleChip("Hospital", UserRole.HOSPITAL, selected, onSelect)
        RoleChip("Admin", UserRole.ADMIN, selected, onSelect)
    }
}

@Composable
fun RoleChip(label: String, role: UserRole, selected: UserRole, onSelect: (UserRole) -> Unit) {
    FilterChip(
        selected = selected == role,
        onClick = { onSelect(role) },
        label = { Text(label) }
    )
}
