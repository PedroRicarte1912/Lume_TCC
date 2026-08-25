package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.univap.lume.R

class ActivityInserirCodigo : AppCompatActivity() {

    private lateinit var edt_codigo: EditText
    private lateinit var btn_validar: Button
    private lateinit var txt_aviso: TextView

    private val db = FirebaseDatabase.getInstance().reference
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inserircodigo)

        email       = intent.getStringExtra("email") ?: ""
        edt_codigo  = findViewById(R.id.edt_codigo)
        btn_validar = findViewById(R.id.btn_validar)
        txt_aviso   = findViewById(R.id.txt_aviso)

        btn_validar.setOnClickListener { validarCodigo() }
    }

    private fun validarCodigo() {
        val codigoDigitado = edt_codigo.text.toString().trim()

        if (codigoDigitado.length != 6) {
            edt_codigo.error = "Digite os 6 dígitos"
            return
        }

        btn_validar.isEnabled = false
        txt_aviso.text = "Validando..."
        txt_aviso.setTextColor(getColor(android.R.color.darker_gray))

        // 3. Formatar a chave do e-mail substituindo ponto por vírgula (igual no envio)
        val emailChave = email.replace(".", ",")

        // 4. Buscar a referência no Realtime Database
        db.child("recuperacao_senha")
            .child(emailChave)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.exists()) {
                    mostrarErro("Código não encontrado. Solicite um novo.")
                    return@addOnSuccessListener
                }

                // Lendo os campos do DataSnapshot
                val codigoSalvo = snapshot.child("codigo").getValue(String::class.java) ?: ""
                val expiraEm    = snapshot.child("expiraEm").getValue(Long::class.java) ?: 0L
                val usado       = snapshot.child("usado").getValue(Boolean::class.java) ?: false
                val agora       = System.currentTimeMillis()

                when {
                    usado ->
                        mostrarErro("Este código já foi utilizado.")
                    agora > expiraEm ->
                        mostrarErro("Código expirado. Volte e solicite um novo.")
                    codigoDigitado != codigoSalvo ->
                        mostrarErro("Código incorreto. Tente novamente.")
                    else -> {
                        // Marca como usado no Realtime Database
                        snapshot.ref.child("usado").setValue(true)

                        txt_aviso.text = "✅ Código validado!"
                        txt_aviso.setTextColor(getColor(android.R.color.holo_green_dark))

                        txt_aviso.postDelayed({
                            val intent = Intent(this, ActivityNovaSenha::class.java)
                            intent.putExtra("email", email)
                            startActivity(intent)
                            finish()
                        }, 1000)
                    }
                }
            }
            .addOnFailureListener { exception ->
                mostrarErro("Erro ao validar: ${exception.localizedMessage}")
            }
    }

    private fun mostrarErro(msg: String) {
        txt_aviso.text = "❌ $msg"
        txt_aviso.setTextColor(getColor(android.R.color.holo_red_dark))
        btn_validar.isEnabled = true
    }
}