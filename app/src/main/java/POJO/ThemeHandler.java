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

        getValuesFromDB(context, id);

        return theme;
    }

    public static void addTheme(String t, Long id, Context context){
        myDb = new ThemeHelper(context);
        getValuesFromDB(context, id);
        if (theme != null){
            Boolean ret = myDb.insertQuery(t, id);
        }
        else{
            myDb.insertQuery("light", id);
        }
        theme = getTheme(context, id);
    }

    public static void removeTheme(Context context, Long id){
        myDb = new ThemeHelper(context);
        Boolean ret = myDb.deleteEntry(id);
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
                theme = res.getString(1);
            }
        }
    }
}
