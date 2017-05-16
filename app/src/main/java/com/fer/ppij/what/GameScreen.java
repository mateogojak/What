package com.fer.ppij.what;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    private static final int NUMBER_OF_QUESTIONS = 10;

    private TextView questionDisplayTextView;
    private TextView scoreNumTxt;
    private Button answerA, answerB, answerC, answerD, checkAnswerButton;

    private FloatingActionButton buttonContinue;

    private ImageView questionImage;
    private ImageView prviZivot;
    private ImageView drugiZivot;
    private RelativeLayout questImageLay;
    private RelativeLayout waitingLay;
    private RelativeLayout allLay;
    private LinearLayout multipleAnswerLayout;
    private LinearLayout topLayout;
    private LinearLayout fillAnswerLayout;
    private LinearLayout zivotLayout;
    private EditText fillEditText;

    private String nickname, gameName;
    Game game;
    AbstractQuestion currentQuestion;
    List<AbstractQuestion> questionPool = new ArrayList<>();

    boolean answered = false;
    boolean fillInCorrect=false;
    boolean fillInFalse=false;
    boolean isTest = false;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        nickname = getIntent().getStringExtra("nickname");
        gameName = getIntent().getStringExtra("gameName");

        questionImage = (ImageView) findViewById(R.id.questImage);
        prviZivot = (ImageView) findViewById(R.id.prviZivot);
        drugiZivot = (ImageView) findViewById(R.id.drugiZivot);
        questImageLay =  (RelativeLayout)findViewById(R.id.questImageLay);
        allLay =  (RelativeLayout)findViewById(R.id.allLay);
        waitingLay =  (RelativeLayout)findViewById(R.id.waitingLay);
        topLayout = (LinearLayout) findViewById(R.id.topLay);
        multipleAnswerLayout = (LinearLayout) findViewById(R.id.fourAnswerLayout);
        fillAnswerLayout = (LinearLayout) findViewById(R.id.fill_in_answe_lay);
        zivotLayout = (LinearLayout) findViewById(R.id.zivotLay);
        fillEditText = (EditText) findViewById(R.id.fill_edit_text);

        questionDisplayTextView = (TextView) findViewById(R.id.questionTextView);
        scoreNumTxt = (TextView) findViewById(R.id.scoreNum);
        answerA = (Button) findViewById(R.id.answerA);
        answerB = (Button) findViewById(R.id.answerB);
        answerC = (Button) findViewById(R.id.answerC);
        answerD = (Button) findViewById(R.id.answerD);
        checkAnswerButton = (Button) findViewById(R.id.check_answer_button);
        buttonContinue = (FloatingActionButton) findViewById(R.id.button_continue);

        if(!(gameName.toUpperCase().equals("KNJIŽEVNOST") || gameName.toUpperCase().equals("POVIJEST") || gameName.toUpperCase().equals("GEOGRAFIJA"))) isTest = true;
        if(isTest)zivotLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(gameName.equalsIgnoreCase("random")) {
            getQuestionForCategory("književnost");
            getQuestionForCategory("geografija");
            getQuestionForCategory("povijest");
        } else {
            getQuestionForCategory(gameName);
        }

        game = new Game(gameName, questionPool, NUMBER_OF_LIFES);
    }

    private void getQuestionForCategory(final String category) {
        QuestionDAL.getQuestions(category, 4, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                getQuestionsForType(dataSnapshot, questionPool, QuestionType.FILL_IN);
                getQuestionsForType(dataSnapshot, questionPool, QuestionType.IMAGE_FILL_IN);
                getQuestionsForType(dataSnapshot, questionPool, QuestionType.MULTIPLE_CHOICE);
                getQuestionsForType(dataSnapshot, questionPool, QuestionType.IMAGE_MULTIPLE_CHOICE);

                Collections.shuffle(questionPool);
                // slice only first 10 questions for the game
                if(NUMBER_OF_QUESTIONS < questionPool.size()) {
                    questionPool = questionPool.subList(0, NUMBER_OF_QUESTIONS);
                }

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
        questionImage.setImageBitmap(question.getImage());
    }

    public void displayFillInQuestion(FillInQuestion question) {
        questionDisplayTextView.setText(question.getText());
    }

    public void displayFillInQuestionWithImage(ImageFillInQuestion question) {
        displayFillInQuestion(question);
        questionImage.setImageBitmap(question.getImage());
    }

    public void displayNextQuestion() {
        AbstractQuestion question = game.getNextQuestion();
        if (question == null) {
            return;
        }

        currentQuestion = question;
        waitingLay.setVisibility(View.GONE);
        if (question instanceof ImageMultipleChoiceQuestion) {
            setDisplayVisibility(View.VISIBLE, View.VISIBLE, View.GONE);
            displayMultipleChoiceWithImage((ImageMultipleChoiceQuestion) question);
        } else if (question instanceof MultipleChoiceQuestion) {
            setDisplayVisibility(View.GONE, View.VISIBLE, View.GONE);
            displayMultipleQuestion((MultipleChoiceQuestion) question);
        } else if (question instanceof ImageFillInQuestion) {
            setDisplayVisibility(View.VISIBLE, View.GONE, View.VISIBLE);
            displayFillInQuestionWithImage((ImageFillInQuestion) question);
        } else if (question instanceof FillInQuestion) {
            setDisplayVisibility(View.GONE, View.GONE, View.VISIBLE);
            displayFillInQuestion((FillInQuestion) question);
        }

    }

    public void setDisplayVisibility(int image, int multiple, int fill) {
        if(image==View.GONE){
            topLayout.setWeightSum(2);
            questionDisplayTextView.setTextSize(15);
        }else{
            topLayout.setWeightSum(6);
            questionDisplayTextView.setTextSize(13);
        }
        questionImage.setVisibility(image);
        questImageLay.setVisibility(image);
        multipleAnswerLayout.setVisibility(multiple);
        if(fill==View.VISIBLE){
            checkAnswerButton.setVisibility(View.VISIBLE);
            checkAnswerButton.setClickable(true);
        }
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

    public void colorizeButtons(final Button choosenButton) {
        int colorFrom = ContextCompat.getColor(this, R.color.greyBtnColor);
        int colorTo = ContextCompat.getColor(this,R.color.greenBtnColor);
        int colorRed = ContextCompat.getColor(this,R.color.redBtnColor);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        ValueAnimator colorAnimationRed = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorRed);
        colorAnimation.setDuration(350); // milliseconds
        colorAnimationRed.setDuration(350); // milliseconds
        if (choosenButton == getCorrectAnswerButton()) {
            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    choosenButton.setBackgroundTintList(ColorStateList.valueOf((int) animator.getAnimatedValue()));
                }
            });
            colorAnimation.start();
            //choosenButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(CORRECT_ANSWER_BUTTONS_COLOR)));
        } else {
            colorAnimationRed.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    choosenButton.setBackgroundTintList(ColorStateList.valueOf((int) animator.getAnimatedValue()));
                }
            });
            colorAnimationRed.start();
            // choosenButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(WRONG_ANSWER_BUTTONS_COLOR)));
            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    getCorrectAnswerButton().setBackgroundTintList(ColorStateList.valueOf((int) animator.getAnimatedValue()));
                }
            });
            colorAnimation.start();
            //getCorrectAnswerButton().setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(CORRECT_ANSWER_BUTTONS_COLOR)));
        }
    }

    public void decolorizeButtons() {
        answerA.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(DEFAULT_BUTTONS_COLOR)));
        answerB.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(DEFAULT_BUTTONS_COLOR)));
        answerC.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(DEFAULT_BUTTONS_COLOR)));
        answerD.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(DEFAULT_BUTTONS_COLOR)));
    }

    public void handleMultipleAnswerButtonClick(Button button) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.grow_anim);
        buttonContinue.startAnimation(animation);
        if (answered == false) {
            answered = true;
            colorizeButtons(button);
            buttonContinue.setVisibility(View.VISIBLE);
            if (checkAnswer(button.getText().toString()) == false) {
                game.decreaseNumberOfLives();
                if(game.getLives()==1){
                    drugiZivot.setVisibility(View.GONE);
                }else{
                    prviZivot.setVisibility(View.GONE);
                }
            } else {
                game.increaseScore();
                scoreNumTxt.setText((Integer.parseInt(scoreNumTxt.getText().toString())+1)+"");
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
        //skloni continue
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.shrink_anim);
        buttonContinue.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                buttonContinue.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if(fillInCorrect){
            animateColorChange(R.color.greenBtnColor,R.color.greyColor,allLay);
            fillInCorrect=false;
        }
        if(fillInFalse){
            animateColorChange(R.color.redBtnColor,R.color.greyColor,allLay);
            fillInFalse=false;
        }
        if ((game.isFinished() && !isTest) || (game.getNumOfQuestions() == game.getCurrentQuestionNumber() && isTest)) {
            LoadEndScreen();
        } else {
            answered = false;
            fillEditText.setText("");
            fillEditText.setTextColor(Color.parseColor(DEFAULT_BUTTONS_COLOR));
            decolorizeButtons();
            displayNextQuestion();
        }

    }

    public void onCheckAnswerButtonClick(View view) {
        fillEditText.clearFocus();
        if(!fillEditText.getText().toString().isEmpty()) hideSoftKeyboard();

        if (answered == false) {
            answered = true;
            //skloni continue
            Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.shrink_anim);
            checkAnswerButton.startAnimation(animation2);
            animation2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    checkAnswerButton.setVisibility(View.INVISIBLE);
                    checkAnswerButton.setClickable(false);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.grow_anim);
            buttonContinue.startAnimation(animation);
            if (checkAnswer(fillEditText.getText().toString())) {
                //fillEditText.setTextColor(Color.parseColor(CORRECT_ANSWER_BUTTONS_COLOR));
                //tocan odgovor
                fillInCorrect=true;
                animateColorChange(R.color.greyColor,R.color.greenBtnColor,allLay);
                game.increaseScore();
                scoreNumTxt.setText((Integer.parseInt(scoreNumTxt.getText().toString())+1)+"");
            } else {
                fillInFalse=true;
                animateColorChange(R.color.greyColor,R.color.redBtnColor,allLay);
                game.decreaseNumberOfLives();
                game.decreaseNumberOfLives();
                if(game.getLives()==1){
                    drugiZivot.setVisibility(View.GONE);
                }else{
                    prviZivot.setVisibility(View.GONE);
                }
                //fillEditText.setTextColor(Color.parseColor(WRONG_ANSWER_BUTTONS_COLOR));
            }
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    buttonContinue.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    public void animateColorChange(int colorFrom, int colorTo, final View layout){
        colorFrom = ContextCompat.getColor(this, colorFrom);
        colorTo = ContextCompat.getColor(this,colorTo);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(350); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                layout.setBackgroundTintList(ColorStateList.valueOf((int) animator.getAnimatedValue()));
            }
        });
        colorAnimation.start();
    }

}
