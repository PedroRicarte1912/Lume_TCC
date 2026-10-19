package com.univap.lume.activity

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object ProgressoRepository {

    private const val PREFS_NAME = "LumeSettings"
    private const val PROGRESSO_KEY = "UNIDADE_CONCLUIDA"
    private const val NIVEL_TEA_KEY = "NIVEL_TEA"   // mesma chave que a ActivityMapearTea usa

    data class Estado(val unidadeConcluida: Int, val nivelTea: Int)

    private fun uid() = FirebaseAuth.getInstance().currentUser?.uid

    private fun chaveLocal() = "${PROGRESSO_KEY}_${uid() ?: "anon"}"

    private fun refProgresso() = uid()?.let {
        FirebaseDatabase.getInstance().getReference("progresso").child(it).child("unidadeConcluida")
    }

    private fun refNivel() = uid()?.let {
        FirebaseDatabase.getInstance().getReference("informacoes_tea").child(it).child("nivel_tea")
    }

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /** Leitura rápida, só do cache local (usar no onCreate). */
    fun lerLocal(context: Context): Int = prefs(context).getInt(chaveLocal(), 0)

    /** Chame ao concluir uma unidade. Nunca diminui o progresso já salvo. */
    fun salvarUnidadeConcluida(context: Context, unidade: Int) {
        val p = prefs(context)
        if (unidade <= p.getInt(chaveLocal(), 0)) return

        p.edit().putInt(chaveLocal(), unidade).apply()
        refProgresso()?.setValue(unidade)
    }

    /**
     * Busca progresso e nível de TEA no Firebase.
     * Se estiver offline ou sem login, devolve os valores locais.
     */
    fun carregar(context: Context, onResultado: (Estado) -> Unit) {
        val p = prefs(context)
        val chave = chaveLocal()
        val progressoLocal = p.getInt(chave, 0)
        val nivelLocal = p.getInt(NIVEL_TEA_KEY, 1).coerceIn(1, 3)

        val refP = refProgresso()
        val refN = refNivel()
        val estadoLocal = Estado(progressoLocal, nivelLocal)

        if (refP == null || refN == null) {
            onResultado(estadoLocal)
            return
        }

        refP.get()
            .addOnSuccessListener { snapP ->
                val remotoP = snapP.getValue(Int::class.java) ?: 0
                val progresso = maxOf(progressoLocal, remotoP)
                p.edit().putInt(chave, progresso).apply()
                if (progressoLocal > remotoP) refP.setValue(progressoLocal)  // sincroniza o que ficou offline

                refN.get()
                    .addOnSuccessListener { snapN ->
                        // no Firebase o nível é salvo como texto ("1", "2" ou "3")
                        val remotoN = snapN.getValue(String::class.java)
                            ?.toIntOrNull()
                            ?.takeIf { it in 1..3 }

                        val nivel = remotoN ?: nivelLocal
                        if (remotoN != null) p.edit().putInt(NIVEL_TEA_KEY, remotoN).apply()
                        onResultado(Estado(progresso, nivel))
                    }
                    .addOnFailureListener { onResultado(Estado(progresso, nivelLocal)) }
            }
            .addOnFailureListener { onResultado(estadoLocal) }
    }
}