package com.zasa.tippy

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sliderOnChange()
        baseAmountChangeListner()
    }

    private fun updateTextDescription(tipPercent: Int) {

        val tipDescription = when(tipPercent){
            in 0..9 -> "POOR"
            in 10..14 -> "ACCEPTABLE"
            in 15..19 -> "GOOD"
            in 20..24 -> "GREAT"
            else -> "AMAZING"
        }
        tv_desText.text = tipDescription
        val colorChange = ArgbEvaluator().evaluate(
            tipPercent.toFloat()/seekbar.max,
            ContextCompat.getColor(this,R.color.color_worst_tip),
            ContextCompat.getColor(this,R.color.colo_best_tip),
        ) as Int
        tv_desText.setTextColor(colorChange)
    }

    private fun sliderOnChange(){
        seekbar.progress = INITIAL_TIP_PERCENT
        tv_percentage.text = "$INITIAL_TIP_PERCENT%"
        updateTextDescription(INITIAL_TIP_PERCENT)
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_percentage.text = "$progress%"
                computeTipTotal()
                updateTextDescription(progress)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    private fun baseAmountChangeListner(){
        et_baseAmount.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "onBaseAmountChanges ${et_baseAmount.text}")
                computeTipTotal()
            }
        })
    }

    private fun computeTipTotal() {
        if (et_baseAmount.text!!.isEmpty()){
            tv_tipAmount.text = ""
            tv_totalAmount.text = ""

            return
        }
        val baseAmountToDouble = et_baseAmount.text.toString().toDouble()
            val tipPercentage = seekbar.progress
            val tip  = baseAmountToDouble * tipPercentage/100
            tv_tipAmount.text = "%.2f".format(tip)

            val totalAmount = baseAmountToDouble + tip
            tv_totalAmount.text = "%.2f".format(totalAmount)
    }
}