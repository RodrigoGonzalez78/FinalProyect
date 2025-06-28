package com.example.finalproyect.presenter.event_detail.components

import com.example.finalproyect.presenter.event_detail.EventDetailUiState

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finalproyect.domain.model.Organizer

@Composable
fun OrganizersSection(
    uiState: EventDetailUiState,
    onUpdateOrganizerRole: (Int, Int) -> Unit,
    onDeleteOrganizer: (Int) -> Unit
) {
    var selectedOrganizerForEdit by remember { mutableStateOf<Organizer?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Equipo organizador",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        if (uiState.organizers.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.People,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "No hay organizadores añadidos",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        if (uiState.canManageOrganizers) {
                            Text(
                                text = "Usa el botón + para añadir organizadores",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        } else {
            items(uiState.organizers) { organizer ->
                OrganizerItem(
                    organizer = organizer,
                    canEdit = uiState.canManageOrganizers && !organizer.isMainAdmin,
                    onEdit = { selectedOrganizerForEdit = organizer },
                    onDelete = { onDeleteOrganizer(organizer.id) }
                )
            }
        }
    }

    // Diálogo para editar organizador
    selectedOrganizerForEdit?.let { organizer ->
        EditOrganizerDialog(
            organizer = organizer,
            onDismiss = { selectedOrganizerForEdit = null },
            onUpdateRole = { newRoleId ->
                onUpdateOrganizerRole(organizer.id, newRoleId)
                selectedOrganizerForEdit = null
            },
            onDelete = {
                onDeleteOrganizer(organizer.id)
                selectedOrganizerForEdit = null
            }
        )
    }
}

@Composable
private fun OrganizerItem(
    organizer: Organizer,
    canEdit: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar del organizador
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = organizer.fullName.firstOrNull()?.toString()?.uppercase() ?: "?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = organizer.fullName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = when (organizer.roleId) {
                        1 -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        2 -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                        3 -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
                        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    },
                    contentColor = when (organizer.roleId) {
                        1 -> MaterialTheme.colorScheme.primary
                        2 -> MaterialTheme.colorScheme.secondary
                        3 -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.outline
                    },
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Text(
                        text = when (organizer.roleId) {
                            1 -> "Administrador Principal"
                            2 -> "Validador"
                            3 -> "Moderador"
                            4 -> "Colaborador"
                            else -> "Organizador"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            if (canEdit) {
                Row {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Editar organizador",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
