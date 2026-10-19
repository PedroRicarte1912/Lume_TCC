package com.univap.lume.activity

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.univap.lume.MainActivity
import com.univap.lume.R
import com.univap.lume.databinding.ActivityParabensBinding

class ActivityParabens : AppCompatActivity() {

    companion object {
        const val EXTRA_UNIDADE_CONCLUIDA = "extra_unidade_concluida"
        const val TOTAL_UNIDADES = 5

        // AvatarActivity salva: "avatar12", "avatar22", ..., "avatar62"
        // Agora usamos direto a imagem estática do avatar
        private val avatarMap = mapOf(
            "avatar12" to R.drawable.avatar12,
            "avatar22" to R.drawable.avatar22,
            "avatar32" to R.drawable.avatar32,
            "avatar42" to R.drawable.avatar42,
            "avatar52" to R.drawable.avatar52,
            "avatar62" to R.drawable.avatar62
        )

        // Título de destaque, muda a cada unidade concluída
        private val tituloMap = mapOf(
            1 to "Primeiro passo! 🎉",
            2 to "Mandou bem! 🔥",
            3 to "Na metade! 🚀",
            4 to "Quase lá! ✨",
            5 to "Você é demais! 🏆"
        )

        // Subtítulo, muda a cada unidade concluída
        private val subtituloMap = mapOf(
            1 to "Você concluiu a Unidade 1!",
            2 to "Você concluiu a Unidade 2!",
            3 to "Você concluiu a Unidade 3!",
            4 to "Você concluiu a Unidade 4!",
            5 to "Você concluiu TODOS os exercícios do app!"
        )

        private val motivacaoMap = mapOf(
            1 to "Você deu o primeiro passo! Continue assim para dominar todos os sons do português!",
            2 to "Já são duas unidades! Você está construindo uma base incrível! 💪",
            3 to "Metade do caminho! Seu progresso é impressionante! 🚀",
            4 to "Quase lá! Só mais uma unidade para completar tudo! ✨",
            5 to "Parabéns por concluir cada exercício de cada unidade! Você terminou o curso inteiro! 🏆"
        )
    }

    private lateinit var binding: ActivityParabensBinding

    private var unidadeConcluida = 1
    private var nomeAvatar = "avatar12"
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParabensBinding.inflate(layoutInflater)
        setContentView(binding.root)

        unidadeConcluida = intent.getIntExtra(EXTRA_UNIDADE_CONCLUIDA, 1)
            .coerceIn(1, TOTAL_UNIDADES)

        // Cache local: mostra o avatar na hora, antes de o Firebase responder
        nomeAvatar = getSharedPreferences("LumeSettings", MODE_PRIVATE)
            .getString("AVATAR_KEY", "avatar12") ?: "avatar12"

        // Salva o progresso (nunca diminui; refazer uma unidade antiga não altera nada)
        ProgressoRepository.salvarUnidadeConcluida(this, unidadeConcluida)

        setupUI()
        setupListeners()

