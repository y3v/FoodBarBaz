package adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import POJO.Const;
import POJO.UserLocation;
import POJO.WebserviceActions;
import dialog.DialogFriendOptions;
import yevoli.release.yev.foodbarbaz.MapsActivity;
import yevoli.release.yev.foodbarbaz.FriendProfile;
import yevoli.release.yev.foodbarbaz.followingList;
import POJO.User;
import yevoli.release.yev.foodbarbaz.R;


public class FollowingAdapter extends ArrayAdapter<User> {

    User requester;
    User friend;
    Long friendId;
    followingList followingList;
    String friendName;
    FollowingAdapter followingAdapter;

    public FollowingAdapter(Context context, List<User> objects, User user, followingList followingList ) {
        super(context, R.layout.following_list_row, objects);
        this.requester = user;
        this.followingList = followingList;
        followingAdapter=this;
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View followingView = inflater.inflate(R.layout.following_list_row, viewGroup, false);

        final User user = getItem(i);

        TextView name = followingView.findViewById(R.id.textViewFollowingUser);
        name.setText(user.getUsername());

        ImageButton unfollowButton = followingView.findViewById(R.id.buttonUnfollow);
        unfollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendId = user.getId();
                Log.i("FRIEND ID :::", friendId.toString());
                friendName = user.getUsername();
                friend = user;


                DialogFriendOptions dialog = new DialogFriendOptions();
                dialog.setParent(followingAdapter);
                dialog.show(followingList.getParent().getFragmentManager(), "DialogFriendOptions");
            }
        });

        return followingView;
    }

    private class RemoveFriend extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                URL url;
                Log.i("URL FOR ADD FRIEND:", "https://foodbarbaz.herokuapp.com/addFriendship/" + requester.getId() + "/" + friendId);
                url = new URL("https://foodbarbaz.herokuapp.com/removeFriendship/" + requester.getId()
                        + "/" + friendId);

                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestProperty("User-Agent", "");
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.connect();
                Log.i("RESPONSE:::", urlConnection.getResponseCode()+"");

                urlConnection.disconnect();
            }
            catch(Exception e){
                Log.e("URL EXCEPTION", e.toString());
            }

            followingList.modifyList(friendName,friend);

            return "Finished doing in Background to Add Friendship";
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    public void removeFriend(){
        new RemoveFriend().execute();
    }

    public void seeFriendOnMap(){
        final Context context = getContext();
        Log.i("LOCATION : ", "BEFORE FUTURE");
        UserLocation location = WebserviceActions.getFriendLocation(friendId,null);
        Log.i("LOCATION : ", "AFTER FUTURE");
        Log.i("FRIEND LOCATION", "" + location.getLongitude() + " , DATE: " + location.getTimestamp());

        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra("mapAction", "seeFriend");
        intent.putExtra("friend", friend);
        intent.putExtra("friendLocation", location);

        getContext().startActivity(intent);
    }

    public void viewProfile(){
        Intent intent = new Intent(followingList.getContext(), FriendProfile.class);
        intent.putExtra("user", requester);
        intent.putExtra("friend", friend);
        followingList.startActivity(intent);
    }

}


