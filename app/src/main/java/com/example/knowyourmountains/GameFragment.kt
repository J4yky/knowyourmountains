package com.example.knowyourmountains

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.knowyourmountains.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private var allQuestions: List<Question> = emptyList()
    private var currentQuizQuestions: MutableList<Question> = mutableListOf()
    private var currentQuestionIndex: Int = 0
    private var score: Int = 0
    private var totalQuestionsCount: Int = 0

    private var selectedCategory: String = "Całość"
    private var numberOfQuestions: Int = 20

    private lateinit var answerButtons: List<Button>

    private var defaultButtonBackgroundTintList: ColorStateList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedCategory = it.getString("category", "Całość")
            numberOfQuestions = it.getInt("questionCount", 20)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        answerButtons = listOf(
            binding.buttonAnswer1,
            binding.buttonAnswer2,
            binding.buttonAnswer3,
            binding.buttonAnswer4
        )

        defaultButtonBackgroundTintList = binding.buttonAnswer1.backgroundTintList

        answerButtons.forEach { button ->
            button.setOnClickListener {
                setButtonsEnabled(false)
                checkAnswer(it as Button)
            }
        }
        setupQuiz()
    }

    private fun setupQuiz() {
        allQuestions = DataSource.getQuestions()

        val filtered = allQuestions.filter { question ->
            when (selectedCategory) {
                "Polska" -> question.country == "Polska" || question.country == "Polska/Słowacja"
                "Słowacja" -> question.country == "Słowacja" || question.country == "Polska/Słowacja"
                "Całość" -> true
                else -> true
            }
        }

        currentQuizQuestions = filtered.shuffled().take(numberOfQuestions).toMutableList()
        totalQuestionsCount = currentQuizQuestions.size

        if (totalQuestionsCount == 0) {
            Toast.makeText(context, "Brak pytań dla wybranej kategorii!", Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
            return
        }

        loadQuestion()
    }

    private fun loadQuestion() {
        resetButtonBackgrounds()
        setButtonsEnabled(true)

        if (currentQuestionIndex >= totalQuestionsCount) {
            showQuizResult()
            return
        }

        val currentQuestion = currentQuizQuestions[currentQuestionIndex]

        binding.imageViewQuestion.setImageResource(currentQuestion.imageResId)
        binding.textViewQuestionProgress.text = "Pytanie: ${currentQuestionIndex + 1}/$totalQuestionsCount"

        val correctAnswer = currentQuestion.mountainName
        val incorrectAnswersPool = allQuestions.map { it.mountainName }.distinct().filter { it != correctAnswer }.toMutableList()

        while (incorrectAnswersPool.size < 3) {
            incorrectAnswersPool.add("Inny Szczyt ${incorrectAnswersPool.size + 1}")
        }

        val wrongAnswers = incorrectAnswersPool.shuffled().take(3)
        val answerOptions = (wrongAnswers + correctAnswer).shuffled()

        answerOptions.forEachIndexed { index, answerText ->
            if (index < answerButtons.size) {
                answerButtons[index].text = answerText
            }
        }
    }

    private fun checkAnswer(clickedButton: Button) {
        val currentQuestion = currentQuizQuestions[currentQuestionIndex]
        val selectedAnswer = clickedButton.text.toString()
        val correctAnswer = currentQuestion.mountainName

        val correctColor = ContextCompat.getColor(requireContext(), R.color.olive_drab)
        val wrongColor = ContextCompat.getColor(requireContext(), R.color.red)

        if (selectedAnswer == correctAnswer) {
            score++
            clickedButton.backgroundTintList = ColorStateList.valueOf(correctColor)
            Toast.makeText(context, "Poprawna odpowiedź!", Toast.LENGTH_SHORT).show()
        } else {
            clickedButton.backgroundTintList = ColorStateList.valueOf(wrongColor)
            Toast.makeText(context, "Błędna odpowiedź!", Toast.LENGTH_LONG).show()

            answerButtons.forEach { button ->
                if (button.text.toString() == correctAnswer) {
                    button.backgroundTintList = ColorStateList.valueOf(correctColor)
                }
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            currentQuestionIndex++
            loadQuestion()
        }, 1500)
    }

    private fun showQuizResult() {
        val action = GameFragmentDirections.actionGameFragmentToResultFragment(
            score = score,
            totalQuestions = totalQuestionsCount,
            category = selectedCategory,
            questionCount = numberOfQuestions
        )
        findNavController().navigate(action)
    }

    private fun setButtonsEnabled(enabled: Boolean) {
        answerButtons.forEach { it.isEnabled = enabled }
    }

    private fun resetButtonBackgrounds() {
        answerButtons.forEach { button ->
            button.backgroundTintList = defaultButtonBackgroundTintList
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}