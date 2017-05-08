package com.fer.ppij.what;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Mateo on 5/2/2017.
 */

public class EndScreen extends AppCompatActivity {

    private Button newGame;
    private TextView scoreText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        newGame = (Button) findViewById(R.id.playAgainButton);
        scoreText = (TextView) findViewById(R.id.scoreTextView);

        scoreText.setText(Integer.toString(getIntent().getIntExtra("score",0)));

    }

    @Override
    protected void onStart() {
        super.onStart();

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EndScreen.this, SelectGameScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
