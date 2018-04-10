package yevoli.release.yev.foodbarbaz


import POJO.User
import adapter.FollowingAdapter
import adapter.RestaurantListAdapter
import adapter.SearchNewAdapter
import android.app.FragmentManager
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_following.*
import kotlinx.android.synthetic.main.fragment_following_list.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList

class Following : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    //The value of this user determines what will happen when you press on the account button
    var user : User? = null


    //Instantiate the Fragments
    var followingList : followingList = yevoli.release.yev.foodbarbaz.followingList.newInstance()
    var searchNewPeople : SearchNewPeople = SearchNewPeople.newInstance()

    var followingAdapter : FollowingAdapter? = null
    var followersList : ArrayList<User>? = null

    var newFollowList : ArrayList<User>? = null
    var newfollowingAdapter : SearchNewAdapter? = null


    var login : PleaseLogIn? = null // do not need to instantiate this yet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_following)

        //If user is logged in, or just registered, the User object will be created
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

        //Setup navigation drawer and toolbar
        navigation_view.setNavigationItemSelectedListener(this)
        setSupportActionBar(include as Toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, include as Toolbar, R.string.open_drawer, R.string.closed_drawer)
        drawer_layout.addDrawerListener(toggle)

        toggle.syncState()

        //Pass the user object to the fragments
        if (user != null){
            followingList.setUser(user!!)
            searchNewPeople.setUser(user!!)

            //All fragment to communicate with this (parent) activity
            searchNewPeople.setParent(this)
            followingList.setParent(this)

            //allow access to adapter to modify list in FollowingList
            followingAdapter = followingList.getAdapter()
            followersList = followingList.getList()

            //allow access to adapter to modify list in SearchNewPeople
            newfollowingAdapter = searchNewPeople.searchNewAdapter
            newFollowList = searchNewPeople.newFollowersList
            println("Following List User: ${followingList.getUser()?.username}")
        }

        //Fragment Manager
        if (user != null ){
            println("FRAGMENT MANAGER ADDING")
            supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .add(R.id.fragmentContainer, followingList)
                    .show(followingList)
                    .hide(searchNewPeople)
                    .commit()

            supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .add(R.id.fragmentContainer, searchNewPeople)
                    .show(followingList)
                    .hide(searchNewPeople)
                    .commit()
        } else{   //create only one fragment that asks the user to go log in
            login = PleaseLogIn.newInstance()

            supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .add(R.id.fragmentContainer, login)
                    .show(login)
                    .commit()
        }

        buttonSeeSearchList.setOnClickListener {

            //Change the Stroke at bottom of the buttons
            println("SECOND FRAGMENT BUTTON CLICKED")
            buttonSeeSearchList.background = getDrawable(R.drawable.frag_buttons)
            buttonSeeFollowing.background = getDrawable(R.color.midTeal)

            if (user != null){
                supportFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .show(searchNewPeople)
                        .commit()
            }else{
                loginFragment(login)
            }
        }

        buttonSeeFollowing.setOnClickListener {
            //Change the Stroke at bottom of the buttons
            println("First Fragment Button clicked!")
            buttonSeeFollowing.background = getDrawable(R.drawable.frag_buttons)
            buttonSeeSearchList.background = getDrawable(R.color.midTeal)

            if (user != null){
                supportFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .hide(searchNewPeople)
                        .commit()
            }else{
                loginFragment(login)
            }
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu_not_home, menu)

        if (user != null) {
            menu.add(0, Menu.FIRST, Menu.FIRST + 2, "Logout").setShowAsAction(Menu.NONE)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.action_settings -> Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show()
            R.id.action_favs -> Toast.makeText(this, "Favourites Clicked", Toast.LENGTH_SHORT).show()
            R.id.account -> accountPressed()
            R.id.search -> {
                val intent = Intent(applicationContext, HomePage::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun accountPressed() {
        if (user == null) {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, UserDetails::class.java)
            intent.putExtra("user", user)
            //Toast.makeText(this,"TO DO: DISPLAY ACCOUNTS PAGE", Toast.LENGTH_SHORT).show();
            startActivityForResult(intent, 1)
        }
    }

    override fun onRestart() {
        super.onRestart()

        println("RESTARTING ACTIVITY")

    }

    private fun loginFragment(login : PleaseLogIn?){
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .show(login)
                .commit()
    }

    fun updateList(user : User){
        followingList.updateList(user)
    }

    fun updateRemoveList(user : User){
        searchNewPeople.updateList(user)
    }

    fun setFollowAddedToast(friendName: String){
        Toast.makeText(this, "Followed $friendName", Toast.LENGTH_SHORT)
    }

    fun setFollowRemovedToast(friendName: String){
        Toast.makeText(this, "Unfollowed $friendName", Toast.LENGTH_SHORT)
    }
}
