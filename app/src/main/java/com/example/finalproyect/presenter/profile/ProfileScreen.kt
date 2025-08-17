package com.example.finalproyect.presenter.profile

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.finalproyect.presenter.navigator.AppDestination
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.unit.sp


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // Estado del ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.errorMessage, uiState.successMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(
                message = it,
                actionLabel = "Cerrar",
                duration = SnackbarDuration.Long
            )
            viewModel.clearMessages()
        }
        uiState.successMessage?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            viewModel.clearMessages()
        }
    }

    // Launcher para seleccionar imagen
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onImageUriChange(uri)
    }

    // Estado del DatePicker
    var showBirthdayPicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.birthday.atStartOfDay(ZoneId.systemDefault())
            .toInstant().toEpochMilli()
    )

    // Estados de animación para las secciones
    val personalInfoExpanded by remember { mutableStateOf(true) }
    val contactInfoExpanded by remember { mutableStateOf(false) }
    val securityExpanded by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { snackbarData ->
                    Snackbar(
                        snackbarData = snackbarData,
                        containerColor = MaterialTheme.colorScheme.inverseSurface,
                        contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                        actionColor = MaterialTheme.colorScheme.inversePrimary,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mi Perfil",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate(AppDestination.Home) },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    AnimatedContent(
                        targetState = uiState.isLoading,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(300)) togetherWith
                                    fadeOut(animationSpec = tween(300))
                        },
                        label = "save_button_animation"
                    ) { isLoading ->
                        if (isLoading) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        } else {
                            IconButton(
                                onClick = { viewModel.saveProfile() },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Save,
                                    contentDescription = "Guardar",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Header con foto de perfil
            item {
                ProfileHeader(
                    name = uiState.name,
                    lastName = uiState.lastName,
                    imageUri = uiState.imageUri,
                    onImageClick = { imagePickerLauncher.launch("image/*") }
                )
            }

            // Información Personal
            item {
                Spacer(modifier = Modifier.height(24.dp))
                PersonalInfoSection(
                    uiState = uiState,
                    viewModel = viewModel,
                    isExpanded = personalInfoExpanded,
                    onDatePickerRequest = { showBirthdayPicker = true }
                )
            }

            // Información de Contacto
            item {
                Spacer(modifier = Modifier.height(16.dp))
                ContactInfoSection(
                    uiState = uiState,
                    viewModel = viewModel,
                    isExpanded = contactInfoExpanded
                )
            }

            // Seguridad
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SecuritySection(
                    viewModel = viewModel,
                    isExpanded = securityExpanded,
                    isLoading = uiState.isLoading
                )
            }

            // Acciones principales
            item {
                Spacer(modifier = Modifier.height(32.dp))
                ActionButtons(
                    viewModel = viewModel,
                    isLoading = uiState.isLoading
                )
            }
        }
    }

    // Diálogos
    ProfileDialogs(
        uiState = uiState,
        viewModel = viewModel,
        showBirthdayPicker = showBirthdayPicker,
        datePickerState = datePickerState,
        onDatePickerDismiss = { showBirthdayPicker = false },
        onDateSelected = { localDate ->
            viewModel.onBirthdayChange(localDate)
            showBirthdayPicker = false
        }
    )
}

