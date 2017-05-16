package com.fer.ppij.what;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.fer.ppij.what.database.ScoreDAL;
import com.fer.ppij.what.database.model.ScoreModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mateo on 5/2/2017.
 */

public class EndScreen extends AppCompatActivity {

    private static final int SCOREBOARD_SIZE = 10;

    private Button newGame;
    private TextView scoreText;
    private String gameName;
    private String nickname;
    private Integer score;
    private RecyclerView mRecyclerView;
    private UserAdapter mOfferAdapter;
    private List<ScoreModel> data = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        newGame = (Button) findViewById(R.id.playAgainButton);
        scoreText = (TextView) findViewById(R.id.scoreTextView);
        score = getIntent().getIntExtra("score", 0);

        scoreText.setText(Integer.toString(getIntent().getIntExtra("score", 0)));
        nickname = getIntent().getStringExtra("nickname");
        gameName = getIntent().getStringExtra("gameName");
        ScoreDAL.createNewScore(new ScoreModel(nickname, score, gameName));
        getScores();
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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

    //ZA RECYCLER VIEW
    class TopListViewHolder extends RecyclerView.ViewHolder {
        public TextView nick;
        public TextView score;
        public TextView category;

        public TopListViewHolder(View itemView) {
            super(itemView);
            nick = (TextView) itemView.findViewById(R.id.nick);
            score = (TextView) itemView.findViewById(R.id.score);
            category = (TextView) itemView.findViewById(R.id.category);
        }

    }

    class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private int lastPosition = -1;

        public UserAdapter() {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(EndScreen.this).inflate(R.layout.top_result_lay, parent, false);
            return new TopListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof TopListViewHolder) {
                ScoreModel model = data.get(position);
                TopListViewHolder userViewHolder = (TopListViewHolder) holder;
                userViewHolder.nick.setText(model.getNickname());
                userViewHolder.score.setText(String.valueOf(model.getScore()));
                userViewHolder.category.setText(model.getCategory());


            }
            setAnimation(holder.itemView, position);
        }

        @Override
        public int getItemCount() {
            return data == null ? 0 : data.size();
        }


        private void setAnimation(View viewToAnimate, int position) {
            // animiraj
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(EndScreen.this, R.anim.fade_in);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }
    }

    public void getScores() {
        //ScoreDAL.createNewScore(new ScoreModel("jure", 50, "povijest"));
        ScoreDAL.getScores(SCOREBOARD_SIZE, new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ScoreModel smodel = dataSnapshot.getValue(ScoreModel.class);
                data.add(smodel);
                Collections.sort(data);
                if (data.size() > SCOREBOARD_SIZE) {
                    data = data.subList(0, SCOREBOARD_SIZE);
                }
                mOfferAdapter = new UserAdapter();
                mRecyclerView.setAdapter(mOfferAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, SelectGameScreen.class);
        intent.putExtra("nickname", nickname);
        startActivity(intent);
    }

}
