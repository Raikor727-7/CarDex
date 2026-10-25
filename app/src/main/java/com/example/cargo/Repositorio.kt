package com.example.cargo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

class Repositorio(context: Context) {
    private val dirFotos = File(context.filesDir, "fotos").apply { mkdirs() }
    private val arquivo = File(context.filesDir, "registros.json")

    fun carregar(): List<Registro> {
        if (!arquivo.exists()) return emptyList()
        return try {
            val arr = JSONArray(arquivo.readText())
            (0 until arr.length()).map { i ->
                val o = arr.getJSONObject(i)
                Registro(
                    id = o.getLong("id"),
                    tipo = TipoRegistro.valueOf(o.getString("tipo")),
                    modeloId = if (o.has("modeloId")) o.getInt("modeloId") else null,
                    descricao = o.optString("descricao", ""),
                    fotoPath = o.getString("fotoPath"),
                    pontos = o.getInt("pontos"),
                    dataMillis = o.getLong("dataMillis")
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun salvar(lista: List<Registro>) {
        val arr = JSONArray()
        lista.forEach { r ->
            val o = JSONObject()
            o.put("id", r.id)
            o.put("tipo", r.tipo.name)
            r.modeloId?.let { o.put("modeloId", it) }
            o.put("descricao", r.descricao)
            o.put("fotoPath", r.fotoPath)
            o.put("pontos", r.pontos)
            o.put("dataMillis", r.dataMillis)
            arr.put(o)
        }
        arquivo.writeText(arr.toString())
    }

    /** Salva a foto no armazenamento interno do app e devolve o caminho. */
    fun salvarFoto(bitmap: Bitmap): String {
        val f = File(dirFotos, "${System.currentTimeMillis()}.jpg")
        FileOutputStream(f).use { bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it) }
        return f.absolutePath
    }
}

fun carregarFoto(path: String): ImageBitmap? =
    BitmapFactory.decodeFile(path)?.asImageBitmap()