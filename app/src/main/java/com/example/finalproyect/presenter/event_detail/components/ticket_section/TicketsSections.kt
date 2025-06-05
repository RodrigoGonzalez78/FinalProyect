package com.example.finalproyect.presenter.event_detail.components.ticket_section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finalproyect.presenter.event_detail.components.ticket_type_section.TicketStat


@Composable
fun TicketsSection() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Resumen de validación
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TicketStat(
                    value = "156",
                    label = "Validadas",
                    color = MaterialTheme.colorScheme.primary
                )

                TicketStat(
                    value = "43",
                    label = "Pendientes",
                    color = MaterialTheme.colorScheme.tertiary
                )

                TicketStat(
                    value = "78%",
                    label = "Asistencia",
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Text(
            text = "Entradas validadas recientemente",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            /*items(sampleTicketValidations) { validation ->
                TicketValidationItem(validation = validation)
            }*/
        }
    }
}
