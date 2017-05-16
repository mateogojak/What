package com.fer.ppij.what;

import com.fer.ppij.what.database.model.AbstractQuestion;

import java.util.List;

/**
 * Created by Duje on 6.5.2017..
 */
//za pamcenje potrebnih aspekata igre(kategorija,score,nick,...)
public class Game {

    private int score = 0;
    private String category;

    //TODO makni nick
    private String nick;
    private int currentQuestionNumber = 0;
    private List<AbstractQuestion> questionPool;
    private int lives;
    boolean finished = false;

    public List<AbstractQuestion> getQuestionPool() {
        return questionPool;
    }

    public Game(String category, List<AbstractQuestion> questionPool, int wrongAnswers) {
        this.category = category;
        this.questionPool = questionPool;
        this.lives = wrongAnswers;
    }

    public int getScore() {
        return score;
    }

    public void increaseScore() {
        score++;
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

    public AbstractQuestion getNextQuestion() {
        int curr = currentQuestionNumber;
        currentQuestionNumber += 1;
        if (curr < questionPool.size()) {
            return questionPool.get(curr);
        } else {
            currentQuestionNumber = 1;
            return questionPool.get(0);
        }
    }

    public void decreaseNumberOfLives() {
        lives -= 1;
        if (lives == 0) {
            finished = true;
        }
    }

    public int getNumOfQuestions(){
        return questionPool.size();
    }

    public int getCurrentQuestionNumber(){
        return currentQuestionNumber;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
}
