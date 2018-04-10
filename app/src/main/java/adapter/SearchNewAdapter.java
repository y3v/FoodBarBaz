package adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import POJO.User;
import yevoli.release.yev.foodbarbaz.R;
import yevoli.release.yev.foodbarbaz.SearchNewPeople;

public class SearchNewAdapter  extends ArrayAdapter<User> {

    User requester;
    User friend;
    Long friendId;
    SearchNewPeople searchNewPeople;
    String friendName;
    int position;



    public SearchNewAdapter(Context context, List<User> objects, User user, SearchNewPeople searchNewPeople) {
        super(context, R.layout.following_list_row, objects);
        this.requester = user;
        this.searchNewPeople = searchNewPeople;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View followingView = inflater.inflate(R.layout.search_new_list_row, viewGroup, false);

        final User user = getItem(i);

        TextView name = followingView.findViewById(R.id.textViewSearchNewUser);
        name.setText(user.getUsername());


        Button followButton = followingView.findViewById(R.id.buttonFollow);
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendId = user.getId();
                Log.i("FRIEND ID :::", friendId.toString());
                friendName = user.getUsername();
                friend = user;
                new AddFriend().execute();
            }
        });

        return followingView;
    }

    private class AddFriend extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                URL url;
                Log.i("URL FOR ADD FRIEND:", "https://foodbarbaz.herokuapp.com/addFriendship/" + requester.getId() + "/" + friendId);
                url = new URL("https://foodbarbaz.herokuapp.com/addFriendship/" + requester.getId()
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

            searchNewPeople.modifyList(friendName,friend);

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
}
