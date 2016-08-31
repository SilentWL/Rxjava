package wanglei.administrator.rotate3d;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{

    private GestureDetector mGestureDetector;
    private ImageView mImageView1;
    private ImageView mImageView2;
    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {

        Rotate3dAnimation rotation = new Rotate3dAnimation(
                0,
                720,
                mImageView1.getWidth() / 2,
                mImageView1.getHeight() / 2, 600, false);
        rotation.setInterpolator(new OvershootInterpolator());
        rotation.setDuration(1000);

        mImageView1.startAnimation(rotation);
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGestureDetector = new GestureDetector(this, this);
        mImageView1 = (ImageView) findViewById(R.id.im1);
        mImageView2 = (ImageView) findViewById(R.id.im2);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }
}
