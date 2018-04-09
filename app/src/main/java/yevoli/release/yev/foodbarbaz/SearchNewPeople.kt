package yevoli.release.yev.foodbarbaz

import POJO.User
import adapter.FollowingAdapter
import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_search_new_people.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList


class SearchNewPeople : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private var user : User? = null

    private lateinit var newFollowersList : ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    //To get the user object from the Main Activity
    fun setUser(user : User){
        this.user = user
    }

    fun getUser() : User?{
        return this.user
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if (user != null){
            val execute = GetNew().execute()
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_new_people, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                SearchNewPeople().apply {

                }
    }

    internal inner class GetNew : AsyncTask<Void, Int, String>() {

        internal var response = ""

        override fun onPreExecute() {
            super.onPreExecute()
            println("PRE-EXECUTE")
        }

        override fun doInBackground(vararg arg0: Void): String {
            println("DO IN BACKGROUND")

            try {
                val url = URL("https://foodbarbaz.herokuapp.com/getNotFollowing/${user?.id}")
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
            newFollowersList = gson.fromJson<ArrayList<User>>(response, type)


            println("IN THE THREAD")

            activity.runOnUiThread {
                createUserListAdapter()
            }


            return "You are at PostExecute"
        }

    }

    fun createUserListAdapter() {
        println("SETTING ADAPTER")
        //Set adapter for followers
        val followersAdapter = FollowingAdapter(context, newFollowersList)
        this.listViewFindFoodies.adapter = followersAdapter
        //this.progressBarFollowing.visibility = View.GONE
    }
}
