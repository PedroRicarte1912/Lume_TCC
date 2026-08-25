package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.univap.lume.R

class ActivityU4L4EX2 : AppCompatActivity() {

        private lateinit var edtLua: EditText
        private lateinit var edtFogo: EditText
        private lateinit var edtEscola: EditText
        private lateinit var edtPorta: EditText
        private lateinit var edtCama: EditText
        private lateinit var edtGato: EditText
        private lateinit var btnVerificar: Button

        // Gabarito: letra que falta em cada palavra
        private val gabarito = mapOf(
            "lua"    to "L",
            "fogo"   to "O",
            "escola" to "O",
            "porta"  to "O",
            "cama"   to "M",
            "gato"   to "T"
        )

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_unidade4licao4ex2)

            edtLua    = findViewById(R.id.edtLua)
            edtFogo   = findViewById(R.id.edtFogo)
            edtEscola = findViewById(R.id.edtEscola)
            edtPorta  = findViewById(R.id.edtPorta)
            edtCama   = findViewById(R.id.edtCama)
            edtGato   = findViewById(R.id.edtGato)
            btnVerificar = findViewById(R.id.btnVerificarCompletar)

            btnVerificar.setOnClickListener { verificar() }
        }

        private fun verificar() {
            val respostas = mapOf(
                "lua"    to edtLua.text.toString().trim().uppercase(),
                "fogo"   to edtFogo.text.toString().trim().uppercase(),
                "escola" to edtEscola.text.toString().trim().uppercase(),
                "porta"  to edtPorta.text.toString().trim().uppercase(),
                "cama"   to edtCama.text.toString().trim().uppercase(),
                "gato"   to edtGato.text.toString().trim().uppercase()
            )

            // Verifica se algum campo está vazio
            if (respostas.values.any { it.isEmpty() }) {
                Toast.makeText(this, "Preencha todos os campos do exercício", Toast.LENGTH_SHORT).show()
                return
            }

            var acertos = 0
            val campos = mapOf(
                "lua"    to edtLua,
                "fogo"   to edtFogo,
                "escola" to edtEscola,
                "porta"  to edtPorta,
                "cama"   to edtCama,
                "gato"   to edtGato
            )

            // Destaca corretos (verde) e errados (vermelho)
            gabarito.forEach { (chave, letraCorreta) ->
                val campo = campos[chave] ?: return@forEach
                if (respostas[chave] == letraCorreta) {
                    acertos++
                    campo.setBackgroundColor(
                        ContextCompat.getColor(this, R.color.lume_verde)
                    )
                } else {
                    campo.setBackgroundColor(
                        ContextCompat.getColor(this, R.color.lume_rosa)
                    )
                }
            }

            if (acertos == gabarito.size) {
                Toast.makeText(this, "Parabéns, suas respostas estão corretas!!!", Toast.LENGTH_SHORT).show()
                // Navegar para próxima activity após acerto total
                startActivity(Intent(this, ActivityU4L4EX3::class.java))
                finish()
            } else {
                Toast.makeText(this, "$acertos de ${gabarito.size} corretas. Tente novamente!", Toast.LENGTH_SHORT).show()
                // Limpa apenas os campos errados após 1.5s
                android.os.Handler(mainLooper).postDelayed({
                    gabarito.forEach { (chave, letraCorreta) ->
                        val campo = campos[chave] ?: return@forEach
                        if (respostas[chave] != letraCorreta) {
                            campo.text.clear()
                            campo.setBackgroundColor(
                                android.graphics.Color.parseColor("#FFDBE8FF")
                            )
                        }
                    }
                }, 1500)
            }
        }
    }
