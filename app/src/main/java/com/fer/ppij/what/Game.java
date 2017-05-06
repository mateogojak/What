package com.fer.ppij.what;

import com.fer.ppij.what.database.QuestionDAL;
import com.fer.ppij.what.database.model.AbstractQuestion;

import java.util.List;

/**
 * Created by Duje on 6.5.2017..
 */
//za pamcenje potrebnih aspekata igre(kategorija,score,nick,...)
public class Game {

    private int score = 0;
    private String category;
    private String nick;
    private int currentQuestionNumber = 0;
    private List<AbstractQuestion> questionPool;
    private int lives;
    boolean finished = false;

    public List<AbstractQuestion> getQuestionPool() {
        return questionPool;
    }

    public Game(String category, String nick, int wrongAnswers) {
        this.category = category;
        this.nick = nick;
        //TODO callback
        this.questionPool = QuestionDAL.getQuestions(category);
        this.lives = wrongAnswers;
    }


    public int getScore() {
        return score;
    }

    public String getCategory() {
        return category;
    }

    public String getNick() {
        return nick;
    }

    public boolean isFinished() {
        return finished;
    }

    public  AbstractQuestion getNextQuestion(){
        int curr = currentQuestionNumber;
        currentQuestionNumber +=1;
        score+=1;
        if(curr < questionPool.size()){
            return questionPool.get(currentQuestionNumber);
        }
        return  null;
    }

    public void decreaseNumberOfLives(){
        lives -= 1;
        if(lives == 0){
            finished = true;
        }
    }


}
