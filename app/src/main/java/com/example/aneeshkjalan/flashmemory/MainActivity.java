package com.example.aneeshkjalan.flashmemory;

import android.animation.Animator;
import android.content.Intent;
import android.service.autofill.TextValueSanitizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
                final int centerX = (int) (/*flashcardAnswer.getX() +*/ (flashcardAnswer.getWidth()/2));
                final int centerY = (int) (/*flashcardAnswer.getY() +*/ (flashcardAnswer.getHeight()/2));

                final float finalRadius = (float) Math.hypot(centerX, centerY);

                Animator revealAnim = ViewAnimationUtils.createCircularReveal(flashcardAnswer, centerX, centerY, 0f, finalRadius);

                flashcardQuestion.setVisibility(View.INVISIBLE);
                flashcardAnswer.setVisibility(View.VISIBLE);

                revealAnim.setDuration(1000);
                revealAnim.start();

            }
        });

        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createQuestion = new Intent(MainActivity.this, AddCardActivity.class);

                startActivityForResult(createQuestion,CREATE_QUESTION_CODE);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                //data = database.getAllCards();
                /*for(int i = 0; i < data.size(); i++) {
                    System.err.println("The card at index " + i + " is " + data.get(i).getQuestion());
                }*/
                //currCard = data.size() - 1;
            }
        });

        nextCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = database.getAllCards();
                currCard = (currCard + 1) % data.size();

                final Animation leftOutAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.left_out);
                final Animation rightInAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.right_in);

                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        displayCard(currCard, data, flashcardQuestion, flashcardAnswer);
                        flashcardQuestion.startAnimation(rightInAnim);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                if( flashcardQuestion.getVisibility() == View.VISIBLE ) {
                    flashcardQuestion.startAnimation(leftOutAnim);
                }
                else {
                    flashcardAnswer.startAnimation(leftOutAnim);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CREATE_QUESTION_CODE) {
            String question = data.getStringExtra("question");
            String answer = data.getStringExtra("answer");

            if(this.data.isEmpty()) {
                // When first card is added
                nextCard.setVisibility(View.VISIBLE);
                emptyState.setVisibility(View.INVISIBLE);
            }

            database.insertCard(new Flashcard(question, answer));

            this.data = database.getAllCards();
            currCard = this.data.size() - 1;
            displayCard(currCard, this.data, flashcardQuestion, flashcardAnswer);
        }
    }

    public void displayCard(int card, List<Flashcard> allCards, TextView question, TextView answer){
        question.setText(allCards.get(card).getQuestion());
        answer.setText(allCards.get(card).getAnswer());
        question.setVisibility(View.VISIBLE);
        answer.setVisibility(View.INVISIBLE);
    }


}
