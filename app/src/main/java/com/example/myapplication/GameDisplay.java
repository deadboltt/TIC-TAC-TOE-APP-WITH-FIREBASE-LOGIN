package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameDisplay extends AppCompatActivity {
    private TicTacToeBoard ticTacToeBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_display);

        Button playAgainBTN = findViewById(R.id.playagain);
        Button homeBTN = findViewById(R.id.homebtn);
        TextView playerTurn = findViewById(R.id.playerdisplay);
        playAgainBTN.setVisibility(View.GONE);
        homeBTN.setVisibility(View.GONE);
        String[] playerNames = getIntent().getStringArrayExtra("PLAYER_NAMES");
        ticTacToeBoard = findViewById(R.id.ticTacToeBoard);
        if (playerNames != null) {
            playerTurn.setText((playerNames[0] + "'s turn"));
        }
        ticTacToeBoard.setUpGame(playAgainBTN,homeBTN,playerTurn,playerNames);
    }
    public void playagainbuttonclick(View view){
        ticTacToeBoard.resetGame();
        ticTacToeBoard.invalidate();

    }
    public void homebuttonclick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}