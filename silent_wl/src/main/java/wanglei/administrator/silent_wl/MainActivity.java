package wanglei.administrator.silent_wl;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements DisplayQRCodeImagePopup.ChangeAlphaInterface, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{
    private SelectImageButton mWeixinBtn;
    private SelectImageButton mQqBtn;
    private GestureDetector mGestureDetector;
    private LinearLayout mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View MainView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        setContentView(MainView);

        mGestureDetector = new GestureDetector(this, this);

        mInfo = (LinearLayout) findViewById(R.id.Info);

        Typeface faceCN = Typeface.createFromAsset (getAssets(), "fonts/cn3.ttf");
        Typeface faceEn = Typeface.createFromAsset (getAssets(), "fonts/Bernhard Modern Italic BT.ttf");
        ((TextView)findViewById(R.id.Name)).setTypeface(faceCN);
        ((TextView)findViewById(R.id.Tel)).setTypeface(faceEn);
        ((TextView)findViewById(R.id.Mail1)).setTypeface(faceEn);
        ((TextView)findViewById(R.id.Mail2)).setTypeface(faceEn);

        mQqBtn = ((SelectImageButton)findViewById(R.id.qq));
        mQqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DisplayQRCodeImagePopup(R.id.QRCodeImage, R.drawable.qq, MainActivity.this).setChangeAlphaInterface(MainActivity.this).show(MainView);
            }
        });

        mWeixinBtn = ((SelectImageButton)findViewById(R.id.weixin));
        mWeixinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DisplayQRCodeImagePopup(R.id.QRCodeImage, R.drawable.weixin, MainActivity.this).setChangeAlphaInterface(MainActivity.this).show(MainView);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        KeepScreenOn.keepScreenOn(MainActivity.this, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        KeepScreenOn.keepScreenOn(MainActivity.this, false);
    }

    @Override
    public void setAlpha(float alpha) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = alpha;
        getWindow().setAttributes(params);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        Rotate3dAnimation rotate3dAnimation = new Rotate3dAnimation(720, 0, mInfo.getWidth() / 2 + MeasureUtils.dp2px(getResources(), 60), mInfo.getHeight() / 2 + MeasureUtils.dp2px(getResources(), 60), 666, true);

        rotate3dAnimation.setDuration(1000);
        rotate3dAnimation.setInterpolator(new OvershootInterpolator());
        mInfo.startAnimation(rotate3dAnimation);
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}