        // Corrige com o que está no Firebase e toca o som escolhido
        PreferenciasRepositorio.carregar(this) { avatar, som ->
            if (isFinishing || isDestroyed) return@carregar

            if (avatar != null && avatar != nomeAvatar) {
                nomeAvatar = avatar
                carregarAvatarPersonagem()
            }
            if (som != null) tocarSom(som)
        }
    }

    // -------------------------------------------------------------------------
    // Setup
    // -------------------------------------------------------------------------

    private fun setupUI() {
        binding.txtParabensTitulo.text = tituloMap[unidadeConcluida] ?: "Parabéns! 🎉"
        binding.txtParabensSubtitulo.text = subtituloMap[unidadeConcluida]
            ?: "Você concluiu a Unidade $unidadeConcluida!"

        carregarAvatarPersonagem()

        val porcentagem = (unidadeConcluida * 100) / TOTAL_UNIDADES
        binding.progressUnidades.progress = porcentagem
        binding.txtPorcentagem.text = "$porcentagem%"

        atualizarDotsUnidade()

        binding.txtMotivacional.text = motivacaoMap[unidadeConcluida] ?: "Continue se dedicando!"

        if (unidadeConcluida >= TOTAL_UNIDADES) {
            // Não existe próxima unidade: o botão passa a levar para a tela inicial
            binding.btnProximaUnidade.text = "Voltar para o Início"
            binding.btnRefazerUnidade.visibility = View.GONE
        }
    }

    private fun carregarAvatarPersonagem() {
        val avatarRes = avatarMap[nomeAvatar] ?: R.drawable.avatar12
        binding.imgPersonagemGif.setImageResource(avatarRes)
    }

    private fun atualizarDotsUnidade() {
        val dots = listOf(
            binding.dotUnidade1,
            binding.dotUnidade2,
            binding.dotUnidade3,
            binding.dotUnidade4,
            binding.dotUnidade5
        )

        dots.forEachIndexed { index, dot ->
            val numero = index + 1
            when {
                numero < unidadeConcluida -> {
                    dot.text = "✓"
                    dot.setTextColor(android.graphics.Color.WHITE)
                    dot.setBackgroundResource(R.drawable.bg_dot_verde)
                }
                numero == unidadeConcluida -> {
                    dot.text = numero.toString()
                    dot.setTextColor(getColor(R.color.lume_verde))
                    dot.setBackgroundResource(R.drawable.bg_dot_verde_outline)
                }
                else -> {
                    dot.text = numero.toString()
                    dot.setTextColor(getColor(R.color.lume_texto))
                    dot.setBackgroundResource(R.drawable.bg_dot_cinza)
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Som
    // -------------------------------------------------------------------------

    private fun tocarSom(nome: String) {
        val resId = when (nome) {
            "som1" -> R.raw.som1
            "som2" -> R.raw.som2
            "som3" -> R.raw.som3
            else -> return
        }

        pararSom()
        mediaPlayer = MediaPlayer.create(this, resId)?.apply {
            setOnCompletionListener {
                it.release()
                mediaPlayer = null
            }
            start()
        }
    }

    private fun pararSom() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onStop() {
        super.onStop()
        // Se o usuário sair da tela (ex.: abrir "Sobre"), o som para
        pararSom()
    }

    override fun onDestroy() {
        super.onDestroy()
        pararSom()
    }

    // -------------------------------------------------------------------------
    // Listeners
    // -------------------------------------------------------------------------

    private fun setupListeners() {
        binding.btnProximaUnidade.setOnClickListener {
            if (unidadeConcluida >= TOTAL_UNIDADES) {
                voltarParaTelaInicial()
            } else {
                navegarParaProximaUnidade()
            }
        }

        binding.btnRefazerUnidade.setOnClickListener {
            refazerUnidade()
        }

        binding.btnAboutParabens.setOnClickListener {
            startActivity(Intent(this, ActivityAboutLicoes::class.java))
        }

        binding.btnStopParabens.setOnClickListener {
            startActivity(
                Intent(this, ActivityStopLicoesAbout::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
    }

    private fun navegarParaProximaUnidade() {
        val proximaUnidade = unidadeConcluida + 1
        if (proximaUnidade > TOTAL_UNIDADES) return

        val destino: Class<*>? = when (proximaUnidade) {
            2 -> ActivityU2EX1::class.java
            3 -> ActivityU3L3EX1::class.java
            4 -> ActivityU4L4EX1::class.java
            5 -> ActivityU5L5EX1::class.java
            else -> null
        }

        destino?.let {
            startActivity(Intent(this, it))
            finish()
        }
    }

    private fun voltarParaTelaInicial() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun refazerUnidade() {
        val destino: Class<*>? = when (unidadeConcluida) {
            1 -> ActivityU1L1EX1::class.java
            2 -> ActivityU2EX1::class.java
            3 -> ActivityU3L3EX1::class.java
            4 -> ActivityU4L4EX1::class.java
            5 -> ActivityU5L5EX1::class.java
            else -> null
        }

        destino?.let {
            startActivity(Intent(this, it))
            finish()
        }
    }
}