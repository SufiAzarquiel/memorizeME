package net.azarquiel.memorizeme

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.random.Random

class MainActivity : AppCompatActivity(), OnClickListener {
    private var tagCartaPulsada: Int = 0
    private var isFirst: Boolean = true
    private var aciertos: Int = 0
    private lateinit var ivPrimera: ImageView
    private lateinit var ivPokemon: ImageView
    private lateinit var llVertical: LinearLayout
    private lateinit var random: Random
    private lateinit var horaInicio: Date

    var vPokemon = Array(809) { i -> i + 1 }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        llVertical = findViewById(R.id.llVertical)

        random = Random(System.currentTimeMillis())
        newGame()

        var x = 0
        for (i in 0 until llVertical.childCount) {
            var llHorizontal = llVertical.getChildAt(i) as LinearLayout

            for (j in 0 until llHorizontal.childCount) {
                ivPokemon = llHorizontal.getChildAt(j) as ImageView
                ivPokemon.setOnClickListener(this)
            }
        }
    }

    private fun newGame() {
        horaInicio = Date()
        pintarPokemon()
    }

    private fun pintarPokemon() {
        var vPokemonEnJuego = crear30Pokemon()

        var x = 0
        for (i in 0 until llVertical.childCount) {
            var llHorizontal = llVertical.getChildAt(i) as LinearLayout

            for (j in 0 until llHorizontal.childCount) {
                ivPokemon = llHorizontal.getChildAt(j) as ImageView
                var idFoto =
                    resources.getIdentifier("pokemon${vPokemonEnJuego[x]}", "drawable", packageName)
                ivPokemon.tag = vPokemonEnJuego[x]
                x++
                ivPokemon.setBackgroundResource(idFoto)
                ivPokemon.setImageResource(R.drawable.tapa)
                ivPokemon.setOnClickListener { pokemOnClick() }
            }
        }
    }

    private fun pokemOnClick() {
        ivPokemon.setImageResource(android.R.color.transparent)
    }

    private fun crear30Pokemon(): Array<Int> {
        vPokemon.shuffle(random)
        var c = 0
        var array = Array(30) { 0 }
        for (i in 0 until 15) {
            array[i] = vPokemon[c]
            c++
        }
        c = 0
        for (j in 15 until 30) {
            array[j] = vPokemon[c]
            c++
        }
        array.shuffle()
        return array
    }

    override fun onClick(v: View?) {
        /* TODO
         * On first card click, store it
         * On second card click, compare it to first:
         *  if equal keep them revealed
         *  else hide them after a small delay
         */
        val iv = v as ImageView
        iv.setImageResource(android.R.color.transparent)
        tagCartaPulsada = iv.tag as Int

        if (isFirst) {
            ivPrimera = iv
            isFirst = false
        } else {
            checkCarta(iv)
        }
    }

    private fun checkCarta(iview: ImageView) {
        if (iview.equals(ivPrimera)) {
            tostada("No pulses la misma manin")
        } else {
            if (tagCartaPulsada == ivPrimera.tag as Int) {
                sumaPunto()
                iview.isEnabled = false
                ivPrimera.isEnabled = false
            } else {
                GlobalScope.launch() {
                    SystemClock.sleep(250)
                    launch(Main) {
                        ivPrimera.setImageResource(R.drawable.tapa)
                        iview.setImageResource(R.drawable.tapa)
                    }
                }
            }
            isFirst = true
        }
    }

    private fun sumaPunto() {
        aciertos++
        finishGame()
        if (aciertos == 15) {
            finishGame()
        } else {
            tostada("Has acertado $aciertos veces")
        }
    }

    private fun finishGame() {
        var ahora = Date()
        var diff = ahora.time - horaInicio.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        var mensajeFinal = "Felicidades!\nHas tardado: $hours:$minutes:$seconds"
        AlertDialog.Builder(this)
            .setTitle("Partida completada")
            .setMessage(mensajeFinal)
            .setPositiveButton("Jugar de nuevo") { _, _ ->
                restartGame()
            }
            .setNegativeButton("Salir") { _, _ ->
                finish()
            }
            .show()
    }

    private fun restartGame() {
        tagCartaPulsada = 0
        isFirst = true
        aciertos = 0
        for (i in 0 until llVertical.childCount) {
            var llHorizontal = llVertical.getChildAt(i) as LinearLayout

            llHorizontal.removeAllViews()
        }
        newGame()
    }

    private fun tostada(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}