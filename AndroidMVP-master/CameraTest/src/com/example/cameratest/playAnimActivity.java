package com.example.cameratest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.util.Constants;
import com.example.util.GlobalUtil;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created with IntelliJ IDEA.
 * User: Appchina
 * Date: 13-9-2
 * Time: ÏÂÎç2:07
 *
 */
public class playAnimActivity extends Activity {
    static final String TAG = "playAnimActivity";
    String filename;                                  //¿¨Æ¬Â·¾¶
    String text;                                      //¿¨Æ¬Ãû³Æ
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_anim);
        initUI();
    }

    ImageView image;
    TextView textView;
    Animation anim;
    private void initUI() {
        image = (ImageView)findViewById(R.id.image);
        textView =(TextView)findViewById(R.id.text);
        anim = AnimationUtils.loadAnimation(this,R.anim.anim);
        anim.setFillAfter(true);
        getIntentData();
        textView.setText("");
        Bitmap mybitmap = GlobalUtil.preHandleImage(null, Constants.dir_path_pic +filename);
        image.setImageBitmap(mybitmap);

        final Handler handler = new Handler(){
             public void handleMessage(Message msg){
                 if(msg.what == 0x123){
                     finish();
                 }
             }
        };
        image.startAnimation(anim);
        //textView.startAnimation(anim);
        new Timer().schedule(new TimerTask(){
            @Override
            public void run() {
                handler.sendEmptyMessage(0x123);
            }
        },3500);                         //3.5Ãëºó¹Ø±Õ
    }

    private void getIntentData() {
        Intent intent = getIntent();
        filename = intent.getStringExtra("filename");
        text = intent.getStringExtra("text");
    }
}
