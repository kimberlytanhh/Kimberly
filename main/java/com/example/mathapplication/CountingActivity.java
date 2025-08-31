package com.example.mathapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CountingActivity extends AppCompatActivity {

    TextView tvQuestion, tvFeedback, tvAttemptCount;
    GridLayout layoutImages;
    Button btnOption1, btnOption2, btnOption3, btnRetry, btnBackToMenu;
    int correctAnswer;
    int attemptCount = 0;
    int correctCount = 0;
    int wrongCount = 0;
    int min = 1;
    int max = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counting);

        // Bind avatar
        ImageView imgAvatar = findViewById(R.id.imgAvatar);
        TextView tvAvatarMessage = findViewById(R.id.tvAvatarMessage);
        imgAvatar.setImageResource(R.drawable.octopus);
        tvAvatarMessage.setText("Ollie says: Count these carefully!");
        tvAttemptCount = findViewById(R.id.tvAttemptCount);

        // Bind main views
        tvQuestion = findViewById(R.id.tvQuestion);
        tvFeedback = findViewById(R.id.tvFeedback);
        layoutImages = findViewById(R.id.layoutImages);
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        btnRetry = findViewById(R.id.btnRetry);
        btnBackToMenu = findViewById(R.id.btnBackToMenu);

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

        generateQuestion(min, max);

        btnRetry.setOnClickListener(v -> generateQuestion(min, max));

        btnBackToMenu.setOnClickListener(v -> {
            Intent backIntent = new Intent(CountingActivity.this, MainActivity.class);
            startActivity(backIntent);
            finish();
        });
    }

    private void generateQuestion(int min, int max) {
        layoutImages.removeAllViews();
        tvFeedback.setText("");

        Random random = new Random();
        correctAnswer = random.nextInt(max - min + 1) + min;

        tvQuestion.setText("How many apples are there?");

        // Add apple images
        for (int i = 0; i < correctAnswer; i++) {
            ImageView img = new ImageView(this);
            img.setImageResource(R.drawable.apple);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 100;
            params.height = 100;
            params.setMargins(8, 8, 8, 8);
            img.setLayoutParams(params);
            layoutImages.addView(img);
        }

        // Set answer options
        ArrayList<Integer> options = new ArrayList<>();
        options.add(correctAnswer);
        while (options.size() < 3) {
            int wrong = correctAnswer + random.nextInt(10) - 5;
            if (wrong > 0 && !options.contains(wrong)) {
                options.add(wrong);
            }
        }
        Collections.shuffle(options);

        btnOption1.setText(String.valueOf(options.get(0)));
        btnOption2.setText(String.valueOf(options.get(1)));
        btnOption3.setText(String.valueOf(options.get(2)));

        View.OnClickListener listener = v -> {
            Button clicked = (Button) v;
            int selected = Integer.parseInt(clicked.getText().toString());

            attemptCount++;
            if (selected == correctAnswer) {
                correctCount++;
                tvFeedback.setText("✅ Correct!");
            } else {
                wrongCount++;
                tvFeedback.setText("❌ Try again!");
            }

            tvAttemptCount.setText("Attempts: " + attemptCount +
                    " | ✅ Correct: " + correctCount +
                    " | ❌ Wrong: " + wrongCount);
        };

        btnOption1.setOnClickListener(listener);
        btnOption2.setOnClickListener(listener);
        btnOption3.setOnClickListener(listener);
    }
}
