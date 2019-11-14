package com.example.loading;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    AnimationDrawable animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView loading = (ImageView)findViewById(R.id.imageView);
        animation = (AnimationDrawable)loading.getDrawable();
    }

    public void start(View v)
    {
        animation.start();
    }

    public void stop(View v)
    {
        animation.stop();
    }
}
