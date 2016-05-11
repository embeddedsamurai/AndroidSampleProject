package com.example.test.testopengl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    MyGLView myGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        myGLView = new MyGLView(this);
        setContentView(myGLView);
        Toast.makeText(this, "onCreate()", Toast.LENGTH_LONG).show();
        String string;
        string ="onCreate()";
        Log.d(TAG, "debug: " + string);
    }

    @Override
    protected void onResume(){
        super.onResume();
        myGLView.onResume();
        Toast.makeText(this, "onResume()", Toast.LENGTH_LONG).show();
        String string;
        string ="onResume()";
        Log.d(TAG, "debug: " + string);
    }

    @Override
    protected void onPause(){
        super.onPause();
        myGLView.onPause();
        Toast.makeText(this, "onPause()", Toast.LENGTH_LONG).show();
        String string;
        string ="onPause()";
        Log.d(TAG, "debug: " + string);
    }


}
