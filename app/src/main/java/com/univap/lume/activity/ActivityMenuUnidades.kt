package com.univap.lume.activity

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.univap.lume.R

class ActivityMenuUnidades : AppCompatActivity() {

    companion object {
        const val PREFS_NAME = "LumeSettings"
        const val NIVEL_TEA_KEY = "NIVEL_TEA"          // 1, 2 ou 3
        const val PROGRESSO_KEY = "UNIDADE_CONCLUIDA"  // 0 a 5 (0 = nenhuma concluida ainda)

        const val TOTAL_UNIDADES = 5

        // -------------------------------------------------------------
        // Regra de negocio: nivel de TEA define o TETO de unidades
        // que podem ser liberadas, independente do progresso.
        //   Nivel 1 -> libera todas (Unidade 5)
        //   Nivel 2 -> libera ate a Unidade 4
        //   Nivel 3 -> libera ate a Unidade 2
        // -------------------------------------------------------------
        // Define a quantidade máxima de unidades liberadas diretamente pelo nível de TEA
        private fun maxUnidadeParaNivel(nivelTea: Int): Int = when (nivelTea) {
            1 -> TOTAL_UNIDADES // Libera até a 5
            2 -> 4              // Libera até a 4
            3 -> 2              // Libera até a 2
            else -> 1           // Padrão de segurança
        }
    }

    private enum class StatusUnidade {
        CONCLUIDA, ATUAL, BLOQUEADA
    }

    // Referencias das views de cada unidade, guardadas por numero (1 a 5)
    private data class ViewsUnidade(
        val header: LinearLayout,
        val txtNumero: TextView,
        val txtStatus: TextView,
        val txtIndicador: TextView
    )

    private lateinit var viewsPorUnidade: Map<Int, ViewsUnidade>
    private lateinit var progressGeral: ProgressBar
    private lateinit var txtProgressoGeralPorcentagem: TextView

