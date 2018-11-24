package com.example.aneeshkjalan.flashmemory;

import android.content.Intent;
import android.service.autofill.TextValueSanitizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final int CREATE_QUESTION_CODE = 1;

    private TextView flashcardQuestion;
    private TextView flashcardAnswer;
    private TextView emptyState;
    private ImageButton addQuestion;
    private ImageButton nextCard;
    private FlashcardDatabase database;
    private List<Flashcard> data;
    private int currCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new FlashcardDatabase(getApplicationContext());
        flashcardQuestion = findViewById(R.id.flashcard_question);
        flashcardAnswer = findViewById(R.id.flashcard_answer);
        addQuestion = findViewById(R.id.add_question);
        emptyState = findViewById(R.id.empty_state);
        nextCard = findViewById(R.id.next_card);
        currCard = 0;

        data = database.getAllCards();
        if ( !data.isEmpty() ) {
            emptyState.setVisibility(View.INVISIBLE);
            nextCard.setVisibility(View.VISIBLE);
            displayCard(currCard, data, flashcardQuestion, flashcardAnswer);
            flashcardQuestion.setVisibility(View.VISIBLE);
        }

        flashcardQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcardQuestion.setVisibility(View.INVISIBLE);
                flashcardAnswer.setVisibility(View.VISIBLE);
            }
        });

        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createQuestion = new Intent(MainActivity.this, AddCardActivity.class);
                startActivityForResult(createQuestion,1);
                //data = database.getAllCards();
            }
        });

        nextCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = database.getAllCards();
                currCard = (currCard + 1) % data.size();
                displayCard(currCard, data, flashcardQuestion, flashcardAnswer);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CREATE_QUESTION_CODE) {
            String question = data.getStringExtra("question");
            String answer = data.getStringExtra("answer");

            System.err.println("The question is " + question + " and the answer is " + answer + ".");

            database.insertCard(new Flashcard(question, answer));

            emptyState.setVisibility(View.INVISIBLE);
            flashcardQuestion.setText(question);
            flashcardAnswer.setText(answer);
            flashcardQuestion.setVisibility(View.VISIBLE);
        }
    }

    public void displayCard(int card, List<Flashcard> allCards, TextView question, TextView answer){
        question.setText(allCards.get(card).getQuestion());
        answer.setText(allCards.get(card).getAnswer());
    }


}
