package com.example.notekeeper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.notekeeper.databinding.ColorSelectorBinding

class ColorSelector @JvmOverloads
constructor(context: Context, attributeSet: AttributeSet? = null,
            defStyle: Int = 0, defRes: Int = 0)
    : LinearLayout(context, attributeSet, defStyle, defRes) {

    private val binding = ColorSelectorBinding.inflate(LayoutInflater.from(context), this)

    private var listOfColors = listOf(Color.BLUE, Color.RED, Color.GREEN)
    private var selectedColorIndex = 0

    init {
        val typedArray = context.obtainStyledAttributes(
                attributeSet, R.styleable.ColorSelector
        )
        listOfColors = typedArray.getTextArray(R.styleable.ColorSelector_colors)
                .map {
                    Color.parseColor(it.toString())
                }
        typedArray.recycle()

        orientation = LinearLayout.HORIZONTAL

        binding.selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])

        binding.colorSelectorArrowLeft.setOnClickListener {
            selectPreviousColor()
        }

        binding.colorSelectorArrowRight.setOnClickListener {
            selectNextColor()
        }

        binding.colorEnabled.setOnCheckedChangeListener { buttonView, isChecked ->
            broadcastColor()
        }
    }

    var selectedColorValue: Int = android.R.color.transparent
        set(value) {
            var index = listOfColors.indexOf(value)
            if (index == -1) {
                binding.colorEnabled.isChecked = false
                index = 0
            } else {
                binding.colorEnabled.isChecked = true
            }
            selectedColorIndex = index
            binding.selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])
        }

    private var colorSelectListeners: ArrayList<(Int) -> Unit> = arrayListOf()

    fun addListener(function: (Int) -> Unit) {
        this.colorSelectListeners.add(function)
    }

    private fun selectPreviousColor() {
        if (selectedColorIndex == 0) {
            selectedColorIndex = listOfColors.lastIndex
        } else {
            selectedColorIndex--
        }
        binding.selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])
        broadcastColor()
    }

    private fun selectNextColor() {
        if (selectedColorIndex == listOfColors.lastIndex) {
            selectedColorIndex = 0
        } else {
            selectedColorIndex++
        }
        binding.selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])
        broadcastColor()
    }

    private fun broadcastColor() {
        val color = if (binding.colorEnabled.isChecked)
            listOfColors[selectedColorIndex]
        else
            Color.TRANSPARENT
        this.colorSelectListeners.forEach { function ->
            function(color)
        }
    }

}