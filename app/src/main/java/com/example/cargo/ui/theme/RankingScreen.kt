package com.example.cargo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun RankingScreen(pontosTotal: Int) {
    // Dados de exemplo: o ranking de verdade precisa de servidor.
    val lista = remember(pontosTotal) {
        listOf(
            "@rafa.jdm" to 6240,
            "@ana_fusca" to 5810,
            "@bia_garage" to 5120,
            "@thi.opala" to 4120,
            "@duda_jdm" to 3980,
            "Você" to pontosTotal
        ).sortedByDescending { it.second }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Ranking", style = MaterialTheme.typography.headlineMedium)
        Text("Dados de exemplo (sem servidor ainda)", style = MaterialTheme.typography.bodySmall)
        LazyColumn(
            modifier = Modifier.padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(lista) { i, item ->
                val (nome, pts) = item
                val euMesmo = nome == "Você"
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (euMesmo) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("#${i + 1}  $nome", fontWeight = FontWeight.Bold)
                        Text("$pts pts")
                    }
                }
            }
        }
    }
}