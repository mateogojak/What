package com.fer.ppij.what;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fer.ppij.what.database.QuestionDAL;
import com.fer.ppij.what.database.model.AbstractQuestion;
import com.fer.ppij.what.database.model.FillInQuestion;
import com.fer.ppij.what.database.model.ImageFillInQuestion;
import com.fer.ppij.what.database.model.ImageMultipleChoiceQuestion;
import com.fer.ppij.what.database.model.MultipleChoiceQuestion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * Created by Mateo on 5/2/2017.
 */

public class GameScreen extends AppCompatActivity {

    private final int NUMBER_OF_LIFES = 2;
    private final String DEFAULT_BUTTONS_COLOR = "#ffffffff";
    private final String CORRECT_ANSWER_BUTTONS_COLOR = "#00ff00";
    private final String WRONG_ANSWER_BUTTONS_COLOR = "#ff0000";

    private TextView questionDisplayTextView;
    private Button answerA, answerB, answerC, answerD, checkAnswerButton;

    private Button buttonContinue,fillInButtonContinue;

    private  ImageView questionImage;
    private  LinearLayout multipleAnswerLayout;
    private  LinearLayout fillAnswerLayout;
    private EditText fillEditText;

    private String nickname,gameName;
    Game game;
    AbstractQuestion currentQuestion;

    boolean answered = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        nickname = getIntent().getStringExtra("nickname");
        gameName = getIntent().getStringExtra("gameName");



        questionImage = (ImageView)findViewById(R.id.questImage);
        multipleAnswerLayout = (LinearLayout)findViewById(R.id.fourAnswerLayout);
        fillAnswerLayout = (LinearLayout)findViewById(R.id.fill_in_answe_lay);
        fillEditText = (EditText)findViewById(R.id.fill_edit_text);

