package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameDisplay extends AppCompatActivity {
    private TicTacToeBoard ticTacToeBoard;
    private ConfettiView confettiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_display);

        Button playAgainBTN = findViewById(R.id.playagain);
        Button homeBTN = findViewById(R.id.homebtn);
        TextView playerTurn = findViewById(R.id.playerdisplay);

        TextView p1NameDisplay = findViewById(R.id.p1NameDisplay);
        TextView p2NameDisplay = findViewById(R.id.p2NameDisplay);
        TextView p1ScoreDisplay = findViewById(R.id.p1ScoreDisplay);
        TextView p2ScoreDisplay = findViewById(R.id.p2ScoreDisplay);
        TextView tiesScoreDisplay = findViewById(R.id.tiesScoreDisplay);
        confettiView = findViewById(R.id.confettiView);

        playAgainBTN.setVisibility(View.GONE);
        homeBTN.setVisibility(View.GONE);

        String[] playerNames = getIntent().getStringArrayExtra("PLAYER_NAMES");
        if (playerNames == null || playerNames.length < 2) {
            playerNames = new String[]{"Player 1", "Player 2"};
        }

        if (p1NameDisplay != null) {
            p1NameDisplay.setText(playerNames[0]);
        }
        if (p2NameDisplay != null) {
            p2NameDisplay.setText(playerNames[1]);
        }

        ticTacToeBoard = findViewById(R.id.ticTacToeBoard);
        playerTurn.setText(playerNames[0] + "'s Turn (X)");
        ticTacToeBoard.setUpGame(playAgainBTN, homeBTN, playerTurn, playerNames,
                p1ScoreDisplay, p2ScoreDisplay, tiesScoreDisplay);

        ticTacToeBoard.setOnWinListener(() -> {
            if (confettiView != null) {
                confettiView.startConfetti();
            }
        });
    }

    public void playagainbuttonclick(View view) {
        if (confettiView != null) {
            confettiView.stopConfetti();
        }
        ticTacToeBoard.resetGame();
    }

    public void homebuttonclick(View view) {
        if (confettiView != null) {
            confettiView.stopConfetti();
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (confettiView != null) {
            confettiView.stopConfetti();
        }
    }
}