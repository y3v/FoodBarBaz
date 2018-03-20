package POJO;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;

/**
 * Created by olile on 2018-03-17.
 */

public class Restaurant implements Parcelable {
    String name;
    String placeID;
    String address;
    String photo;
    String priceLevel;
    String rating;
    String category;
    // Where each key is a section like appetizers, main course, desserts, etc.
    HashMap<String, List<MenuItem>> menu;


    public Restaurant(String name, String placeId, String address, String imageUrl, String priceLevel, String rating, String category) {
        this.name = name;
        this.placeID = placeId;
        this.address = address;
        this.photo = imageUrl;
        this.priceLevel = priceLevel;
        this.rating = rating;
        this.category = category;

        this.menu = new HashMap<>();
    }

    protected Restaurant(Parcel in) {
        name = in.readString();
        placeID = in.readString();
        address = in.readString();
        photo = in.readString();
        priceLevel = in.readString();
        rating = in.readString();
        category = in.readString();
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

    public String getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(String priceLevel) {
        this.priceLevel = priceLevel;
    }


    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String imageUrl) {
        this.photo = imageUrl;
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
        parcel.writeString(name);
        parcel.writeString(placeID);
        parcel.writeString(address);
        parcel.writeString(photo);
        parcel.writeString(priceLevel);
        parcel.writeString(rating);
        parcel.writeString(category);
        parcel.writeMap(menu);
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", placeID='" + placeID + '\'' +
                ", address='" + address + '\'' +
                ", imageUrl=" + photo +
                ", priceLevel='" + priceLevel + '\'' +
                ", rating='" + rating + '\'' +
                ", category='" + category + '\'' +
                ", menu=" + menu +
                '}';
    }
}
