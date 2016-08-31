package wanglei.administrator.silent_wl;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class DisplayQRCodeImagePopup extends PopupWindow implements PopupWindow.OnDismissListener{
    private Context mContext;
    private DisplayMetrics mDisplayMetrics = new DisplayMetrics();
    private ChangeAlphaInterface mChangeAlphaInterface;

    public DisplayQRCodeImagePopup(int ImageViewId, int ImageResourceId, Context context) {

        mContext = context;
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.qrcode_display_popup, null);

        ((ImageView) contentView.findViewById(ImageViewId)).setImageResource(ImageResourceId);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        setWidth(mDisplayMetrics.heightPixels - MeasureUtils.dp2px(context.getResources(), 100));
        setHeight(mDisplayMetrics.heightPixels - MeasureUtils.dp2px(context.getResources(), 100));

        setContentView(contentView);


        setTouchable(true);

        // 使其聚集
        setFocusable(true);
        // 设置允许在外点击消失
        setOutsideTouchable(true);
        setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                dismiss();
                //mChangeAlphaInterface.setAlpha(1.0f);
                return true;
            }
        });

        setOnDismissListener(this);

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        setBackgroundDrawable(new BitmapDrawable());
    }

    public void show(final View ParentView) {
        setAnimationStyle(R.style.AnimationPopup);
        showAtLocation(ParentView, Gravity.CENTER, 0, 0);

        if (mChangeAlphaInterface != null){
            mChangeAlphaInterface.setAlpha(0.2f);
        }
    }

    @Override
    public void onDismiss() {

        if (mChangeAlphaInterface != null){
            mChangeAlphaInterface.setAlpha(1.0f);
        }
    }

    public interface ChangeAlphaInterface{
        void setAlpha(float alpha);
    }

    public DisplayQRCodeImagePopup setChangeAlphaInterface(ChangeAlphaInterface changeAlphaInterface){
        mChangeAlphaInterface = changeAlphaInterface;
        return this;
    }
}
