package dialog

import adapter.FollowingAdapter
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.R.array
import android.content.DialogInterface
import yevoli.release.yev.foodbarbaz.R
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.dialog_friend_options.view.*


class DialogEmptyList : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity.layoutInflater
        val view : View? = inflater.inflate(R.layout.dialog_friend_options, null)
        view?.textViewDialogTitle?.text = getString(R.string.oops)

        val builder = AlertDialog.Builder(activity)
                .setCustomTitle(view)
                .setMessage("Your list is currently empty!")
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                    activity.finish()
                    println("FINISHED!")
                })


        val dialog : AlertDialog = builder.create()
        dialog.window.attributes.windowAnimations = R.style.DialogAnimation

        return dialog
    }
}
