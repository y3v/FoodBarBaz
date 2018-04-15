package yevoli.release.yev.foodbarbaz;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.red5pro.streaming.R5Connection;
import com.red5pro.streaming.R5Stream;
import com.red5pro.streaming.R5StreamProtocol;
import com.red5pro.streaming.config.R5Configuration;
import com.red5pro.streaming.source.R5Camera;
import com.red5pro.streaming.source.R5Microphone;

import POJO.Const;

public class BasicStreamActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    protected Camera camera;
    protected boolean isStreaming = false;
    protected R5Stream stream;
    protected R5Configuration config;
    private Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_stream);

        startBtn = (Button) findViewById(R.id.start_streaming_btn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                onPublishToggle();
            }
        });

        config = new R5Configuration(R5StreamProtocol.RTSP, "localhost",  8554, "live", 1.0f);
        config.setLicenseKey(Const.getR5_SDK_LICENSE());
        config.setBundleID(getPackageName());
    }

    private void preview(){
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        SurfaceView surface = (SurfaceView) findViewById(R.id.surfaceView);
        surface.getHolder().addCallback(this);
    }

    private void onPublishToggle() {
        if (isStreaming)
            stop();
        else
            start();

        isStreaming = !isStreaming;
        startBtn.setText(isStreaming ? "stop" : "start");
    }

    public void start(){
        camera.stopPreview();

        stream = new R5Stream(new R5Connection(config));
        stream.setView((SurfaceView) findViewById(R.id.surfaceView));

        R5Camera r5Camera = new R5Camera(camera, 320,240);
        R5Microphone r5Microphone = new R5Microphone();

        stream.attachCamera(r5Camera);
        stream.attachMic(r5Microphone);

        stream.publish("AYYYEEE", R5Stream.RecordType.Live);
        camera.startPreview();
    }

    public void stop() {
        if (stream != null){
            stream.stop();
            camera.startPreview();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        preview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isStreaming)
            onPublishToggle();
    }
}
