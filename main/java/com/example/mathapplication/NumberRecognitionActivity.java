package com.example.mathapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class NumberRecognitionActivity extends AppCompatActivity {

    TextView tvShowNumber, tvNRFeedback, tvAttemptCount;
    Button btnWordOption1, btnWordOption2, btnWordOption3;
    int correctNumber;
    int attemptCount = 0;
    int correctCount = 0;
    int wrongCount = 0;
    String correctWord;

    int min = 1;
    int max = 10;

    // Number to word mapping
    HashMap<Integer, String> numberWords = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_recognition);

        // Avatar
        ImageView imgAvatar = findViewById(R.id.imgAvatar);
        TextView tvAvatarMessage = findViewById(R.id.tvAvatarMessage);
        imgAvatar.setImageResource(R.drawable.shark);
        tvAvatarMessage.setText("Sharko says: Think fast! Which word matches the number?");
        tvAttemptCount = findViewById(R.id.tvAttemptCount);

        // Bind views
        tvShowNumber = findViewById(R.id.tvShowNumber);
        tvNRFeedback = findViewById(R.id.tvNRFeedback);
        btnWordOption1 = findViewById(R.id.btnWordOption1);
        btnWordOption2 = findViewById(R.id.btnWordOption2);
        btnWordOption3 = findViewById(R.id.btnWordOption3);

        Button btnRetry = findViewById(R.id.btnRetry);
        Button btnBackToMenu = findViewById(R.id.btnBackToMenu);

        // Prepare number-to-word mapping
        populateNumberWords();

        // Get difficulty
        Intent intent = getIntent();
        String difficulty = intent.getStringExtra("difficulty");

        if ("medium".equals(difficulty)) {
            min = 11;
            max = 50;
        } else if ("hard".equals(difficulty)) {
            min = 51;
            max = 99;
        }

        // First question
        generateQuestion(min, max);

        // Retry
        btnRetry.setOnClickListener(v -> generateQuestion(min, max));

        // Go back to menu
        btnBackToMenu.setOnClickListener(v -> {
            Intent backIntent = new Intent(NumberRecognitionActivity.this, MainActivity.class);
            startActivity(backIntent);
            finish();
        });
    }

    private void populateNumberWords() {
        String[] units = {
                "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
        };

        String[] teens = {
                "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen",
                "sixteen", "seventeen", "eighteen", "nineteen"
        };

        String[] tens = {
                "", "", "twenty", "thirty", "forty", "fifty",
                "sixty", "seventy", "eighty", "ninety"
        };

        // 0–9
        for (int i = 0; i < 10; i++) {
            numberWords.put(i, units[i]);
        }

        // 10–19
        for (int i = 10; i < 20; i++) {
            numberWords.put(i, teens[i - 10]);
        }

        // 20–99
        for (int i = 20; i < 100; i++) {
            int tensDigit = i / 10;
            int unitDigit = i % 10;

            String word = tens[tensDigit];
            if (unitDigit > 0) {
                word += "-" + units[unitDigit];
            }

            numberWords.put(i, word);
        }
    }

    private void generateQuestion(int min, int max) {
        tvNRFeedback.setText("");
        Random random = new Random();

        correctNumber = random.nextInt(max - min + 1) + min;
        correctWord = numberWords.get(correctNumber);

        // Show number
        tvShowNumber.setText(String.valueOf(correctNumber));

        // Create options
        ArrayList<String> options = new ArrayList<>();
        options.add(correctWord);

        while (options.size() < 3) {
            int fake = random.nextInt(max - min + 1) + min;
            String fakeWord = numberWords.get(fake);

            if (fakeWord != null && !options.contains(fakeWord)) {
                options.add(fakeWord);
            }
        }

        Collections.shuffle(options);

        btnWordOption1.setText(options.get(0));
        btnWordOption2.setText(options.get(1));
        btnWordOption3.setText(options.get(2));

        View.OnClickListener listener = v -> {
            Button clicked = (Button) v;
            String answer = clicked.getText().toString();

            attemptCount++;

            if (answer.equals(correctWord)) {
                correctCount++;
                tvNRFeedback.setText("✅ Correct!");
            } else {
                wrongCount++;
                tvNRFeedback.setText("❌ Try again!");
            }

            tvAttemptCount.setText("Attempts: " + attemptCount +
                    " | ✅ Correct: " + correctCount +
                    " | ❌ Wrong: " + wrongCount);
        };

        btnWordOption1.setOnClickListener(listener);
        btnWordOption2.setOnClickListener(listener);
        btnWordOption3.setOnClickListener(listener);
    }
}