        questionDisplayTextView = (TextView) findViewById(R.id.questionTextView);
        answerA = (Button) findViewById(R.id.answerA);
        answerB = (Button) findViewById(R.id.answerB);
        answerC = (Button) findViewById(R.id.answerC);
        answerD = (Button) findViewById(R.id.answerD);
        checkAnswerButton = (Button)findViewById(R.id.check_answer_button);
        buttonContinue = (Button)findViewById(R.id.button_continue);
        fillInButtonContinue = (Button)findViewById(R.id.fill_in_button_continue);

    }

    @Override
    protected void onStart() {
        super.onStart();

        QuestionDAL.getQuestions(gameName, 1, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<AbstractQuestion> questionPool = new ArrayList<AbstractQuestion>();

                for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                    AbstractQuestion q = questionSnapshot.getValue(MultipleChoiceQuestion.class);
                    Log.d("TAG",q.toString());
                    questionPool.add(q);
                }
                game = new Game(gameName,questionPool,NUMBER_OF_LIFES);
                displayNextQuestion();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void displayMultipleQuestion(MultipleChoiceQuestion question){
        List<String > answers = question.getAnswers();
        int a,b,c,d=0;
        Random rnd = new Random();

        a = rnd.nextInt(4);

        b=rnd.nextInt(4);
        while (b == a){
            b = rnd.nextInt(4);
        }

        c=rnd.nextInt(4);
        while (c == a || c == b){
            c=rnd.nextInt(4);
        }

        for(int i=0;i<4;i++){
            if(a != i && b!= i && c!= i){
                d=i;
                break;
            }
        }

        questionDisplayTextView.setText(question.getText());
        answerA.setText(answers.get(a));
        answerB.setText(answers.get(b));
        answerC.setText(answers.get(c));
        answerD.setText(answers.get(d));
    }

    public void displayMultipleChoiceWithImage(ImageMultipleChoiceQuestion question){
        displayMultipleQuestion(question);
        //TODO namistit izvor slike
        //questionImage.setBackground(question.getImage());
    }

    public void displayFillInQuestion(FillInQuestion question){
        questionDisplayTextView.setText(question.getText());
    }

    public void displayFillInQuestionWithImage(ImageFillInQuestion question){
        displayFillInQuestion(question);
        //TODO namistit izvor slike
        //questionImage.setImageBitmap();
    }

    public void displayNextQuestion(){
        AbstractQuestion question = game.getNextQuestion();


        if(question == null) {
            Log.d("LOG","nema pitanja");
            return;
        }

        currentQuestion = question;

        if(question instanceof ImageMultipleChoiceQuestion){
            Log.d("LOG","1111111111111111111111111");
            setDisplayVisibility(View.VISIBLE,View.VISIBLE,View.GONE);
            displayFillInQuestionWithImage((ImageFillInQuestion) question);
        }
        else if(question instanceof MultipleChoiceQuestion){
            Log.d("LOG","2222222222222222222222");
            setDisplayVisibility(View.GONE,View.VISIBLE,View.GONE);
            displayMultipleQuestion((MultipleChoiceQuestion)question);
        }
        else if(question instanceof ImageFillInQuestion){
            Log.d("LOG","33333333333333333333333333");
            setDisplayVisibility(View.VISIBLE,View.GONE,View.VISIBLE);
            displayFillInQuestionWithImage((ImageFillInQuestion)question);
        }
        else if(question instanceof FillInQuestion){
            Log.d("LOG","4444444444444444");
            setDisplayVisibility(View.GONE,View.GONE,View.VISIBLE);
            displayFillInQuestion((FillInQuestion)question);
        }

    }

    public void setDisplayVisibility(int image,int multiple,int fill){
        questionImage.setVisibility(image);
        multipleAnswerLayout.setVisibility(multiple);
        fillAnswerLayout.setVisibility(fill);
    }


    public void LoadEndScreen(){
        Intent intent = new Intent(this, EndScreen.class);
        intent.putExtra("nickname", nickname);
        intent.putExtra("gameName", gameName);
        intent.putExtra("score",game.getScore());
        startActivity(intent);
    }

    public boolean checkAnswer(String answer){
        String correctAnswer = currentQuestion.getCorrectAnswer();
        if(correctAnswer.equals(answer)){
            return true;
        }
        else{
            return false;
        }
    }

    public Button getCorrencAnswerButton(){
        if(checkAnswer(answerA.getText().toString())){
            return answerA;
        }
        else if(checkAnswer(answerB.getText().toString())){
            return answerB;
        }
        else if(checkAnswer(answerC.getText().toString())) {
            return answerC;
        }
        else {
            return answerD;
        }
    }

    public void colorizeButtons(Button choosenButton){
        if(choosenButton == getCorrencAnswerButton()){
            choosenButton.setBackgroundColor(Color.parseColor(CORRECT_ANSWER_BUTTONS_COLOR));
        }
        else {
            choosenButton.setBackgroundColor(Color.parseColor(WRONG_ANSWER_BUTTONS_COLOR));
            getCorrencAnswerButton().setBackgroundColor(Color.parseColor(CORRECT_ANSWER_BUTTONS_COLOR));
        }
    }

    public void decolorizeButtons(){
        answerA.setBackgroundColor(Color.parseColor(DEFAULT_BUTTONS_COLOR));
        answerB.setBackgroundColor(Color.parseColor(DEFAULT_BUTTONS_COLOR));
        answerC.setBackgroundColor(Color.parseColor(DEFAULT_BUTTONS_COLOR));
        answerD.setBackgroundColor(Color.parseColor(DEFAULT_BUTTONS_COLOR));
    }

    public void handleMultipleAnswerButtonClick(Button button){
        if(answered == false) {
            answered = true;
            colorizeButtons(button);
            buttonContinue.setVisibility(View.VISIBLE);
            if (checkAnswer(button.getText().toString()) == false) {
                game.decreaseNumberOfLives();
            }
        }

    }

    public void OnAnswerAClick(View view) {
       handleMultipleAnswerButtonClick(answerA);
    }

    public void OnAnswerBClick(View view) {
        handleMultipleAnswerButtonClick(answerB);
    }

    public void OnAnswerCClick(View view) {
        handleMultipleAnswerButtonClick(answerC);
    }

    public void OnAnswerDClick(View view) {
        handleMultipleAnswerButtonClick(answerD);
    }

    public void OnContinueButtonClick(View view) {
        if(game.isFinished()){
            LoadEndScreen();
        }
        else {
            answered = false;
            decolorizeButtons();
            displayNextQuestion();
        }

    }
}
