package com.example.wordle_and

import android.content.Intent
import android.os.Bundle
import android.text.Spanned
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan

class MainActivity : AppCompatActivity() {

    // Word to be guessed
    private val fourLetterWords = "Star"

    // Variables to track game state
    private lateinit var targetWord: String
    private var remainingGuesses = 3
    private var attemptCount = 1

    // TextViews to display information
    private lateinit var textViewTargetWord: TextView
    private lateinit var textViewAttemptHistory: TextView
    private lateinit var textViewGuessCheckHistory: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize TextViews
        textViewTargetWord = findViewById(R.id.textViewTargetWord)
        textViewAttemptHistory = findViewById(R.id.textViewAttemptHistory)
        textViewGuessCheckHistory = findViewById(R.id.textViewGuessCheckHistory)


        // Initialize the game
        initializeGame()


    }



    // Function to initialize the game
    private fun initializeGame() {
        targetWord = getRandomFourLetterWord()
        remainingGuesses = 3
        attemptCount = 1

        // Reset UI
        findViewById<EditText>(R.id.editTextGuess).text.clear()
        findViewById<Button>(R.id.buttonSubmit).isEnabled = true
        findViewById<Button>(R.id.buttonReset).isEnabled = false

        // Clear TextViews
        textViewTargetWord.text = ""
        textViewAttemptHistory.text = ""
        textViewGuessCheckHistory.text = ""
    }

    // Function called when the user submits a guess
    fun onGuessSubmit(view: View) {
        val guessEditText = findViewById<EditText>(R.id.editTextGuess)
        val guess = guessEditText.text.toString().uppercase()

        // Check if the guess is valid
        if (guess.length != 4 || !guess.isAllAlphabetic()) {
            // Display error for invalid input
            guessEditText.error = "Invalid guess! Must be a 4-letter word."
            return
        }

        // Check the guess and get the result
        val result = checkGuess(guess)

        // Update TextViews
        textViewAttemptHistory.append("Attempt #$attemptCount: $guess\n")
        textViewGuessCheckHistory.append("Guess Check #$attemptCount: ")
        textViewGuessCheckHistory.append(result)
        textViewGuessCheckHistory.append("\n")

        // Display a Toast for the current attempt
        val toastMessage = "Attempt #$attemptCount: Guess: $guess"
        showToast(toastMessage)

        remainingGuesses--
        attemptCount++

        // Check if the game is over
        if (remainingGuesses == 0 || (guess == targetWord)) {
            // Disable submit button after 3 guesses
            findViewById<Button>(R.id.buttonSubmit).isEnabled = false
            findViewById<Button>(R.id.buttonReset).isEnabled = true

            // Display the target word
            textViewTargetWord.text = "Target Word: $targetWord"
        }
    }

    // Function to display a Toast message
    private fun showToast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.show()
    }

    // Function called when the user clicks the Reset button
    fun onResetClick(view: View) {
        // Initialize the game for a new round
        initializeGame()
    }

    // Extension function to check if a string contains only alphabetic characters
    fun String.isAllAlphabetic(): Boolean {
        return all { it.isLetter() }
    }

    // Function to get a random 4-letter word from the predefined list
    private fun getRandomFourLetterWord(): String {
        val allWords = fourLetterWords.split(",")
        val randomNumber = (0 until allWords.size).shuffled().first()
        return allWords[randomNumber].uppercase()
    }

    // Function to check the user's guess and return a Spanned result
    private fun checkGuess(guess: String): Spanned {
        val coloredLetters = SpannableStringBuilder()
        for (i in 0 until 4) {
            val currentLetter = guess[i]
            when {
                currentLetter == targetWord[i] -> {
                    // Letter matches in position, set color to green
                    coloredLetters.append(currentLetter.toString(), ForegroundColorSpan(Color.GREEN), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                currentLetter in targetWord -> {
                    // Letter matches, but in the wrong position, set color to red
                    coloredLetters.append(currentLetter.toString(), ForegroundColorSpan(Color.RED), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                else -> {
                    // Letter doesn't match, set color to grey
                    coloredLetters.append(currentLetter.toString(), ForegroundColorSpan(Color.GRAY), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }

        return coloredLetters
    }
}
