package POJO;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import database.FavouritesHelper;
import database.HistoryHelper;

public class Favourites {

    private static List<String> favourites;
    private static FavouritesHelper myDb;

    protected Favourites(){
    }

    public static List<String> getList(Context context, Long id){
        if (favourites == null){
            Log.i("Favourites Size:", "NULLLLLL");
            getValuesFromDB(context, id);
        }
        return favourites;
    }

    public static void addToList(String value, Long id){

        Boolean ret = myDb.insertEntry(value, id);
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
        favourites = new ArrayList<>();
        myDb = new FavouritesHelper(context);
        Cursor res = myDb.getData(id);
        if (res.getCount() == 0){
            Log.d("DATABASE::::", "FRESH PAGE");
        }
        else{
            while(res.moveToNext()){
                favourites.add(res.getString(1));
            }
        }
    }
}
