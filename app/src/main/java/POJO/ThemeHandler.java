package POJO;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import database.ThemeHelper;

public class ThemeHandler {

    private static String theme;
    private static ThemeHelper myDb;

    protected ThemeHandler(){
    }

    public static String getTheme(Context context, Long id){
        if (theme == null){
            Log.i("Theme:", "NULLLLLL");
            getValuesFromDB(context, id);
        }
        return theme;
    }

    public static void changeTheme(Long id, Context context){
        myDb = new ThemeHelper(context);
        theme = getTheme(context, id);
        if (theme == null){
            if (theme.equals("dark")){
                Boolean ret = myDb.updateEntry(id,"light");
            }
            else if (theme.equals("light")){
                Boolean ret = myDb.updateEntry(id, "dark");
            }
        }
        else{
            myDb.insertQuery("light", id);
        }
    }

    public static void removeFromList(String value, Long id){

        Boolean ret = myDb.deleteEntry(value, id);
        if (ret){
            Log.i("DELETE", " SUCCEEDED!!!");
        }
        else{
            Log.i("DELETE", " FAILED!!!");
        }
    }

    private static void getValuesFromDB(Context context, Long id){
        myDb = new ThemeHelper(context);
        Cursor res = myDb.getData(id);
        if (res.getCount() == 0){
            Log.d("DATABASE::::", "FRESH PAGE");
        }
        else{
            while(res.moveToNext()){
                theme = res.getString(2);
            }
        }
    }
}
