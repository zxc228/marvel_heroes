package dev.ilya.marvelheroes.screens.heroes.filter

import android.content.Context
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import dev.ilya.marvelheroes.databinding.DialogFilterBinding

class FilterDialog(
    context : Context,
    value :Int? = null,
    range :Pair<Int, Int>,
    callback :(Int?)->Unit
) :AlertDialog(context){
    val binding = DialogFilterBinding.inflate(layoutInflater)

    init {
        binding.seek.min = range.first
        binding.seek.max = range.second
        value
            ?.let {
                binding.seek.progress = it
                binding.value.text = it.toString()
            }
            ?:binding.value.setText(range.first.toString())
        setButton(BUTTON_POSITIVE, "filter"){ _, _ -> callback(binding.seek.progress) }
        setButton(BUTTON_NEUTRAL, "clear"){ _, _ -> callback(null) }
        setButton(BUTTON_NEGATIVE, "cancel"){ _, _ -> }
        setView(binding.root)
        setTitle("Filter")
        binding.seek.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.value.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
}