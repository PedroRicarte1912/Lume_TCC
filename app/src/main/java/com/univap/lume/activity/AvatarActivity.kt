package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import com.univap.lume.model.MidiaLocalAvatar1
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.univap.lume.MainActivity
import com.google.android.material.imageview.ShapeableImageView
import com.univap.lume.R

class AvatarActivity : AppCompatActivity() {

    private lateinit var txt_mensagem2: TextView
    private lateinit var avatar1: ShapeableImageView
    private lateinit var avatar2: ShapeableImageView
    private lateinit var avatar3: ShapeableImageView
    private lateinit var avatar4: ShapeableImageView
    private lateinit var avatar5: ShapeableImageView
    private lateinit var avatar6: ShapeableImageView
    private lateinit var btn_confirmar_avatar: Button

    private var avatarSelecionado: String = ""
    private var usuarioIdRecebido: String? = null

    // Lista de todos os avatars, pra facilitar percorrer e atualizar a borda
    private val avatarViews by lazy {
        listOf(
            avatar1 to "1",
            avatar2 to "2",
            avatar3 to "3",
            avatar4 to "4",
            avatar5 to "5",
            avatar6 to "6"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar)

        val sharedPreferences = getSharedPreferences("LumeSettings", MODE_PRIVATE)
        usuarioIdRecebido = sharedPreferences.getString("USER_ID_KEY", null)

        txt_mensagem2 = findViewById(R.id.txt_mensagem2)
        avatar1 = findViewById(R.id.avatar1)
        avatar2 = findViewById(R.id.avatar2)
        avatar3 = findViewById(R.id.avatar3)
        avatar4 = findViewById(R.id.avatar4)
        avatar5 = findViewById(R.id.avatar5)
        avatar6 = findViewById(R.id.avatar6)
        btn_confirmar_avatar = findViewById(R.id.btn_confirmar_avatar)

        avatar1.setOnClickListener { selecionarAvatar("1") }
        avatar2.setOnClickListener { selecionarAvatar("2") }
        avatar3.setOnClickListener { selecionarAvatar("3") }
        avatar4.setOnClickListener { selecionarAvatar("4") }
        avatar5.setOnClickListener { selecionarAvatar("5") }
        avatar6.setOnClickListener { selecionarAvatar("6") }

        btn_confirmar_avatar.setOnClickListener {
            if (avatarSelecionado.isNotEmpty()) {
                registraAvatar()
            } else {
                Toast.makeText(this, "Por favor, selecione um avatar primeiro!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Destaca visualmente o avatar clicado com uma borda colorida
     * e um leve efeito de "pulso", e remove a borda dos demais.
     */
    private fun selecionarAvatar(numero: String) {
        avatarSelecionado = numero

        val strokeWidthPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics
        )

        avatarViews.forEach { (view, id) ->
            if (id == numero) {
                view.strokeWidth = strokeWidthPx

                // Efeito de destaque: dá um leve "pulo" de escala
                view.animate()
                    .scaleX(1.1f)
                    .scaleY(1.1f)
                    .setDuration(120)
                    .withEndAction {
                        view.animate().scaleX(1f).scaleY(1f).setDuration(120).start()
                    }
                    .start()
            } else {
                view.strokeWidth = 0f
            }
        }
    }

    private fun registraAvatar() {
        val id_user = usuarioIdRecebido ?: FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // ex: "avatar12", "avatar22", ..., "avatar62"
        val nomeArquivoDrawable = "avatar${avatarSelecionado}2"

        if (id_user.isNotEmpty()) {
            val referencia = FirebaseDatabase.getInstance().getReference("midias_avatar")

            val novoAvatar = MidiaLocalAvatar1(
                id_usuario = id_user,
                id_midiaavatar = id_user,
                nome_res_avatar = nomeArquivoDrawable,
                titulo = "Avatar do Usuário"
            )

            referencia.child(id_user).setValue(novoAvatar).addOnSuccessListener {

                getSharedPreferences("LumeSettings", MODE_PRIVATE)
                    .edit()
                    .putString("AVATAR_KEY", nomeArquivoDrawable)
                    .apply()

                Toast.makeText(this, "Avatar salvo com sucesso!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()

            }.addOnFailureListener {
                Toast.makeText(this, "Erro ao salvar no banco", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Erro: Usuário não identificado", Toast.LENGTH_SHORT).show()
        }
    }
}