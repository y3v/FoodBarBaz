package adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import yevoli.release.yev.foodbarbaz.R;

import java.util.HashMap;
import java.util.List;

import POJO.MenuItem;

/**
 * Created by olile on 2018-03-17.
 */

public class RestaurantMenuExListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private HashMap<String, List<MenuItem>> menu;

    public RestaurantMenuExListAdapter(Context c, HashMap<String, List<MenuItem>> menu){
        this.context = c;
        this.menu = menu;
    }

    @Override
    public int getGroupCount() {
        return menu.keySet().size();
    }

    @Override
    public int getChildrenCount(int i) {
        return menu.get(menu.keySet().toArray()[i]).size();
    }

    @Override
    public Object getGroup(int i) {
        return menu.get(menu.keySet().toArray()[i]);
    }

    @Override
    public Object getChild(int i, int i1) {
        return menu.get(menu.keySet().toArray()[i]).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPos, boolean b, View view, ViewGroup parent) {
        TextView textView = new TextView(parent.getContext());
        textView.setText((String)menu.keySet().toArray()[groupPos]);
        textView.setTextSize(24);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setPadding(0,10,0,10);
        textView.setBackgroundColor(parent.getResources().getColor(R.color.teal));
        textView.setTextColor(parent.getResources().getColor(R.color.white));
        textView.setTypeface(null, Typeface.BOLD);
        return textView;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup parent) {
        // get content
        String itemName = menu.get(menu.keySet().toArray()[i]).get(i1).getItemName();
        String price = "" + menu.get(menu.keySet().toArray()[i]).get(i1).getPrice();
        String desc = menu.get(menu.keySet().toArray()[i]).get(i1).getDescription();

        // declare textViews
        TextView nameView = new TextView(parent.getContext());
        TextView priceView = new TextView(parent.getContext());
        TextView descView = new TextView(parent.getContext());

        // set text in views
        nameView.setText(itemName);
        priceView.setText(price);
        descView.setText(desc);

        // set margin in dips constants;
        final int margin10Dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, parent.getContext().getResources().getDisplayMetrics());
        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 3);
        LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams vertParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams horizParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // set text styles
        nameParams.setMargins(margin10Dp,0,0,0);
        nameView.setLayoutParams(nameParams);
        nameView.setTextSize(16);

        priceParams.setMargins(0,0,margin10Dp,0);
        priceView.setLayoutParams(priceParams);
        priceView.setTypeface(null, Typeface.BOLD);
        priceView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

        descParams.setMargins(margin10Dp,0,0,0);
        descView.setLayoutParams(descParams);
        descView.setTypeface(null, Typeface.ITALIC);

        // Set horizontal linear layout for name & price
        LinearLayout horizontalLayout = new LinearLayout(parent.getContext());
        horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
        horizParams.setMargins(0, margin10Dp, 0, 0);
        horizontalLayout.setLayoutParams(horizParams);

        // attach name and price to horizontal layout;
        horizontalLayout.addView(nameView);
        horizontalLayout.addView(priceView);

        // Set Vertical linear layout for horizontal layout and description
        LinearLayout verticalLayout = new LinearLayout(parent.getContext());
        verticalLayout.setOrientation(LinearLayout.VERTICAL);
        verticalLayout.setLayoutParams(vertParams);

        // attach horizontal layout & description to vertical layout
        verticalLayout.addView(horizontalLayout);
        verticalLayout.addView(descView);


        return verticalLayout;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