@Composable
private fun ProfileHeader(
    name: String,
    lastName: String,
    imageUri: Uri?,
    onImageClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Foto de perfil con animación
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                        )
                    )
                )
                .border(
                    width = 3.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    ),
                    shape = CircleShape
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false, radius = 70.dp)
                ) { onImageClick() },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )
            }

            // Botón de cámara mejorado
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-8).dp, y = (-8).dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.surface,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = "Cambiar foto",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Nombre con estilo mejorado
        Text(
            text = "$name $lastName",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            letterSpacing = 0.5.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Indicador de estado
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ) {
            Text(
                text = "Perfil activo",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun PersonalInfoSection(
    uiState: ProfileUiState,
    viewModel: ProfileViewModel,
    isExpanded: Boolean,
    onDatePickerRequest: () -> Unit
) {
    var expanded by remember { mutableStateOf(isExpanded) }

    ProfileSection(
        title = "Información Personal",
        icon = Icons.Outlined.Person,
        isExpanded = expanded,
        onExpandToggle = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Campo Nombre
            EnhancedTextField(
                value = uiState.name,
                onValueChange = { viewModel.onNameChange(it) },
                label = "Nombre",
                leadingIcon = Icons.Outlined.Badge,
                isError = uiState.name.isBlank(),
                errorMessage = if (uiState.name.isBlank()) "El nombre es obligatorio" else null
            )

            // Campo Apellido
            EnhancedTextField(
                value = uiState.lastName,
                onValueChange = { viewModel.onLastNameChange(it) },
                label = "Apellido",
                leadingIcon = Icons.Outlined.Badge,
                isError = uiState.lastName.isBlank(),
                errorMessage = if (uiState.lastName.isBlank()) "El apellido es obligatorio" else null
            )

            // Campo Fecha de nacimiento
            EnhancedTextField(
                value = uiState.birthday.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                onValueChange = { },
                label = "Fecha de nacimiento",
                leadingIcon = Icons.Outlined.DateRange,
                readOnly = true,
                trailingIcon = {
                    IconButton(
                        onClick = onDatePickerRequest,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.EditCalendar,
                            contentDescription = "Seleccionar fecha",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                isError = uiState.birthday.isAfter(LocalDate.now().minusYears(18)),
                errorMessage = if (uiState.birthday.isAfter(LocalDate.now().minusYears(18)))
                    "Debes ser mayor de 18 años" else null
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ContactInfoSection(
    uiState: ProfileUiState,
    viewModel: ProfileViewModel,
    isExpanded: Boolean
) {
    var expanded by remember { mutableStateOf(isExpanded) }

    ProfileSection(
        title = "Información de Contacto",
        icon = Icons.Outlined.ContactPage,
        isExpanded = expanded,
        onExpandToggle = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Campo Teléfono
            val phoneError = uiState.phone.isBlank() ||
                    (uiState.phone.isNotBlank() && !uiState.phone.matches("^[+]?[0-9]{8,15}$".toRegex()))

            EnhancedTextField(
                value = uiState.phone,
                onValueChange = { viewModel.onPhoneChange(it) },
                label = "Teléfono",
                leadingIcon = Icons.Outlined.Phone,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = phoneError,
                errorMessage = when {
                    uiState.phone.isBlank() -> "El teléfono es obligatorio"
                    phoneError -> "Formato de teléfono inválido (8-15 dígitos)"
                    else -> null
                }
            )

            // Campo Email (solo lectura)
            EnhancedTextField(
                value = uiState.email,
                onValueChange = { },
                label = "Correo electrónico",
                leadingIcon = Icons.Outlined.Email,
                readOnly = true,
                enabled = false,
                supportingText = "El correo electrónico no se puede modificar"
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun SecuritySection(
    viewModel: ProfileViewModel,
    isExpanded: Boolean,
    isLoading: Boolean
) {
    var expanded by remember { mutableStateOf(isExpanded) }

    ProfileSection(
        title = "Seguridad",
        icon = Icons.Outlined.Security,
        isExpanded = expanded,
        onExpandToggle = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Button(
                onClick = { viewModel.onChangePasswordRequested() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Cambiar contraseña",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ActionButtons(
    viewModel: ProfileViewModel,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Cerrar sesión
        OutlinedButton(
            onClick = { viewModel.onLogoutRequested() },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Logout,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Cerrar sesión",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }

        // Eliminar cuenta
        Button(
            onClick = { viewModel.onDeleteAccountRequested() },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.DeleteForever,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Eliminar cuenta",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ProfileSection(
    title: String,
    icon: ImageVector,
    isExpanded: Boolean,
    onExpandToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    val animatedElevation by animateDpAsState(
        targetValue = if (isExpanded) 4.dp else 1.dp,
        animationSpec = tween(300),
        label = "elevation_animation"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        tonalElevation = animatedElevation,
        shadowElevation = animatedElevation
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header de la sección
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple()
                    ) { onExpandToggle() }
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                AnimatedContent(
                    targetState = isExpanded,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(150)) +
                                scaleIn(animationSpec = tween(150)) togetherWith
                                fadeOut(animationSpec = tween(150)) +
                                scaleOut(animationSpec = tween(150))
                    },
                    label = "expand_icon_animation"
                ) { expanded ->
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Contraer" else "Expandir",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Contenido expandible
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(
                    animationSpec = tween(300, easing = EaseInOutCubic)
                ) + fadeIn(
                    animationSpec = tween(300, easing = EaseInOutCubic)
                ),
                exit = shrinkVertically(
                    animationSpec = tween(300, easing = EaseInOutCubic)
                ) + fadeOut(
                    animationSpec = tween(300, easing = EaseInOutCubic)
                )
            ) {
                Column {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    content()
                }
            }
        }
    }
}

@Composable
private fun EnhancedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    errorMessage: String? = null,
    supportingText: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = if (isError) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        trailingIcon = trailingIcon,
        readOnly = readOnly,
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        isError = isError,
        supportingText = {
            when {
                isError && errorMessage != null -> {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                supportingText != null -> {
                    Text(
                        text = supportingText,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
            errorBorderColor = MaterialTheme.colorScheme.error,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileDialogs(
    uiState: ProfileUiState,
    viewModel: ProfileViewModel,
    showBirthdayPicker: Boolean,
    datePickerState: DatePickerState,
    onDatePickerDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    // DatePicker Dialog
    if (showBirthdayPicker) {
        DatePickerDialog(
            onDismissRequest = onDatePickerDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val localDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            onDateSelected(localDate)
                        }
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = onDatePickerDismiss) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                    todayDateBorderColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }

    // Diálogo cambiar contraseña
    if (uiState.isChangingPassword) {
        ChangePasswordDialog(
            viewModel = viewModel,
            isLoading = uiState.isLoading
        )
    }

    // Diálogo cerrar sesión
    if (uiState.isLoggingOut) {
        ConfirmationDialog(
            title = "Cerrar sesión",
            message = "¿Estás seguro de que deseas cerrar sesión?",
            confirmText = "Cerrar sesión",
            onConfirm = { viewModel.performLogout() },
            onDismiss = { viewModel.onLogoutDialogDismissed() },
            isLoading = uiState.isLoading
        )
    }

    // Diálogo eliminar cuenta
    if (uiState.isDeletingAccount) {
        ConfirmationDialog(
            title = "Eliminar cuenta",
            message = "¿Estás seguro de que deseas eliminar tu cuenta? Esta acción no se puede deshacer.",
            confirmText = "Eliminar cuenta",
            onConfirm = { viewModel.performDeleteAccount() },
            onDismiss = { viewModel.onDeleteAccountDialogDismissed() },
            isLoading = uiState.isLoading,
            isDestructive = true
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ChangePasswordDialog(
    viewModel: ProfileViewModel,
    isLoading: Boolean
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    val isFormValid = currentPassword.isNotBlank() &&
            newPassword.isNotBlank() &&
            confirmPassword.isNotBlank() &&
            newPassword == confirmPassword

    AlertDialog(
        onDismissRequest = { viewModel.onPasswordDialogDismissed() },
        title = {
            Text(
                "Cambiar contraseña",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Contraseña actual
                PasswordField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = "Contraseña actual",
                    showPassword = showCurrentPassword,
                    onToggleVisibility = { showCurrentPassword = !showCurrentPassword }
                )

                // Nueva contraseña
                PasswordField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = "Nueva contraseña",
                    showPassword = showNewPassword,
                    onToggleVisibility = { showNewPassword = !showNewPassword }
                )

                // Confirmar nueva contraseña
                PasswordField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Confirmar nueva contraseña",
                    showPassword = showConfirmPassword,
                    onToggleVisibility = { showConfirmPassword = !showConfirmPassword },
                    isError = newPassword != confirmPassword && confirmPassword.isNotEmpty(),
                    errorMessage = if (newPassword != confirmPassword && confirmPassword.isNotEmpty())
                        "Las contraseñas no coinciden" else null
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.onChangePasswordConfirm(
                        currentPassword,
                        newPassword,
                        confirmPassword
                    )
                },
                enabled = isFormValid && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("Cambiar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { viewModel.onPasswordDialogDismissed() },
                enabled = !isLoading
            ) {
                Text("Cancelar")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    showPassword: Boolean,
    onToggleVisibility: () -> Unit,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (showPassword) "Ocultar contraseña" else "Mostrar contraseña"
                )
            }
        },
        isError = isError,
        supportingText = errorMessage?.let {
            {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
    )
}

@Composable
private fun ConfirmationDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean,
    isDestructive: Boolean = false
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Text(
                message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDestructive)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.primary,
                    contentColor = if (isDestructive)
                        MaterialTheme.colorScheme.onError
                    else
                        MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = if (isDestructive)
                            MaterialTheme.colorScheme.onError
                        else
                            MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text("Cancelar")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}