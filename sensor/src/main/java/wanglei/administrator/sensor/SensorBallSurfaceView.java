package wanglei.administrator.sensor;

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
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Created by Administrator on 2016/8/19 0019.
 */
public class SensorBallSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "SensorBallView";
    private static final int LIMIT_RADIUS = 150; //dp

    private SurfaceHolder mSurfaceHolder;
    private Context mContext = null;
    private boolean mViewRestore = false;


    private static final int LIMIT_CIRCLE_LINE_WIDTH = 5; //dp
    private DrawViewThread mDrawViewThread;
    private boolean mThreadRun = false;
    private Semaphore mSemaphore = new Semaphore(1);

    private int mRadius = dp2px(LIMIT_RADIUS);
    private int mCircleX = 0;
    private int mWidth = 0;
    private int mHeight = 0;
    private Paint mCirclePaint = null;
    private int mCircleLineWidth = dp2px(LIMIT_CIRCLE_LINE_WIDTH);

    private float mCircleBallRaidus;
    private Paint mCircleBallPaint = null;
    private float mCircleBallX = 0;
    private float mCircleBallY = 0;
    private Bitmap mCircleBallBitmap;
    private Paint mCircleBallBitmapPaint;
    private float mRotateDegree = 0;
    private Matrix mRotateMatrix;

    private SensorManager mSensorMgr;
    private Sensor mSensor;
    private float x, y, z;
    private SensorEventListener mSensorEventListener;

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

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

        initStyles(context, attrs, defStyleAttr);
        initPaint();
        initCircleBallBitmap();

        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
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
                    mSemaphore.acquire();
                    x = sensorEvent.values[SensorManager.DATA_X];
                    y = sensorEvent.values[SensorManager.DATA_Y];
                    z = sensorEvent.values[SensorManager.DATA_Z];

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
        mCircleLineWidth = dp2px(typedArray.getDimensionPixelSize(R.styleable.SensorBallViewAttrs_CircleLineWidth, mCircleLineWidth));
        typedArray.recycle();
    }

    private void initPaint() {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(true);
        mCirclePaint.setColor(Color.RED);
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
        int radius = Math.min(mCircleBallBitmap.getWidth() - 10, mCircleBallBitmap.getHeight() - 10) / 2;
        Bitmap target = Bitmap.createBitmap(radius * 2, radius * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        mCircleBallBitmapPaint = new Paint();

        mCircleBallBitmapPaint.setAntiAlias(true);
        mCircleBallBitmapPaint.setDither(true);
        mCircleBallBitmapPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCircleBallBitmapPaint.setStrokeWidth(1);

        canvas.drawCircle(radius, radius, radius, mCircleBallBitmapPaint);

        mCircleBallBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(mCircleBallBitmap, radius - mCircleBallBitmap.getWidth() / 2, radius - mCircleBallBitmap.getHeight() / 2, mCircleBallBitmapPaint);

        mCircleBallBitmap = target;
        mCircleBallRaidus = radius;
        mRotateMatrix = new Matrix();
        mRotateDegree = 20;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

        mWidth = Math.min(width, height);

        if (mWidth == 0) {
            mWidth = mRadius * 2 + getPaddingLeft() + getPaddingRight() + mCircleLineWidth;
        }

        mRadius = (mWidth - getPaddingLeft() - getPaddingRight() - mCircleLineWidth) / 2;
        mHeight = mWidth - getPaddingLeft() - getPaddingRight() + getPaddingBottom() + getPaddingTop();

        mCircleX = mRadius + mCircleLineWidth / 2;

        if (!mViewRestore) {
            mCircleBallY = mCircleBallX = mCircleX;
        }
        mRotateMatrix.setTranslate(mCircleBallX - mCircleBallRaidus, mCircleBallY - mCircleBallRaidus);

        setMeasuredDimension(width, height);
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

    private class DrawViewThread extends Thread {
        @Override
        public void run() {
            while (mThreadRun) {
                try {
                    drawUI();
                    Thread.sleep(16);
                    mSemaphore.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        private void drawUI() {
            Canvas canvas = mSurfaceHolder.lockCanvas();
            canvas.save();
            canvas.drawColor(Color.WHITE);
            drawOnCanvas(canvas);
            canvas.restore();
            mSurfaceHolder.unlockCanvasAndPost(canvas);
        }

        private void drawOnCanvas(Canvas canvas) {
            canvas.translate(getPaddingLeft(), getPaddingTop());
            canvas.drawCircle(mCircleX, mCircleX, mRadius, mCirclePaint);
            drawCircleBallBitmap(canvas);
            //drawCircleBall(canvas);
        }

        private void drawCircleBallBitmap(Canvas canvas) {
            float preX = mCircleBallX;
            float preY = mCircleBallY;
            canvas.scale(1, -1);
            mCircleBallX = mCircleBallX - dp2px((int) x) * 2;
            mCircleBallY = mCircleBallY + dp2px((int) y) * 2;
            float l = (float) Math.sqrt((mCircleBallX - mCircleX) * (mCircleBallX - mCircleX) + (mCircleBallY - mCircleX) * (mCircleBallY - mCircleX));

            if (l > mRadius - mCircleLineWidth - mCircleBallRaidus) {
                double degree = Math.atan2((mCircleBallX - mCircleX), (mCircleBallY - mCircleX));

                mCircleBallX = (float) Math.sin(degree) * (mRadius - mCircleLineWidth - mCircleBallRaidus) + mCircleX;
                mCircleBallY = (float) Math.cos(degree) * (mRadius - mCircleLineWidth - mCircleBallRaidus) + mCircleX;
            }
            Log.w(TAG, "ThreadId = " + Thread.currentThread().getId() + " drawCircleBall: mCircleBallX=" + mCircleBallX + ",mCircleBallY=" + mCircleBallY);

            mRotateDegree = (mCircleBallX - preX + mCircleBallY - preY) * 6;
            mRotateMatrix.postTranslate(mCircleBallX - preX, mCircleBallY - preY);
            mRotateMatrix.postRotate(mRotateDegree, mCircleBallX, mCircleBallY);

            canvas.drawBitmap(mCircleBallBitmap, mRotateMatrix, null);

        }

        private void drawCircleBall(Canvas canvas) {
            mCircleBallX = mCircleBallX - dp2px((int) x) * 2;
            mCircleBallY = mCircleBallY + dp2px((int) y) * 2;
            float l = (float) Math.sqrt((mCircleBallX - mCircleX) * (mCircleBallX - mCircleX) + (mCircleBallY - mCircleX) * (mCircleBallY - mCircleX));

            if (l > mRadius - mCircleLineWidth - mRadius / 4) {
                double degree = Math.atan2((mCircleBallX - mCircleX), (mCircleBallY - mCircleX));

                mCircleBallX = (float) Math.sin(degree) * (mRadius - mCircleLineWidth - mCircleBallRaidus) + mCircleX;
                mCircleBallY = (float) Math.cos(degree) * (mRadius - mCircleLineWidth - mCircleBallRaidus) + mCircleX;
            }
            Log.w(TAG, "ThreadId = " + Thread.currentThread().getId() + " drawCircleBall: mCircleBallX=" + mCircleBallX + ",mCircleBallY=" + mCircleBallY);

            canvas.drawCircle(mCircleBallX, mCircleBallY, mCircleBallRaidus, mCircleBallPaint);
        }
    }

}
