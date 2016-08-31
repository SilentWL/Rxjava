package wanglei.administrator.silent_wl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class SelectImageButton extends ImageButton implements View.OnTouchListener{

    public SelectImageButton(Context context) {
        super(context);
        setOnTouchListener(this);
    }

    public SelectImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    public SelectImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            view.setAlpha(0.5f);
            view.setBackgroundColor(Color.parseColor("#ffffbb33"));
        }
        else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
            view.setAlpha(1.0f);
            view.setBackgroundColor(Color.parseColor("#ff0099cc"));
        }
        return false;
    }
}
