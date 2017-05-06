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

import java.util.List;
import java.util.Random;

/**
 * Created by Mateo on 5/2/2017.
 */

public class GameScreen extends AppCompatActivity {

    private TextView questionDisplayTextView;
    private Button answerA, answerB, answerC, answerD, checkAnswerButton;

    private  ImageView questionImage;
    private  LinearLayout multipleAnswerLayout;
    private  LinearLayout fillAnswerLayout;
    private EditText fillEditText;

    private String nickname,gameName;
    Game game;
    AbstractQuestion currentQuestion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        nickname = getIntent().getStringExtra("nickname");
        gameName = getIntent().getStringExtra("gameName");

        game = new Game(gameName,nickname,3);

        questionImage = (ImageView)findViewById(R.id.questImage);
        multipleAnswerLayout = (LinearLayout)findViewById(R.id.fourAnswerLayout);
        fillAnswerLayout = (LinearLayout)findViewById(R.id.addonAnswer);
        fillEditText = (EditText)findViewById(R.id.fill_edit_text);

        questionDisplayTextView = (TextView) findViewById(R.id.questionTextView);
        answerA = (Button) findViewById(R.id.answerA);
        answerB = (Button) findViewById(R.id.answerB);
        answerC = (Button) findViewById(R.id.answerC);
        answerD = (Button) findViewById(R.id.answerD);
        checkAnswerButton = (Button)findViewById(R.id.check_answer_button);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Question question = new Question("Koje je pitanje?", "Odgovor", "Odgovor drugi", "Odgovor treci", "Odgovor cetvrti");

        displayNextQuestion();




      /*  answerA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameScreen.this, EndScreen.class);
                startActivity(intent);
                finish();
            }
        });
*/

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
                return;
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
        if(question == null) return;

        currentQuestion = question;

        if(question instanceof ImageMultipleChoiceQuestion){
            setDisplayVisibility(View.VISIBLE,View.VISIBLE,View.GONE);
            displayFillInQuestionWithImage((ImageFillInQuestion) question);
        }
        else if(question instanceof MultipleChoiceQuestion){
            setDisplayVisibility(View.GONE,View.VISIBLE,View.GONE);
            displayMultipleQuestion((MultipleChoiceQuestion)question);
        }
        else if(question instanceof ImageFillInQuestion){
            setDisplayVisibility(View.VISIBLE,View.GONE,View.VISIBLE);
            displayFillInQuestionWithImage((ImageFillInQuestion)question);
        }
        else if(question instanceof FillInQuestion){
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
            //TODO obojaj ga u zeleno
            return;
        }
        else {
            //TODO jos bojanja
            //obojaj ga u crveno
            //getCorrencAnswerButton() obojaj u zeleno
        }
    }

    public void OnAnswerAClick(View view) {
        colorizeButtons(answerA);
        if(checkAnswer(answerA.getText().toString())){
            //TODO dodaj novi botun za dalje koji poziva displayNextQuestion()
        }
        else {
            game.decreaseNumberOfLives();
            if(game.isFinished()){
                //pricekaj malo
                LoadEndScreen();
            }
            else {
                //dodaj botun za dalje
            }
        }
    }

    public void OnAnswerBClick(View view) {
        //TODO iskopiraj prethodne botune
    }

    public void OnAnswerCClick(View view) {
    }

    public void OnAnswerDClick(View view) {
    }
}
