package POJO;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by olile on 2018-03-17.
 */

public class Restaurant implements Parcelable {
    String placeId;
    String name;
    String address;
    String category;
    String rating;
    Uri imageUri;

    double latitude;
    double longitude;

    // Where each key is a section like appetizers, main course, desserts, etc.
    HashMap<String, List<MenuItem>> menu;


    public Restaurant(String placeId, String name, String address, String category, String rating, Uri imageUri) {
        this.placeId = placeId;
        this.name = name;
        this.address = address;
        this.category = category;
        this.rating = rating;

        if(imageUri == null || imageUri.equals(""))
            this.imageUri = Uri.fromFile(new File("/src/res/drawable/restaurant_icon.bmp"));
        else
            this.imageUri = imageUri;

        this.menu = new HashMap<>();
    }

    protected Restaurant(Parcel in) {
        placeId = in.readString();
        name = in.readString();
        address = in.readString();
        category = in.readString();
        rating = in.readString();
        imageUri = in.readParcelable(Uri.class.getClassLoader());
        latitude = in.readDouble();
        longitude = in.readDouble();
        menu = in.readHashMap(ClassLoader.getSystemClassLoader());
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public HashMap<String, List<MenuItem>> getMenu() {
        return menu;
    }

    public void setMenu(HashMap<String, List<MenuItem>> menu) {
        this.menu = menu;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(placeId);
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(category);
        parcel.writeString(rating);
        parcel.writeParcelable(imageUri, i);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeMap(menu);
    }
}
