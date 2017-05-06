package com.fer.ppij.what;

import com.fer.ppij.what.database.model.AbstractQuestion;

import java.util.List;

/**
 * Created by Duje on 6.5.2017..
 */
//za pamcenje potrebnih aspekata igre(kategorija,score,nick,...)
public class Game {

    private int score;
    private String category;
    private String nick;
    private int currentQuestion;
    private List<AbstractQuestion> questionPool;

    public List<AbstractQuestion> getQuestionPool() {
        return questionPool;
    }

    public Game(String category, String nick) {
        this.category = category;
        this.nick = nick;
        score = 0;
        currentQuestion = 0;

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

    public int getCurrentQuestion() {
        return currentQuestion;
    }

    public void nextQuestion(){
        currentQuestion +=1;
        score+=1;
    }

}
