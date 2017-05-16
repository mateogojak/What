package com.fer.ppij.what;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fer.ppij.what.database.QuestionDAL;
import com.fer.ppij.what.database.QuestionType;
import com.fer.ppij.what.database.model.AbstractQuestion;
import com.fer.ppij.what.database.model.FillInQuestion;
import com.fer.ppij.what.database.model.ImageFillInQuestion;
import com.fer.ppij.what.database.model.ImageMultipleChoiceQuestion;
import com.fer.ppij.what.database.model.MultipleChoiceQuestion;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Mateo on 5/2/2017.
 */

public class GameScreen extends AppCompatActivity {

    private final int NUMBER_OF_LIFES = 2;
    private final String DEFAULT_BUTTONS_COLOR = "#d3d3d3";
    private final String CORRECT_ANSWER_BUTTONS_COLOR = "#00ff00";
    private final String WRONG_ANSWER_BUTTONS_COLOR = "#ff0000";
    private static final int NUMBER_OF_QUESTIONS = 10;

    private TextView questionDisplayTextView;
    private Button answerA, answerB, answerC, answerD, checkAnswerButton;

    private Button buttonContinue, fillInButtonContinue;

    private ImageView questionImage;
    private LinearLayout multipleAnswerLayout;
    private LinearLayout fillAnswerLayout;
    private EditText fillEditText;

    private String nickname, gameName;
    Game game;
    AbstractQuestion currentQuestion;

    boolean answered = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        nickname = getIntent().getStringExtra("nickname");
        gameName = getIntent().getStringExtra("gameName");

        questionImage = (ImageView) findViewById(R.id.questImage);
        multipleAnswerLayout = (LinearLayout) findViewById(R.id.fourAnswerLayout);
        fillAnswerLayout = (LinearLayout) findViewById(R.id.fill_in_answe_lay);
        fillEditText = (EditText) findViewById(R.id.fill_edit_text);

