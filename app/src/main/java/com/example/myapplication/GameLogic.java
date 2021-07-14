package com.example.myapplication;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

class GameLogic<r> {
    private final int[][] gameboard;
    private int player = 1;
    private String[] playerNames = {"Player1","Player2"};

    // 1st element =row 2nd element = col 3rd element = line type
    private int[] winType = {-1,-1,-1};

    private Button playAgainBTN;
    private Button homeBTN;
    private TextView playerTurn;

    GameLogic(){
       gameboard = new int[3][3];
       for (int r =0;r <3; r++){
           for (int c =0;c <3; c++){
               gameboard[r][c]=0;
           }
       }
    }
    public boolean updateGameBoard(int row,int col){
        if(gameboard[row-1][col-1] == 0){
            gameboard[row-1][col-1] = player;
            if (player == 1){
                playerTurn.setText((playerNames[1])+ "'s Turn");
            }else {
                playerTurn.setText((playerNames[0]) + "'s Turn");
            }
            return true;
    }
    else{
        return false;
        }}
        public boolean winnerCheck(){
        boolean isWinner  = false;
        // horizontal check
        for (int r=0; r<3; r++){
            if(gameboard[r][0]==gameboard[r][1] && gameboard[r][0]==gameboard[r][2] && gameboard[r][0] != 0){
                winType = new int[]{r,0,1};
                isWinner = true;
                break;
        }}
        //vertical check
        for (int c=0; c<3; c++){
                if(gameboard[0][c]==gameboard[1][c] && gameboard[2][c]==gameboard[0][c] && gameboard[0][c] != 0){
                    winType = new int[]{0,c,2};
                    isWinner = true;
                    break;
                }}
        // negative diagonal check
        if(gameboard[0][0]==gameboard[1][1] && gameboard[0][0]==gameboard[2][2] && gameboard[0][0] != 0){
            winType = new int[]{0,2,3};
            isWinner = true;
        }
        // positive  diagonal check
            if(gameboard[2][0]==gameboard[1][1] && gameboard[2][0]==gameboard[0][2] && gameboard[2][0] != 0){
                winType = new int[]{2,2,4};
                isWinner = true;
    }
            int boardFilled = 0;
            for (int r=0;r<3;r++){
                for (int c=0;c<3;c++){
                    if (gameboard[r][c]!=0){
                        boardFilled+=1;
                    }
                }
            }
    if(isWinner){
        playAgainBTN.setVisibility(View.VISIBLE);
        homeBTN.setVisibility(View.VISIBLE);
        playerTurn.setText((playerNames[player-1] + "Won!!!"));
        return true;
    }
    else if(boardFilled == 9){
        playAgainBTN.setVisibility(View.VISIBLE);
        homeBTN.setVisibility(View.VISIBLE);
        playerTurn.setText("Tie Game!!!!");
        return true;
    }else{
    return  false;
    }
}
    public void setPlayer(int player) {
        this.player = player;
    }

    public int getPlayer() {
        return player;
    }

    public int[] getWinType() {
        return winType;
    }

    //returning 2D board on call
    public int[][] getGameboard() {
        return gameboard;
    }

    public void setPlayAgainBTN(Button playAgainBTN) {
        this.playAgainBTN = playAgainBTN;
    }

    public void setHomeBTN(Button homeBTN) {
        this.homeBTN = homeBTN;
    }

    public void setPlayerTurn(TextView playerTurn) {
        this.playerTurn = playerTurn;
    }

    public void setPlayerNames(String[] playerNames) {
        this.playerNames = playerNames;
    }


    //resetting the game board
    public void resetGame(){
        for (int r =0;r <3; r++){
            for (int c =0;c <3; c++){
                gameboard[r][c]=0;
            }
        }
        player = 1;
        playAgainBTN.setVisibility(View.GONE);
        homeBTN.setVisibility(View.GONE);
        playerTurn.setText((playerNames[0]+"'s Turn"));
    }


}
