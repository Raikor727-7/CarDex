package com.example.cargo.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cargo.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private fun formatar(ms: Long): String =
    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR")).format(Date(ms))

@Composable
fun AbandonadosScreen(registros: List<Registro>) {
    val lista = registros.filter { it.tipo == TipoRegistro.ABANDONADO }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Carros abandonados", style = MaterialTheme.typography.headlineMedium)
        if (lista.isEmpty()) {
            Text(
                "Nenhum marcado ainda. Vá em Registrar e escolha \"Abandonado\".",
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        LazyColumn(
            modifier = Modifier.padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(lista, key = { it.id }) { r ->
                Card(Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        val img = remember(r.fotoPath) { carregarFoto(r.fotoPath) }
                        if (img != null) {
                            Image(
                                bitmap = img,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(72.dp).clip(RoundedCornerShape(8.dp))
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(r.descricao, fontWeight = FontWeight.Bold)
                            Text(formatar(r.dataMillis), style = MaterialTheme.typography.bodySmall)
                        }
                        Text(
                            "+${r.pontos}",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}