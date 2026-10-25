package com.example.cargo.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cargo.*

@Composable
fun CarDexScreen(registros: List<Registro>) {
    val carros = registros.filter { it.tipo == TipoRegistro.CARRO }
    val descobertos = carros.mapNotNull { it.modeloId }.toSet()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("CarDex", style = MaterialTheme.typography.headlineMedium)
        Text("${descobertos.size} / ${Catalogo.carros.size} descobertos")
        LinearProgressIndicator(
            progress = { descobertos.size / Catalogo.carros.size.toFloat() },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(Catalogo.carros) { modelo ->
                CartaCarro(modelo, carros.firstOrNull { it.modeloId == modelo.id })
            }
        }
    }
}

@Composable
private fun CartaCarro(modelo: CarroModelo, registro: Registro?) {
    val descoberto = registro != null
    val borda = if (descoberto) Modifier.border(2.dp, modelo.raridade.cor, RoundedCornerShape(12.dp)) else Modifier

    Card(Modifier.fillMaxWidth().height(160.dp).then(borda)) {
        Column(Modifier.fillMaxSize().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("#%03d".format(modelo.id), style = MaterialTheme.typography.labelSmall)
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                val img = remember(registro?.fotoPath) { registro?.let { carregarFoto(it.fotoPath) } }
                if (img != null) {
                    Image(
                        bitmap = img,
                        contentDescription = modelo.nome,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp))
                    )
                } else {
                    Text("?", style = MaterialTheme.typography.displayMedium)
                }
            }
            Text(if (descoberto) modelo.nome else "???", fontWeight = FontWeight.Bold, maxLines = 1)
            Text(
                if (descoberto) modelo.raridade.rotulo else "não descoberto",
                color = if (descoberto) modelo.raridade.cor else Color.Gray,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}