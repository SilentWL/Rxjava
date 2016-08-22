package wanglei.administrator.sensor;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Administrator on 2016/8/18 0018.
 */
public class SensorBallView extends View {

    private static final String TAG = "SensorBallView";
    private static final int LIMIT_RADIUS = 150; //dp
    private static final int LIMIT_CIRCLE_LINE_WIDTH = 5; //dp

    private Context mContext = null;

    private int mRadius = dp2px(LIMIT_RADIUS);
    private int mCircleX = 0;
    private int mWidth = 0;
    private int mHeight = 0;
    private Paint mCirclePaint = null;
    private int mCircleLineWidth = dp2px(LIMIT_CIRCLE_LINE_WIDTH);

    private Paint mCircleBallPaint = null;
    private float mCircleBallX = 0;
    private float mCircleBallY = 0;
    private Bitmap mCircleBallBitmap;

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

    public SensorBallView(Context context) {
        this(context, null);
    }

    public SensorBallView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SensorBallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        initStyles(context, attrs, defStyleAttr);
        initPaint();
        initSensor(context);

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

                x = sensorEvent.values[SensorManager.DATA_X];
                y = sensorEvent.values[SensorManager.DATA_Y];
                z = sensorEvent.values[SensorManager.DATA_Z];

                Log.w(TAG, "ThreadId = " + Thread.currentThread().getId() + " onSensorChanged: x=" + x + ",y=" + y + ",z=" + z);
                postInvalidate();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        // 注册listener，第三个参数是检测的精确度
        mSensorMgr.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    private void initStyles(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SensorBallViewAttrs, defStyleAttr, -1);

        Drawable drawable = typedArray.getDrawable(R.styleable.SensorBallViewAttrs_BallBitmap);

        if (drawable != null) {
            mCircleBallBitmap = drawable2Bitmap(drawable);
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);

        mWidth = Math.min(width, height);

        if (mWidth == 0) {
            mWidth = mRadius * 2 + getPaddingLeft() + getPaddingRight() + mCircleLineWidth;
        }

        mRadius = (mWidth - getPaddingLeft() - getPaddingRight() - mCircleLineWidth) / 2;
        mHeight = mWidth - getPaddingLeft() - getPaddingRight() + getPaddingBottom() + getPaddingTop();

        mCircleBallY = mCircleBallX = mCircleX = mRadius + mCircleLineWidth / 2;

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        canvas.drawCircle(mCircleX, mCircleX, mRadius, mCirclePaint);
        drawCircleBall(canvas);
        canvas.restore();
    }

    private void drawCircleBall(Canvas canvas) {
        mCircleBallX = mCircleBallX - x * 3;
        mCircleBallY = mCircleBallY + y * 3;
        float l = (float) Math.sqrt((mCircleBallX - mCircleX) * (mCircleBallX - mCircleX) + (mCircleBallY - mCircleX) * (mCircleBallY - mCircleX));

        if (l > mRadius - mCircleLineWidth - mRadius / 4) {
            double degree = Math.atan2((mCircleBallX - mCircleX), (mCircleBallY - mCircleX));

            mCircleBallX = (float) Math.sin(degree) * (mRadius - mCircleLineWidth - mRadius / 4) + mCircleX;
            mCircleBallY = (float) Math.cos(degree) * (mRadius - mCircleLineWidth - mRadius / 4) + mCircleX;
        }

        canvas.drawCircle(mCircleBallX, mCircleBallY, mRadius / 4, mCircleBallPaint);
    }
}
