package POJO

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

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
                        Log.i("Content: ", myLine)
                        strBuilder.append(myLine)

                        if (myLine == null)
                            hasMoreLines = false
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

                    lateinit var myLine: String
                    var hasMoreLines = true
                    val strBuilder = StringBuilder()

                    while (hasMoreLines) {
                        myLine = responseBuffer.readLine()
                        Log.i("Content: ", myLine)
                        strBuilder.append(myLine)

                        if (myLine == null)
                            hasMoreLines = false
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
    }
}