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
import kotlinx.android.synthetic.main.activity_profile.*
import android.graphics.PorterDuffXfermode
import android.graphics.Bitmap
import android.os.AsyncTask
import android.support.v4.app.ActivityCompat
import android.util.Base64
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


class Profile : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //The value of this user determines what will happen when you press on the account button
    var user : User? = null
    val cont = this
    var photo : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        checkIOPermissions()

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

        //Start thread that fetches the user profile picture
        val decodeImage = DecodeImage(cont)
        photo = decodeImage.execute().get()
        println("RESPONSE:::: $photo")
        val decodedString = Base64.decode(photo, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        imageButtonProfilePicture.setImageBitmap(getCroppedBitmap(decodedByte))

        //Setup navigation drawer and toolbar -- Do not forget to implement Navigation View Listener to class
        navigation_view.setNavigationItemSelectedListener(this)
        setSupportActionBar(include as Toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, include as Toolbar, R.string.open_drawer, R.string.closed_drawer)
        drawer_layout.addDrawerListener(toggle)

        toggle.syncState()

        //Customize the UI Elements based on User information
        imageButtonProfilePicture.setOnClickListener{
            val dialog = DialogProfilePictureOptions()
            dialog.setParent(this)
            dialog.show(fragmentManager, "Profile Options")
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

    fun takePicture(){
        println("USING CAMERA")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null){
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        println("ON ACTIVITY RESULT")

        when(requestCode){
            0-> {
                if (resultCode == Activity.RESULT_OK && data != null){
                    val profilePic = getCroppedBitmap(data.extras.get("data") as Bitmap)
                    imageButtonProfilePicture.setImageBitmap(profilePic)
                    val save = Save()
                    save.SaveImage(applicationContext, profilePic)
                    //TODO:Store picture in the DB
                    val encodeImage = EncodeImage(cont)
                    encodeImage.execute(profilePic)

                }
            }
            else -> {
                Toast.makeText(this, "Unrecognized Request Code", Toast.LENGTH_SHORT)
                println("UNCRECOGNIZED REQUEST CODE")
            }
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

    private class EncodeImage(context: Context) : AsyncTask<Bitmap, Void, Void>() {

        private var context = context
        //private var user = user

        override fun doInBackground(vararg bitmap : Bitmap): Void? {

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap[0].compress(Bitmap.CompressFormat.JPEG, 85, byteArrayOutputStream)

            //BLOB
            var array = byteArrayOutputStream.toByteArray()
            val encoded_string = Base64.encodeToString(array, Base64.NO_WRAP)

            println("CATCHMEHERE $encoded_string")

            try {
                val url: URL
                //Log.i("URL FOR ADD FRIEND:", "https://foodbarbaz.herokuapp.com/addFriendship/" + requester.getId() + "/" + friendId)
                url = URL("http://192.168.1.5:8080/addProfilePic")

                val jsonParam = JSONObject()
                jsonParam.put("id", 21)
                println("LOOKHERE")
                jsonParam.put("photo", encoded_string)
                println(jsonParam.toString())

                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"
                urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8")
                urlConnection.setRequestProperty("Accept", "application/json")
                urlConnection.doInput = true
                urlConnection.doOutput = true

                val os = DataOutputStream(urlConnection.outputStream)
                os.writeBytes(jsonParam.toString())

                os.flush()
                Log.i("RESPONSE:::", urlConnection.responseCode.toString() + "")

                urlConnection.disconnect()
            } catch (e: Exception) {
                Log.e("URL EXCEPTION", e.toString())
            }

            return null
        }
    }

    private class DecodeImage(context: Context) : AsyncTask<Void?, Void, String?>() {

        private var context = context
        //private var user = user

        override fun doInBackground(vararg bitmap : Void?): String? {

            var temp : String? = null

            try {
                val url: URL
                url = URL("http://192.168.1.5:8080/getPhoto/21")

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
}


