package yevoli.release.yev.foodbarbaz

import POJO.Const
import POJO.User
import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.Button
import io.kickflip.sdk.Kickflip;
import io.kickflip.sdk.activity.BroadcastActivity
import io.kickflip.sdk.api.KickflipApiClient
import io.kickflip.sdk.api.KickflipCallback
import io.kickflip.sdk.api.json.Response
import io.kickflip.sdk.api.json.Stream
import io.kickflip.sdk.av.BroadcastListener;
import io.kickflip.sdk.exception.KickflipException
import kotlinx.android.synthetic.main.activity_broadcast.*
import kotlinx.android.synthetic.main.activity_following.*
import io.kickflip.sdk.av.AVRecorder
import io.kickflip.sdk.fragment.BroadcastFragment


class BroadcastActivity : AppCompatActivity(){
    private var mRecorder: AVRecorder? = null
    private val mRecordingButton: Button? = null
    private val mFirstRecording = true
    private var mKickflipReady = false
    private var user : User? = null
    private var streamStarted = false
    private var broadcastListener = setBroadcastListener()
    private var broadcastActivity = BroadcastActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_broadcast)

        checkMediaPermissions()

        Kickflip.setup(this, Const.CLIENT_ID, Const.CLIENT_SECRET, object:KickflipCallback {
            override fun onSuccess(response: Response?) {
                mKickflipReady = true
                println("KICKFLIP RESPONSE : " + response.toString())
            }

            override fun onError(error: KickflipException?) {
                println("KICKFLIP ERROR : " + error?.message)
            }
        })
        setBroadcastListener()

    }

    fun toggleStream(view :View){
        if (view == broadcast_start_stop){
            if (mKickflipReady)
                startBroadcastingActivity()
        }
    }

    fun startBroadcastingActivity(){
        Kickflip.setSessionConfig(Const.create720pSessionConfig(Const.createNewRecordingFile()))
        Kickflip.startBroadcastActivity(this@BroadcastActivity, broadcastListener)
        BroadcastFragment.getInstance()
    }

    fun setBroadcastListener() :BroadcastListener {
        return object:BroadcastListener{
            override fun onBroadcastStart() {
                println("BROADCAST STARTED")
            }

            override fun onBroadcastStop() {
                println("BROADCAST STOPPED")
            }

            override fun onBroadcastError(error: KickflipException?) {
                println("KICKFLIP ERROR : " + error?.message)
            }

            override fun onBroadcastLive(stream: Stream?) {
                println("KICKFLIP URL" + stream?.kickflipUrl)
                println("STREAM URL : " + stream?.streamUrl)
            }

        }
    }

    fun checkMediaPermissions() {
        println("HEY IM IN PERMISSIONS!?")
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                                                || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                                                || ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.;
            //
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            Log.i("lat,lon", "NO PERMISSION")
            return
        }

    }
}
