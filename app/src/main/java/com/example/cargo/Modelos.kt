package com.example.cargo

import androidx.compose.ui.graphics.Color

const val PONTOS_MODELO_NOVO = 50
const val PONTOS_ABANDONADO = 40

enum class Raridade(val rotulo: String, val pontos: Int, val cor: Color) {
    COMUM("Comum", 10, Color(0xFF9AA3B5)),
    INCOMUM("Incomum", 30, Color(0xFF4ADE80)),
    RARO("Raro", 100, Color(0xFF3B82F6)),
    EPICO("Épico", 250, Color(0xFFA855F7)),
    LENDARIO("Lendário", 500, Color(0xFFFBBF24))
}

data class CarroModelo(val id: Int, val nome: String, val raridade: Raridade)

object Catalogo {
    val carros = listOf(
        CarroModelo(1, "Volkswagen Gol", Raridade.COMUM),
        CarroModelo(2, "Fiat Uno", Raridade.COMUM),
        CarroModelo(3, "Chevrolet Celta", Raridade.COMUM),
        CarroModelo(4, "Chevrolet Chevette", Raridade.INCOMUM),
        CarroModelo(5, "Volkswagen Kombi", Raridade.INCOMUM),
        CarroModelo(6, "Volkswagen Fusca", Raridade.RARO),
        CarroModelo(7, "Ford Maverick", Raridade.RARO),
        CarroModelo(8, "Chevrolet Opala", Raridade.EPICO),
        CarroModelo(9, "Dodge Charger", Raridade.EPICO),
        CarroModelo(10, "Nissan Skyline GT-R", Raridade.LENDARIO),
        CarroModelo(11, "Ferrari F40", Raridade.LENDARIO)
    )

    fun porId(id: Int): CarroModelo? = carros.firstOrNull { it.id == id }
}

enum class TipoRegistro { CARRO, ABANDONADO }

data class Registro(
    val id: Long,
    val tipo: TipoRegistro,
    val modeloId: Int?,       // só para CARRO
    val descricao: String,    // local/descrição, usado em ABANDONADO
    val fotoPath: String,
    val pontos: Int,
    val dataMillis: Long
)