package com.example.cargo.ui

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.cargo.*

@Composable
fun RegistrarScreen(
    pontosTotal: Int,
    onSalvar: (Bitmap, TipoRegistro, Int?, String) -> Int
) {
    var tipo by remember { mutableStateOf(TipoRegistro.CARRO) }
    var foto by remember { mutableStateOf<Bitmap?>(null) }
    var mensagem by remember { mutableStateOf<String?>(null) }

    val camera = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bmp -> if (bmp != null) foto = bmp }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text("$pontosTotal", style = MaterialTheme.typography.displayLarge)
        Text("pontos")

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = tipo == TipoRegistro.CARRO,
                onClick = { tipo = TipoRegistro.CARRO },
                label = { Text("Carro") }
            )
            FilterChip(
                selected = tipo == TipoRegistro.ABANDONADO,
                onClick = { tipo = TipoRegistro.ABANDONADO },
                label = { Text("Abandonado") }
            )
        }

        Button(onClick = { camera.launch(null) }) { Text("Tirar foto") }

        mensagem?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
    }

    foto?.let { bmp ->
        if (tipo == TipoRegistro.CARRO) {
            EscolherModeloDialog(
                foto = bmp,
                onCancelar = { foto = null },
                onEscolher = { modelo ->
                    val p = onSalvar(bmp, TipoRegistro.CARRO, modelo.id, "")
                    mensagem = "${modelo.nome} (${modelo.raridade.rotulo}): +$p pts"
                    foto = null
                }
            )
        } else {
            AbandonadoDialog(
                foto = bmp,
                onCancelar = { foto = null },
                onSalvar = { local ->
                    val p = onSalvar(bmp, TipoRegistro.ABANDONADO, null, local)
                    mensagem = "Abandonado marcado: +$p pts"
                    foto = null
                }
            )
        }
    }
}

@Composable
private fun EscolherModeloDialog(
    foto: Bitmap,
    onCancelar: () -> Unit,
    onEscolher: (CarroModelo) -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Que carro é esse?") },
        text = {
            Column {
                Image(
                    bitmap = foto.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(140.dp)
                )
                Spacer(Modifier.height(8.dp))
                LazyColumn(Modifier.height(240.dp)) {
                    items(Catalogo.carros) { c ->
                        ListItem(
                            headlineContent = { Text(c.nome) },
                            supportingContent = { Text("${c.raridade.rotulo} · ${c.raridade.pontos} pts") },
                            modifier = Modifier.clickable { onEscolher(c) }
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = { TextButton(onClick = onCancelar) { Text("Cancelar") } }
    )
}

@Composable
private fun AbandonadoDialog(
    foto: Bitmap,
    onCancelar: () -> Unit,
    onSalvar: (String) -> Unit
) {
    var local by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Marcar carro abandonado") },
        text = {
            Column {
                Image(
                    bitmap = foto.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(140.dp)
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = local,
                    onValueChange = { local = it },
                    label = { Text("Local / descrição") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSalvar(local) }, enabled = local.isNotBlank()) { Text("Marcar") }
        },
        dismissButton = { TextButton(onClick = onCancelar) { Text("Cancelar") } }
    )
}