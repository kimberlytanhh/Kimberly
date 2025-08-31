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
import java.util.Random;

public class MissingNumberActivity extends AppCompatActivity {

    TextView tvPattern, tvMNFeedback, tvAttemptCount;
    Button btnMissingOption1, btnMissingOption2, btnMissingOption3, btnRetry, btnBackToMenu;

    int correctAnswer;
    int attemptCount = 0;
    int correctCount = 0;
    int wrongCount = 0;
    int min = 1;
    int max = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing_number);

        // Bind avatar and message
        ImageView imgAvatar = findViewById(R.id.imgAvatar);
        TextView tvAvatarMessage = findViewById(R.id.tvAvatarMessage);
        imgAvatar.setImageResource(R.drawable.dolphin);
        tvAvatarMessage.setText("Tiki says: Can you find the missing number in the pattern?");
        tvAttemptCount = findViewById(R.id.tvAttemptCount);

        // Bind views
        tvPattern = findViewById(R.id.tvPattern);
        tvMNFeedback = findViewById(R.id.tvMNFeedback);
        btnMissingOption1 = findViewById(R.id.btnMissingOption1);
        btnMissingOption2 = findViewById(R.id.btnMissingOption2);
        btnMissingOption3 = findViewById(R.id.btnMissingOption3);
        btnRetry = findViewById(R.id.btnRetry);
        btnBackToMenu = findViewById(R.id.btnBackToMenu);

        // Get difficulty
        Intent intent = getIntent();
        String difficulty = intent.getStringExtra("difficulty");

        if ("medium".equals(difficulty)) {
            max = 50;
        } else if ("hard".equals(difficulty)) {
            max = 99;
        }

        // Generate first question
        generatePattern(min, max);

        // Button logic
        btnRetry.setOnClickListener(v -> generatePattern(min, max));

        btnBackToMenu.setOnClickListener(v -> {
            Intent backIntent = new Intent(MissingNumberActivity.this, MainActivity.class);
            startActivity(backIntent);
            finish();
        });
    }

    private void generatePattern(int min, int max) {
        tvMNFeedback.setText("");
        Random random = new Random();

        int step = random.nextInt(4) + 1;
        int maxStart = max - (step * 3);
        if (maxStart < min) {
            maxStart = min;
        }
        int start = random.nextInt(maxStart - min + 1) + min;

        int[] pattern = new int[4];
        for (int i = 0; i < 4; i++) {
            pattern[i] = start + (i * step);
        }

        int missingIndex = random.nextInt(4);
        correctAnswer = pattern[missingIndex];

        StringBuilder display = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (i == missingIndex) {
                display.append("__ ");
            } else {
                display.append(pattern[i]).append(" ");
            }
        }
        tvPattern.setText(display.toString().trim());

        ArrayList<Integer> options = new ArrayList<>();
        options.add(correctAnswer);
        while (options.size() < 3) {
            int fake = correctAnswer + random.nextInt(7) - 3;
            if (fake > 0 && fake != correctAnswer && !options.contains(fake)) {
                options.add(fake);
            }
        }
        Collections.shuffle(options);

        btnMissingOption1.setText(String.valueOf(options.get(0)));
        btnMissingOption2.setText(String.valueOf(options.get(1)));
        btnMissingOption3.setText(String.valueOf(options.get(2)));

        View.OnClickListener listener = v -> {
            Button clicked = (Button) v;
            int selected = Integer.parseInt(clicked.getText().toString());

            attemptCount++;
            if (selected == correctAnswer) {
                correctCount++;
                tvMNFeedback.setText("✅ Correct!");
            } else {
                wrongCount++;
                tvMNFeedback.setText("❌ Try again!");
            }

            tvAttemptCount.setText("Attempts: " + attemptCount +
                    " | ✅ Correct: " + correctCount +
                    " | ❌ Wrong: " + wrongCount);
        };

        btnMissingOption1.setOnClickListener(listener);
        btnMissingOption2.setOnClickListener(listener);
        btnMissingOption3.setOnClickListener(listener);
    }
}
