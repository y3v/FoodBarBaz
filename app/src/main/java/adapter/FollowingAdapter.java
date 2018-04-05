package adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import POJO.Restaurant;
import POJO.User;
import yevoli.release.yev.foodbarbaz.MapsActivity;
import yevoli.release.yev.foodbarbaz.R;
import yevoli.release.yev.foodbarbaz.RestaurantMenu;


public class FollowingAdapter extends ArrayAdapter<User> {

    public FollowingAdapter(Context context, List<User> objects) {
        super(context, R.layout.following_list_row, objects);
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View followingView = inflater.inflate(R.layout.following_list_row, viewGroup, false);

        User user = getItem(i);

        TextView name = followingView.findViewById(R.id.textViewFollowingUser);
        name.setText(user.getUsername());

        //TO DO : Set listener on button

        return followingView;
    }

}


