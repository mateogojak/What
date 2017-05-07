package com.fer.ppij.what;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fer.ppij.what.database.QuestionDAL;
import com.fer.ppij.what.database.model.FillInQuestion;
import com.fer.ppij.what.database.model.MultipleChoiceQuestion;

/**
 * Created by Mateo on 5/2/2017.
 */

public class SelectGameScreen extends AppCompatActivity {

    private String nickname;
    private TextView nicknameDisplayTextView;
    private Button goToGame1, goToGame2,goToGame3,goToGame4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_game);

        nickname = getIntent().getStringExtra("nickname");
        nicknameDisplayTextView = (TextView) findViewById(R.id.nicknameDisplayTextView);
        goToGame1 = (Button) findViewById(R.id.selectGame1);
        goToGame2 = (Button) findViewById(R.id.selectGame2);
        goToGame3 = (Button) findViewById(R.id.selectGame3);
        goToGame4 = (Button) findViewById(R.id.selectGame4);
    }

    @Override
    protected void onStart() {
        super.onStart();

        nicknameDisplayTextView.setText(nickname);
        QuestionDAL.createQuestion("prvo", new FillInQuestion("Filin1", "Lijep", "povijest"));
        QuestionDAL.createQuestion("drugo", new FillInQuestion("Filin2", "Ružan", "povijest"));
        QuestionDAL.createQuestion("prvo", new MultipleChoiceQuestion("Multiple1", "Lijep", "povijest", "Lijep", "Odgovor1", "Odgovor2", "Odgovor3"));
        QuestionDAL.createQuestion("drugo", new MultipleChoiceQuestion("Multiple2", "Ružan", "povijest", "Ružan", "Odgovor1", "Odgovor2", "Odgovor3"));
        goToGame1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectGameScreen.this, GameScreen.class);
                intent.putExtra("nickname", nickname);
                intent.putExtra("gameName", goToGame1.getText());
                startActivity(intent);
                finish();
            }
        });

        goToGame2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectGameScreen.this, GameScreen.class);
                intent.putExtra("nickname", nickname);
                intent.putExtra("gameName", goToGame2.getText());
                startActivity(intent);
                finish();
            }
        });

        goToGame3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectGameScreen.this, GameScreen.class);
                intent.putExtra("nickname", nickname);
                intent.putExtra("gameName", goToGame3.getText());
                startActivity(intent);
                finish();
            }
        });

        goToGame4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectGameScreen.this, GameScreen.class);
                intent.putExtra("nickname", nickname);
                intent.putExtra("gameName", goToGame4.getText());
                startActivity(intent);
                finish();
            }
        });




    }
}
