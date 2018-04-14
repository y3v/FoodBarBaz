package POJO;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import database.HistoryHelper;

public class SearchHistory {
    private static List<String> history;
    private static HistoryHelper myDb;

    protected SearchHistory(){
    }

    public static List<String> getList(Context context, Long id){
        if (history == null){
            Log.i("History Size:", "NULLLLLL");
            getValuesFromDB(context, id);
        }
        return history;
    }

    public static void addToList(String value, Long id){

        Boolean ret = myDb.insertQuery(value, id);
        if (ret){
            Log.i("INSERT", " SUCCEEDED!!!");
        }
        else{
            Log.i("INSERT", " FAILED!!!");
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
        history = new ArrayList<>();
        myDb = new HistoryHelper(context);
        Cursor res = myDb.getData(id);
        if (res.getCount() == 0){
            Log.d("DATABASE::::", "FRESH PAGE");
        }
        else{
            while(res.moveToNext()){
                history.add(res.getString(1));
            }
        }
        if(!history.isEmpty()){
            Collections.reverse(history);
        }
    }

}
