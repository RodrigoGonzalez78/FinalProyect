package com.example.finalproyect.presenter.public_event.components

import com.example.finalproyect.domain.model.Organizer
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun OrganizerInfo(
    organizer: Organizer,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Organizador",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Avatar del organizador
//                if (organizer.user.profileImage.isNotBlank()) {
//                    AsyncImage(
//                        model = organizer.user.profileImage,
//                        contentDescription = "Foto del organizador",
//                        modifier = Modifier
//                            .size(48.dp)
//                            .clip(CircleShape),
//                        contentScale = ContentScale.Crop
//                    )
//                } else {
//                    Box(
//                        modifier = Modifier
//                            .size(48.dp)
//                            .clip(CircleShape),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Surface(
//                            modifier = Modifier.fillMaxSize(),
//                            color = MaterialTheme.colorScheme.primaryContainer,
//                            shape = CircleShape
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Person,
//                                contentDescription = null,
//                                modifier = Modifier.padding(12.dp),
//                                tint = MaterialTheme.colorScheme.onPrimaryContainer
//                            )
//                        }
//                    }
//                }

                Column {
                    Text(
                        text = "${organizer.name} ${organizer.lastName}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Organizador Principal",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )

//                    if (organizer.email.isNotBlank()) {
//                        Text(
//                            text = organizer.user.email,
//                            style = MaterialTheme.typography.bodySmall,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant
//                        )
//                    }
                }
            }
        }
    }
}
