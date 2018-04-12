package dialog

import adapter.FollowingAdapter
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.R.array
import yevoli.release.yev.foodbarbaz.R
import android.view.LayoutInflater
import android.view.View


class DialogFriendOptions : DialogFragment() {

    var followingAdapter : FollowingAdapter? = null

    fun setParent(followingAdapter: FollowingAdapter?){
        this.followingAdapter = followingAdapter
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity.layoutInflater
        val view : View? = inflater.inflate(R.layout.dialog_friend_options, null)

        val builder = AlertDialog.Builder(activity)
                .setCustomTitle(view)
                .setItems(R.array.friend_options, DialogInterface.OnClickListener { dialog, which ->
                    when(which){
                        0 ->{
                            println("ZERO INDEX")
                            followingAdapter!!.viewProfile()
                        }
                        1 ->{
                            followingAdapter!!.seeFriendOnMap()
                        }
                        2->{
                            followingAdapter!!.removeFriend()
                        }
                    }
                })

        val dialog : AlertDialog = builder.create()
        dialog.window.attributes.windowAnimations = R.style.DialogAnimation

        return dialog
    }
}
