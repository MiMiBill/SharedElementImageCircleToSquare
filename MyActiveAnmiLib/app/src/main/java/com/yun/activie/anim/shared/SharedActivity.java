package com.yun.activie.anim.shared;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yun.activie.anim.lib.MainActivity;
import com.yun.activie.anim.lib.R;
import com.yun.activie.anim.myTransition.CircleToRectTransition;

public class SharedActivity extends AppCompatActivity {


    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(new CircleToRectTransition().setDuration(500));
            getWindow().setSharedElementExitTransition(new CircleToRectTransition().setDuration(500));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);
        imageView = findViewById(R.id.second_head);
        Glide.with(this).load(MainActivity.url).centerCrop().into(imageView);

    }
}