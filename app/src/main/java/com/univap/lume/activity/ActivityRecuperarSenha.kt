package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.univap.lume.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class ActivityRecuperarSenha : AppCompatActivity() {

    private lateinit var txt_avisoSenha: TextView
    private lateinit var btn_recSenha: Button
    private lateinit var edt_insiraCodigo: EditText
    private lateinit var mAuth: FirebaseAuth

    // Instância do Realtime Database (em vez do Firestore)
    private val db = FirebaseDatabase.getInstance().reference

    private val EMAIL_SERVICE_ID  = "service_k9jos4w"
    private val EMAIL_TEMPLATE_ID = "template_95afnin"
    private val EMAIL_PUBLIC_KEY  = "ChqaXUwzM1HLwQGLj"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperasenha)

        txt_avisoSenha   = findViewById(R.id.txt_avisoSenha)
        btn_recSenha     = findViewById(R.id.btn_recSenha)
        edt_insiraCodigo = findViewById(R.id.edt_insiraCodigo)
        mAuth            = FirebaseAuth.getInstance()

        btn_recSenha.setOnClickListener {
            enviarEmailRecuperacao()
        }
    }

    // ─── PASSO 1: verifica e-mail no Realtime Database ───────────────────────
    private fun enviarEmailRecuperacao() {
        val email = edt_insiraCodigo.text.toString().trim().lowercase()

        if (!validarEmail(email)) return

        btn_recSenha.isEnabled = false
        txt_avisoSenha.setTextColor(getColor(android.R.color.darker_gray))
        txt_avisoSenha.text = "Verificando e-mail..."

        // Consulta otimizada com índice
        db.child("usuarios").orderByChild("email_user").equalTo(email)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    gerarESalvarCodigo(email)
                } else {
                    exibirMensagemErro("E-mail não cadastrado.")
                }
            }
            .addOnFailureListener { exception ->
                exibirMensagemErro("Erro ao verificar e-mail: ${exception.localizedMessage}")
            }
    }


    // ─── PASSO 2: gera código e salva no Realtime Database ───────────────────
    private fun gerarESalvarCodigo(email: String) {
        txt_avisoSenha.text = "Gerando código..."

        val codigo   = (100000..999999).random().toString()
        val expiraEm = System.currentTimeMillis() + (10 * 60 * 1000) // 10 min

        val dados = hashMapOf(
            "codigo"   to codigo,
            "email"    to email,
            "expiraEm" to expiraEm,
            "usado"    to false
        )


        val emailChave = email.replace(".", ",")

        db.child("recuperacao_senha").child(emailChave)
            .setValue(dados)
            .addOnSuccessListener {
                enviarEmailComCodigo(email, codigo)
            }
            .addOnFailureListener { exception ->
                exibirMensagemErro("Erro ao gerar código: ${exception.localizedMessage}")
            }
    }
    private fun enviarEmailComCodigo(email: String, codigo: String) {
        txt_avisoSenha.text = "Enviando código..."

        val json = JSONObject().apply {
            put("service_id",  EMAIL_SERVICE_ID)
            put("template_id", EMAIL_TEMPLATE_ID)
            put("user_id",     EMAIL_PUBLIC_KEY)
            put("template_params", JSONObject().apply {
                put("to_email", email)
                put("codigo",   codigo)
            })
        }

        val body = json.toString()
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("https://api.emailjs.com/api/v1.0/email/send")
            .post(body)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string() ?: ""
                runOnUiThread {
                    if (response.isSuccessful) {
                        exibirMensagemSucesso(email)
                    } else {

                        exibirMensagemErro("Erro ${response.code}: $responseBody")
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    exibirMensagemErro("Sem conexão com a internet.")
                }
            }
        })
    }

    private fun exibirMensagemSucesso(email: String) {
        txt_avisoSenha.text = "✅ Código enviado para $email"
        txt_avisoSenha.setTextColor(getColor(android.R.color.holo_green_dark))
        edt_insiraCodigo.text.clear()
        btn_recSenha.text = "Código enviado"

        txt_avisoSenha.postDelayed({
            val intent = Intent(this, ActivityInserirCodigo::class.java)
            intent.putExtra("email", email)
            startActivity(intent)
        }, 1500)
    }

    private fun validarEmail(email: String): Boolean {
        if (email.isEmpty()) {
            edt_insiraCodigo.error = "Informe seu e-mail"
            edt_insiraCodigo.requestFocus()
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_insiraCodigo.error = "E-mail inválido"
            edt_insiraCodigo.requestFocus()
            return false
        }
        return true
    }

    private fun exibirMensagemErro(mensagem: String) {
        txt_avisoSenha.text = "❌ $mensagem"
        txt_avisoSenha.setTextColor(getColor(android.R.color.holo_red_dark))
        btn_recSenha.isEnabled = true
    }
}