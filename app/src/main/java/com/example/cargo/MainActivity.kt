package com.example.cargo

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.cargo.ui.AbandonadosScreen
import com.example.cargo.ui.CarDexScreen
import com.example.cargo.ui.RankingScreen
import com.example.cargo.ui.RegistrarScreen
import com.example.cargo.ui.theme.CarGoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repo = Repositorio(applicationContext)
        setContent {
            CarGoTheme {
                CarGoApp(repo)
            }
        }
    }
}

private enum class Aba(val titulo: String, val icone: ImageVector) {
    REGISTRAR("Registrar", Icons.Default.Add),
    CARDEX("CarDex", Icons.Default.Search),
    ABANDONADOS("Abandonados", Icons.Default.Place),
    RANKING("Ranking", Icons.Default.Star)
}

@Composable
private fun CarGoApp(repo: Repositorio) {
    val registros = remember { mutableStateListOf<Registro>().apply { addAll(repo.carregar()) } }
    var aba by remember { mutableStateOf(Aba.REGISTRAR) }
    val pontosTotal = registros.sumOf { it.pontos }

    fun adicionar(bitmap: Bitmap, tipo: TipoRegistro, modeloId: Int?, descricao: String): Int {
        val pontos = when (tipo) {
            TipoRegistro.CARRO -> {
                val modelo = Catalogo.porId(modeloId!!)!!
                val novo = registros.none { it.tipo == TipoRegistro.CARRO && it.modeloId == modeloId }
                modelo.raridade.pontos + if (novo) PONTOS_MODELO_NOVO else 0
            }
            TipoRegistro.ABANDONADO -> PONTOS_ABANDONADO
        }
        val agora = System.currentTimeMillis()
        registros.add(0, Registro(agora, tipo, modeloId, descricao, repo.salvarFoto(bitmap), pontos, agora))
        repo.salvar(registros)
        return pontos
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                Aba.entries.forEach { item ->
                    NavigationBarItem(
                        selected = aba == item,
                        onClick = { aba = item },
                        icon = { Icon(item.icone, contentDescription = item.titulo) },
                        label = { Text(item.titulo) }
                    )
                }
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            when (aba) {
                Aba.REGISTRAR -> RegistrarScreen(pontosTotal, ::adicionar)
                Aba.CARDEX -> CarDexScreen(registros)
                Aba.ABANDONADOS -> AbandonadosScreen(registros)
                Aba.RANKING -> RankingScreen(pontosTotal)
            }
        }
    }
}