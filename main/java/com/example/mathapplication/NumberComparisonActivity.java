package com.example.mathapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class NumberComparisonActivity extends AppCompatActivity {

    TextView tvNumberLeft, tvNumberRight, tvComparePrompt, tvCompareFeedback, tvAvatarMessage, tvAttemptCount;
    ImageView imgAvatar;
    Button btnLeftIsBigger, btnRightIsBigger, btnCompareRetry, btnCompareBack;
    int leftNumber, rightNumber;
    int attemptCount = 0;
    int correctCount = 0;
    int wrongCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_comparison);

        // Bind views
        imgAvatar = findViewById(R.id.imgAvatar);
        tvAvatarMessage = findViewById(R.id.tvAvatarMessage);
        tvComparePrompt = findViewById(R.id.tvComparePrompt);
        tvNumberLeft = findViewById(R.id.tvNumberLeft);   // ✅ Match your XML ID
        tvNumberRight = findViewById(R.id.tvNumberRight); // ✅ Match your XML ID
        tvCompareFeedback = findViewById(R.id.tvCompareFeedback);
        tvAttemptCount = findViewById(R.id.tvAttemptCount);
        btnLeftIsBigger = findViewById(R.id.btnLeftIsBigger);
        btnRightIsBigger = findViewById(R.id.btnRightIsBigger);
        btnCompareRetry = findViewById(R.id.btnCompareRetry);
        btnCompareBack = findViewById(R.id.btnCompareBack);

        // Set avatar image and message
        imgAvatar.setImageResource(R.drawable.whale);
        tvAvatarMessage.setText("Whaley says: Which number is bigger?");

        // Get difficulty
        Intent intent = getIntent();
        String difficulty = intent.getStringExtra("difficulty");

        int min = 1;
        int max = 10;
        if ("medium".equals(difficulty)) {
            max = 50;
        } else if ("hard".equals(difficulty)) {
            max = 99;
        }

        int finalMin = min;
        int finalMax = max;

        // Load first question
        generateQuestion(finalMin, finalMax);

        // Retry button
        btnCompareRetry.setOnClickListener(v -> generateQuestion(finalMin, finalMax));

        // Back button
        btnCompareBack.setOnClickListener(v -> {
            Intent backIntent = new Intent(NumberComparisonActivity.this, MainActivity.class);
            startActivity(backIntent);
            finish();
        });
    }

    private void generateQuestion(int min, int max) {
        tvCompareFeedback.setText("");

        Random random = new Random();
        leftNumber = random.nextInt(max - min + 1) + min;
        rightNumber = random.nextInt(max - min + 1) + min;

        // Ensure left and right numbers are not equal
        while (leftNumber == rightNumber) {
            rightNumber = random.nextInt(max - min + 1) + min;
        }

        tvNumberLeft.setText(String.valueOf(leftNumber));
        tvNumberRight.setText(String.valueOf(rightNumber));

        View.OnClickListener listener = v -> {
            Button clicked = (Button) v;
            boolean isCorrect = (clicked.getId() == R.id.btnLeftIsBigger && leftNumber > rightNumber)
                    || (clicked.getId() == R.id.btnRightIsBigger && rightNumber > leftNumber);

            if (isCorrect) {
                tvCompareFeedback.setText("✅ Correct!");
            } else {
                tvCompareFeedback.setText("❌ Try again!");
            }

            attemptCount++;
            if (isCorrect) {
                correctCount++;
                tvCompareFeedback.setText("✅ Correct!");
            } else {
                wrongCount++;
                tvCompareFeedback.setText("❌ Try again!");
            }

            tvAttemptCount.setText("Attempts: " + attemptCount +
                    " | ✅ Correct: " + correctCount +
                    " | ❌ Wrong: " + wrongCount);
        };

        btnLeftIsBigger.setOnClickListener(listener);
        btnRightIsBigger.setOnClickListener(listener);
    }
}