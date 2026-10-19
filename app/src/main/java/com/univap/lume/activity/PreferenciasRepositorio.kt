package com.univap.lume.activity

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object PreferenciasRepositorio {

    private const val PREFS_NAME = "LumeSettings"
    private const val AVATAR_KEY = "AVATAR_KEY"
    private const val SOM_KEY = "SOM_KEY"

    private fun uid() = FirebaseAuth.getInstance().currentUser?.uid
    private fun db() = FirebaseDatabase.getInstance()

    /** Chamar ao clicar em "Selecionar" na tela de teste sonoro. nomeRes = "som1", "som2" ou "som3". */
    fun salvarSom(context: Context, nomeRes: String) {
        val uid = uid() ?: return
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(SOM_KEY, nomeRes).apply()
        db().getReference("configuracoes_som").child(uid)
            .setValue(mapOf("id_usuario" to uid, "nome_res_som" to nomeRes))
    }

    /**
     * Busca avatar e som escolhidos no Firebase.
     * Sucesso: devolve o que está salvo (null = o usuário ainda não escolheu).
     * Falha (offline): devolve o cache local.
     */
    fun carregar(context: Context, onResultado: (avatar: String?, som: String?) -> Unit) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val uid = uid()
        if (uid == null) {
            onResultado(prefs.getString(AVATAR_KEY, null), prefs.getString(SOM_KEY, null))
            return
        }

        db().getReference("midias_avatar").child(uid).child("nome_res_avatar").get()
            .addOnSuccessListener { snapA ->
                val avatar = snapA.getValue(String::class.java)
                prefs.edit().apply {
                    if (avatar != null) putString(AVATAR_KEY, avatar) else remove(AVATAR_KEY)
                }.apply()

                db().getReference("configuracoes_som").child(uid).child("nome_res_som").get()
                    .addOnSuccessListener { snapS ->
                        val som = snapS.getValue(String::class.java)
                        prefs.edit().apply {
                            if (som != null) putString(SOM_KEY, som) else remove(SOM_KEY)
                        }.apply()
                        onResultado(avatar, som)
                    }
                    .addOnFailureListener { onResultado(avatar, prefs.getString(SOM_KEY, null)) }
            }
            .addOnFailureListener {
                onResultado(prefs.getString(AVATAR_KEY, null), prefs.getString(SOM_KEY, null))
            }
    }
}