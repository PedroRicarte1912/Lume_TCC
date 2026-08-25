package com.univap.lume.activity
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.univap.lume.MainActivity
import com.univap.lume.R

class Register : AppCompatActivity() {

    private lateinit var edt_nome_register: EditText
    private lateinit var edt_email_register: EditText
    private lateinit var edt_senha_register: EditText
    private lateinit var edt_confirmar_senha_register: EditText
    private lateinit var btn_registro: Button
    private lateinit var btn_ir_login: Button

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()

        edt_nome_register = findViewById(R.id.edt_nome_register)
        edt_email_register = findViewById(R.id.edt_email_register)
        edt_senha_register = findViewById(R.id.edt_senha_register)
        edt_confirmar_senha_register = findViewById(R.id.edt_confirmar_senha_register)
        btn_registro = findViewById(R.id.btn_registro)
        btn_ir_login=findViewById(R.id.btn_ir_login)


        btn_ir_login.setOnClickListener {
            val tela_login=Intent(this, ActivityLogin::class.java)
            startActivity(tela_login)
        }
        btn_registro.setOnClickListener {
            registrarUsuario()
        }
    }

    private fun registrarUsuario() {
        val nome = edt_nome_register.text.toString().trim()
        val email = edt_email_register.text.toString().trim()
        val senha = edt_senha_register.text.toString()
        val confirmar = edt_confirmar_senha_register.text.toString()

        // Validações
        if (nome.isBlank()) {
            edt_nome_register.error = "Digite seu nome"
            return
        }
        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_email_register.error = "Email inválido"
            return
        }
        if (senha.length < 6) {
            edt_senha_register.error = "Senha deve ter no mínimo 6 caracteres"
            return
        }
        if (senha != confirmar) {
            edt_confirmar_senha_register.error = "As senhas não coincidem"
            return
        }

        btn_registro.isEnabled = false

        // 1. Cria o usuário no Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 2. Se criou no Auth, pegamos o UID único gerado pelo Firebase
                    val uid = mAuth.currentUser?.uid ?: ""

                    // 3. Criamos o objeto usuário usando esse UID
                    val user = Usuario(uid, nome, email, senha)

                    // 4. Salvamos no Realtime Database usando o UID como chave
                    val referencia = FirebaseDatabase.getInstance().getReference("usuarios")
                    referencia.child(uid).setValue(user).addOnCompleteListener { dbTask ->
                        if (dbTask.isSuccessful) {
                            // Salvar no SharedPreferences (opcional, já que o Firebase Auth mantém a sessão)
                            val sharedPreferences = getSharedPreferences("LumeSettings", MODE_PRIVATE)
                            sharedPreferences.edit().putString("USER_ID_KEY", uid).apply()

                            Toast.makeText(this, "Sucesso!", Toast.LENGTH_SHORT).show()


                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            btn_registro.isEnabled = true
                            Toast.makeText(this, "Erro ao salvar dados no banco", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    btn_registro.isEnabled = true
                    Toast.makeText(this, "Erro: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}