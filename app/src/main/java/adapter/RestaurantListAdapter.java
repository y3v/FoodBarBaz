package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by olile on 2018-03-15.
 */

public class RestaurantListAdapter extends ArrayAdapter{

    public RestaurantListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}