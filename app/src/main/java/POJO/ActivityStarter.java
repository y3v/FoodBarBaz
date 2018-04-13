package POJO;

import android.app.Activity;
import android.content.Intent;

import yevoli.release.yev.foodbarbaz.Following;

public class ActivityStarter {

    public static void startSocialActivity(Activity activity, User user){
        Intent intent = new Intent(activity, Following.class);
        intent.putExtra("user", user);
        activity.startActivity(intent);
    }

}
