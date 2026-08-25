package com.univap.lume.util

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.univap.lume.activity.ActivityLicoes1
import com.univap.lume.activity.ActivityLicoes3
import com.univap.lume.activity.ActivityLicoes5
import com.univap.lume.activity.ActivityMapearTea

object TeaRouter {

    fun irParaLicoes(activity: Activity) {
        val idUsuario = FirebaseAuth.getInstance().currentUser?.uid

        if (idUsuario.isNullOrEmpty()) {
            Toast.makeText(activity, "Erro: usuário não encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        val referencia = FirebaseDatabase.getInstance()
            .getReference("informacoes_tea")
            .child(idUsuario)

        referencia.get()
            .addOnSuccessListener { snapshot ->
                val nivel = snapshot.child("nivel_tea").getValue(String::class.java)
                val destino = destinoPorNivel(activity, nivel)   //  passa a activity também
                activity.startActivity(Intent(activity, destino))
                activity.finish()
            }
    }

    private fun destinoPorNivel(activity: Activity, nivel: String?): Class<out Activity> {
        val nivelInt = nivel?.trim()?.toDoubleOrNull()?.toInt()

        return when (nivelInt) {
            3 -> ActivityLicoes1::class.java
            2 -> ActivityLicoes3::class.java
            1 -> ActivityLicoes5::class.java
            else -> {
                Toast.makeText(activity, "Nível de TEA não definido. Defina seu nível de TEA.", Toast.LENGTH_LONG).show()
                ActivityMapearTea::class.java
            }
        }
    }
}