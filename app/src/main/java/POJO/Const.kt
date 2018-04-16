package POJO

import android.os.Environment
import android.text.format.DateUtils
import com.google.api.client.util.DateTime
import io.kickflip.sdk.Util.getHumanDateString
import io.kickflip.sdk.av.SessionConfig
import java.text.DateFormat
import java.util.*
import android.os.Environment.getExternalStorageDirectory
import java.io.File


class Const {
    companion object {
        @JvmStatic val HEROKU_URL :String = "https://foodbarbaz.herokuapp.com"
        @JvmStatic val OLI_LOCAL_URL : String = "http://192.168.15.115:8080"
        @JvmStatic val CLIENT_ID :String = "I!8mHatelK1iV.-O-?-AYm_lq2tETVbP=ij?i9UE"
        @JvmStatic val CLIENT_SECRET :String = "zdFaY3o=dc7hZeMThTXJ=MUMUaZVTdFA:IP0Ty792qzbog.vKd3M3KuaoLX=bWvL9Ktbbd46Vw-6a5P@g7.vfcos0I7iHWRDkBovXG0Yo2x=qLv17tlz;-JMKl::gje4"
        @JvmStatic val DEV_EMAIL :String = "yevoli.dev@gmail.com"

        @JvmStatic fun create720pSessionConfig(outputPath: String): SessionConfig {
            val extraData = HashMap<String,String>()
            extraData.put("key", "value")
            val date = Date()

            val config = SessionConfig.Builder(outputPath)
                    .withTitle(date.time.toString())
                    .withDescription("A live stream!")
                    .withAdaptiveStreaming(true)
                    .withVideoResolution(1280, 720)
                    .withVideoBitrate(2 * 1000 * 1000)
                    .withAudioBitrate(192 * 1000)
                    .withExtraInfo(extraData)
                    .withPrivateVisibility(false)
                    .withLocation(true)
                    .build()
            return config
        }

        fun createNewRecordingFile(): String {
            return File(Environment.getExternalStorageDirectory(), String.format("MySampleApp/%d.mp4", System.currentTimeMillis())).getAbsolutePath()
        }
    }
}