package yevoli.release.yev.foodbarbaz

import POJO.ActivityStarter
import POJO.Save
import POJO.User
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.SpannableStringBuilder
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import dialog.DialogProfilePictureOptions
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.app_bar.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class EditProfile : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var user : User? = null
    var photo : String? = null
    var context : Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

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
            if (data.containsKey("photo")){
                photo = data.getString("photo")
            }
        }

        if (photo != null){
            val decodedString = Base64.decode(photo, Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            imageViewEditProfilePic.setImageBitmap(getCroppedBitmap(decodedByte))
        }

        //Set Default Text on the UI Elements
        editFirstname.text = SpannableStringBuilder(user?.firstname)
        editLastname.text = SpannableStringBuilder(user?.lastname)
        editEmail.text = SpannableStringBuilder(user?.email)


        //Setup navigation drawer and toolbar -- Do not forget to implement Navigation View Listener to class
        val toolBar = findViewById<Toolbar>(R.id.include)
        setSupportActionBar(toolBar)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)
        drawerLayout.bringToFront()

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open_drawer, R.string.closed_drawer)
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        imageViewEditProfilePic.setOnClickListener {
            val dialog = DialogProfilePictureOptions()
            dialog.setParent(this)
            dialog.show(fragmentManager, "Profile Options")
        }

        buttonSaveEdit.setOnClickListener {
            saveChanges()
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
                    imageViewEditProfilePic.setImageBitmap(profilePic)
                    val save = Save()
                    save.SaveImage(applicationContext, profilePic)
                    val encodeImage = EncodeImage(context, user)
                    encodeImage.execute(profilePic)

                }
            }
            1->{
                if (resultCode == Activity.RESULT_OK && data != null){
                    val imageUri = data?.data
                    val imageStream = contentResolver.openInputStream(imageUri)
                    val profilePic = BitmapFactory.decodeStream(imageStream)
                    val save = Save()
                    imageViewEditProfilePic.setImageBitmap(getCroppedBitmap(profilePic))
                    save.SaveImage(applicationContext, profilePic)
                    val encodeImage = EncodeImage(context, user)
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

    private class EncodeImage(context: Context, user : User?) : AsyncTask<Bitmap, Void, Void>() {

        private var context = context
        private var user = user

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
                url = URL("https://foodbarbaz.herokuapp.com/addProfilePic")

                val jsonParam = JSONObject()
                jsonParam.put("id", user?.id)
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

    fun getPhotoFromGallery(){
        val photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, 1)
    }

    fun saveChanges(){
        val thread = Thread(object : Runnable {
            internal var response: Long? = null

            override fun run() {
                try {
                    val url = URL("https://foodbarbaz.herokuapp.com/edit")
                    val conn = url.openConnection() as HttpURLConnection
                    conn.requestMethod = "POST"
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8")
                    conn.setRequestProperty("Accept", "application/json")
                    conn.doOutput = true
                    conn.doInput = true

                    val jsonParam = JSONObject()
                    jsonParam.put("id", user?.id)
                    jsonParam.put("firstname", editFirstname.getText().toString())
                    jsonParam.put("lastname", editLastname.getText().toString())
                    jsonParam.put("email", editEmail.getText().toString())


                    Log.i("JSON", jsonParam.toString())
                    val os = DataOutputStream(conn.outputStream)
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString())

                    os.flush()

                    Log.i("STATUS", conn.responseCode.toString())
                    Log.i("MSG", conn.responseMessage)

                    if (conn.responseCode == 200) {

                        user?.firstname = editFirstname.text.toString()
                        user?.lastname = editLastname.text.toString()
                        user?.email = editEmail.text.toString()

                        val intent = Intent(applicationContext, Profile::class.java)
                        intent.putExtra("user", user)
                        startActivity(intent)
                    } else {
                        runOnUiThread {
                            editFirstname.requestFocus()
                            editFirstname.setError(getString(R.string.connection_error))
                        }
                    }

                    conn.disconnect()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })

        thread.start()
    }
}


