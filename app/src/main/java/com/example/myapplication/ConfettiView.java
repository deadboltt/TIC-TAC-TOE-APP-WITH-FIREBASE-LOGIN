package com.example.myapplication;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConfettiView extends View {
    private static final String TAG = "ConfettiView";
    private static final int PARTICLE_COUNT = 95;
    private final List<Particle> particles = new ArrayList<>();
    private final Random random = new Random();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ValueAnimator animator;
    private boolean isRunning = false;

    // Harmonious neon palette matching app theme
    private static final int[] CONFETTI_COLORS = {
            0xFFFF3366, // Electric Crimson (Player X)
            0xFF00F5D4, // Neon Mint (Player O)
            0xFFFBBF24, // Amber Gold (Win Line)
            0xFF6366F1, // Indigo Accent
            0xFFA855F7, // Neon Purple
            0xFF38BDF8, // Sky Blue
            0xFFF472B6  // Coral Pink
    };

    public ConfettiView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        paint.setStyle(Paint.Style.FILL);
        setVisibility(INVISIBLE);
    }

    public void startConfetti() {
        setVisibility(VISIBLE);
        post(() -> {
            int width = getWidth();
            int height = getHeight();
            Log.d(TAG, "startConfetti running, size: " + width + "x" + height);

            if (width <= 0 || height <= 0) {
                return;
            }

            particles.clear();

            // Spawn particles distributed vertically for instant celebration
            for (int i = 0; i < PARTICLE_COUNT; i++) {
                Particle p = new Particle();
                p.x = width * 0.05f + random.nextFloat() * (width * 0.9f);
                p.y = -20f + random.nextFloat() * (height * 0.85f);

                p.vx = (random.nextFloat() - 0.5f) * 12f;
                p.vy = random.nextFloat() * 7f + 5f;

                p.w = random.nextFloat() * 26f + 22f; // 22 to 48 px wide
                p.h = random.nextFloat() * 14f + 14f; // 14 to 28 px high
                p.color = CONFETTI_COLORS[random.nextInt(CONFETTI_COLORS.length)];
                p.rotation = random.nextFloat() * 360f;
                p.rotationSpeed = (random.nextFloat() - 0.5f) * 12f;
                p.swaySpeed = random.nextFloat() * 0.03f + 0.015f;
                p.swayOffset = random.nextFloat() * (float) (Math.PI * 2);
                p.flipSpeed = random.nextFloat() * 0.08f + 0.03f;
                p.isCircle = random.nextBoolean();

                particles.add(p);
            }

            isRunning = true;

            if (animator != null && animator.isRunning()) {
                animator.cancel();
            }

            animator = ValueAnimator.ofFloat(0f, 1f);
            animator.setDuration(1000);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setInterpolator(new LinearInterpolator());
            animator.addUpdateListener(animation -> {
                updateParticles();
                postInvalidateOnAnimation();
            });
            animator.start();
        });
    }

    private void updateParticles() {
        int width = getWidth();
        int height = getHeight();
        if (width <= 0 || height <= 0) return;

        for (Particle p : particles) {
            p.y += p.vy;
            p.x += p.vx + (float) Math.sin(p.y * p.swaySpeed + p.swayOffset) * 2.8f;
            p.rotation += p.rotationSpeed;
            p.scaleY = Math.abs((float) Math.cos(p.y * p.flipSpeed));

            // Recycle particle to top when it falls below screen
            if (p.y > height + 40f) {
                p.y = -30f - random.nextFloat() * 60f;
                p.x = width * 0.05f + random.nextFloat() * (width * 0.9f);
                p.vx = (random.nextFloat() - 0.5f) * 10f;
                p.vy = random.nextFloat() * 7f + 5f;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isRunning) return;

        for (Particle p : particles) {
            paint.setColor(p.color);

            canvas.save();
            canvas.translate(p.x, p.y);
            canvas.rotate(p.rotation);
            canvas.scale(1.0f, Math.max(0.15f, p.scaleY));

            if (p.isCircle) {
                canvas.drawCircle(0, 0, p.w / 2f, paint);
            } else {
                canvas.drawRect(-p.w / 2f, -p.h / 2f, p.w / 2f, p.h / 2f, paint);
            }

            canvas.restore();
        }
    }

    public void stopConfetti() {
        Log.d(TAG, "stopConfetti called");
        isRunning = false;
        if (animator != null) {
            animator.cancel();
        }
        particles.clear();
        setVisibility(INVISIBLE);
        postInvalidateOnAnimation();
    }

    private static class Particle {
        float x, y;
        float vx, vy;
        float w, h;
        int color;
        float rotation;
        float rotationSpeed;
        float swaySpeed;
        float swayOffset;
        float scaleY = 1.0f;
        float flipSpeed;
        boolean isCircle;
    }
}
