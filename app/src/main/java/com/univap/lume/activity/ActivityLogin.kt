package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.univap.lume.MainActivity
import com.univap.lume.R



class ActivityLogin : AppCompatActivity() {
    private lateinit var edt_SenhaLogin: TextInputEditText
    private lateinit var senhaInputLayout: TextInputLayout
    private lateinit var edt_EmailLogin: EditText
    private lateinit var btn_login2: Button
    private lateinit var btn_recuperaSenha: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()

        edt_EmailLogin = findViewById(R.id.edt_EmailLogin)
        edt_SenhaLogin = findViewById(R.id.edt_SenhaLogin)
        senhaInputLayout = findViewById(R.id.senhaInputLayout)
        btn_login2 = findViewById(R.id.btn_login2)
        btn_recuperaSenha=findViewById(R.id.btn_recuperaSenha)

        btn_recuperaSenha.setOnClickListener {
            val tela_recuperaSenha1=Intent(this, ActivityRecuperarSenha::class.java)
            startActivity(tela_recuperaSenha1)
        }

        btn_login2.setOnClickListener {
            fazerLogin()
        }
    }

    private fun fazerLogin() {
        val email = edt_EmailLogin.text.toString().trim()
        val senha = edt_SenhaLogin.text.toString().trim()

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_EmailLogin.error = "Email Inválido"
            edt_EmailLogin.requestFocus()
            return
        }

        if (senha.isBlank()) {
            // Erro no TextInputLayout mostra abaixo do campo
            senhaInputLayout.error = "Digite sua Senha"
            senhaInputLayout.requestFocus()
            return
        } else {
            senhaInputLayout.error = null  // limpa o erro ao digitar certo
        }

        btn_login2.isEnabled = false

        mAuth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Bem-vindo!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    btn_login2.isEnabled = true
                    Toast.makeText(this, "Erro: Usuário ou senha incorretos", Toast.LENGTH_SHORT).show()
                }
            }
    }
}