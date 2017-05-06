package com.fer.ppij.what;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fer.ppij.what.database.model.AbstractQuestion;
import com.fer.ppij.what.database.model.FillInQuestion;
import com.fer.ppij.what.database.model.ImageFillInQuestion;
import com.fer.ppij.what.database.model.ImageMultipleChoiceQuestion;
import com.fer.ppij.what.database.model.MultipleChoiceQuestion;

/**
 * Created by Mateo on 5/2/2017.
 */

public class GameScreen extends AppCompatActivity {

    private TextView questionDisplayTextView;
    private Button answerA, answerB, answerC, answerD;

    private  ImageView questionImage;
    private  LinearLayout multipleAnswerLayout;
    private  LinearLayout fillAnswerLayout;
    private EditText fillEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        questionImage = (ImageView)findViewById(R.id.questImage);
        multipleAnswerLayout = (LinearLayout)findViewById(R.id.fourAnswerLayout);
        fillAnswerLayout = (LinearLayout)findViewById(R.id.addonAnswer);
        fillEditText = (EditText)findViewById(R.id.fill_edit_text);

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

    public void displayQuestion(AbstractQuestion question){

        if(question instanceof ImageMultipleChoiceQuestion){
            setDisplayVisibility(View.VISIBLE,View.VISIBLE,View.GONE);
        }
        else if(question instanceof MultipleChoiceQuestion){
            setDisplayVisibility(View.GONE,View.VISIBLE,View.GONE);
        }
        else if(question instanceof ImageFillInQuestion){
            setDisplayVisibility(View.VISIBLE,View.GONE,View.VISIBLE);
        }
        else if(question instanceof FillInQuestion){
            setDisplayVisibility(View.GONE,View.GONE,View.VISIBLE);
        }

    }

    public void setDisplayVisibility(int image,int multiple,int fill){
        questionImage.setVisibility(image);
        multipleAnswerLayout.setVisibility(multiple);
        fillAnswerLayout.setVisibility(fill);
    }



}
