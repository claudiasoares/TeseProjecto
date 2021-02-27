package com.example.mobiledatacolection.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.mobiledatacolection.activities.FormActivity;
import com.google.firebase.database.DatabaseReference;

import org.javarosa.core.model.QuestionDef;
import org.javarosa.form.api.FormEntryPrompt;

import java.io.File;
import java.io.IOException;


public class ImageBinaryWidget {
    private static final int CAMERA_IMAGES_REQUEST = -1;
    private final LinearLayout screen;
    private final TextView textView;
    private final Button button;
    private final Context context;
    private final DatabaseReference databaseReference;
    private final long delay = 1000; // 1 seconds after user stops typing
    private long last_text_edit = 0;
    private final Handler handler = new Handler();
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public ImageBinaryWidget(Context context, LinearLayout screen, QuestionDef form, FormEntryPrompt fep, int version, DatabaseReference databaseReference){
        this.databaseReference = databaseReference;
        this.screen = screen;
        String name = fep.mTreeElement.getName();
        // form.getLabelInnerText() == null ? form.getTextID().split("/")[2].split(":")[0] : form.getLabelInnerText();
        this.context = context;

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params
                = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(params);
        params.setMargins(0,5,5,0);

        textView = new TextView(context);
        textView.setTextColor(Color.BLACK);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setText(name);
        LinearLayout.LayoutParams paramsTextView
                = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        linearLayout.setLayoutParams(params);
        textView.setLayoutParams(paramsTextView);

        button = new Button(context);
        LinearLayout.LayoutParams paramsButton
                = new LinearLayout.LayoutParams(40,100,1);

        linearLayout.setLayoutParams(params);
        button.setLayoutParams(paramsButton);
        button.setBackgroundColor(Color.LTGRAY);
        button.setText(name);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i.setType("image/*");
                if (context instanceof Activity) {
                    ((Activity) context).startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
                } else {
                    Log.e("ImageBinary","context should be an instanceof Activity.");
                }
            }
        });


        linearLayout.addView(textView);
        linearLayout.addView(button);
        this.screen.addView(linearLayout);
    }


    public LinearLayout getElement(){
        return screen;
    }


}
