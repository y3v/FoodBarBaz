package yevoli.release.yev.foodbarbaz

import POJO.ActivityStarter
import POJO.Favourites
import POJO.User
import adapter.FavouritesAdapter
import android.app.DialogFragment
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import dialog.DialogEmptyList
import kotlinx.android.synthetic.main.activity_favourites.*
import kotlinx.android.synthetic.main.app_bar.*

class Favourites : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var user : User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        /*Get the user object... should not be null because to get to this page you need
        to go through the Following Activity which does a check*/
        val data = intent.extras
        if (data != null) {
            if (data.containsKey("user")) {
                user = data.getParcelable("user")
                if (user != null) {
                    Log.i("USERNAME----", user?.username)
                    Log.i("PASSWORD----", user?.password)
                    Log.i("FIRSTNAME----", user?.firstname)
                    Log.i("EMAIL----", user?.email)
                }
            }
        }

        //Setup navigation drawer and toolbar -- Do not forget to implement Navigation View Listener to class
        navigation_view.setNavigationItemSelectedListener(this)
        setSupportActionBar(appBar as Toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, appBar as Toolbar, R.string.open_drawer, R.string.closed_drawer)
        drawer_layout.addDrawerListener(toggle)

        toggle.syncState()

        val list = Favourites.getList(this, user?.id)
        val adapter = FavouritesAdapter(this, list, user)
        favourites_list_view.adapter = adapter

        if (Favourites.getList(this, user?.id).isEmpty()){
            val dialog = DialogEmptyList()
            dialog.show(fragmentManager, "Empty List")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        ActivityStarter.NavigationItemSelected(this, user, item)

        return false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu_not_home, menu)

        if (user != null) {
            menu.add(0, Menu.FIRST, Menu.FIRST + 2, "Logout").setShowAsAction(Menu.NONE)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        ActivityStarter.OptionsItemsSelected(this, user, item, drawer_layout)

        return super.onOptionsItemSelected(item)
    }

}
