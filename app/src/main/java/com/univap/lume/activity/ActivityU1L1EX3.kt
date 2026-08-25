package com.univap.lume.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.univap.lume.R
import java.util.Locale

class ActivityU1L1EX3 : AppCompatActivity() {
    private lateinit var txt_explicacao4: TextView
    private lateinit var btn_proxlicao3: Button
    private lateinit var btn_escutaA: Button
    private lateinit var btn_escutaE: Button
    private lateinit var btn_escutaI: Button
    private lateinit var btn_escutaO: Button
    private lateinit var btn_escutaU: Button

    private var speechRecognizer: SpeechRecognizer? = null
    private var isRecording = false

    // Rastreia quais vogais foram ditas corretamente
    private val vogaisCorretas = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unidade1licao1ex3)

        // Inicialização de UI
        btn_escutaA = findViewById(R.id.btn_escutaA2)
        btn_escutaE = findViewById(R.id.btn_escutaE2)
        btn_escutaI = findViewById(R.id.btn_escutaI2)
        btn_escutaO = findViewById(R.id.btn_escutaO2)
        btn_escutaU = findViewById(R.id.btn_escutaU2)
        btn_proxlicao3 = findViewById(R.id.btn_proxlicao)

        // Configuração dos cliques com a vogal esperada
        btn_escutaA.setOnClickListener { handleSpeechAction(btn_escutaA, "A") }
        btn_escutaE.setOnClickListener { handleSpeechAction(btn_escutaE, "E") }
        btn_escutaI.setOnClickListener { handleSpeechAction(btn_escutaI, "I") }
        btn_escutaO.setOnClickListener { handleSpeechAction(btn_escutaO, "O") }
        btn_escutaU.setOnClickListener { handleSpeechAction(btn_escutaU, "U") }

        btn_proxlicao3.setOnClickListener {
            if (todasVogaisCorretas()) {
                // ✅ Só salva depois de validar, com valor 1
                val prefs = getSharedPreferences("Progresso", MODE_PRIVATE)
                prefs.edit().putInt("U1L3_concluido", 1).apply()

                val intent = Intent(this, ActivityU1L1EX4::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Pronuncie todas as vogais corretamente antes de continuar!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Verifica se todas as 5 vogais foram reconhecidas
    private fun todasVogaisCorretas(): Boolean {
        return vogaisCorretas.containsAll(listOf("A", "E", "I", "O", "U"))
    }

    private fun handleSpeechAction(button: Button, expectedVowel: String) {
        if (isRecording) return

        if (checkPermissions()) {
            startRecognitionProcess(button, expectedVowel)
        } else {
            requestPermissions()
        }
    }

    private fun startRecognitionProcess(button: Button, expectedVowel: String) {
        val originalColor = button.backgroundTintList
        isRecording = true

        button.setBackgroundColor(Color.YELLOW)

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR")
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 2000)
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {
                isRecording = false
            }

            override fun onError(error: Int) {
                isRecording = false
                button.backgroundTintList = originalColor
                Toast.makeText(this@ActivityU1L1EX3, "Erro ao ouvir, tente novamente.", Toast.LENGTH_SHORT).show()
            }

            override fun onResults(results: Bundle?) {
                val data = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val transcription = data?.get(0)?.uppercase(Locale.ROOT) ?: ""

                val success = transcription.contains(expectedVowel)

                if (success) {
                    button.setBackgroundColor(Color.GREEN)
                    Toast.makeText(this@ActivityU1L1EX3, "Correto: $transcription", Toast.LENGTH_SHORT).show()

                    // Marca a vogal como correta
                    vogaisCorretas.add(expectedVowel)

                    // Avisa quando todas estiverem corretas
                    if (todasVogaisCorretas()) {
                        Toast.makeText(
                            this@ActivityU1L1EX3,
                            "Parabéns! Você pronunciou todas as vogais corretamente!",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } else {
                    button.setBackgroundColor(Color.RED)
                    Toast.makeText(this@ActivityU1L1EX3, "Ouvi: $transcription", Toast.LENGTH_SHORT).show()
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    button.backgroundTintList = originalColor
                }, 2000)
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        speechRecognizer?.startListening(intent)

        Handler(Looper.getMainLooper()).postDelayed({
            if (isRecording) {
                speechRecognizer?.stopListening()
            }
        }, 10000)
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer?.destroy()
    }

    private fun checkPermissions() = ActivityCompat.checkSelfPermission(
        this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 101)
    }
}