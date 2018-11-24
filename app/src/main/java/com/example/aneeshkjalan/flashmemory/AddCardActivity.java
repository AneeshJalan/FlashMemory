package com.example.aneeshkjalan.flashmemory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class AddCardActivity extends AppCompatActivity {

    public final int CREATE_QUESTION_CODE = 1;

    EditText newQuestion;
    EditText newAnswer;
    ImageButton cancelButton;
    ImageButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        newQuestion = findViewById(R.id.new_question);
        newAnswer = findViewById(R.id.new_answer);
        cancelButton = findViewById(R.id.cancel_question_creation);
        saveButton = findViewById(R.id.save_question);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateFlashcard = new Intent(AddCardActivity.this, MainActivity.class);
                updateFlashcard.putExtra("question", newQuestion.getText().toString());
                updateFlashcard.putExtra("answer", newAnswer.getText().toString());
                setResult(RESULT_OK, updateFlashcard );
                finish();
            }
        });
    }
}
