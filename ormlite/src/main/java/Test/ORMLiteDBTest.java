package Test;

import android.hardware.usb.UsbRequest;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.test.ServiceTestCase;
import android.test.mock.MockContext;
import android.util.Log;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

import DBHelper.DataBaseHelper;
import bean.User;
import android.content.Context;
/**
 * Created by Administrator on 2016/8/1 0001.
 */
public class ORMLiteDBTest extends InstrumentationTestCase{
    private static final String TAG = "ORMLiteDBTest";
    Context context;
    private Context getTestContext() {
        try {
            Method getTestContext = ServiceTestCase.class.getMethod("getTestContext");
            return (Context) getTestContext.invoke(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void testAddUser(){


        DataBaseHelper helper = DataBaseHelper.getHelper(getTestContext());

        try{
            User u1 = new User("wl", "lei.wang@szupai.com");
//            helper.getUserDao().create(u1);
//
//            u1 = new User("ym", "min.yang@szupai.com");
//            helper.getUserDao().create(u1);
//
//            u1 = new User("xb", "xinbo.zhang@szupai.com");
//            helper.getUserDao().create(u1);
//
//            u1 = new User("gt", "ting.guo@szupai.com");
//            helper.getUserDao().create(u1);
            u1 = new User("u1", "u1@szupai.com");
            helper.getUserDao().create(u1);
            testList();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void testList(){
        DataBaseHelper helper = DataBaseHelper.getHelper(getTestContext());

        try {
            List<User> users = helper.getUserDao().queryForAll();
            System.out.print(users.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
