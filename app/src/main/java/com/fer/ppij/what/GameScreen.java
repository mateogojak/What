package com.fer.ppij.what;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Mateo on 5/2/2017.
 */

public class GameScreen extends AppCompatActivity {

    private TextView questionDisplayTextView;
    private Button answerA, answerB, answerC, answerD;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //setUpTypeOfLayout
        ImageView questImg = (ImageView)findViewById(R.id.questImage);
        LinearLayout fourAnswerLay = (LinearLayout)findViewById(R.id.fourAnswerLayout);
        LinearLayout addOnLay = (LinearLayout)findViewById(R.id.addonAnswer);
        //TODO CHANGE THIS
        boolean imageQuest=true;
        boolean addOnQuest=true;
        //TODO
        if(imageQuest){
            questImg.setVisibility(View.VISIBLE);
        }
        if(addOnQuest){
            fourAnswerLay.setVisibility(View.GONE);
            addOnLay.setVisibility(View.VISIBLE);
        }

        questionDisplayTextView = (TextView) findViewById(R.id.questionTextView);
        answerA = (Button) findViewById(R.id.answerA);
        answerB = (Button) findViewById(R.id.answerB);
        answerC = (Button) findViewById(R.id.answerC);
        answerD = (Button) findViewById(R.id.answerD);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Question question = new Question("Koje je pitanje?", "Odgovor", "Odgovor drugi", "Odgovor treci", "Odgovor cetvrti");

        questionDisplayTextView.setText(question.getQuestionText());
        answerA.setText(question.getCorrectAnswer());
        answerB.setText(question.getIncorrectAnswer1());
        answerC.setText(question.getIncorrectAnswer2());
        answerD.setText(question.getIncorrectAnswer3());



        answerA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameScreen.this, EndScreen.class);
                startActivity(intent);
                finish();
            }
        });


    }




}
