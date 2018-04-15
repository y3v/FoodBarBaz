package adapter

import POJO.Favourites
import POJO.User
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.listview_favourites.view.*
import kotlinx.android.synthetic.main.listview_history_rows.view.*
import yevoli.release.yev.foodbarbaz.NearbyRestaurantList
import yevoli.release.yev.foodbarbaz.R


class FavouritesAdapter(context: Context, objects: List<String>, user: User?) : ArrayAdapter<String>(context, R.layout.listview_favourites, objects) {

    val favs = objects
    val user = user

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {

        val inflater = LayoutInflater.from(context)
        val favouritesView = inflater.inflate(R.layout.listview_favourites, viewGroup, false)

        val name = favs[i]

        favouritesView.textViewFavourites.text = name

        favouritesView.buttonMenuAgain.setOnClickListener {
            Toast.makeText(context, "See Menu", Toast.LENGTH_SHORT).show()
        }

        favouritesView.buttonDeleteFavourite.setOnClickListener {
            Favourites.removeFromList(name, user?.id)
            Favourites.getList(context,user?.id).remove(name)
            Toast.makeText(context, "$name removed from Favourites!", Toast.LENGTH_SHORT).show()
            this.notifyDataSetChanged()
        }

        return favouritesView
    }



}