        questionDisplayTextView = (TextView) findViewById(R.id.questionTextView);
        answerA = (Button) findViewById(R.id.answerA);
        answerB = (Button) findViewById(R.id.answerB);
        answerC = (Button) findViewById(R.id.answerC);
        answerD = (Button) findViewById(R.id.answerD);
        checkAnswerButton = (Button) findViewById(R.id.check_answer_button);
        buttonContinue = (Button) findViewById(R.id.button_continue);
        //fillInButtonContinue = (Button)findViewById(R.id.fill_in_button_continue);

    }

    @Override
    protected void onStart() {
        super.onStart();

        QuestionDAL.getQuestions(gameName, 4, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<AbstractQuestion> questionPool = new ArrayList<AbstractQuestion>();

                getQuestionsForType(dataSnapshot, questionPool, QuestionType.FILL_IN);
                getQuestionsForType(dataSnapshot, questionPool, QuestionType.IMAGE_FILL_IN);
                getQuestionsForType(dataSnapshot, questionPool, QuestionType.MULTIPLE_CHOICE);
                getQuestionsForType(dataSnapshot, questionPool, QuestionType.IMAGE_MULTIPLE_CHOICE);

                Collections.shuffle(questionPool);
                // slice only first 10 questions for the game
//                questionPool = questionPool.subList(0, NUMBER_OF_QUESTIONS);
                game = new Game(gameName, questionPool, NUMBER_OF_LIFES);
                displayNextQuestion();
            }

            private void getQuestionsForType(DataSnapshot dataSnapshot, List<AbstractQuestion> questionPool, QuestionType type) {
                for (DataSnapshot questionSnapshot : dataSnapshot.child(type.getName()).getChildren()) {
                    String id = questionSnapshot.getKey();

                    switch (type) {
                        case FILL_IN:
                            questionPool.add(questionSnapshot.getValue(FillInQuestion.class));
                            break;
                        case IMAGE_FILL_IN:
                            ImageFillInQuestion imf = questionSnapshot.getValue(ImageFillInQuestion.class);
                            QuestionDAL.getQuestionImage(id, imf, new OnSuccessListener<byte[]>() {
                                private ImageFillInQuestion imf;

                                private OnSuccessListener<byte[]> init(ImageFillInQuestion imf) {
                                    this.imf = imf;
                                    return this;
                                }

                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    imf.setImage(bitmap);
                                }
                            }.init(imf));
                            questionPool.add(imf);
                            break;
                        case MULTIPLE_CHOICE:
                            questionPool.add(questionSnapshot.getValue(MultipleChoiceQuestion.class));
                            break;
                        case IMAGE_MULTIPLE_CHOICE:
                            ImageMultipleChoiceQuestion imc = questionSnapshot.getValue(ImageMultipleChoiceQuestion.class);
                            QuestionDAL.getQuestionImage(id, imc, new OnSuccessListener<byte[]>() {
                                private ImageMultipleChoiceQuestion imc;

                                private OnSuccessListener<byte[]> init(ImageMultipleChoiceQuestion imc) {
                                    this.imc = imc;
                                    return this;
                                }

                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    imc.setImage(bitmap);
                                }
                            }.init(imc));
                            questionPool.add(imc);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void displayMultipleQuestion(MultipleChoiceQuestion question) {
        List<String> answers = question.getAnswers();
        int a, b, c, d = 0;
        Random rnd = new Random();

        a = rnd.nextInt(4);

        b = rnd.nextInt(4);
        while (b == a) {
            b = rnd.nextInt(4);
        }

        c = rnd.nextInt(4);
        while (c == a || c == b) {
            c = rnd.nextInt(4);
        }

        for (int i = 0; i < 4; i++) {
            if (a != i && b != i && c != i) {
                d = i;
                break;
            }
        }

        questionDisplayTextView.setText(question.getText());
        answerA.setText(answers.get(a));
        answerB.setText(answers.get(b));
        answerC.setText(answers.get(c));
        answerD.setText(answers.get(d));
    }

    public void displayMultipleChoiceWithImage(ImageMultipleChoiceQuestion question) {
        displayMultipleQuestion(question);
        String mDrawableName = question.getCorrectAnswer().toLowerCase();
        int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
        if(question.getImage() != null){
            questionImage.setImageBitmap(question.getImage());
        }
        else{
            questionImage.setImageResource(resID);
        }
    }

    public void displayFillInQuestion(FillInQuestion question) {
        questionDisplayTextView.setText(question.getText());
    }

    public void displayFillInQuestionWithImage(ImageFillInQuestion question) {
        displayFillInQuestion(question);
        String mDrawableName = question.getCorrectAnswer().toLowerCase();
        int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
        if(question.getImage() != null){
            questionImage.setImageBitmap(question.getImage());
        }
        else{
            questionImage.setImageResource(resID);
        }
    }

    public void displayNextQuestion() {
        AbstractQuestion question = game.getNextQuestion();


        if (question == null) {
            Log.d("LOG", "nema pitanja");
            return;
        }

        currentQuestion = question;

        if (question instanceof ImageMultipleChoiceQuestion) {
            Log.d("LOG", "1111111111111111111111111");
            setDisplayVisibility(View.VISIBLE, View.VISIBLE, View.GONE);
            displayMultipleChoiceWithImage((ImageMultipleChoiceQuestion) question);
        } else if (question instanceof MultipleChoiceQuestion) {
            Log.d("LOG", "2222222222222222222222");
            setDisplayVisibility(View.GONE, View.VISIBLE, View.GONE);
            displayMultipleQuestion((MultipleChoiceQuestion) question);
        } else if (question instanceof ImageFillInQuestion) {
            Log.d("LOG", "33333333333333333333333333");
            setDisplayVisibility(View.VISIBLE, View.GONE, View.VISIBLE);
            displayFillInQuestionWithImage((ImageFillInQuestion) question);
        } else if (question instanceof FillInQuestion) {
            Log.d("LOG", "4444444444444444");
            setDisplayVisibility(View.GONE, View.GONE, View.VISIBLE);
            displayFillInQuestion((FillInQuestion) question);
        }

    }

    public void setDisplayVisibility(int image, int multiple, int fill) {
        questionImage.setVisibility(image);
        multipleAnswerLayout.setVisibility(multiple);
        fillAnswerLayout.setVisibility(fill);
    }


    public void LoadEndScreen() {
        Intent intent = new Intent(this, EndScreen.class);
        intent.putExtra("nickname", nickname);
        intent.putExtra("gameName", gameName);
        intent.putExtra("score", game.getScore());
        startActivity(intent);
    }

    public boolean checkAnswer(String answer) {
        String correctAnswer = currentQuestion.getCorrectAnswer().toUpperCase();
        return correctAnswer.equals(answer.toUpperCase());
    }

    public void hideSoftKeyboard() {

        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    public Button getCorrectAnswerButton() {
        if (checkAnswer(answerA.getText().toString())) {
            return answerA;
        } else if (checkAnswer(answerB.getText().toString())) {
            return answerB;
        } else if (checkAnswer(answerC.getText().toString())) {
            return answerC;
        } else {
            return answerD;
        }
    }

    public void colorizeButtons(Button choosenButton) {
        if (choosenButton == getCorrectAnswerButton()) {
            choosenButton.setBackgroundColor(Color.parseColor(CORRECT_ANSWER_BUTTONS_COLOR));
        } else {
            choosenButton.setBackgroundColor(Color.parseColor(WRONG_ANSWER_BUTTONS_COLOR));
            getCorrectAnswerButton().setBackgroundColor(Color.parseColor(CORRECT_ANSWER_BUTTONS_COLOR));
        }
    }

    public void decolorizeButtons() {
        answerA.setBackgroundColor(Color.parseColor(DEFAULT_BUTTONS_COLOR));
        answerB.setBackgroundColor(Color.parseColor(DEFAULT_BUTTONS_COLOR));
        answerC.setBackgroundColor(Color.parseColor(DEFAULT_BUTTONS_COLOR));
        answerD.setBackgroundColor(Color.parseColor(DEFAULT_BUTTONS_COLOR));
    }

    public void handleMultipleAnswerButtonClick(Button button) {
        if (answered == false) {
            answered = true;
            colorizeButtons(button);
            buttonContinue.setVisibility(View.VISIBLE);
            if (checkAnswer(button.getText().toString()) == false) {
                game.decreaseNumberOfLives();
            } else {
                game.increaseScore();
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
        if (game.isFinished()) {
            LoadEndScreen();
        } else {
            answered = false;
            decolorizeButtons();
            displayNextQuestion();
        }

    }

    public void onCheckAnswerButtonClick(View view) {
        fillEditText.clearFocus();
        if(!fillEditText.getText().toString().isEmpty())
        hideSoftKeyboard();

        if (answered == false) {
            answered = true;
            checkAnswerButton.setText("NASTAVI");
            if (checkAnswer(fillEditText.getText().toString())) {
                fillEditText.setTextColor(Color.parseColor(CORRECT_ANSWER_BUTTONS_COLOR));
                game.increaseScore();
            } else {
                game.decreaseNumberOfLives();
                fillEditText.setTextColor(Color.parseColor(WRONG_ANSWER_BUTTONS_COLOR));
            }
        } else {
            if (game.isFinished()) {
                LoadEndScreen();
            } else {
                checkAnswerButton.setText("POTVRDI ODGOVOR");
                answered = false;
                fillEditText.setText("");
                fillEditText.setTextColor(Color.parseColor(DEFAULT_BUTTONS_COLOR));
                displayNextQuestion();
            }
        }
    }

}
