package com.example.test.testopengl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    MyGLView myGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        myGLView = new MyGLView(this);
        setContentView(myGLView);
    }

    @Override
    protected void onResume(){
        super.onResume();
        myGLView.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        myGLView.onPause();
    }


}
