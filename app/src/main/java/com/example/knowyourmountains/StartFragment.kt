package com.example.knowyourmountains

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.knowyourmountains.databinding.FragmentStartBinding
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.materialswitch.MaterialSwitch

class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    private var selectedQuestionCount: Int = 10
    private var selectedCategoryButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedQuestionCount = binding.howManyQuestions.progress
        binding.howManyQuestionsTextView.text = "Ilość pytań: $selectedQuestionCount"

        binding.howManyQuestions.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                selectedQuestionCount = if (progress < 1) 1 else progress
                binding.howManyQuestionsTextView.text = "Ilość pytań: $selectedQuestionCount"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val themeSwitch: MaterialSwitch = binding.themeSwitch

        val currentNightMode = AppCompatDelegate.getDefaultNightMode()
        if (currentNightMode == AppCompatDelegate.MODE_NIGHT_YES ||
            (currentNightMode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM && isSystemInNightMode())
        ) {
            themeSwitch.isChecked = true
            themeSwitch.text = "Tryb Dzienny"
        } else {
            themeSwitch.isChecked = false
            themeSwitch.text = "Tryb Nocny"
        }

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                themeSwitch.text = "Tryb Dzienny"
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                themeSwitch.text = "Tryb Nocny"
            }
        }

        binding.startButton.setOnClickListener {
            val category: String = when (selectedCategoryButton?.text.toString()) {
                "Tatry: Polskie" -> "Polska"
                "Tatry: Słowackie" -> "Słowacja"
                "Tatry: Całość" -> "Całość"
                else -> "Całość"
            }

            val action = StartFragmentDirections.actionStartFragmentToGameFragment(
                category = category,
                questionCount = selectedQuestionCount
            )
            findNavController().navigate(action)
        }

        val categoryButtons = listOf(
            binding.buttonPolishTatry,
            binding.buttonSlovakTatry,
            binding.buttonAllTatry
        )

        val categoryClickListener = View.OnClickListener { clickedButton ->
            if (selectedCategoryButton == clickedButton) {
                return@OnClickListener
            }

            categoryButtons.forEach { button ->
                if (button == clickedButton) {
                    button.alpha = 1.0f
                    selectedCategoryButton = button
                } else {
                    button.alpha = 0.5f
                }
            }
            android.widget.Toast.makeText(context, "Wybrano: ${(clickedButton as Button).text}", android.widget.Toast.LENGTH_SHORT).show()
        }

        binding.buttonFranceAlpy.setOnClickListener {
            if (binding.buttonFranceAlpy.isEnabled) {
                android.widget.Toast.makeText(context, "Ta sekcja wkrótce dostępna!", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonPolishTatry.setOnClickListener(categoryClickListener)
        binding.buttonSlovakTatry.setOnClickListener (categoryClickListener)
        binding.buttonAllTatry.setOnClickListener(categoryClickListener)
    }

    private fun isSystemInNightMode(): Boolean {
        val currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}