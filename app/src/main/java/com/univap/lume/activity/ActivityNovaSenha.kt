package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.univap.lume.R

class ActivityNovaSenha : AppCompatActivity() {

    private lateinit var edt_novaSenha: EditText
    private lateinit var edt_confirmarSenha: EditText
    private lateinit var btn_salvar: Button
    private lateinit var txt_avisos: TextView
    private lateinit var mAuth: FirebaseAuth

    private val db = FirebaseDatabase.getInstance().reference
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novasenha)

        email              = intent.getStringExtra("email") ?: ""
        edt_novaSenha      = findViewById(R.id.edt_novaSenha)
        edt_confirmarSenha = findViewById(R.id.edt_confirmarSenha)
        btn_salvar         = findViewById(R.id.btn_salvar)
        txt_avisos         = findViewById(R.id.txt_avisos)
        mAuth              = FirebaseAuth.getInstance()

        // Como a senha será redefinida via link seguro do Firebase,
        // oculta os campos de texto para evitar confusão no layout
        edt_novaSenha.visibility = View.GONE
        edt_confirmarSenha.visibility = View.GONE
        btn_salvar.text = "Enviar link de redefinição"

        btn_salvar.setOnClickListener { solicitarResetOficial() }
    }

    private fun solicitarResetOficial() {
        if (email.isEmpty()) {
            mostrarErro("E-mail inválido. Volte e tente novamente.")
            return
        }

        btn_salvar.isEnabled = false
        txt_avisos.text = "Enviando e-mail de redefinição..."
        txt_avisos.setTextColor(getColor(android.R.color.darker_gray))

        // Envia o e-mail de redefinição oficial do Firebase Auth
        mAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                limparCodigoRecuperacao()

                txt_avisos.text = "✅ Link enviado com sucesso para:\n$email\n\nAcesse sua caixa de entrada ou spam, clique no link para redefinir sua senha e retorne para realizar o login."
                txt_avisos.setTextColor(getColor(android.R.color.holo_green_dark))

                txt_avisos.postDelayed({ irParaLogin() }, 4000)
            }
            .addOnFailureListener { exception ->
                mostrarErro("Erro ao enviar e-mail: ${exception.localizedMessage}")
            }
    }

    // Apaga o código de recuperação temporário do Realtime Database após o uso
    private fun limparCodigoRecuperacao() {
        val emailChave = email.replace(".", ",")
        db.child("recuperacao_senha").child(emailChave).removeValue()
    }

    private fun irParaLogin() {
        val intent = Intent(this, ActivityLogin::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun mostrarErro(msg: String) {
        txt_avisos.text = "❌ $msg"
        txt_avisos.setTextColor(getColor(android.R.color.holo_red_dark))
        btn_salvar.isEnabled = true
    }
}