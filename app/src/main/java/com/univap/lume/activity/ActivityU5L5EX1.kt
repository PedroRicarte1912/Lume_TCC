package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.univap.lume.R

class ActivityU5L5EX1 : AppCompatActivity() {

    private lateinit var edtSapo: EditText
    private lateinit var edtLivro: EditText
    private lateinit var edtChuva: EditText
    private lateinit var edtCachorro: EditText
    private lateinit var edtElefante: EditText
    private lateinit var edtBorboleta: EditText
    private lateinit var btnVerificar: Button

    // Gabarito: letra que falta em cada palavra
    private val gabarito = mapOf(
        "sapo"      to "A", // S_PO
        "livro"     to "V", // LI_RO
        "chuva"     to "U", // CH_VA
        "cachorro"  to "R", // CACHO_RO
        "elefante"  to "F", // ELE_ANTE
        "borboleta" to "T"  // BORBOLE_A
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade5licao5)

        edtSapo      = findViewById(R.id.edtSapo)
        edtLivro     = findViewById(R.id.edtLivro)
        edtChuva     = findViewById(R.id.edtChuva)
        edtCachorro  = findViewById(R.id.edtCachorro)
        edtElefante  = findViewById(R.id.edtElefante)
        edtBorboleta = findViewById(R.id.edtBorboleta)
        btnVerificar = findViewById(R.id.btnVerificarCompletar)

        btnVerificar.setOnClickListener { verificar() }
    }

    private fun verificar() {
        val respostas = mapOf(
            "sapo"      to edtSapo.text.toString().trim().uppercase(),
            "livro"     to edtLivro.text.toString().trim().uppercase(),
            "chuva"     to edtChuva.text.toString().trim().uppercase(),
            "cachorro"  to edtCachorro.text.toString().trim().uppercase(),
            "elefante"  to edtElefante.text.toString().trim().uppercase(),
            "borboleta" to edtBorboleta.text.toString().trim().uppercase()
        )

        // Verifica se algum campo está vazio
        if (respostas.values.any { it.isEmpty() }) {
            Toast.makeText(this, "Preencha todos os campos do exercício", Toast.LENGTH_SHORT).show()
            return
        }

        var acertos = 0
        val campos = mapOf(
            "sapo"      to edtSapo,
            "livro"     to edtLivro,
            "chuva"     to edtChuva,
            "cachorro"  to edtCachorro,
            "elefante"  to edtElefante,
            "borboleta" to edtBorboleta
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
            startActivity(Intent(this, ActivityU5L5EX2::class.java))
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