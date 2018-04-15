package yevoli.release.yev.foodbarbaz

import POJO.Const
import android.content.Context
import android.hardware.Camera


import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import com.red5pro.streaming.R5Connection
import com.red5pro.streaming.R5StreamProtocol
import com.red5pro.streaming.config.R5Configuration
import com.red5pro.streaming.source.R5Camera
import com.red5pro.streaming.source.R5Camera2
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCharacteristics.*
import android.hardware.camera2.params.StreamConfigurationMap
import android.util.Size
import android.content.pm.PackageManager
import android.hardware.camera2.*
import android.hardware.camera2.CameraDevice.StateCallback
import android.support.v4.app.ActivityCompat
import android.view.*
import kotlinx.android.synthetic.main.fragment_stream.*
import kotlinx.android.synthetic.main.fragment_stream.view.*
import org.w3c.dom.Text
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [StreamFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [StreamFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class StreamFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    protected lateinit var config:R5Configuration
    private lateinit var camManager:CameraManager
    private val cameraFacing = CameraMetadata.LENS_FACING_BACK
    lateinit var previewSize:Size
    lateinit var cameraId:String
    var cameraDevice: CameraDevice? = null
    lateinit var surface:TextureView
    lateinit var captureRequestBuilder:CaptureRequest.Builder
    lateinit var camera:Camera

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StreamFragment.
         */
        @JvmStatic
        fun newInstance() = StreamFragment().apply {  }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments

        config = R5Configuration(R5StreamProtocol.RTSP, Const.R5_PRO_LOCAL_SERVER,  8554, "live", 1.0f)
        config.licenseKey = Const.R5_SDK_LICENSE
        config.bundleID = activity.packageName

        println("ON CREATE FRAG")
        var connection:R5Connection = R5Connection(config)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        println("ON CREATE VIEW")
        val vue :View = inflater.inflate(R.layout.fragment_stream, container, false)
        surface = vue.streamSurfaceView
        vue.streamButton.setOnClickListener({
            preview()
        })

        camManager = container?.context?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        return vue
    }

    private fun preview() {
        setUpCamera()
        openCamera()
    }

    private fun setUpCamera() {
        try {
            for (cameraId in camManager.cameraIdList) {
                println("CAMERAID : $cameraId")
                val cameraCharacteristics = camManager.getCameraCharacteristics(cameraId)
                if (cameraCharacteristics.get(LENS_FACING) == cameraFacing) {
                    val streamConfigurationMap = cameraCharacteristics.get(
                            SCALER_STREAM_CONFIGURATION_MAP)
                    previewSize = streamConfigurationMap!!.getOutputSizes(SurfaceTexture::class.java)[0]
                    this.cameraId = cameraId
                }
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun openCamera(){
        try {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                camManager.openCamera(cameraId, getStateCallBack(), null)
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

    }

    private fun createPreviewSession() {
        val surface = Surface(this.surface.surfaceTexture)
        captureRequestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        captureRequestBuilder.addTarget(surface)
        cameraDevice!!.createCaptureSession(Collections.singletonList(surface), getCameraCaptureStateCallback(), null)
    }

    override fun onResume() {
        super.onResume()
        surface.surfaceTextureListener = getSurfaceTextureListener()
    }

    private fun getCameraCaptureStateCallback() :CameraCaptureSession.StateCallback {
        return object:CameraCaptureSession.StateCallback(){
            override fun onConfigureFailed(session: CameraCaptureSession?) {
            }

            override fun onConfigured(session: CameraCaptureSession?) {
            }

        }
    }

    private fun getSurfaceTextureListener() :TextureView.SurfaceTextureListener {
        return object:TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                return false
            }

            override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
            }

        }
    }

    private fun getStateCallBack() : StateCallback{

        return object: StateCallback() {
            override fun onOpened(camera: CameraDevice?) {
                cameraDevice = camera
            }

            override fun onDisconnected(camera: CameraDevice?) {
                camera?.close()
                cameraDevice = null
            }

            override fun onError(camera: CameraDevice?, error: Int) {
                camera?.close()
                cameraDevice = null
            }

        }
    }

    private fun getSurfaceHolderCallback() :SurfaceHolder.Callback{
        return object:SurfaceHolder.Callback{
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                camManager
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
            }

        }
    }

    // ************************************************

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
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
}
