package wanglei.administrator.glide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private ImageView mPicasso;
    private ImageView mGlide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPicasso = (ImageView) findViewById(R.id.Picasso);
        mGlide = (ImageView) findViewById(R.id.Glide);
        Picasso.with(this).load("https://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg").noFade().into(mPicasso);
        Glide.with(this).load("https://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg").centerCrop().into(mGlide);

    }
}
