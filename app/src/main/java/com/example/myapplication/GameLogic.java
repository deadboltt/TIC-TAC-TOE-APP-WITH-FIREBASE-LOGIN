package com.example.myapplication;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

class GameLogic {
    private final int[][] gameboard;
    private int player = 1;
    private String[] playerNames = {"Player 1", "Player 2"};

    // 1st element = row, 2nd element = col, 3rd element = line type (1: horiz, 2: vert, 3: neg diag, 4: pos diag)
    private int[] winType = {-1, -1, -1};

    private int p1Score = 0;
    private int p2Score = 0;
    private int tiesScore = 0;
    private boolean roundOver = false;

    private Button playAgainBTN;
    private Button homeBTN;
    private TextView playerTurn;
    private TextView p1ScoreText;
    private TextView p2ScoreText;
    private TextView tiesScoreText;

    GameLogic() {
        gameboard = new int[3][3];
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                gameboard[r][c] = 0;
            }
        }
    }

    public boolean updateGameBoard(int row, int col) {
        if (roundOver) {
            return false;
        }
        if (row < 1 || row > 3 || col < 1 || col > 3) {
            return false;
        }

        if (gameboard[row - 1][col - 1] == 0) {
            gameboard[row - 1][col - 1] = player;
            if (playerTurn != null && playerNames != null && playerNames.length >= 2) {
                if (player == 1) {
                    playerTurn.setText(playerNames[1] + "'s Turn (O)");
                    playerTurn.setBackgroundResource(R.drawable.badge_turn_p2);
                    playerTurn.setTextColor(0xFF00F5D4);
                } else {
                    playerTurn.setText(playerNames[0] + "'s Turn (X)");
                    playerTurn.setBackgroundResource(R.drawable.badge_turn_p1);
                    playerTurn.setTextColor(0xFFFF3366);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean winnerCheck() {
        boolean isWinner = false;

        // horizontal check
        for (int r = 0; r < 3; r++) {
            if (gameboard[r][0] == gameboard[r][1] && gameboard[r][0] == gameboard[r][2] && gameboard[r][0] != 0) {
                winType = new int[]{r, 0, 1};
                isWinner = true;
                break;
            }
        }

        // vertical check
        if (!isWinner) {
            for (int c = 0; c < 3; c++) {
                if (gameboard[0][c] == gameboard[1][c] && gameboard[2][c] == gameboard[0][c] && gameboard[0][c] != 0) {
                    winType = new int[]{0, c, 2};
                    isWinner = true;
                    break;
                }
            }
        }

        // negative diagonal check (\)
        if (!isWinner && gameboard[0][0] == gameboard[1][1] && gameboard[0][0] == gameboard[2][2] && gameboard[0][0] != 0) {
            winType = new int[]{0, 2, 3};
            isWinner = true;
        }

        // positive diagonal check (/)
        if (!isWinner && gameboard[2][0] == gameboard[1][1] && gameboard[2][0] == gameboard[0][2] && gameboard[2][0] != 0) {
            winType = new int[]{2, 2, 4};
            isWinner = true;
        }

        int boardFilled = 0;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (gameboard[r][c] != 0) {
                    boardFilled += 1;
                }
            }
        }

        if (isWinner) {
            if (!roundOver) {
                roundOver = true;
                if (player == 1) {
                    p1Score++;
                } else {
                    p2Score++;
                }
                updateScores();
            }
            if (playAgainBTN != null) playAgainBTN.setVisibility(View.VISIBLE);
            if (homeBTN != null) homeBTN.setVisibility(View.VISIBLE);
            if (playerTurn != null && playerNames != null && playerNames.length >= player) {
                playerTurn.setText(playerNames[player - 1] + " Won! \uD83C\uDFC6");
                playerTurn.setBackgroundResource(R.drawable.badge_turn_win);
                playerTurn.setTextColor(0xFFFBBF24);
            }
            return true;
        } else if (boardFilled == 9) {
            winType = new int[]{-1, -1, -1};
            if (!roundOver) {
                roundOver = true;
                tiesScore++;
                updateScores();
            }
            if (playAgainBTN != null) playAgainBTN.setVisibility(View.VISIBLE);
            if (homeBTN != null) homeBTN.setVisibility(View.VISIBLE);
            if (playerTurn != null) {
                playerTurn.setText("It's a Draw! \uD83E\uDD1D");
                playerTurn.setBackgroundResource(R.drawable.badge_turn_tie);
                playerTurn.setTextColor(0xFF94A3B8);
            }
            return true;
        } else {
            return false;
        }
    }

    private void updateScores() {
        if (p1ScoreText != null) {
            p1ScoreText.setText(String.valueOf(p1Score));
        }
        if (p2ScoreText != null) {
            p2ScoreText.setText(String.valueOf(p2Score));
        }
        if (tiesScoreText != null) {
            tiesScoreText.setText(String.valueOf(tiesScore));
        }
    }

    public boolean isRoundOver() {
        return roundOver;
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

    public void setScoreViews(TextView p1ScoreText, TextView p2ScoreText, TextView tiesScoreText) {
        this.p1ScoreText = p1ScoreText;
        this.p2ScoreText = p2ScoreText;
        this.tiesScoreText = tiesScoreText;
        updateScores();
    }

    public void setPlayerNames(String[] playerNames) {
        if (playerNames != null && playerNames.length >= 2) {
            this.playerNames = playerNames;
        }
    }

    public void resetGame() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                gameboard[r][c] = 0;
            }
        }
        player = 1;
        roundOver = false;
        winType = new int[]{-1, -1, -1};
        if (playAgainBTN != null) playAgainBTN.setVisibility(View.GONE);
        if (homeBTN != null) homeBTN.setVisibility(View.GONE);
        if (playerTurn != null && playerNames != null && playerNames.length > 0) {
            playerTurn.setText(playerNames[0] + "'s Turn (X)");
            playerTurn.setBackgroundResource(R.drawable.badge_turn_p1);
            playerTurn.setTextColor(0xFFFF3366);
        }
    }
}
