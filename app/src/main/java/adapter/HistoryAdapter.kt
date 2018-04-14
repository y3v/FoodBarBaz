package adapter

import POJO.User
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.listview_history_rows.view.*
import yevoli.release.yev.foodbarbaz.NearbyRestaurantList
import yevoli.release.yev.foodbarbaz.R


class HistoryAdapter(context: Context, objects: List<String>, user: User?) : ArrayAdapter<String>(context, R.layout.listview_history_rows, objects) {

    val history = objects
    val user = user

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {

        val inflater = LayoutInflater.from(context)
        val historyView = inflater.inflate(R.layout.listview_history_rows, viewGroup, false)

        historyView.textViewHistory.text = history[i]

        historyView.buttonSearchAgain.setOnClickListener {
            val intent = Intent(context, NearbyRestaurantList::class.java)
            intent.putExtra("user", user)
            intent.putExtra("query", history[i])
            context.startActivity(intent)
        }

        return historyView
    }



}


