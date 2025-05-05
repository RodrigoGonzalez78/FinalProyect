package com.example.finalproyect.presenter.home


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onEventClick: (Long) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchExpanded by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }

    // Filtrar eventos basados en la búsqueda
    val filteredEvents = remember(searchQuery) {
        sampleEvents.filter { event ->
            if (searchQuery.isBlank()) {
                true
            } else {
                event.name.contains(searchQuery, ignoreCase = true) ||
                        (event.description?.contains(searchQuery, ignoreCase = true) ?: false) ||
                        (event.locationName?.contains(searchQuery, ignoreCase = true) ?: false)
            }
        }
    }

    // Eventos cercanos (simulados)
    val nearbyEvents = remember {
        sampleEvents.shuffled().take(3).map { event ->
            // Simular distancias aleatorias entre 0.5 y 10 km
            val distance = (0.5 + Math.random() * 9.5).roundToInt().toDouble()
            Pair(event, distance)
        }
    }

    Scaffold(
        topBar = {
            if (!isSearchExpanded) {
                TopAppBar(
                    title = { Text("Buscar") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = if (isSearchExpanded) 0.dp else 8.dp,
                    bottom = 80.dp
                )
            ) {
                // Barra de búsqueda
                item {
                    SearchItemBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearch = { /* Realizar búsqueda */ },
                        onFilterClick = { showFilterDialog = true },
                        expanded = isSearchExpanded,
                        onExpandedChange = { isSearchExpanded = it }
                    )
                }

                // Eventos cercanos (solo se muestran cuando no hay búsqueda activa)
                if (!isSearchExpanded && searchQuery.isEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Cerca de ti",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    items(nearbyEvents) { (event, distance) ->
                        NearbyEventItem(
                            event = event,
                            distance = distance,
                            onClick = { onEventClick(event.idEvent) }
                        )
                    }
                }

                // Resultados de búsqueda
                if (searchQuery.isNotEmpty() || isSearchExpanded) {
                    item {
                        Text(
                            text = "Resultados",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    if (filteredEvents.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.SearchOff,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "No se encontraron eventos",
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Intenta con otra búsqueda o cambia los filtros",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        items(filteredEvents) { event ->
                            SearchResultItem(
                                event = event,
                                onClick = { onEventClick(event.idEvent) }
                            )
                        }
                    }
                }

                // Eventos populares (se muestran cuando no hay búsqueda activa)
                if (!isSearchExpanded && searchQuery.isEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Eventos populares",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    items(sampleEvents.sortedByDescending { it.guestsNumber }.take(5)) { event ->
                        SearchResultItem(
                            event = event,
                            onClick = { onEventClick(event.idEvent) }
                        )
                    }
                }
            }

            // Diálogo de filtros
            if (showFilterDialog) {
                FilterDialog(
                    onDismiss = { showFilterDialog = false },
                    onApplyFilters = { /* Aplicar filtros */ }
                )
            }
        }
    }
}

@Composable
fun NearbyEventItem(
    event: Event,
    distance: Double,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del evento
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = event.banner ?: "https://via.placeholder.com/80",
                    contentDescription = event.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = event.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = event.locationName ?: "Sin ubicación",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarToday,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = SimpleDateFormat("d MMM, HH:mm", Locale("es", "ES")).format(event.date),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            /*Chip(
                onClick = { /* Mostrar en mapa */ },
                colors = ChipDefaults.chipColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    labelColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.NearMe,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "$distance km",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }*/
        }
    }
}

@Composable
fun SearchResultItem(
    event: Event,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Imagen del evento
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                AsyncImage(
                    model = event.banner ?: "https://via.placeholder.com/400x200",
                    contentDescription = event.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Gradiente para mejorar legibilidad
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.6f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.6f)
                                )
                            )
                        )
                )

                // Fecha del evento
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                ) {
                    Text(
                        text = SimpleDateFormat("d MMM", Locale("es", "ES")).format(event.date),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Nombre del evento
                Text(
                    text = event.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = event.locationName ?: "Sin ubicación",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = SimpleDateFormat("HH:mm", Locale("es", "ES")).format(event.date),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun FilterDialog(
    onDismiss: () -> Unit,
    onApplyFilters: () -> Unit
) {
    var selectedDateRange by remember { mutableStateOf("Cualquier fecha") }
    var selectedPrice by remember { mutableStateOf("Cualquier precio") }
    var selectedDistance by remember { mutableStateOf("Cualquier distancia") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Filtros de búsqueda",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Filtro de fecha
                Text(
                    text = "Fecha",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterOption(
                        text = "Hoy",
                        selected = selectedDateRange == "Hoy",
                        onClick = { selectedDateRange = "Hoy" }
                    )

                    FilterOption(
                        text = "Mañana",
                        selected = selectedDateRange == "Mañana",
                        onClick = { selectedDateRange = "Mañana" }
                    )

                    FilterOption(
                        text = "Esta semana",
                        selected = selectedDateRange == "Esta semana",
                        onClick = { selectedDateRange = "Esta semana" }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterOption(
                        text = "Este mes",
                        selected = selectedDateRange == "Este mes",
                        onClick = { selectedDateRange = "Este mes" }
                    )

                    FilterOption(
                        text = "Cualquier fecha",
                        selected = selectedDateRange == "Cualquier fecha",
                        onClick = { selectedDateRange = "Cualquier fecha" }
                    )
                }

                Divider()

                // Filtro de precio
                Text(
                    text = "Precio",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterOption(
                        text = "Gratis",
                        selected = selectedPrice == "Gratis",
                        onClick = { selectedPrice = "Gratis" }
                    )

                    FilterOption(
                        text = "Hasta $50",
                        selected = selectedPrice == "Hasta $50",
                        onClick = { selectedPrice = "Hasta $50" }
                    )

                    FilterOption(
                        text = "Hasta $100",
                        selected = selectedPrice == "Hasta $100",
                        onClick = { selectedPrice = "Hasta $100" }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterOption(
                        text = "Hasta $200",
                        selected = selectedPrice == "Hasta $200",
                        onClick = { selectedPrice = "Hasta $200" }
                    )

                    FilterOption(
                        text = "Cualquier precio",
                        selected = selectedPrice == "Cualquier precio",
                        onClick = { selectedPrice = "Cualquier precio" }
                    )
                }

                Divider()

                // Filtro de distancia
                Text(
                    text = "Distancia",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterOption(
                        text = "1 km",
                        selected = selectedDistance == "1 km",
                        onClick = { selectedDistance = "1 km" }
                    )

                    FilterOption(
                        text = "5 km",
                        selected = selectedDistance == "5 km",
                        onClick = { selectedDistance = "5 km" }
                    )

                    FilterOption(
                        text = "10 km",
                        selected = selectedDistance == "10 km",
                        onClick = { selectedDistance = "10 km" }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterOption(
                        text = "25 km",
                        selected = selectedDistance == "25 km",
                        onClick = { selectedDistance = "25 km" }
                    )

                    FilterOption(
                        text = "Cualquier distancia",
                        selected = selectedDistance == "Cualquier distancia",
                        onClick = { selectedDistance = "Cualquier distancia" }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onApplyFilters()
                    onDismiss()
                }
            ) {
                Text("Aplicar filtros")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun FilterOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = if (selected)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        contentColor = if (selected)
            MaterialTheme.colorScheme.onPrimary
        else
            MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}
