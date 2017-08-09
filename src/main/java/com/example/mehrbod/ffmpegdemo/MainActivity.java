package com.example.mehrbod.ffmpegdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

public class MainActivity extends AppCompatActivity {
    private EditText commandEditText;
    private Button runButton;
    private TextView resultTextView;

    private FFmpeg ffmpeg = null;

    private static final String FFMPEG_LOAD_TAG = "FFmpegLoad";
    private static final String FFMPEG_EXECUTE_TAG = "FFmpegExecute";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ffmpeg = FFmpeg.getInstance(this);



        try {
            ffmpeg.loadBinary(new FFmpegLoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    Log.d(FFMPEG_LOAD_TAG, "Failed");
                }

                @Override
                public void onSuccess() {
                    Log.d(FFMPEG_LOAD_TAG, "Success");
                }

                @Override
                public void onStart() {

                }

                @Override
                public void onFinish() {

                }
            });
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
            Log.d(FFMPEG_LOAD_TAG, "Exception");
        }

        commandEditText = (EditText) findViewById(R.id.commandEditText);
        runButton = (Button) findViewById(R.id.runButton);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
    }

    public void onRunButtonClickListener(View view) {

        //ffmpeg -r 10 -i image_%03d.jpg -vcodec mjpeg -y file.mp4
        String command = "-i /storage/emulated/0/Download/testing.gif -vcodec mpeg4 /storage/emulated/0/Download/hello44.mp4";

        String[] cmd = command.split(" ");

        for (int i = 0; i < cmd.length; i++) {
            Log.d("FFmpegCommand", cmd[i]);
        }

        try {
            ffmpeg.execute(cmd, new FFmpegExecuteResponseHandler() {
                @Override
                public void onSuccess(String message) {
                    Log.d(FFMPEG_EXECUTE_TAG, "Success");
                    resultTextView.setText(message);
                }

                @Override
                public void onProgress(String message) {
                    Log.d(FFMPEG_EXECUTE_TAG, "Progressing");
                    resultTextView.setText(message);
                }

                @Override
                public void onFailure(String message) {
                    Log.d(FFMPEG_EXECUTE_TAG, message);
                    resultTextView.setText(message);
                }

                @Override
                public void onStart() {
                    Log.d(FFMPEG_EXECUTE_TAG, "Started");
                }

                @Override
                public void onFinish() {
                    Log.d(FFMPEG_EXECUTE_TAG, "Finished");
                    resultTextView.setText("Finished");
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }

    }
}
