package net.azarquiel.memorizeme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.LinearLayout
import kotlin.random.Random

class MainActivity : AppCompatActivity(), OnClickListener {
    private lateinit var ivPokemon: ImageView
    private lateinit var llVertical: LinearLayout
    private lateinit var random: Random

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
                x++
                ivPokemon.tag = vPokemonEnJuego[x]
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

    override fun onClick(p0: View?) {
        val iv = p0 as ImageView
        iv.setImageResource(android.R.color.transparent)


    }
}