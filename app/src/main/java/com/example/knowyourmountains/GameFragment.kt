package com.example.knowyourmountains

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.knowyourmountains.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private var allQuestions: List<Question> = emptyList() // Pełna pula pytań
    private var currentQuizQuestions: MutableList<Question> = mutableListOf() // Pytania dla bieżącej gry
    private var currentQuestionIndex: Int = 0 // Indeks bieżącego pytania
    private var score: Int = 0 // Liczba poprawnych odpowiedzi
    private var totalQuestionsCount: Int = 0 // Całkowita liczba pytań w tej grze

    private var selectedCategory: String = "Całość" // "Polska", "Słowacja", "Całość"
    private var numberOfQuestions: Int = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Tutaj odbieramy argumenty przekazane ze StartFragment (omówimy to za chwilę)
        // Na razie przyjmijmy domyślne wartości lub ustawimy je na stałe dla testów
        arguments?.let {
            // Te nazwy argumentów będą pochodzić z Safe Args
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

        setupQuiz()

        binding.buttonAnswer1.setOnClickListener { checkAnswer((it as Button).text.toString()) }
        binding.buttonAnswer2.setOnClickListener { checkAnswer((it as Button).text.toString()) }
        binding.buttonAnswer3.setOnClickListener { checkAnswer((it as Button).text.toString()) }
        binding.buttonAnswer4.setOnClickListener { checkAnswer((it as Button).text.toString()) }
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
        if (currentQuestionIndex >= totalQuestionsCount) {
            showQuizResult()
            return
        }

        val currentQuestion = currentQuizQuestions[currentQuestionIndex]

        binding.imageViewQuestion.setImageResource(currentQuestion.imageResId)

        binding.textViewQuestionProgress.text = "Pytanie: ${currentQuestionIndex + 1}/$totalQuestionsCount"

        val correctAnswer = currentQuestion.mountainName

        val incorrectAnswersPool = allQuestions.map { it.mountainName }.distinct().filter { it != correctAnswer }.toMutableList()

        if (incorrectAnswersPool.size < 3) {
            while (incorrectAnswersPool.size < 3) {
                incorrectAnswersPool.add("Inny Szczyt ${incorrectAnswersPool.size + 1}")
            }
        }

        val wrongAnswers = incorrectAnswersPool.shuffled().take(3)

        val answerOptions = (wrongAnswers + correctAnswer).shuffled()

        val answerButtons = listOf(
            binding.buttonAnswer1,
            binding.buttonAnswer2,
            binding.buttonAnswer3,
            binding.buttonAnswer4
        )

        answerOptions.forEachIndexed { index, answerText ->
            if (index < answerButtons.size) {
                answerButtons[index].text = answerText
            }
        }
    }

    private fun checkAnswer(selectedAnswer: String) {
        val currentQuestion = currentQuizQuestions[currentQuestionIndex]

        if (selectedAnswer == currentQuestion.mountainName) {
            score++
            Toast.makeText(context, "Poprawna odpowiedź!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Błędna odpowiedź! Poprawna to: ${currentQuestion.mountainName}", Toast.LENGTH_LONG).show()
        }

        currentQuestionIndex++
        loadQuestion()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}