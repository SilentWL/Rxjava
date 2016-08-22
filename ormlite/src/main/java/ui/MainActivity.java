package ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.sql.SQLException;
import java.util.List;

import DBHelper.DataBaseHelper;
import bean.User;
import wanglei.administrator.ormlite.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataBaseHelper helper = DataBaseHelper.getHelper(MainActivity.this);

        try{
            User u1 = new User("wl", "lei.wang@szupai.com");
            helper.getUserDao().create(u1);

            u1 = new User("ym", "min.yang@szupai.com");
            helper.getUserDao().create(u1);

            u1 = new User("xb", "xinbo.zhang@szupai.com");
            helper.getUserDao().create(u1);

            u1 = new User("gt", "ting.guo@szupai.com");
            helper.getUserDao().create(u1);

            testList();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void testList(){
        DataBaseHelper helper = DataBaseHelper.getHelper(MainActivity.this);

        try {
            List<User> users = helper.getUserDao().queryForAll();
            System.out.print(users.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
