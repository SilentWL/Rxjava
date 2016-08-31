package wanglei.administrator.silent_wl;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Created by Administrator on 2016/8/19 0019.
 */
public class SensorBallSurfaceView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private static final String TAG = "SensorBallView";
    private static final int LIMIT_RADIUS = 150; //dp

    private SurfaceHolder mSurfaceHolder;
    private Context mContext = null;
    private boolean mViewRestore = false;


    private static final int LIMIT_CIRCLE_LINE_WIDTH = 5; //dp
    private DrawViewThread mDrawViewThread;
    private boolean mThreadRun = false;
    private Semaphore mSemaphore = new Semaphore(1);

    private int mRadius = MeasureUtils.dp2px(getResources(), LIMIT_RADIUS);
    private int mCircleX = 0;
    private int mWidth = 0;
    private int mHeight = 0;
    private Paint mCirclePaint = null;
    private int mCircleLineWidth = LIMIT_CIRCLE_LINE_WIDTH;

    private float mCircleBallRaidus;
    private Paint mCircleBallPaint = null;
    private float mCircleBallX = 0;
    private float mCircleBallY = 0;
    private Bitmap mCircleBallBitmap;
    private Paint mCircleBallBitmapPaint;
    private float mRotateDegree = 0;
    private Matrix mRotateMatrix;
    private WindowManager mWindowManager;
    private boolean mDrawView = true;
    private boolean mDragCircleBall = false;
    private float mPreX;
    private float mPreY;

    private SensorManager mSensorMgr;
    private Sensor mSensor;
    private float x, y, z;
    private SensorEventListener mSensorEventListener;


    private Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    public SensorBallSurfaceView(Context context) {
        this(context, null);
    }

    public SensorBallSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SensorBallSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        initStyles(context, attrs, defStyleAttr);
        initPaint();
        initCircleBallBitmap();

        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);

        setOnTouchListener(this);
    }

    private void initSensor(Context context) {
        // 得到当前手机传感器管理对象
        mSensorMgr = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        // 加速重力感应对象
        mSensor = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // 实例化一个监听器
        mSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                // 得到各轴上的重力加速度

                try {
                    x = sensorEvent.values[SensorManager.DATA_X];
                    y = sensorEvent.values[SensorManager.DATA_Y];
                    z = sensorEvent.values[SensorManager.DATA_Z];
                    mDrawView = true;
                    mSemaphore.release();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.w(TAG, "ThreadId = " + Thread.currentThread().getId() + " onSensorChanged: x=" + x + ",y=" + y + ",z=" + z);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        // 注册listener，第三个参数是检测的精确度
        mSensorMgr.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_UI);
    }

    private void initStyles(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SensorBallViewAttrs, defStyleAttr, -1);

        Drawable drawable = typedArray.getDrawable(R.styleable.SensorBallViewAttrs_BallBitmap);

        if (drawable != null) {
            mCircleBallBitmap = drawable2Bitmap(drawable).copy(Bitmap.Config.ARGB_8888, true);
        }
        mCircleLineWidth = typedArray.getDimensionPixelSize(R.styleable.SensorBallViewAttrs_CircleLineWidth, mCircleLineWidth);
        mCircleBallRaidus = typedArray.getDimensionPixelSize(R.styleable.SensorBallViewAttrs_BallRadius, mRadius / 8);
        typedArray.recycle();
    }

    private void initPaint() {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(true);
        mCirclePaint.setColor(Color.parseColor("#7f0099cc"));
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mCircleLineWidth);

        mCircleBallPaint = new Paint();
        mCircleBallPaint.setAntiAlias(true);
        mCircleBallPaint.setDither(true);
        mCircleBallPaint.setColor(Color.BLUE);
        mCircleBallPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCircleBallPaint.setStrokeWidth(mCircleLineWidth);
    }

    private void initCircleBallBitmap() {
        int radius = (int) mCircleBallRaidus;
        Bitmap target = Bitmap.createBitmap(radius * 2, radius * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        mCircleBallBitmapPaint = new Paint();

        mCircleBallBitmapPaint.setAntiAlias(true);
        mCircleBallBitmapPaint.setDither(true);
        mCircleBallBitmapPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        canvas.drawCircle(radius, radius, radius, mCircleBallBitmapPaint);

        mCircleBallBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        Matrix matrix = new Matrix();

        matrix.setScale((float) (radius * 2) / (mCircleBallBitmap.getWidth()), (float) radius * 2 / mCircleBallBitmap.getHeight(), 0, 0);
        canvas.drawBitmap(mCircleBallBitmap, matrix, mCircleBallBitmapPaint);

        mCircleBallBitmap = target;
        mRotateMatrix = new Matrix();
        mRotateDegree = 20;
    }

    public int getDisplayRotation() {
        int rotation = mWindowManager.getDefaultDisplay().getRotation();

        if (rotation == Surface.ROTATION_90) {
            return 1;

        } else if (rotation == Surface.ROTATION_270) {
            return -1;
        } else {
            return 0;
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));

        if (mWidth == 0) {
            mWidth = mRadius * 2 + getPaddingLeft() + getPaddingRight() + mCircleLineWidth;
        }

        mRadius = (mWidth - getPaddingLeft() - getPaddingRight() - mCircleLineWidth) / 2;
        mHeight = mWidth - getPaddingLeft() - getPaddingRight() + getPaddingBottom() + getPaddingTop();

        mCircleX = mRadius + mCircleLineWidth / 2;

        if (!mViewRestore) {
            mPreX = mPreY = mCircleBallY = mCircleBallX = mCircleX;
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {


    }

    private static String INSTANCE = "Instance";
    private static String ORITATION = "Oritation";


    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        ArrayList<Float> oritation = new ArrayList<>();
        oritation.add(x);
        oritation.add(y);
        oritation.add(z);
        oritation.add(mCircleBallX);
        oritation.add(mCircleBallY);
        oritation.add(mPreX);
        oritation.add(mPreY);

        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        bundle.putSerializable(ORITATION, oritation);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            ArrayList<Float> oritation = (ArrayList<Float>) bundle.getSerializable(ORITATION);
            x = oritation.get(0);
            y = oritation.get(1);
            z = oritation.get(2);
            mCircleBallX = oritation.get(3);
            mCircleBallY = oritation.get(4);
            mPreX = oritation.get(5);
            mPreY = oritation.get(6);

            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            mViewRestore = true;

        } else {
            super.onRestoreInstanceState(state);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        initSensor(mContext);

        mThreadRun = true;
        mDrawViewThread = new DrawViewThread();
        mDrawViewThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mSensorMgr != null) {
            mSensorMgr.unregisterListener(mSensorEventListener);
            mSensorMgr = null;
        }
        mThreadRun = false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (pointInCircleBallRect(motionEvent.getX(), motionEvent.getY()) && (motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_MOVE)) {
            mDragCircleBall = true;
            mDrawView = true;
            mCircleBallX = motionEvent.getX();
            mCircleBallY = motionEvent.getY();
            mSemaphore.release();

            if (mSensorMgr != null) {
                mSensorMgr.unregisterListener(mSensorEventListener);
                mSensorMgr = null;
            }
        } else {
            mDrawView = false;
            mDragCircleBall = false;

            if (mSensorMgr == null) {
                initSensor(mContext);
            }
        }
        return true;
    }

    private class DrawViewThread extends Thread {
        @Override
        public void run() {
            while (mThreadRun) {
                try {
                    if (mDrawView) {
                        mSemaphore.acquire();
                        drawUI();
                        mDrawView = false;
                    }
                    //Thread.sleep(16);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void drawUI() {
            Canvas canvas = mSurfaceHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC);
                drawOnCanvas(canvas);

                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }

        @NonNull
        private Paint clearCanvas(Canvas canvas) {
            Paint paint = new Paint();
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawPaint(paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            return paint;
        }

        private void drawOnCanvas(Canvas canvas) {
            canvas.translate(getPaddingLeft(), getPaddingTop());
            canvas.drawCircle(mCircleX, mCircleX, mRadius, mCirclePaint);
            drawCircleBallBitmap(canvas);
        }

        private void drawCircleBallBitmap(Canvas canvas) {

            int orientation = getDisplayRotation();

            if (!mDragCircleBall) {
                mCircleBallX = mCircleBallX + MeasureUtils.dp2px(getResources(), ((int) y)) * 2 * orientation;
                mCircleBallY = mCircleBallY + MeasureUtils.dp2px(getResources(), (int) x) * 2 * orientation;
            }

            if (!circleBallInCircleRect(mCircleBallX, mCircleBallY)) {
                double degree = Math.atan2((mCircleBallX - mCircleX), (mCircleBallY - mCircleX));

                mCircleBallX = (float) Math.sin(degree) * (mRadius - mCircleLineWidth - mCircleBallRaidus) + mCircleX;
                mCircleBallY = (float) Math.cos(degree) * (mRadius - mCircleLineWidth - mCircleBallRaidus) + mCircleX;
            }
            Log.w(TAG, "ThreadId = " + Thread.currentThread().getId() + " drawCircleBall: mCircleBallX=" + mCircleBallX + ",mCircleBallY=" + mCircleBallY);

            mRotateMatrix.setTranslate(mCircleBallX - mCircleBallRaidus, mCircleBallY - mCircleBallRaidus);

            if (!mDragCircleBall) {
                mRotateDegree += (mCircleBallX - mPreX + mCircleBallY - mPreY) * 6;
                mRotateMatrix.postRotate(mRotateDegree, mCircleBallX, mCircleBallY);
            }


            mPreX = mCircleBallX;
            mPreY = mCircleBallY;
            canvas.drawBitmap(mCircleBallBitmap, mRotateMatrix, null);
        }
    }

    private boolean circleBallInCircleRect(float x, float y) {
        float l = (float) Math.sqrt((x - mCircleX) * (x - mCircleX) + (y - mCircleX) * (y - mCircleX));

        return l <= mRadius - mCircleLineWidth - mCircleBallRaidus;
    }

    private boolean pointInCircleBallRect(float x, float y) {
        float l = (float) Math.sqrt((x - mCircleBallX) * (x - mCircleBallX) + (y - mCircleBallY) * (y - mCircleBallY));

        return l <= mCircleBallRaidus;
    }

}
