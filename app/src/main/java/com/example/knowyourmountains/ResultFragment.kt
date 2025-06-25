package com.example.knowyourmountains

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.knowyourmountains.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private var finalScore: Int = 0
    private var totalQuestions: Int = 0
    private var selectedCategory: String = "Całość"
    private var numberOfQuestions: Int = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            finalScore = it.getInt("score", 0)
            totalQuestions = it.getInt("totalQuestions", 0)
            selectedCategory = it.getString("category", "Całość")
            numberOfQuestions = it.getInt("questionCount", 20)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.textViewResult.text = "Twój wynik: $finalScore / $totalQuestions"


        binding.buttonBackToMenu.setOnClickListener {
            findNavController().popBackStack(R.id.startFragment, false)
        }

        binding.buttonPlayAgain.setOnClickListener {
            val action = ResultFragmentDirections.actionResultFragmentToGameFragment(
                category = selectedCategory,
                questionCount = numberOfQuestions
            )
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}