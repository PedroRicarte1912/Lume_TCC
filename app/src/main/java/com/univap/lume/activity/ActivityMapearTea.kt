package com.univap.lume.activity

import android.content.Intent
import android.widget.EditText
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.univap.lume.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.univap.lume.model.InformacoesTea
import android.widget.Toast
import com.univap.lume.MainActivity

class ActivityMapearTea: AppCompatActivity() {
    private lateinit var txt_mensagem6: TextView
    private lateinit var txt_mensagem7: TextView
    private lateinit var edt_nivel: EditText
    private lateinit var btn_confirma_mt: Button
    // Set do xml e dos itens dele correspondente a classe ActivityMapearTea
    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(com.univap.lume.R.layout.activity_mapeartea)

        txt_mensagem6=findViewById<TextView>(R.id.txt_mensagem6)
        txt_mensagem7=findViewById<TextView>(R.id.txt_mensagem7)
        edt_nivel=findViewById<EditText>(R.id.edt_nivel)
        btn_confirma_mt=findViewById<Button>(R.id.btn_confirma_mt)

        //Chamada da função mapeamentoTea
        btn_confirma_mt.setOnClickListener {
            mapeamentoTea()
        }
    }
    //Função privada para validar o campo nivel
    private fun mapeamentoTea() {
        val nivelTexto = edt_nivel.text.toString().trim().replace(",", ".")
        val nivelDouble = nivelTexto.toDoubleOrNull()

        if (nivelDouble == null || nivelDouble !in listOf(1.0, 2.0, 3.0)) {
            edt_nivel.error = "Insira um nível entre 1 e 3 (1, 2 ou 3)"
            edt_nivel.requestFocus()
            return
        }

        val nivelInt = nivelDouble.toInt() // Converte 1.0 -> 1, 2.0 -> 2, 3.0 -> 3

        btn_confirma_mt.isEnabled = false

        val idUsuario = FirebaseAuth.getInstance().currentUser?.uid
        if (idUsuario.isNullOrEmpty()) {
            Toast.makeText(this, "Erro: usuário não encontrado", Toast.LENGTH_SHORT).show()
            btn_confirma_mt.isEnabled = true
            return
        }

        val informacoesTea = InformacoesTea(
            id_usuario = idUsuario,
            nivel_tea = nivelInt.toString()
        )

        val referencia = FirebaseDatabase.getInstance().getReference("informacoes_tea")
        referencia.child(idUsuario).setValue(informacoesTea)
            .addOnSuccessListener {
                Toast.makeText(this, "Informações salvas!", Toast.LENGTH_SHORT).show()

                // Salva no SharedPreferences
                val prefs = getSharedPreferences("LumeSettings", MODE_PRIVATE)
                prefs.edit().putInt("NIVEL_TEA", nivelInt).apply()

                // Transmite pela Intent
                val tela_main = Intent(this, MainActivity::class.java).apply {
                    putExtra("NIVEL_TEA", nivelInt)
                }
                startActivity(tela_main)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao salvar: ${it.message}", Toast.LENGTH_SHORT).show()
                btn_confirma_mt.isEnabled = true
            }
    }
}