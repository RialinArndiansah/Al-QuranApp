package com.example.quranapp.ui.screens

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quranapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val indonesiaFont = FontFamily(Font(R.font.indonesia))
    val headerFont = FontFamily(Font(R.font.headerfont))

    var name by remember { mutableStateOf(sharedPrefs.getString("name", "") ?: "") }
    var email by remember { mutableStateOf(sharedPrefs.getString("email", "") ?: "") }
    var dob by remember { mutableStateOf(sharedPrefs.getString("dob", "DD/MM/YYYY") ?: "DD/MM/YYYY") }
    var country by remember { mutableStateOf(sharedPrefs.getString("country", "Indonesia") ?: "Indonesia") }
    var countryExpanded by remember { mutableStateOf(false) }
    val countries = listOf("Indonesia", "Malaysia", "Amerika Serikat", "Inggris", "Mesir", "Arab Saudi")

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val calendar = Calendar.getInstance()
    val datePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            dob = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
            sharedPrefs.edit().putString("dob", dob).apply()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header with background image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.headerbaground),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "PROFIL PENGGUNA",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(fontFamily = headerFont)
                    )
                }
                IconButton(
                    onClick = {
                        googleSignInClient.signOut().addOnCompleteListener {
                            sharedPrefs.edit().clear().apply()
                            navController.navigate("auth") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Logout",
                        tint = Color.White
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.9f))
            ) {
                Image(
                    painter = painterResource(R.drawable.profile_placeholder),
                    contentDescription = "Profile Photo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { },
                        label = { Text("Nama", fontFamily = indonesiaFont) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        readOnly = true,
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color(0xFF00796B),
                            disabledLabelColor = Color(0xFF00796B),
                            disabledBorderColor = Color(0xFF00796B)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { },
                        label = { Text("Email", fontFamily = indonesiaFont) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        readOnly = true,
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color(0xFF00796B),
                            disabledLabelColor = Color(0xFF00796B),
                            disabledBorderColor = Color(0xFF00796B)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = dob,
                        onValueChange = {},
                        label = { Text("Tanggal Lahir", fontFamily = indonesiaFont) },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { datePicker.show() },
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = "Pick Date") },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFF00796B),
                            unfocusedTextColor = Color(0xFF00796B),
                            focusedLabelColor = Color(0xFF00796B),
                            unfocusedLabelColor = Color(0xFF00796B),
                            focusedBorderColor = Color(0xFF00796B),
                            unfocusedBorderColor = Color(0xFF00796B)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ExposedDropdownMenuBox(
                        expanded = countryExpanded,
                        onExpandedChange = { countryExpanded = !countryExpanded }
                    ) {
                        OutlinedTextField(
                            value = country,
                            onValueChange = {},
                            label = { Text("Negara", fontFamily = indonesiaFont) },
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = countryExpanded) },
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color(0xFF00796B),
                                unfocusedTextColor = Color(0xFF00796B),
                                focusedLabelColor = Color(0xFF00796B),
                                unfocusedLabelColor = Color(0xFF00796B),
                                focusedBorderColor = Color(0xFF00796B),
                                unfocusedBorderColor = Color(0xFF00796B)
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = countryExpanded,
                            onDismissRequest = { countryExpanded = false }
                        ) {
                            countries.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option, fontFamily = indonesiaFont) },
                                    onClick = {
                                        country = option
                                        countryExpanded = false
                                        sharedPrefs.edit().putString("country", option).apply()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}