    private var nivelTea = 1
    private var unidadeConcluida = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_unidades)

        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        // 1. Obtém o valor salvo previamente em SharedPreferences
        val nivelSalvo = prefs.getInt(NIVEL_TEA_KEY, 1)

        // 2. Tenta obter o valor repassado via Intent Extra (se houver)
        // Se a Intent tiver o extra 'NIVEL_TEA', ele assume; caso contrário, usa o nivelSalvo
        nivelTea = intent.getIntExtra(NIVEL_TEA_KEY, nivelSalvo).coerceIn(1, 3)

        // 3. Atualiza as SharedPreferences garantindo que o valor mais recente esteja lá
        prefs.edit().putInt(NIVEL_TEA_KEY, nivelTea).apply()

        // 4. Carrega o progresso atual do usuário
        unidadeConcluida = prefs.getInt(PROGRESSO_KEY, 0).coerceIn(0, TOTAL_UNIDADES)

        vincularViews()
        setupUI()
        setupListeners()
    }

    // -------------------------------------------------------------------------
    // Vincular views (equivalente ao ViewBinding, mas com findViewById)
    // -------------------------------------------------------------------------

    private fun vincularViews() {
        viewsPorUnidade = mapOf(
            1 to ViewsUnidade(
                header = findViewById(R.id.headerUnidade1),
                txtNumero = findViewById(R.id.txtNumeroUnidade1),
                txtStatus = findViewById(R.id.txtStatusUnidade1),
                txtIndicador = findViewById(R.id.indicadorUnidade1)
            ),
            2 to ViewsUnidade(
                header = findViewById(R.id.headerUnidade2),
                txtNumero = findViewById(R.id.txtNumeroUnidade2),
                txtStatus = findViewById(R.id.txtStatusUnidade2),
                txtIndicador = findViewById(R.id.indicadorUnidade2)
            ),
            3 to ViewsUnidade(
                header = findViewById(R.id.headerUnidade3),
                txtNumero = findViewById(R.id.txtNumeroUnidade3),
                txtStatus = findViewById(R.id.txtStatusUnidade3),
                txtIndicador = findViewById(R.id.indicadorUnidade3)
            ),
            4 to ViewsUnidade(
                header = findViewById(R.id.headerUnidade4),
                txtNumero = findViewById(R.id.txtNumeroUnidade4),
                txtStatus = findViewById(R.id.txtStatusUnidade4),
                txtIndicador = findViewById(R.id.indicadorUnidade4)
            ),
            5 to ViewsUnidade(
                header = findViewById(R.id.headerUnidade5),
                txtNumero = findViewById(R.id.txtNumeroUnidade5),
                txtStatus = findViewById(R.id.txtStatusUnidade5),
                txtIndicador = findViewById(R.id.indicadorUnidade5)
            )
        )

        progressGeral = findViewById(R.id.progressGeral)
        txtProgressoGeralPorcentagem = findViewById(R.id.txtProgressoGeralPorcentagem)
    }

    // -------------------------------------------------------------------------
    // Setup
    // -------------------------------------------------------------------------

    private fun setupUI() {
        val maxUnidadeNivel = maxUnidadeParaNivel(nivelTea)

        for (numero in 1..TOTAL_UNIDADES) {
            val status = calcularStatus(numero, maxUnidadeNivel)
            aplicarStatusNaUnidade(numero, status)
        }

        val progresso = (unidadeConcluida * 100) / TOTAL_UNIDADES
        progressGeral.progress = progresso
        txtProgressoGeralPorcentagem.text = "$progresso%"
    }

    // Unidade só fica liberada se: (1) o nível de TEA permite chegar até ela
    // e (2) o usuário já concluiu a unidade anterior (não pula etapas).
    private fun calcularStatus(numero: Int, maxUnidadeNivel: Int): StatusUnidade {
        // 1. Verifica se esta unidade é permitida pelo nível de TEA do usuário
        val liberadaPeloNivel = numero <= maxUnidadeNivel

        if (!liberadaPeloNivel) {
            return StatusUnidade.BLOQUEADA
        }

        // 2. Se for Nível 1, libera o acesso a todas as unidades permitidas (1 a 5)
        if (nivelTea == 1) {
            return if (numero <= unidadeConcluida) StatusUnidade.CONCLUIDA else StatusUnidade.ATUAL
        }

        // 3. Para Nível 2 e 3, mantém a trava de progresso sequencial (precisa fazer a anterior)
        val liberadaPeloProgresso = numero <= unidadeConcluida + 1

        return when {
            !liberadaPeloProgresso -> StatusUnidade.BLOQUEADA
            numero <= unidadeConcluida -> StatusUnidade.CONCLUIDA
            else -> StatusUnidade.ATUAL
        }
    }

    private fun aplicarStatusNaUnidade(numero: Int, status: StatusUnidade) {
        val views = viewsPorUnidade[numero] ?: return

        when (status) {
            StatusUnidade.CONCLUIDA -> {
                views.txtNumero.text = "OK"
                views.txtNumero.setBackgroundColor(0xFF22C08C.toInt())
                views.txtNumero.setTextColor(0xFFFFFFFF.toInt())

                views.txtStatus.text = "Concluida"

                views.txtIndicador.text = ">"
                views.txtIndicador.setTextColor(0xFF22C08C.toInt())

                views.header.isClickable = true
                views.header.alpha = 1f
            }

            StatusUnidade.ATUAL -> {
                views.txtNumero.text = numero.toString()
                views.txtNumero.setBackgroundColor(0xFF22C08C.toInt())
                views.txtNumero.setTextColor(0xFFFFFFFF.toInt())

                views.txtStatus.text = "Disponivel"

                views.txtIndicador.text = ">"
                views.txtIndicador.setTextColor(0xFF22C08C.toInt())

                views.header.isClickable = true
                views.header.alpha = 1f
            }

            StatusUnidade.BLOQUEADA -> {
                views.txtNumero.text = numero.toString()
                views.txtNumero.setBackgroundColor(0xFFE4E4EE.toInt())
                views.txtNumero.setTextColor(0xFF9B9BB0.toInt())

                views.txtStatus.text = "Bloqueada"

                views.txtIndicador.text = "X"
                views.txtIndicador.setTextColor(0xFF9B9BB0.toInt())

                views.header.isClickable = false
                views.header.alpha = 0.6f
            }
        }
    }

    // -------------------------------------------------------------------------
    // Listeners
    // -------------------------------------------------------------------------

    private fun setupListeners() {
        findViewById<TextView>(R.id.btnVoltarMenu).setOnClickListener { finish() }

        for (numero in 1..TOTAL_UNIDADES) {
            viewsPorUnidade[numero]?.header?.setOnClickListener {
                abrirUnidadeSeLiberada(numero)
            }
        }
    }

    private fun abrirUnidadeSeLiberada(numero: Int) {
        val maxUnidadeNivel = maxUnidadeParaNivel(nivelTea)
        val status = calcularStatus(numero, maxUnidadeNivel)

        if (status == StatusUnidade.BLOQUEADA) return

        val destino: Class<*>? = when (numero) {
            1 -> ActivityU1L1EX1::class.java
            2 -> ActivityU2EX1::class.java
            3 -> ActivityLicoes3::class.java
            4 -> ActivityLicoes4::class.java
            5 -> ActivityLicoes5::class.java
            else -> null
        }

        destino?.let {
            startActivity(Intent(this, it))
        }
    }
}