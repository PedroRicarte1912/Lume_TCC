package com.univap.lume.activity

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.univap.lume.R

class ActivityRecuperarSenha : AppCompatActivity() {

    private lateinit var txt_avisoSenha: TextView
    private lateinit var btn_recSenha: Button
    private lateinit var edt_insiraCodigo: EditText   // campo do e-mail (mantive o id do seu layout)
    private lateinit var mAuth: FirebaseAuth

    private var emailEnviado = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperasenha)

        txt_avisoSenha = findViewById(R.id.txt_avisoSenha)
        btn_recSenha = findViewById(R.id.btn_recSenha)
        edt_insiraCodigo = findViewById(R.id.edt_insiraCodigo)
        mAuth = FirebaseAuth.getInstance()

        btn_recSenha.setOnClickListener {
            if (emailEnviado) finish() else enviarEmailRecuperacao()
        }
    }

    private fun enviarEmailRecuperacao() {
        val email = edt_insiraCodigo.text.toString().trim().lowercase()
        if (!validarEmail(email)) return

        btn_recSenha.isEnabled = false
        txt_avisoSenha.setTextColor(getColor(android.R.color.darker_gray))
        txt_avisoSenha.text = "Enviando e-mail..."

        mAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener { exibirSucesso(email) }
            .addOnFailureListener { e ->
                when (e) {
                    is FirebaseAuthInvalidUserException -> exibirErro("E-mail não cadastrado.")
                    is FirebaseNetworkException -> exibirErro("Sem conexão com a internet.")
                    else -> exibirErro("Erro ao enviar e-mail: ${e.localizedMessage}")
                }
            }
    }

    private fun exibirSucesso(email: String) {
        emailEnviado = true
        txt_avisoSenha.text = "✅ Se o email $email estiver cadastrado receberá um link pra recuperação de senha\n\nAbra o e-mail (confira o spam), clique no link, crie sua nova senha e volte para fazer login."
        txt_avisoSenha.setTextColor(getColor(android.R.color.holo_green_dark))
        edt_insiraCodigo.text.clear()
        btn_recSenha.text = "Voltar ao login"
        btn_recSenha.isEnabled = true
    }

    private fun validarEmail(email: String): Boolean {
        if (email.isEmpty()) {
            edt_insiraCodigo.error = "Informe seu e-mail"
            edt_insiraCodigo.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_insiraCodigo.error = "E-mail inválido"
            edt_insiraCodigo.requestFocus()
            return false
        }
        return true
    }

    private fun exibirErro(mensagem: String) {
        txt_avisoSenha.text = "❌ $mensagem"
        txt_avisoSenha.setTextColor(getColor(android.R.color.holo_red_dark))
        btn_recSenha.isEnabled = true
    }
}