package wanglei.administrator.qqdragger;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;


/**
 * Created by Administrator on 2016/8/29 0029.
 */
public class DraggerBallView extends View {
    private final static int BALL_RADIUS = 20; //dp
    private final static int BALL_DRAGGER_DISTANCE = 160; //dp

    private Context mContext;
    private int mBallRadius = dp2Px(BALL_RADIUS);
    private int mBallDraggerDistance = dp2Px(BALL_DRAGGER_DISTANCE);
    private Paint mBallPaint;
    private Paint mBezierPaint;
    private Path mPath1;
    private Path mPath2;
    private float mSX, mSY;
    private float mCSX, mCSY;
    private ValueAnimator mValueAnimator;
    private boolean mBeyondDistance;
    private boolean mStop;
    private int mBallPaintAlpha = 255;


    private int dp2Px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public void resetBallPaint(){
        mBallPaintAlpha = 255;
        mBeyondDistance = false;
        mStop = false;
        placeXYToCenter();
        requestLayout();
        invalidate();
    }
    public DraggerBallView(Context context) {
        this(context, null);
    }

    public DraggerBallView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }



    public DraggerBallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        initBallPaint();
        placeXYToCenter();
        initBezierPaint();
        mPath1 = new Path();

        mValueAnimator = ValueAnimator.ofObject(new PointEvaluator(), new PointF(mSX, mSY), new PointF(mCSX, mCSY));
        mValueAnimator.setDuration(300);
        mValueAnimator.setInterpolator(new OvershootInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PointF point = (PointF) valueAnimator.getAnimatedValue();
                mCSX = point.x;
                mCSY = point.y;
                invalidate();
            }
        });
    }

    static class PointEvaluator implements TypeEvaluator{
        @Override
        public Object evaluate(float v, Object o, Object t1) {
            PointF startPoint = (PointF)o;
            PointF endPoint = (PointF)t1;
            return new PointF(startPoint.x + (endPoint.x - startPoint.x) * v, startPoint.y + (endPoint.y - startPoint.y) * v);
        }
    }

    private void initBezierPaint() {
        mBezierPaint = new Paint();
        mBezierPaint.setDither(true);
        mBezierPaint.setAntiAlias(true);
        mBezierPaint.setColor(Color.RED);
        mBezierPaint.setStyle(Paint.Style.FILL);
        mBezierPaint.setStrokeWidth(1);
    }

    private void placeXYToCenter() {
        //mCSX = mContext.getResources().getDisplayMetrics().widthPixels / 2 - mBallRadius;
        //mCSY = mContext.getResources().getDisplayMetrics().heightPixels / 2 - mBallRadius;
        mSX = mCSX = mBallRadius;
        mSY = mCSY = mBallRadius;
    }

    private void initBallPaint() {
        mBallPaint = new Paint();
        mBallPaint.setStyle(Paint.Style.FILL);
        mBallPaint.setStrokeWidth(1);
        mBallPaint.setColor(Color.RED);
        mBallPaint.setAntiAlias(true);
        mBallPaint.setDither(true);
    }


    private boolean xyBeyondDistance(float x, float y, float distance) {
        return Math.sqrt(Math.pow(x - mSX, 2) + Math.pow(y - mSY, 2)) > distance;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        if (mStop) {

            canvas.drawCircle(mCSX - 30, mCSY + 30, 8, mBallPaint);
            canvas.drawCircle(mCSX + 30, mCSY + 20, 8, mBallPaint);
            canvas.drawCircle(mCSX - 20, mCSY - 20, 5, mBallPaint);
            canvas.drawCircle(mCSX - 10, mCSY - 10, 3, mBallPaint);
            canvas.drawCircle(mCSX + 5, mCSY + 5, 6, mBallPaint);
            canvas.drawCircle(mCSX - 20, mCSY, 3, mBallPaint);
            canvas.drawCircle(mCSX + 5 ,mCSY  + 10, 6, mBallPaint);
            canvas.drawCircle(mCSX + 1 ,mCSY  + 2, 3, mBallPaint);
            canvas.drawCircle(mCSX + 5 ,mCSY  -10, 5, mBallPaint);
            mBallPaint.setAlpha(mBallPaintAlpha-=5);
            if (mBallPaintAlpha >= 0) {

                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       postInvalidate();
                    }
            }, 10);
            }
        }else {

            if (!mBeyondDistance) {
                if (mSX != mCSX || mSY != mCSY) {
                    drawDraggerPath(canvas);
                    canvas.drawCircle(mSX, mSY, mBallRadius / 2, mBallPaint);
                }
            }
            canvas.drawCircle(mCSX, mCSY, mBallRadius, mBallPaint);
        }
    }

    private void drawDraggerPath(Canvas canvas) {
        float dx = mCSX - mSX;
        float dy = mCSY - mSY;
        float angle = (float) Math.atan(Math.abs(dy / dx));

        final boolean flag = (dy / dx < 0);

        mPath1.reset();

        if (flag) {
            mPath1.moveTo((float) (mSX - Math.sin(angle) * mBallRadius* 0.5), (float)(mSY - Math.cos(angle) * mBallRadius * 0.5));
            mPath1.quadTo((float) ((mSX + mCSX) * 0.5), (float) ((mSY + mCSY) * 0.5), (float)(mCSX - mBallRadius * Math.sin(angle)), (float)(mCSY - mBallRadius * Math.cos(angle)));
            mPath1.lineTo((float) (mCSX + Math.sin(angle) * mBallRadius), (float) (mCSY + Math.cos(angle) * mBallRadius));
            mPath1.quadTo((float) ((mSX + mCSX) * 0.5), (float) ((mSY + mCSY) * 0.5), (float) (mSX + mBallRadius * 0.5 * Math.sin(angle)), (float)(mSY + mBallRadius * Math.cos(angle) * 0.5));
        } else {

            mPath1.moveTo((float) (mSX - Math.sin(angle) * mBallRadius * 0.5), (float) (mSY + Math.cos(angle) * mBallRadius * 0.5));
            mPath1.quadTo((float) ((mSX + mCSX) * 0.5), (float) ((mSY + mCSY) * 0.5), (float) (mCSX - mBallRadius * Math.sin(angle)), (float) (mCSY + mBallRadius * Math.cos(angle)));
            mPath1.lineTo((float) (mCSX + mBallRadius * Math.sin(angle)), (float) (mCSY - mBallRadius * Math.cos(angle)));
            mPath1.quadTo((float) ((mSX + mCSX) * 0.5), (float) ((mSY + mCSY) * 0.5), (float) (mSX + Math.sin(angle) * mBallRadius * 0.5), (float) (mSY - Math.cos(angle) * mBallRadius * 0.5));
        }

        mPath1.close();
        canvas.drawPath(mPath1, mBezierPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mBallRadius * 2, mBallRadius * 2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mStop){
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!xyBeyondDistance(event.getX(), event.getY(), mBallRadius)) {
                    getParent().requestDisallowInterceptTouchEvent(true);

                    mCSX = event.getX();
                    mCSY = event.getY();
                    mBeyondDistance = false;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mCSX = event.getX();
                mCSY = event.getY();
                if (!xyBeyondDistance(mCSX, mCSY, mBallDraggerDistance)) {
                    //mBeyondDistance = false;
                }else{
                    //mValueAnimator.setObjectValues(new PointF(mSX, mSY), new PointF(mCSX, mCSY));
                    //mValueAnimator.start();
                    mBeyondDistance = true;
                }

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mCSX = event.getX();
                mCSY = event.getY();
                if (!xyBeyondDistance(mCSX, mCSY, mBallDraggerDistance)) {
                    //mCSX = mSX;
                    //mCSY = mSY;
                    mValueAnimator.setObjectValues(new PointF(mCSX, mCSY), new PointF(mSX, mSY));
                    mValueAnimator.start();
                    //mBeyondDistance = false;
                }else{
                    mBeyondDistance = true;
                    mStop = true;
                }
                invalidate();
                break;
        }

        return true;
    }
}
