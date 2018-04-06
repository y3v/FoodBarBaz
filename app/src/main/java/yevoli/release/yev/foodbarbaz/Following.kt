package yevoli.release.yev.foodbarbaz


import POJO.User
import adapter.FollowingAdapter
import adapter.RestaurantListAdapter
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

    private var followersList : ArrayList<User>? = null

    //Instantiate the Fragments
    var followingList : followingList = yevoli.release.yev.foodbarbaz.followingList.newInstance()
    var searchNewPeople : SearchNewPeople = SearchNewPeople.newInstance()


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

        //Fragment Manager
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .add(R.id.fragmentContainer, searchNewPeople)
                .show(followingList)
                .hide(searchNewPeople)
                .commit()


        //Get list (Following)
        getFollowUsers()

        buttonSeeSearchList.setOnClickListener {

            //Change the Stroke at bottom of the buttons
            println("SECOND FRAGMENT BUTTON CLICKED")
            buttonSeeSearchList.background = getDrawable(R.drawable.frag_buttons)
            buttonSeeFollowing.background = getDrawable(R.color.midTeal)

            supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .show(searchNewPeople)
                    .commit()
        }

        buttonSeeFollowing.setOnClickListener {
            //Change the Stroke at bottom of the buttons
            println("First Fragment Button clicked!")
            buttonSeeFollowing.background = getDrawable(R.drawable.frag_buttons)
            buttonSeeSearchList.background = getDrawable(R.color.midTeal)

            supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .hide(searchNewPeople)
                    .commit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getFollowUsers() {
        val thread = Thread(object : Runnable {
            internal var response = ""

            override fun run() {
                try {
                    val url = URL("https://foodbarbaz.herokuapp.com/getUsers")
                    val conn = url.openConnection() as HttpURLConnection
                    conn.requestMethod = "POST"
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8")
                    conn.setRequestProperty("Accept", "application/json")
                    conn.doOutput = true
                    conn.doInput = true


                    val inputStream = conn.inputStream
                    val responseBuffer = BufferedReader(InputStreamReader(inputStream))

                    var myLine: String? = null
                    val strBuilder = StringBuilder()


                    while ({ myLine = responseBuffer.readLine(); myLine }() != null) {
                        System.out.println(myLine)
                        strBuilder.append(myLine)
                    }

                    Log.i("CONTENT", response)
                    response = strBuilder.toString()

                    Log.i("STATUS", conn.responseCode.toString())
                    Log.i("MSG", conn.responseMessage)

                    conn.disconnect()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val type = object : TypeToken<ArrayList<User>>() {}.type
                val gson = Gson()
                followersList = gson.fromJson<ArrayList<User>>(response, type)

                runOnUiThread {
                    createUserListAdapter()
                }
            }
        })

        thread.start()
    }

    fun createUserListAdapter(){

        //Set adapter for followers
        val followersAdapter = FollowingAdapter(this, followersList)
        listViewFindFollowers.adapter = followersAdapter

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
}
