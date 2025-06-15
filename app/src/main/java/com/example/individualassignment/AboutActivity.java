package com.example.individualassignment;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Optional: make GitHub link clickable
        TextView txtGitHub = findViewById(R.id.txtGitHub);
        txtGitHub.setMovementMethod(LinkMovementMethod.getInstance());

        // Back button to return to MainActivity
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }
}
