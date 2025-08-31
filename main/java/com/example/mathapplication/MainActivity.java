package com.example.mathapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imgAvatar = findViewById(R.id.imgAvatar);
        TextView tvAvatarMessage = findViewById(R.id.tvAvatarMessage);

        // Set avatar image and message
        imgAvatar.setImageResource(R.drawable.fish);
        tvAvatarMessage.setText("Finn says: Let's get started! What would you like to do today?");

        // Buttons
        Button btnCounting = findViewById(R.id.button);
        Button btnNumberWords = findViewById(R.id.button2);
        Button btnMissingNumber = findViewById(R.id.button3);
        Button btnNumberComparison = findViewById(R.id.button4);  // Make sure button4 exists in activity_main.xml

        // COUNTING
        btnCounting.setOnClickListener(v -> {
            showDifficultyDialog(CountingActivity.class);
        });

        // NUMBER TO WORDS
        btnNumberWords.setOnClickListener(v -> {
            showDifficultyDialog(NumberRecognitionActivity.class);
        });

        // MISSING NUMBER
        btnMissingNumber.setOnClickListener(v -> {
            showDifficultyDialog(MissingNumberActivity.class);
        });

        // NUMBER COMPARISON
        btnNumberComparison.setOnClickListener(v -> {
            showDifficultyDialog(NumberComparisonActivity.class);
        });
    }

    // ✅ Common dialog for difficulty
    private void showDifficultyDialog(Class<?> activityToLaunch) {
        String[] difficulties = {"Easy", "Medium", "Hard"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select Difficulty")
                .setItems(difficulties, (dialog, which) -> {
                    String difficulty = "easy";
                    if (which == 1) difficulty = "medium";
                    else if (which == 2) difficulty = "hard";

                    Intent intent = new Intent(MainActivity.this, activityToLaunch);
                    intent.putExtra("difficulty", difficulty);
                    startActivity(intent);
                })
                .show();
    }
}