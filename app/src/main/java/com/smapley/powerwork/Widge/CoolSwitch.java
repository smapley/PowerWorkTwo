package com.smapley.powerwork.Widge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ToggleButton;

import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by smapley on 2015/5/2.
 */
public class CoolSwitch extends ToggleButton implements View.OnClickListener {

    private static final int BORDER_WIDTH = 2;
    private static final int MAX_BACKGROUND_ALPHA = 0;
    private static final int MIN_BACKGROUND_ALPHA = 50;
    private static final long MOVEMENT_ANIMATION_DURATION_MS = 200;
    private static final int OPAQUE = 255;
    private static final float SELECTOR_RATIO = 0.85f;

    private AnimationListener animationListener;
    private int backgroundAlpha;
    private final RectF backgroundRect = new RectF(0, 0, 0, 0);
    private final Point currentSelectorCenter = new Point(0, 0);
    private final Point disabledSelectorCenter = new Point(0, 0);
    private final Point enabledSelectorCenter = new Point(0, 0);
    private final Interpolator interpolator = new DecelerateInterpolator(1.0f);
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int selectorRadius;
    private int mcolor;

    public CoolSwitch(Context context) {
        super(context);
        initialize();
    }

    public CoolSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public CoolSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {

        initialize();
    }

    private void initialize() {
        backgroundAlpha = isChecked() ? MAX_BACKGROUND_ALPHA : MIN_BACKGROUND_ALPHA;

        setBackgroundColor(Color.argb(0, 0, 0, 0));
        setOnClickListener(this);
    }

    public void addAnimationListener(AnimationListener listener) {
        animationListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int minWidth = getSuggestedMinimumWidth() + getPaddingLeft() + getPaddingRight();
        int width = ViewCompat.resolveSizeAndState(minWidth, widthMeasureSpec, 1);

        int minHeight = MeasureSpec.getSize(width) + getPaddingBottom() + getPaddingTop();
        int height = ViewCompat.resolveSizeAndState(minHeight, heightMeasureSpec, 0);

        selectorRadius = height / 2;
        enabledSelectorCenter.set(width - selectorRadius, height / 2);
        disabledSelectorCenter.set(selectorRadius, height / 2);
        if (isChecked()) {
            currentSelectorCenter.set(disabledSelectorCenter.x, disabledSelectorCenter.y);
        } else {
            currentSelectorCenter.set(enabledSelectorCenter.x, enabledSelectorCenter.y);
        }

        int borderPadding = BORDER_WIDTH / 2;
        backgroundRect.set(borderPadding, borderPadding, width - borderPadding,
                height - borderPadding);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        drawBackground(canvas);
        drawBorder(canvas);
        drawSelector(canvas);
    }


    @Override
    public void onClick(View v) {
        setEnabled(false);
        animationListener.onCheckedAnimationStar();
        ObjectAnimator.ofFloat(CoolSwitch.this, "animationProgress", 0, 1)
                .setDuration(MOVEMENT_ANIMATION_DURATION_MS)
                .start();


    }

    public void setColor(int color) {
        mcolor = color;
    }

    private void drawBackground(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mcolor);
        paint.setAlpha(backgroundAlpha);
        canvas.drawRoundRect(backgroundRect, selectorRadius, selectorRadius, paint);
    }

    private void drawBorder(Canvas canvas) {
        paint.setStrokeWidth(BORDER_WIDTH);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mcolor);
        canvas.drawRoundRect(backgroundRect, selectorRadius, selectorRadius, paint);
    }

    private void drawSelector(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(mcolor);
        paint.setAlpha(OPAQUE);
        canvas.drawCircle(currentSelectorCenter.x, currentSelectorCenter.y,
                (int) (selectorRadius * SELECTOR_RATIO), paint);
    }

    @SuppressWarnings("unused")
    public void setBackgroundAlpha(int backgroundAlpha) {
        this.backgroundAlpha = backgroundAlpha;

        postInvalidate();
    }

    @SuppressWarnings("unused")
    public void setAnimationProgress(float animationProgress) {
        int left = disabledSelectorCenter.x;
        int right = enabledSelectorCenter.x;

        currentSelectorCenter.x = interpolate(animationProgress, left, right);
        if (isChecked()) {
            currentSelectorCenter.x = getWidth() - currentSelectorCenter.x;
        }
        if (animationProgress == 1f) {
            if (isChecked()) {
                animationListener.onCheckedAnimationFinished();
                setEnabled(true);
            } else {
                animationListener.onUncheckedAnimationFinished();
                setEnabled(true);
            }
        }
        initialize();
        postInvalidate();
    }

    private int interpolate(float animationProgress, int left, int right) {
        return (int) (left + interpolator.getInterpolation(animationProgress) * (right - left));
    }


    /**
     * Listener to receive notifications about the state of the CoolSwitch.
     */
    public static interface AnimationListener {

        void onCheckedAnimationStar();

        void onCheckedAnimationFinished();

        void onUncheckedAnimationFinished();
    }


}
