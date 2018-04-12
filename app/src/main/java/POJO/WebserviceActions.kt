package POJO

import android.content.Context
import android.renderscript.Type
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.DateFormat
import java.util.*
import com.google.gson.JsonParseException
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonDeserializer
import yevoli.release.yev.foodbarbaz.R.string.cancel
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.RunnableFuture


open class WebserviceActions {

    companion object {

        @JvmStatic fun addUserLocation(lat :String, lon :String, user :User, progressBar: ProgressBar?) : Boolean{
            val thread = Thread(Runnable {
                progressBar?.visibility = View.VISIBLE

                var url: URL? = null
                try {
                    // url = URL(Const.OLI_LOCAL_URL + "/addUserLocation")
                    url = URL(Const.HEROKU_URL + "/addUserLocation")
                    val conn = url.openConnection() as HttpURLConnection
                    conn.requestMethod = "POST"
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8")
                    conn.setRequestProperty("Accept", "application/json")
                    conn.doOutput = true
                    conn.doInput = true

                    // Needed so the user is mapped correctly by Hibernate server side
                    val jsonUser = JSONObject()
                    jsonUser.put("id", user.id)
                    jsonUser.put("username", user.username)
                    jsonUser.put("password", user.password)
                    jsonUser.put("firstname", user.firstname)
                    jsonUser.put("lastname", user.lastname)
                    jsonUser.put("email", user.email)

                    val jsonParam = JSONObject()
                    jsonParam.put("latitude", lat)
                    jsonParam.put("longitude", lon)
                    jsonParam.put("user", jsonUser)


                    Log.i("JSON", jsonParam.toString())
                    val os = DataOutputStream(conn.outputStream)
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"))
                    os.writeBytes(jsonParam.toString())

                    os.flush()

                    val inputStream = conn.inputStream
                    val responseBuffer = BufferedReader(InputStreamReader(inputStream))

                    lateinit var myLine: String
                    var hasMoreLines = true
                    val strBuilder = StringBuilder()

                    while (hasMoreLines) {
                        myLine = responseBuffer.readLine()
                        if (myLine == null)
                            hasMoreLines = false
                        else{
                            strBuilder.append(myLine)
                            Log.i("Content: ", myLine)
                        }
                    }

                    Log.i("CONTENT", strBuilder.toString())

                    Log.i("STATUS", conn.responseCode.toString())
                    Log.i("MSG", conn.responseMessage)

                    conn.disconnect()

                    progressBar?.visibility = View.GONE
                } catch (e: Exception) {
                    e.printStackTrace()
                    progressBar?.visibility = View.GONE
                }
            })

            thread.start()
            return true
        }

        @JvmStatic fun getFriendsLocation(userId: Long, progressBar: ProgressBar?) :List<UserLocation>{
            lateinit var friendsLocation :List<UserLocation>

            val thread = Thread(Runnable {
                progressBar?.visibility = View.VISIBLE

                var url: URL? = null
                try {
                    // url = URL(Const.OLI_LOCAL_URL + "/getFriendsLocation/" + userId)
                    url = URL(Const.HEROKU_URL + "/getFriendsLocation/" + userId)
                    val conn = url.openConnection() as HttpURLConnection
                    conn.setRequestProperty("User-Agent", "")
                    conn.requestMethod = "GET"
                    conn.doInput = true
                    conn.connect()

                    val inputStream = conn.inputStream
                    val responseBuffer = BufferedReader(InputStreamReader(inputStream))

                    var myLine: String?
                    var hasMoreLines = true
                    val strBuilder = StringBuilder()

                    while (hasMoreLines) {
                        myLine = responseBuffer.readLine()
                        if (myLine == null)
                            hasMoreLines = false
                        else{
                            strBuilder.append(myLine)
                            Log.i("Content: ", myLine)
                        }
                    }

                    Log.i("JSON CONTENT", strBuilder.toString())

                    Log.i("STATUS", conn.responseCode.toString())
                    Log.i("MSG", conn.responseMessage)

                    conn.disconnect()

                    progressBar?.visibility = View.GONE
                    val listType = object : TypeToken<List<UserLocation>>() {

                    }.type
                    val gson = Gson()
                    friendsLocation =  gson.fromJson(strBuilder.toString(), listType)

                } catch (e: Exception) {
                    e.printStackTrace()
                    progressBar?.visibility = View.GONE
                }
            })

            thread.start()
            while (thread.isAlive){ print("STATUS : " + friendsLocation.size)}
            return friendsLocation
        }

        @JvmStatic fun getFriendLocation(userId: Long, progressBar: ProgressBar?) :UserLocation{
            val service: ExecutorService = Executors.newFixedThreadPool(2)
            val isDone = false

            val result = service.submit(Callable<UserLocation> {
                var friendLocation = UserLocation()
                progressBar?.visibility = View.VISIBLE

                var url: URL? = null
                try {
                    print("IN FUTURE")
                    // url = URL(Const.OLI_LOCAL_URL + "/getFriendLocation/" + userId)
                    url = URL(Const.HEROKU_URL + "/getFriendLocation/" + userId)
                    val conn = url.openConnection() as HttpURLConnection
                    conn.setRequestProperty("User-Agent", "")
                    conn.requestMethod = "GET"
                    conn.doInput = true
                    conn.connect()

                    val inputStream = conn.inputStream
                    val responseBuffer = BufferedReader(InputStreamReader(inputStream))

                    var myLine: String?
                    var hasMoreLines = true
                    val strBuilder = StringBuilder()

                    while (hasMoreLines) {
                        myLine = responseBuffer.readLine()
                        if (myLine == null)
                            hasMoreLines = false
                        else{
                            strBuilder.append(myLine)
                            Log.i("Content: ", myLine)
                        }
                    }

                    Log.i("JSON CONTENT", strBuilder.toString())

                    Log.i("STATUS", conn.responseCode.toString())
                    Log.i("MSG", conn.responseMessage)

                    conn.disconnect()

                    progressBar?.visibility = View.GONE
                    val type = object : TypeToken<UserLocation>() {

                    }.type
                    val gsonBuilder = GsonBuilder()
                    gsonBuilder.registerTypeAdapter(Date::class.java, JsonDeserializer<Date> { json, typeOfT, context -> Date(json!!.asJsonPrimitive.asLong) })

                    val gson = gsonBuilder.create()
                    friendLocation =  gson.fromJson(strBuilder.toString(), type)

                } catch (e: Exception) {
                    e.printStackTrace()
                    progressBar?.visibility = View.GONE
                }
                return@Callable friendLocation
            })

            while (!isDone) {
                if (result.isDone)
                    return result.get()
            }
            return UserLocation()
        }

        @JvmStatic fun getAddressLocation(address: String, progressBar: ProgressBar?) :LatLng{
            lateinit var latLng:LatLng
            val service: ExecutorService = Executors.newFixedThreadPool(2)
            val isDone = false

            val result = service.submit(Callable<LatLng> {
                progressBar?.visibility = View.VISIBLE

                try {

                    Log.i("QUERY:", address)
                    val query = address.replace(" ".toRegex(), "%20")
                    Log.i("QUERY:", query)
                    val url = URL(Const.HEROKU_URL + "/myGeoCode/$query")
                    val urlConnection = url.openConnection() as HttpURLConnection
                    urlConnection.setRequestProperty("User-Agent", "")
                    urlConnection.requestMethod = "GET"
                    urlConnection.doInput = true
                    urlConnection.connect()

                    val inputStream = urlConnection.inputStream
                    val responseBuffer = BufferedReader(InputStreamReader(inputStream))

                    var hasMoreLines = true
                    var myLine: String?
                    val strBuilder = StringBuilder()

                    while (hasMoreLines) {
                        myLine = responseBuffer.readLine()
                        if (myLine == null)
                            hasMoreLines = false
                        else{
                            strBuilder.append(myLine)
                            Log.i("Content: ", myLine)
                        }
                    }

                    val strings = strBuilder.toString().split("/")
                    val lat = java.lang.Double.parseDouble(strings[0])
                    val lon = java.lang.Double.parseDouble(strings[1])

                    latLng = LatLng(lat,lon)
                    Log.i("CONTENT", strBuilder.toString())

                } catch (e: Exception) {
                    Log.e("URL EXCEPTION", e.toString())
                }

                return@Callable latLng
            })

            while (!isDone) {
                if (result.isDone)
                    return result.get()
            }
            return LatLng(45.00,-75.00)
        }
    }
}