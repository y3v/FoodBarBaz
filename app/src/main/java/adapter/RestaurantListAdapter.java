package adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.os.Bundle;
import android.app.Activity;
import android.os.AsyncTask;
import java.io.InputStream;
import android.util.Log;
import android.graphics.BitmapFactory;
import android.content.res.Resources;

//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.example.yev.foodbarbaz.HomePage;
import com.example.yev.foodbarbaz.R;
import com.example.yev.foodbarbaz.RestaurantMenu;

import org.w3c.dom.Text;

import java.util.List;

import POJO.Restaurant;

/**
 * Created by olile on 2018-03-15.
 */

public class RestaurantListAdapter extends ArrayAdapter<Restaurant>{

    public RestaurantListAdapter(Context context, List<Restaurant> objects) {
        super(context, R.layout.restaurant_list_item, objects);
    }


    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View restaurantView = inflater.inflate(R.layout.restaurant_list_item, viewGroup, false);

        final Restaurant restaurant = (Restaurant) getItem(i);

        TextView name = (TextView) restaurantView.findViewById(R.id.restaurant_name);
        TextView address = (TextView) restaurantView.findViewById(R.id.address);
        TextView category = (TextView) restaurantView.findViewById(R.id.category);
        TextView rating = (TextView) restaurantView.findViewById(R.id.rating);
        ImageView restaurantImage = (ImageView) restaurantView.findViewById(R.id.imageRestaurant);
        ToggleButton favorited = (ToggleButton) restaurantView.findViewById(R.id.favorited);
        Button seeMap = (Button) restaurantView.findViewById(R.id.see_on_map);
        Button seeMenu = (Button) restaurantView.findViewById(R.id.see_menu);

        name.setText(restaurant.getName());
        address.setText(restaurant.getAddress());
        //category.setText(restaurant.getCategory());
        rating.setText(restaurant.getRating());
        //restaurantImage.setImageResource(restaurant.getPhoto());
        //Glide.with(restaurantView).load(restaurant.getPhoto()).into(restaurantImage);
        Glide.with(restaurantView.getContext())
                .load(restaurant.getPhoto())
                .placeholder(R.drawable.ic_home_black_24dp)
                .into(restaurantImage);

        // map click listener
        seeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewGroup.getContext(), RestaurantMenu.class);
                intent.putExtra("restaurant", restaurant);

                view.getContext().startActivity(intent);
                Toast.makeText(view.getContext(), "henlo", Toast.LENGTH_SHORT).show();
            }
        });

        restaurantImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialogBox(restaurant.getPhoto());
                Log.e("DIALOG BOX", "CLICK ON PIC" );
            }
        });

        Log.e("IMAGEError!!!", restaurantImage.toString());


        restaurantImage.setMaxWidth(100);
        restaurantImage.setMaxHeight(100);

        return restaurantView;
    }

  private void createDialogBox(String imageURI){
       /* LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.image_popup, null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        Button dismiss = dialogView.findViewById(R.id.dismissButton);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();*/

      AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
      LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
      View mView = inflater.inflate(R.layout.image_popup, null);

      Button dismiss = mView.findViewById(R.id.dismissButton);
      ImageView bigPic = mView.findViewById(R.id.bigRestaurantPic);
      Glide.with(bigPic.getContext())
              .load(imageURI)
              .placeholder(R.drawable.ic_home_black_24dp)
              .into(bigPic);
      bigPic.setMaxHeight(220);
      bigPic.setMaxWidth(220);
      mBuilder.setView(mView);
      final AlertDialog dialog = mBuilder.create();



      dismiss.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              dialog.dismiss();
          }
      });


      dialog.show();

    }
}

