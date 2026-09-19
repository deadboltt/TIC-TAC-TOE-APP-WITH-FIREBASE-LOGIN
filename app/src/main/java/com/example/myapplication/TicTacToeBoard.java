package com.example.myapplication;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class TicTacToeBoard extends View {
    private final int boardcolor;
    private final int XColor;
    private final int OColor;
    private final int WinningLineColor;
    private boolean winningLine = false;
    private final Paint paint = new Paint();
    private final GameLogic game;
    private int cellSize = 0;

    private final float[][] markerScales = new float[3][3];
    private float winLineProgress = 0f;
    private ValueAnimator winLineAnimator;

    public interface OnWinListener {
        void onWin();
    }
    private OnWinListener onWinListener;

    public TicTacToeBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setHapticFeedbackEnabled(true);
        game = new GameLogic();

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                markerScales[r][c] = 1.0f;
            }
        }

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TicTacToeBoard, 0, 0);
        try {
            boardcolor = a.getColor(R.styleable.TicTacToeBoard_boardcolor, 0xFF475569);
            XColor = a.getColor(R.styleable.TicTacToeBoard_XColor, 0xFFFF3366);
            OColor = a.getColor(R.styleable.TicTacToeBoard_OColor, 0xFF00F5D4);
            WinningLineColor = a.getColor(R.styleable.TicTacToeBoard_WinningLineColor, 0xFFFBBF24);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);
        int dimension = Math.min(getMeasuredWidth(), getMeasuredHeight());
        cellSize = dimension / 3;
        setMeasuredDimension(dimension, dimension);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            if (cellSize <= 0) {
                return false;
            }

            int row = (int) Math.ceil(y / cellSize);
            int col = (int) Math.ceil(x / cellSize);

            if (row < 1 || row > 3 || col < 1 || col > 3) {
                return false;
            }

            if (!winningLine && !game.isRoundOver()) {
                if (game.updateGameBoard(row, col)) {
                    // Tactile haptic feedback on marker placement
                    performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);

                    // Animate marker pop
                    animateMarker(row - 1, col - 1);

                    if (game.winnerCheck()) {
                        winningLine = true;
                        performHapticFeedback(HapticFeedbackConstants.LONG_PRESS, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
                        if (game.getWinType()[2] > 0) {
                            animateWinningLine();
                            if (onWinListener != null) {
                                onWinListener.onWin();
                            }
                        }
                    } else {
                        // update player turn
                        if (game.getPlayer() % 2 == 0) {
                            game.setPlayer(game.getPlayer() - 1);
                        } else {
                            game.setPlayer(game.getPlayer() + 1);
                        }
                    }
                    invalidate();
                }
            }
            return true;
        }
        return false;
    }

    private void animateMarker(final int r, final int c) {
        markerScales[r][c] = 0.2f;
        ValueAnimator animator = ValueAnimator.ofFloat(0.2f, 1.0f);
        animator.setDuration(260);
        animator.setInterpolator(new OvershootInterpolator(1.4f));
        animator.addUpdateListener(animation -> {
            markerScales[r][c] = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    private void animateWinningLine() {
        if (winLineAnimator != null && winLineAnimator.isRunning()) {
            winLineAnimator.cancel();
        }
        winLineProgress = 0f;
        winLineAnimator = ValueAnimator.ofFloat(0f, 1.0f);
        winLineAnimator.setDuration(450);
        winLineAnimator.setInterpolator(new DecelerateInterpolator());
        winLineAnimator.addUpdateListener(animation -> {
            winLineProgress = (float) animation.getAnimatedValue();
            invalidate();
        });
        winLineAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);

        drawGameBoard(canvas);
        drawMarkers(canvas);

        if (winningLine && game.getWinType()[2] > 0) {
            drawWinningLine(canvas);
        }
    }

    private void drawGameBoard(Canvas canvas) {
        paint.setColor(boardcolor);
        paint.setStrokeWidth(14);
        paint.setStrokeCap(Paint.Cap.ROUND);

        float boardSize = cellSize * 3;
        for (int c = 1; c < 3; c++) {
            canvas.drawLine(cellSize * c, cellSize * 0.08f, cellSize * c, boardSize - cellSize * 0.08f, paint);
        }
        for (int r = 1; r < 3; r++) {
            canvas.drawLine(cellSize * 0.08f, cellSize * r, boardSize - cellSize * 0.08f, cellSize * r, paint);
        }
    }

    private void drawMarkers(Canvas canvas) {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (game.getGameboard()[r][c] != 0) {
                    if (game.getGameboard()[r][c] == 1) {
                        drawX(canvas, r, c);
                    } else {
                        drawO(canvas, r, c);
                    }
                }
            }
        }
    }

    private void drawX(Canvas canvas, int row, int col) {
        paint.setColor(XColor);
        paint.setStrokeWidth(16);
        paint.setStrokeCap(Paint.Cap.ROUND);

        float centerX = (col + 0.5f) * cellSize;
        float centerY = (row + 0.5f) * cellSize;
        float scale = markerScales[row][col];
        if (scale <= 0f) scale = 1f;

        canvas.save();
        canvas.scale(scale, scale, centerX, centerY);

        float inset = cellSize * 0.24f;
        canvas.drawLine(col * cellSize + inset, row * cellSize + inset,
                (col + 1) * cellSize - inset, (row + 1) * cellSize - inset, paint);
        canvas.drawLine((col + 1) * cellSize - inset, row * cellSize + inset,
                col * cellSize + inset, (row + 1) * cellSize - inset, paint);

        canvas.restore();
    }

    private void drawO(Canvas canvas, int row, int col) {
        paint.setColor(OColor);
        paint.setStrokeWidth(16);
        paint.setStrokeCap(Paint.Cap.ROUND);

        float centerX = (col + 0.5f) * cellSize;
        float centerY = (row + 0.5f) * cellSize;
        float scale = markerScales[row][col];
        if (scale <= 0f) scale = 1f;

        canvas.save();
        canvas.scale(scale, scale, centerX, centerY);

        float inset = cellSize * 0.24f;
        canvas.drawOval(col * cellSize + inset, row * cellSize + inset,
                (col + 1) * cellSize - inset, (row + 1) * cellSize - inset, paint);

        canvas.restore();
    }

    private void drawWinningLine(Canvas canvas) {
        int row = game.getWinType()[0];
        int col = game.getWinType()[1];
        int type = game.getWinType()[2];

        float startX = 0, startY = 0, endX = 0, endY = 0;

        switch (type) {
            case 1: // horizontal
                startX = cellSize * 0.15f;
                startY = row * cellSize + (float) cellSize / 2;
                endX = cellSize * 2.85f;
                endY = startY;
                break;
            case 2: // vertical
                startX = col * cellSize + (float) cellSize / 2;
                startY = cellSize * 0.15f;
                endX = startX;
                endY = cellSize * 2.85f;
                break;
            case 3: // negative diagonal (\)
                startX = cellSize * 0.2f;
                startY = cellSize * 0.2f;
                endX = cellSize * 2.8f;
                endY = cellSize * 2.8f;
                break;
            case 4: // positive diagonal (/)
                startX = cellSize * 0.2f;
                startY = cellSize * 2.8f;
                endX = cellSize * 2.8f;
                endY = cellSize * 0.2f;
                break;
            default:
                return;
        }

        float currentEndX = startX + (endX - startX) * winLineProgress;
        float currentEndY = startY + (endY - startY) * winLineProgress;

        paint.setColor(WinningLineColor);
        paint.setStrokeWidth(20);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(startX, startY, currentEndX, currentEndY, paint);
    }

    public void setUpGame(Button playAgain, Button home, TextView playerDisplay, String[] names) {
        setUpGame(playAgain, home, playerDisplay, names, null, null, null);
    }

    public void setUpGame(Button playAgain, Button home, TextView playerDisplay, String[] names,
                          TextView p1Score, TextView p2Score, TextView tiesScore) {
        game.setPlayAgainBTN(playAgain);
        game.setHomeBTN(home);
        game.setPlayerTurn(playerDisplay);
        game.setPlayerNames(names);
        game.setScoreViews(p1Score, p2Score, tiesScore);
    }

    public GameLogic getGameLogic() {
        return game;
    }

    public void setOnWinListener(OnWinListener onWinListener) {
        this.onWinListener = onWinListener;
    }

    public void resetGame() {
        if (winLineAnimator != null && winLineAnimator.isRunning()) {
            winLineAnimator.cancel();
        }
        game.resetGame();
        winningLine = false;
        winLineProgress = 0f;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                markerScales[r][c] = 1.0f;
            }
        }
        invalidate();
    }
}
