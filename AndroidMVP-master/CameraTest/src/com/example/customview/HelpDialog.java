package com.example.customview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import com.example.cameratest.R;


public class HelpDialog extends Dialog {
    public HelpDialog(Context context, Drawable drawable)
    {
        super(context, R.style.dialog_fullscreen);
        // TODO Auto-generated constructor stub
        this.drawable = drawable;
    }
    private HelpDialog td;
    View ll;
    Drawable drawable;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_help);
        td= this;
        // 设置背景透明度
        ll = findViewById(R.id.ll_dialog);
        ll.getBackground().setAlpha(120);// 120为透明的比率
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                td.dismiss();
            }
        });
        ll.setBackgroundDrawable(drawable);
    }

}
