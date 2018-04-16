package POJO;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import yevoli.release.yev.foodbarbaz.EditProfile;
import yevoli.release.yev.foodbarbaz.Favourites;
import yevoli.release.yev.foodbarbaz.Following;
import yevoli.release.yev.foodbarbaz.FriendProfile;
import yevoli.release.yev.foodbarbaz.HomePage;
import yevoli.release.yev.foodbarbaz.Login;
import yevoli.release.yev.foodbarbaz.Profile;
import yevoli.release.yev.foodbarbaz.R;
import yevoli.release.yev.foodbarbaz.ReportBug;
import yevoli.release.yev.foodbarbaz.SearchHistory;
import yevoli.release.yev.foodbarbaz.Settings;

public class ActivityStarter {

    private static int counter = 0;

    public static void startSocialActivity(Activity activity, User user){
        Intent intent = new Intent(activity, Following.class);
        intent.putExtra("user", user);
        activity.startActivity(intent);
    }

    public static void startHistoryActivity(Activity activity, User user){
        Intent intent = new Intent(activity, SearchHistory.class);
        intent.putExtra("user", user);
        activity.startActivity(intent);
    }

    public static void startFavouritesActivity(Activity activity, User user){
        Intent intent = new Intent(activity, Favourites.class);
        intent.putExtra("user", user);
        activity.startActivity(intent);
    }

    public static void startHomepage(Activity activity, User user){
        Intent intent = new Intent(activity, HomePage.class);
        intent.putExtra("user", user);
        activity.startActivity(intent);
    }

    public static void startLoginActivity(Activity activity){
        Intent intent = new Intent(activity, Login.class);
        activity.startActivity(intent);
    }

    public static void startSettingsActivity(Activity activity, User user){
        Intent intent = new Intent(activity, Settings.class);
        intent.putExtra("user", user);
        activity.startActivity(intent);
    }

    public static void startBugReportActivity(Activity activity, User user){
        Intent intent = new Intent(activity, ReportBug.class);
        intent.putExtra("user", user);
        activity.startActivity(intent);
    }

    public static void NavigationItemSelected(Activity activity, User user, @NonNull MenuItem item){
        int id = item.getItemId();

        Log.i("" + id, "NAVIGATION OPTION PRESSED");
        switch (id){
            case R.id.about_us_drawer:
                break;

            case R.id.favourites_drawer:
                if (user != null){
                    startFavouritesActivity(activity, user);
                }
                else if (!(activity instanceof  Login)){
                    Toast.makeText(activity, "Please Log in!", Toast.LENGTH_SHORT).show();
                    startLoginActivity(activity);
                }
                else{
                    Toast.makeText(activity, "Please Log in!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.app_settings_drawer:
                startSettingsActivity(activity, user);
                break;

            case R.id.history_drawer:
                if (user != null) {
                    startHistoryActivity(activity, user);
                }
                else if (!(activity instanceof  Login)){
                    Toast.makeText(activity, "Please Log in!", Toast.LENGTH_SHORT).show();
                    startLoginActivity(activity);
                }
                else{
                    Toast.makeText(activity, "Please Log in!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.following_drawer:
                Intent intent2 = new Intent(activity, Following.class);
                intent2.putExtra("user", user);
                activity.startActivity(intent2);
                break;

            case R.id.report_drawer:
                Intent bugIntent = new Intent(activity, ReportBug.class);
                bugIntent.putExtra("user", user);
                activity.startActivity(bugIntent);
                break;
        }
    }

    public static void OptionsItemsSelected(Activity activity, User user, @NonNull MenuItem item, DrawerLayout drawer){
        int id = item.getItemId();

        switch(id){
            case android.R.id.home:
                if (counter % 2 == 0) {
                    drawer.openDrawer(Gravity.START);
                }
                else{
                    drawer.closeDrawer(Gravity.START);
                }
                counter++;
                break;
            case R.id.action_settings:
                Toast.makeText(activity,"Settings Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_favs:
                Toast.makeText(activity,"Favourites Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.account:
                accountPressed(activity, user);
                break;
            case R.id.social:
                ActivityStarter.startSocialActivity(activity, user);
                break;
            case R.id.search:
                ActivityStarter.startHomepage(activity, user);
                break;
            case Menu.FIRST:
                POJO.SearchHistory.getList(activity, user.getId()).clear();
                POJO.Favourites.getList(activity, user.getId()).clear();
                AutoLogin.removeUser(user.getId());
                Toast.makeText(activity, R.string.user_logged_out, Toast.LENGTH_SHORT).show();
                user = null;
                activity.getIntent().removeExtra("user");
                if (activity instanceof Following ||
                        activity instanceof Favourites ||
                        activity instanceof SearchHistory ||
                        activity instanceof FriendProfile ||
                        activity instanceof EditProfile ||
                        activity instanceof Profile ||
                        activity instanceof Settings){
                    startHomepage(activity, null);
                }
                else {
                    //Refresh
                    Intent intent = activity.getIntent();
                    activity.overridePendingTransition(0, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    activity.finish();
                    activity.overridePendingTransition(0, 0);
                    activity.startActivity(intent);
                }
                break;
        }
    }

    private static void accountPressed(Activity activity, User user){
        if (user == null){
            Intent intent = new Intent(activity, Login.class);
            activity.startActivity(intent);
        }
        else{
            Intent intent = new Intent(activity, Profile.class);
            intent.putExtra("user", user);
            //Toast.makeText(this,"TO DO: DISPLAY ACCOUNTS PAGE", Toast.LENGTH_SHORT).show();
            activity.startActivity(intent);
        }
    }

}
