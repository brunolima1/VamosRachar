package com.example.vamosrachar

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import java.util.*


class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var tts : TextToSpeech? = null
    private var resultado : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val listenButton: Button = findViewById(R.id.listen)
        val shareButton: Button = findViewById(R.id.share)
        val account : EditText = findViewById(R.id.money)
        val people : EditText = findViewById(R.id.peopleCount)
        resultado = findViewById(R.id.result)

        fun updateValue(){
            var valorT = 0.0
            var pessoasT = 0.0
            if(account.text.isNotEmpty()){
                val valorTstr : String = account.text.toString()
                valorT = valorTstr.toDouble()
            }
            if(people.text.isNotEmpty()){
                val pessoasTstr : String = people.text.toString()
                pessoasT = pessoasTstr.toDouble()
            }

            if(account.text.isNotEmpty() and people.text.isNotEmpty()){
                val valorF: Double = valorT/pessoasT
                var valorFstr : String = valorF.toString()
                if(valorFstr == "Infinity"){
                    valorFstr = "0.0"
                }
                if(valorFstr.length > 4){
                    valorFstr = valorFstr.removeRange(5, valorFstr.length)
                }
                if(Locale.getDefault().displayLanguage == "português"){
                    resultado!!.text = ("${valorFstr} R$ para cada")
                }
                else{
                    resultado!!.text = ("${valorFstr} $ for each")
                }
            }

            if(account.text.isEmpty() or people.text.isEmpty()){
                resultado!!.text = ("R$: 0.00")
                if(Locale.getDefault().displayLanguage == "português"){
                    resultado!!.text = ("0.00 R$")
                }
                else{
                    resultado!!.text = ("0.00 $")
                }
            }

        }

        account.addTextChangedListener{
            updateValue()
        }

        people.addTextChangedListener{
            updateValue()
        }

        tts = TextToSpeech(this, this)

        listenButton.setOnClickListener{
            speak()
        }

        shareButton.setOnClickListener{
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