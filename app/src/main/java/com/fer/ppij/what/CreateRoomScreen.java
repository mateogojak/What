package com.fer.ppij.what;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.fer.ppij.what.database.QuestionDAL;
import com.fer.ppij.what.database.model.AbstractQuestion;
import com.fer.ppij.what.database.model.FillInQuestion;
import com.fer.ppij.what.database.model.MultipleChoiceQuestion;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Created by Mateo on 5/9/2017.
 */

public class CreateRoomScreen extends AppCompatActivity {

    private EditText question, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3;
    private TextView displayRoomName, wrongAnswer1TextView, wrongAnswer2TextView, wrongAnswer3TextView;
    private FloatingActionButton  finishCreatingRoom;
    private FloatingActionButton addQuestion;
    //pitanja koja korisnik doda za sobu
    private ArrayList<AbstractQuestion> questions = new ArrayList<>();

    private Switch switchTypeOfQuestions;
    private boolean checked = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        displayRoomName = (TextView) findViewById(R.id.roomNameDisplayTextView);
        question = (EditText) findViewById(R.id.newQuestionEditText);
        correctAnswer = (EditText) findViewById(R.id.newCorrectAnswerEditText);
        wrongAnswer1 = (EditText) findViewById(R.id.newWrongAnswer1EditText);
        wrongAnswer2 = (EditText) findViewById(R.id.newWrongAnswer2EditText);
        wrongAnswer3 = (EditText) findViewById(R.id.newWrongAnswer3EditText);
        addQuestion = (FloatingActionButton) findViewById(R.id.addNewQuestionButton);
        finishCreatingRoom = (FloatingActionButton) findViewById(R.id.finishCreatingRoomButton);
        switchTypeOfQuestions = (Switch) findViewById(R.id.switchTypesOfQuestion);

        wrongAnswer1TextView = (TextView) findViewById(R.id.wrongAnswer1TextView);
        wrongAnswer2TextView = (TextView) findViewById(R.id.wrongAnswer2TextView);
        wrongAnswer3TextView = (TextView) findViewById(R.id.wrongAnswer3TextView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        displayRoomName.setText(getIntent().getStringExtra("roomName"));


        switchTypeOfQuestions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    wrongAnswer1.setVisibility(View.INVISIBLE);
                    wrongAnswer1TextView.setVisibility(View.INVISIBLE);
                    LinearLayout cv1= (LinearLayout) wrongAnswer1.getParent();
                    cv1.setVisibility(View.GONE);
                    wrongAnswer2.setVisibility(View.INVISIBLE);
                    wrongAnswer2TextView.setVisibility(View.INVISIBLE);
                    LinearLayout cv2= (LinearLayout) wrongAnswer2.getParent();
                    cv2.setVisibility(View.GONE);
                    wrongAnswer3.setVisibility(View.INVISIBLE);
                    wrongAnswer3TextView.setVisibility(View.INVISIBLE);
                    LinearLayout cv3= (LinearLayout) wrongAnswer3.getParent();
                    cv3.setVisibility(View.GONE);
                    checked = true;
                } else {
                    wrongAnswer1.setVisibility(View.VISIBLE);
                    wrongAnswer1TextView.setVisibility(View.VISIBLE);
                    LinearLayout cv1= (LinearLayout) wrongAnswer1.getParent();
                    cv1.setVisibility(View.VISIBLE);
                    wrongAnswer2.setVisibility(View.VISIBLE);
                    wrongAnswer2TextView.setVisibility(View.VISIBLE);
                    LinearLayout cv2= (LinearLayout) wrongAnswer2.getParent();
                    cv2.setVisibility(View.VISIBLE);
                    wrongAnswer3.setVisibility(View.VISIBLE);
                    wrongAnswer3TextView.setVisibility(View.VISIBLE);
                    LinearLayout cv3= (LinearLayout) wrongAnswer3.getParent();
                    cv3.setVisibility(View.VISIBLE);
                    checked = false;
                }
            }
        });


        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checked) {
                    questions.add(new FillInQuestion(question.getText().toString(), correctAnswer.getText().toString(), getIntent().getStringExtra("roomName")));
                    question.setText("");
                    correctAnswer.setText("");
                } else {
                    questions.add(new MultipleChoiceQuestion(question.getText().toString(), correctAnswer.getText().toString(), getIntent().getStringExtra("roomName"), correctAnswer.getText().toString(), wrongAnswer1.getText().toString(), wrongAnswer2.getText().toString(), wrongAnswer3.getText().toString()));
                    question.setText("");
                    correctAnswer.setText("");
                    wrongAnswer1.setText("");
                    wrongAnswer2.setText("");
                    wrongAnswer3.setText("");
                }

            }
        });

        //DODAT DA SE PITANJA SPREMAJU U BAZU, PITANJA SE NALAZE U LISTI questions.
        finishCreatingRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pitanja se stvore i dodaju pod kategoriju sa imenom sobe, dakle umjesto geografija bit će ime_sobe
                for(AbstractQuestion question : questions) {
                    QuestionDAL.createQuestion(displayRoomName.getText().toString(), question);
                }

                Intent intent = new Intent(CreateRoomScreen.this, SelectGameScreen.class);
                intent.putExtra("nickname", getIntent().getStringExtra("nickname"));
                startActivity(intent);
                finish();
            }
        });


    }
}
