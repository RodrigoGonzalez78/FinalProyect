package com.example.finalproyect.presenter.home.component


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.LocalActivity
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.finalproyect.domain.model.EventDetail
import com.example.finalproyect.presenter.home.ErrorMessage
import com.example.finalproyect.presenter.home.LoadMoreButton
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TicketsContent(
    tickets: List<EventDetail>,
    isLoading: Boolean,
    error: String?,
    canLoadMore: Boolean,
    onLoadMore: () -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        if (error != null) {
            item {
                ErrorMessage(
                    message = error,
                    onRetry = { /* Implementar retry */ }
                )
            }
        }

        items(tickets) { ticket ->
            TicketItem(
                event = ticket,
                navController = navController
            )
        }

        if (canLoadMore) {
            item {
                LoadMoreButton(
                    isLoading = isLoading,
                    onClick = onLoadMore
                )
            }
        }

        if (tickets.isEmpty() && !isLoading && error == null) {
            item {
                EmptyTicketsMessage()
            }
        }
    }
}

@Composable
fun EmptyTicketsMessage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.LocalActivity,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No tienes tickets comprados",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Busca eventos interesantes y compra tu primer ticket",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}
