package wanglei.administrator.dagger;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import javax.inject.Inject;
import javax.inject.Named;

public class MainActivity extends AppCompatActivity {

    @Inject @Named("activity") Toast toast;
    @Inject @Named("application") Toast toast1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContructCompoment.Contruct(getApplication(), this, "Hello Android!").inject(this);

        toast.show();
        toast1.show();


    }
}
