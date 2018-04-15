package POJO;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import database.AutoLoginHelper;


public class AutoLogin {

    private static User user;
    private static AutoLoginHelper myDb;

    protected AutoLogin(){
    }

    public static User getUser(Context context){
        if (user == null){
            Log.i("User:", "NULLLLLL");
            getValuesFromDB(context);
        }
        return user;
    }

    public static void addToDb(Context context, User user){
        myDb = new AutoLoginHelper(context);
        Boolean ret = myDb.insertEntry(user.getId(), user.getUsername(), user.getPassword(), user.getFirstname(), user.getLastname(), user.getEmail());
        if (ret){
            Log.i("INSERT", " SUCCEEDED!!!");
        }
        else{
            Log.i("INSERT", " FAILED!!!");
        }

    }

    public static void removeUser(Long id){

        Boolean ret = myDb.deleteEntry(id);
        if (ret){
            Log.i("DELETE", " SUCCEEDED!!!");
        }
        else{
            Log.i("DELETE", " FAILED!!!");
        }
    }

    private static void getValuesFromDB(Context context){
        myDb = new AutoLoginHelper(context);
        Cursor res = myDb.getData();
        if (res.getCount() == 0){
            Log.d("DATABASE::::", "FRESH PAGE");
        }
        else{
            while(res.moveToNext()){
                user = new User(res.getLong(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6));
            }
        }
    }
}
