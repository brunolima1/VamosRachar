package com.example.vamosrachar

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener


class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var tts : TextToSpeech? = null
    private var resultado : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val listenButton: Button = findViewById(R.id.listen)
        val shareButton: Button = findViewById(R.id.share)
        val conta : EditText = findViewById(R.id.money)
        val pessoas : EditText = findViewById(R.id.peopleCount)
        resultado = findViewById(R.id.result)

        fun updateValue(){
            var valorT = 0.0
            var pessoasT = 0.0
            if(conta.text.isNotEmpty()){
                val valorTstr : String = conta.text.toString()
                valorT = valorTstr.toDouble()
            }
            if(pessoas.text.isNotEmpty()){
                val pessoasTstr : String = pessoas.text.toString()
                pessoasT = pessoasTstr.toDouble()
            }

            if(conta.text.isNotEmpty() and pessoas.text.isNotEmpty()){
                val valorF: Double = valorT/pessoasT
                var valorFstr : String = valorF.toString()
                if(valorFstr == "Infinity"){
                    valorFstr = "0.0"
                }
                if(valorFstr.length > 4){
                    valorFstr = valorFstr.removeRange(5, valorFstr.length)
                }
                resultado!!.text = ("R$: ${valorFstr}")
            }

            if(conta.text.isEmpty() or pessoas.text.isEmpty()){
                resultado!!.text = ("R$: 0.00")

            }

        }

        conta.addTextChangedListener{
            updateValue()
        }

        pessoas.addTextChangedListener{
            updateValue()
        }

        tts = TextToSpeech(this, this)

        listenButton!!.setOnClickListener{
            speak()
        }

        shareButton!!.setOnClickListener{
            val myIntent = Intent(Intent.ACTION_SEND)
            myIntent.type = "type/palin"
            val shareBody = "Resultado VamosRachar"
            val shareSub = "A divisão de pagamento resultou em: " + resultado!!.text.toString() + " para cada pessoa."
            myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody)
            myIntent.putExtra(Intent.EXTRA_TEXT, shareSub)
            startActivity(Intent.createChooser(myIntent, "Compartilhe a Conta"))
        }
    }
    override fun onInit(status: Int){

    }

    private fun speak(){
        val text = resultado!!.text.toString()
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onDestroy(){
        if(tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

}