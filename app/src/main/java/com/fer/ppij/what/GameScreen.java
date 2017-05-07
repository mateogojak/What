package com.fer.ppij.what;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mateo on 5/2/2017.
 */

public class GameScreen extends AppCompatActivity {

    private TextView questionDisplayTextView;
    private Button answerA, answerB, answerC, answerD;
    private ArrayList<Button> buttons = new ArrayList<>();
    private Score score = new Score();
    private int questionCounter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //setUpTypeOfLayout
        ImageView questImg = (ImageView)findViewById(R.id.questImage);
        LinearLayout fourAnswerLay = (LinearLayout)findViewById(R.id.fourAnswerLayout);
        LinearLayout addOnLay = (LinearLayout)findViewById(R.id.addonAnswer);

//
//        //TODO CHANGE THIS
//        boolean imageQuest=true;
//        boolean addOnQuest=true;
//        //TODO
//        if(imageQuest){
//            questImg.setVisibility(View.VISIBLE);
//        }
//        if(addOnQuest){
//            fourAnswerLay.setVisibility(View.GONE);
//            addOnLay.setVisibility(View.VISIBLE);
//        }

        questionDisplayTextView = (TextView) findViewById(R.id.questionTextView);
        answerA = (Button) findViewById(R.id.answerA);
        answerB = (Button) findViewById(R.id.answerB);
        answerC = (Button) findViewById(R.id.answerC);
        answerD = (Button) findViewById(R.id.answerD);

        buttons.add(answerA);
        buttons.add(answerB);
        buttons.add(answerC);
        buttons.add(answerD);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Question question1 = new Question("Koje je pitanje1?", "Odgovor1", "Odgovor drugi", "Odgovor treci", "Odgovor cetvrti");
        Question question2 = new Question("Koje je pitanje2?", "Odgovor2", "Odgovor drugi", "Odgovor treci", "Odgovor cetvrti");
        Question question3 = new Question("Koje je pitanje3?", "Odgovor3", "Odgovor drugi", "Odgovor treci", "Odgovor cetvrti");
        Question question4 = new Question("Koje je pitanje4?", "Odgovor4", "Odgovor drugi", "Odgovor treci", "Odgovor cetvrti");

        final ArrayList<Question> questions = new ArrayList<>();
        questions.add(question1);
        questions.add(question2);
        questions.add(question3);
        questions.add(question4);


        if(questionCounter>questions.size()-1){
            Intent intent = new Intent(GameScreen.this, EndScreen.class);
            intent.putExtra("score", score.getScore());
            startActivity(intent);
            finish();
        }else {
            manageQuestions(questions.get(questionCounter));
            questionCounter++;
        }


        for(final Button button: buttons){

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(answerCorrect(questions.get(questionCounter-1).getCorrectAnswer(), button)){
                        if(questionCounter>questions.size()-1){
                            score.increaseScore();
                            Intent intent = new Intent(GameScreen.this, EndScreen.class);
                            intent.putExtra("score", score.getScore());
                            startActivity(intent);
                            finish();
                        }else {
                            manageQuestions(questions.get(questionCounter));
                            questionCounter++;
                            score.increaseScore();
                        }
                    }else{
                        Intent intent = new Intent(GameScreen.this, EndScreen.class);
                        intent.putExtra("score", score.getScore());
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }

    }

    public boolean answerCorrect(String correctAnswer, Button button){
        if (button.getText().toString().toUpperCase().equals(correctAnswer.toUpperCase()))
            return true;
        else
            return false;
    }

    public void manageQuestions(Question question){
        
        questionDisplayTextView.setText(question.getQuestionText());
        answerA.setText(question.getCorrectAnswer());
        answerB.setText(question.getIncorrectAnswer1());
        answerC.setText(question.getIncorrectAnswer2());
        answerD.setText(question.getIncorrectAnswer3());
    }

}
