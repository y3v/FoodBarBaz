package dialog

import adapter.FollowingAdapter
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.content.Intent
import android.hardware.Camera
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import yevoli.release.yev.foodbarbaz.R
import android.graphics.Bitmap
import android.R.attr.data
import android.support.v4.app.NotificationCompat.getExtras
import android.app.Activity.RESULT_OK
import android.R.attr.data
import android.app.Activity
import android.support.v4.app.NotificationCompat.getExtras
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_profile.*
import yevoli.release.yev.foodbarbaz.Profile


@Suppress("DEPRECATION")
class DialogProfilePictureOptions : DialogFragment() {

    var profile : Profile? = null

    fun setParent(profile : Profile?){
        this.profile = profile
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = activity.layoutInflater
        val view: View? = inflater.inflate(R.layout.dialog_camera_options, null)

        val builder = AlertDialog.Builder(activity)
                .setCustomTitle(view)
                .setItems(R.array.camera_options, DialogInterface.OnClickListener { dialog, which ->
                    when(which){
                        0->{
                            //GALLERY
                            println("USING GALLERY")
                            profile?.getPhotoFromGallery()

                        }
                        1->{
                            //CAMERA
                            println("USING CAMERA")
                            profile?.takePicture()
                        }
                    }
                })

        val dialog : AlertDialog = builder.create()
        dialog.window.attributes.windowAnimations = R.style.DialogAnimation

        return dialog
    }



}