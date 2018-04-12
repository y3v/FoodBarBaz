package yevoli.release.yev.foodbarbaz

import POJO.Save
import POJO.User
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.NavigationView
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import dialog.DialogProfilePictureOptions
import android.graphics.PorterDuffXfermode
import android.graphics.Bitmap
import android.os.AsyncTask
import android.support.v4.app.ActivityCompat
import android.util.Base64
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_friend_profile.*
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList


class FriendProfile : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //The value of this user determines what will happen when you press on the account button
    var user : User? = null
    var friend : User? = null
    val cont = this
    var photo : String? = null
    var friendId : Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_profile)

        checkIOPermissions()

        //Setup navigation drawer and toolbar -- Do not forget to implement Navigation View Listener to class
        navigation_view.setNavigationItemSelectedListener(this)
        setSupportActionBar(include as Toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, include as Toolbar, R.string.open_drawer, R.string.closed_drawer)
        drawer_layout.addDrawerListener(toggle)

        toggle.syncState()

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
            if(data.containsKey("friend")){
                friend = data.getParcelable("friend")
                if (friend != null) {
                    Log.i("USERNAME----", friend?.username)
                    Log.i("PASSWORD----", friend?.password)
                    Log.i("FIRSTNAME----", friend?.firstname)
                    Log.i("EMAIL----", friend?.email)
                }
            }
        }

        //Start thread that fetches the user profile picture
        val decodeImage = DecodeImage(cont, friend)
        photo = decodeImage.execute().get()
        println("RESPONSE:::: $photo")
        if (photo != null){
            val decodedString = Base64.decode(photo, Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            imageViewProfileFriendPicture.setImageBitmap(getCroppedBitmap(decodedByte))
        }

        //Start Thread that fetches the rest of the user information
        /*val getFriendInfo = GetFriendInfo(friendId)
        friend = getFriendInfo.execute().get()*/

        //Customize the UI Elements based on User information
        textViewProfileFriendUsername.text = friend!!.username
        textViewProfileFriendName.text = "${friend!!.firstname} ${friend!!.lastname}"
        textViewProfileFriendEmail.text = friend!!.email

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

    private fun getCroppedBitmap(bitmap: Bitmap): Bitmap {
        val output: Bitmap

        if (bitmap.width > bitmap.height) {
            output = Bitmap.createBitmap(bitmap.height, bitmap.height, Bitmap.Config.ARGB_8888)
        } else {
            output = Bitmap.createBitmap(bitmap.width, bitmap.width, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(output)

        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)

        var r = 0f

        if (bitmap.width > bitmap.height) {
            r = (bitmap.height / 2).toFloat()
        } else {
            r = (bitmap.width / 2).toFloat()
        }

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawCircle(r, r, r, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

    private fun checkIOPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            Log.i("lat,lon", "NO PERMISSION")
            return
        }

    }

    private class DecodeImage(context: Context, user : User?) : AsyncTask<Void?, Void, String?>() {

        private var context = context
        private var user = user

        override fun doInBackground(vararg bitmap : Void?): String? {

            var temp : String? = null

            try {
                val url: URL
                url = URL("https://foodbarbaz.herokuapp.com/getPhoto/${user?.id}")

                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.setRequestProperty("User-Agent", "")
                urlConnection.doInput = true
                urlConnection.doOutput = true

                val inputStream = urlConnection.inputStream
                val responseBuffer = BufferedReader(InputStreamReader(inputStream))
                temp = responseBuffer.readLine()

                Log.i("RESPONSE:::", urlConnection.responseCode.toString() + "")

                urlConnection.disconnect()
            } catch (e: Exception) {
                Log.e("URL EXCEPTION", e.toString())
            }

            return temp
        }
    }

   /* private class GetFriendInfo(friendId : Long?) : AsyncTask <Void?, Void, User?>(){

        val friendId : Long? = friendId
        internal var response : String = ""
        var temp : User? = null

        override fun doInBackground(vararg bitmap : Void?): User? {

            try {
                val url: URL
                url = URL("http://192.168.1.5:8080/getUser/$friendId")

                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.setRequestProperty("User-Agent", "")
                urlConnection.doInput = true
                urlConnection.doOutput = true

                val inputStream = urlConnection.inputStream
                val responseBuffer = BufferedReader(InputStreamReader(inputStream))

                var myLine: String? = null
                val strBuilder = StringBuilder()


                while ({ myLine = responseBuffer.readLine(); myLine }() != null) {
                    System.out.println(myLine)
                    strBuilder.append(myLine)
                }

                Log.i("RESPONSE:::", urlConnection.responseCode.toString() + "")

                urlConnection.disconnect()
            } catch (e: Exception) {
                Log.e("URL EXCEPTION", e.toString())
            }

            val type = object : TypeToken<User>() {}.type
            val gson = Gson()
            temp = gson.fromJson<User>(response, type)

            return temp
        }

    }*/
}


