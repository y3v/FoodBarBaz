package yevoli.release.yev.foodbarbaz

import POJO.Const
import POJO.User
import adapter.FollowingAdapter
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_following_list.*
import kotlinx.android.synthetic.main.fragment_following_list.view.*
import kotlinx.android.synthetic.main.search_new_list_row.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList


class followingList : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var user : User? = null
    private var followersAdapter : FollowingAdapter? = null
    private var followersList : ArrayList<User>? = null
    private var following : Following? = null

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

    fun getList() : ArrayList<User>?{
        if (followersList == null)
            followersList = ArrayList<User>()
        return followersList
    }

    fun getAdapter() : FollowingAdapter?{
        return followersAdapter
    }

    //Set Parent activity
    fun setParent(following : Following){
        this.following = following
    }
    fun getParent() : Following?{
        return following
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //Get list (Following)
        println("GET FOLLOW USERS")
        if (user != null){
            val execute = GetFollowers().execute()
        }
        val followingList = inflater.inflate(R.layout.fragment_following_list, container, false)
        val buttonFollowAll = followingList.buttonFollowAll
        buttonFollowAll.setOnClickListener({ Log.i("BUTTON FOLLOW ALL", "CLICKED")})
        // TODO: LINK TO MAP ACTIVITY.

        return followingList
    }


    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                followingList().apply {

                }
    }

    //THREAD WHICH LOADS THE USERS FROM THE WEBSERVICE
    //TODO : ALTER THE WEBSERVICE TO RETURN USERS THAT DO NOT INCLUDE YOURSELF
    internal inner class GetFollowers : AsyncTask<Void, Int, String>() {

        internal var response = ""

        override fun onPreExecute() {
            super.onPreExecute()
            println("PRE-EXECUTE")
        }

        override fun doInBackground(vararg arg0: Void): String {
            println("DO IN BACKGROUND")

            try {
                // val url = URL(Const.OLI_LOCAL_URL + "/getFriends/${user?.id}")
                val url = URL("https://foodbarbaz.herokuapp.com/getFriends/${user?.id}")
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


            println("IN THE THREAD")

            activity.runOnUiThread{
                createUserListAdapter()
            }


            return "You are at PostExecute"
        }

        fun onProgressUpdate(vararg a: Int) {
            super.onProgressUpdate()
        }

        override fun onPostExecute(result: String) {
            println("I'M IN POST-EXECUTE")
            super.onPostExecute(result)
        }
    }

    fun createUserListAdapter(){
        println("SETTING ADAPTER")
        //Set adapter for followers
        followersAdapter = FollowingAdapter(context, followersList, user, this)
        this.listViewFindFollowers.adapter = followersAdapter
        this.progressBarFollowing.visibility = View.GONE
        println("WHY IS THIS SHOWING!?!?!??!")
    }

    fun modifyList(friendName:String, user:User){
        println("SHOULD BE REMOVING FROM LIST")

        /*for (user in this!!.followersList!!){
            if (user.username.equals(friendName)){
                println("REMOVING")
                followersList?.remove(user)
            }
        }*/

        val it = followersList?.iterator()
        while (it!!.hasNext()) {
            var temp : User = it.next()
            if (temp.username.equals(friendName)){
                it.remove()
            }
        }

        for (user in this!!.followersList!!){
            println("USERNAME OF PPL LEFT : " + user.username)
        }

        following?.newFollowList?.add(user)

        activity.runOnUiThread {
            println("NOTIFYING CHANGE LIST")
            followersAdapter?.notifyDataSetChanged()
            if (following == null){
                println("PARENT IS NULL!")
            }
            following!!.updateRemoveList(user)
            following?.setFollowRemovedToast(friendName)
        }
    }
    fun updateList(user : User){
        followersList!!.add(user)
        followersAdapter!!.notifyDataSetChanged()
    }
}
