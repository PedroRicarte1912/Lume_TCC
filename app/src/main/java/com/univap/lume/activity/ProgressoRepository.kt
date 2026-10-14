package com.univap.lume.activity

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object ProgressoRepository {

    private const val PREFS_NAME = "LumeSettings"
    private const val PROGRESSO_KEY = "UNIDADE_CONCLUIDA"

    private fun uid() = FirebaseAuth.getInstance().currentUser?.uid

    private fun chaveLocal() = "${PROGRESSO_KEY}_${uid() ?: "anon"}"

    private fun referencia() = uid()?.let {
        FirebaseDatabase.getInstance().getReference("progresso").child(it).child("unidadeConcluida")
    }

    /** Leitura rápida, só do cache local (usar no onCreate). */
    fun lerLocal(context: Context): Int =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getInt(chaveLocal(), 0)

    /** Chame ao concluir uma unidade. Nunca diminui o progresso já salvo. */
    fun salvarUnidadeConcluida(context: Context, unidade: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (unidade <= prefs.getInt(chaveLocal(), 0)) return

        prefs.edit().putInt(chaveLocal(), unidade).apply()
        referencia()?.setValue(unidade)
    }

    /** Busca no Firebase; se falhar (offline), devolve o valor local. */
    fun carregar(context: Context, onResultado: (Int) -> Unit) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val chave = chaveLocal()
        val local = prefs.getInt(chave, 0)
        val ref = referencia()

        if (ref == null) {
            onResultado(local)
            return
        }

        ref.get()
            .addOnSuccessListener { snap ->
                val remoto = snap.getValue(Int::class.java) ?: 0
                val valor = maxOf(local, remoto)
                prefs.edit().putInt(chave, valor).apply()
                if (local > remoto) ref.setValue(local)   // sincroniza o que ficou offline
                onResultado(valor)
            }
            .addOnFailureListener { onResultado(local) }
    }
}